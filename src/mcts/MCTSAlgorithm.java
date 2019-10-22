package mcts;

import battleship.Player;
import utils.Logger;

import java.util.ArrayList;
import java.util.List;

class MCTSAlgorithm {
    private Node root;
    private int maxIterations;

    private int currentLevel;

    MCTSAlgorithm(Player self, Player opponent, int iterations) {
        this.root = new Node(self, opponent);
        this.maxIterations = iterations;

        // The current depth of the tree
        this.currentLevel = 0;
    }

    /**
     * Run the algorithm.
     *
     * @return Field(row, col) of the suggested next move.
     */
    Field run() {
        int currentIteration = 1;

        Field suggestedMove = new Field(0, 0);

        Logger.debug("Starting MCTS");

        // Step 0: Determinization

        while (currentIteration <= maxIterations) {
            Logger.debug("MCTS Iteration: " + currentIteration);

            // Step 1: Select a child node to run
            Node selectedNode = selectNode(this.root);

            // Step 2: Expand node

            // Step 3: Simulate game play (playout)

            // Step 4: Back-propagate, update parents
            for (Node n : root.getChildren()) {
                // TODO

                this.decrementLevel();
            }

            currentIteration++;
        }

        return suggestedMove;
    }

    /**
     * Select next node with max. UCT value.
     *
     * @return selected node
     */
    private Node selectNode(Node root) {
        int playsAtRoot = root.getPlays();
        List<Node> childrenOfRoot = root.getChildren();

        Node selectedNode = root;

        int bestUctValue = Integer.MIN_VALUE;

        for (Node c : childrenOfRoot) {
            if (uctValue(playsAtRoot, c.getWins(), c.getPlays()) > bestUctValue) {
                selectedNode = c;
            };
        }

        return selectedNode;
    }

    /**
     * Takes a Node and simulates game play.
     *
     * @param node The node on which to simulate game play.
     * @return true if WIN, false else
     */
    private boolean simulateGamePlayForNode(Node node) {
        //

        return false;
    }

    /**
     * Get children of node.
     *
     * @return The children nodes
     */
    private List<Node> expandNode() {
        List <Node> children = new ArrayList<>();


        // TODO

        this.incrementLevel();
        return children;
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
            return Integer.MAX_VALUE;
        }

        return ((double) totalWinsCurrent / (double) totalPlaysCurrent)
                + Math.sqrt(2 * Math.log10(totalPlaysParent) / (double) totalPlaysCurrent);
    }
}
