package mcts;

import battleship.Grid;

import java.util.ArrayList;
import java.util.List;

class Node {
    private Grid ownGrid;
    private Grid opponentGrid;

    private List<Node> children;

    /**
     * Initialize a Node.
     *
     * @param opponentGrid The grid of the human (opponent)
     * @param ownGrid The grid of the computer
     */
    public Node(Grid opponentGrid, Grid ownGrid) {
        this.ownGrid = ownGrid;
        this.opponentGrid = opponentGrid;

        this.children = new ArrayList<>();
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public List<Node> getChildren() {
        return this.children;
    }
}
