package mcts;

import battleship.Player;
import utils.Logger;

public class MCTS {
    private Tree gameTree;
    private int maxIterations;

    public MCTS(Player self, Player opponent, int iterations) {
        this.gameTree = new Tree(new Node(self, opponent));
        this.maxIterations = iterations;
    }

    // Run the algorithm
    public void run() {
        int currentIteration = 1;

        Logger.debug("Starting MCTS");

        while (currentIteration <= maxIterations) {
            Logger.debug("MCTS Iteration: " + currentIteration);

            // Select a child node to run

            // Expand node

            // Simulate game play (playout)

            // Back-propagate, update parents

            currentIteration++;
        }
    }

}
