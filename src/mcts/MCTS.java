package mcts;

import battleship.Grid;

public class MCTS {
    private Tree gameTree;
    private int maxIterations;

    public MCTS(Grid opponentGrid, Grid ownGrid, int iterations) {
        this.gameTree = new Tree(new Node(opponentGrid, ownGrid));
        this.maxIterations = iterations;
    }
}
