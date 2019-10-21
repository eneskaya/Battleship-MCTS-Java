package mcts;

import battleship.Grid;
import utils.Logger;

public class MCTS {
    private Tree gameTree;
    private int maxIterations;

    public MCTS(Grid opponentGrid, Grid ownGrid, int iterations) {
        this.gameTree = new Tree(new Node(opponentGrid, ownGrid));
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

            // Simulate game play

            // Back-propagate

            currentIteration++;
        }
    }

}
