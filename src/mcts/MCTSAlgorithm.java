package mcts;

import battleship.Player;
import battleship.Randomizer;
import utils.Logger;

import java.util.List;

class MCTSAlgorithm {
    private Node root;
    private int maxIterations;

    private int currentLevel;

    MCTSAlgorithm(Player self, Player opponent, int iterations) {
        this.root = new Node(null, self, opponent);
        this.maxIterations = iterations;

        // The current depth of the tree
        this.currentLevel = 0;
    }

    /**
     * Run the algorithm.
     *
     * @return Field(row, col) of the suggested next move.
     */
    List<Node> run() {
        int currentIteration = 1;

        Logger.debug("Starting MCTS");


        while (currentIteration <= maxIterations) {
            Logger.debug("MCTS Iteration: " + currentIteration);

            Node currentNode = selectNode(this.root); // Step 1: Select a child node to expand
            expandNode(currentNode); // Step 2: Expand node
            Node simulateNode = currentNode.randomChild();

            boolean win = simulateGamePlayForNode(simulateNode); // Step 3: Simulate game play (playout)

            backPropagate(simulateNode, win); // Step 4: Back-propagate, update parents

            currentIteration++;
        }

        /*// Assign a random child node to be the winner
        Node winningNode = this.root.getChildren().get(Randomizer.nextInt(0, root.getChildren().size()-1));

        // ... then check if it was the best node, else re-assign
        for (Node c : this.root.getChildren()) {
            if ((c.getWinChance()) > winningNode.getWinChance()) {
                winningNode = c;
            }
        }*/

        return root.getChildren();
    }

    private Node selectNode(Node node) {
        if (node.getChildren().isEmpty()) {
            return node;
        }

        Node bestNode = node;

        double bestUct = 0;
        for (Node c : node.getChildren()) {
            double uct = uctValue(c.getParent().getPlays(), c.getWins(), c.getPlays());

            if (uct == Double.MAX_VALUE) return c;

            if (bestNode == node || uct > bestUct) {
                bestNode = c;
                bestUct = uct;
            }
        }

        return selectNode(bestNode);
    }

    private void expandNode(Node parent) {
        List<Field> possibleMoves = parent.getSelf().getAllPossibleMoves();

        for (Field move : possibleMoves) {
            Node child = new Node(parent, parent.getOpponent(), parent.getSelf());
            child.playNextMove(move);
            parent.addChild(child);
        }
    }

    private void backPropagate(Node node, boolean win)
    {
        if (win) node.incrementWins();
        node.incrementPlays();

        Node parent = node.getParent();

        if (parent != null) backPropagate(parent, !win);
    }

    /**
     * Takes a Node and simulates game play.
     *
     * @param node The node on which to simulate game play.
     * @return true if WIN, false else
     */
    private boolean simulateGamePlayForNode(Node node) {
        int rand = Randomizer.nextInt(0,1);
        return rand == 0;

//        Player winner = node.getSelf();
//        Player loser = node.getOpponent();
//
//        while (!loser.playerGrid.hasLost()) {
//            List<Field> possibleMoves = winner.getAllPossibleMoves();
//            Field fieldToShoot = possibleMoves.get(Randomizer.nextInt(0, winner.getAllPossibleMoves().size()-1));
//
//            if (loser.playerGrid.hasShip(fieldToShoot.row, fieldToShoot.col))
//            {
//                loser.playerGrid.markHit(fieldToShoot.row, fieldToShoot.col);
//                winner.oppGrid.markHit(fieldToShoot.row, fieldToShoot.col);
//            }
//            else
//            {
//                loser.playerGrid.markMiss(fieldToShoot.row, fieldToShoot.col);
//                winner.oppGrid.markMiss(fieldToShoot.row, fieldToShoot.row);
//            }
//
//            Player temp = winner;
//            winner = loser;
//            loser = temp;
//        }
//
//        return node.getSelf()==winner; //Finds out if node.getSelf() has won by comparing its reference with the winner
    }

    private void incrementLevel() {
        this.currentLevel = currentLevel + 1;
    }

    private void decrementLevel() {
        this.currentLevel = currentLevel + 1;
    }

    /**
     * UCT calculation.
     *
     * @param totalPlaysParent n
     * @param totalWinsCurrent w_i
     * @param totalPlaysCurrent n_i
     *
     * @return UCT value
     *
     *      (w_i / n_i) + sqrt((2 * n) / n_i)
     *          where
     *              w_i     total wins at current node
     *              n_i     total plays at current node
     *              n       total plays at parent node
     */
    private static double uctValue(int totalPlaysParent, int totalWinsCurrent, int totalPlaysCurrent) {
        if (totalPlaysCurrent == 0) {
            return Double.MAX_VALUE;
        }

        return ((double) totalWinsCurrent / (double) totalPlaysCurrent)
                + Math.sqrt(2 * Math.log(totalPlaysParent) / (double) totalPlaysCurrent);
    }
}
