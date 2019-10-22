package mcts;

import battleship.Player;
import utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class MCTS {
    private Node root;
    private int maxIterations;

    private int currentLevel;

    public MCTS(Player self, Player opponent, int iterations) {
        this.root = new Node(self, opponent);
        this.maxIterations = iterations;

        // The current depth of the tree
        this.currentLevel = 0;
    }

    public int[] run() {
        int currentIteration = 1;

        int[] suggestedMove = {0, 0};

        Logger.debug("Starting MCTS");

        Node selectedRoot = this.root;

        while (currentIteration <= maxIterations) {
            Logger.debug("MCTS Iteration: " + currentIteration);

            // Select a child node to run

            // Expand node

            // Simulate game play (playout)

            // Back-propagate, update parents
            for (Node n : selectedRoot.getChildren()) {
                // TODO

                this.decrementLevel();
            }

            currentIteration++;
        }

        return suggestedMove;
    }

    /**
     * Select next node with UCT algorithm.
     *
     * @return selected node
     */
    private Node selectNode(Node root) {
        //
        return null;
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
