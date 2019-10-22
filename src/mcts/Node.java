package mcts;

import battleship.Player;

import java.util.ArrayList;
import java.util.List;

class Node {
    private Node parent; // null means root node

    // The move that was done by the algorithm
    private Field move;

    private Player opponent;
    private Player self;

    // Statistics
    private int plays = 0;
    private int wins = 0;

    private List<Node> children;

    /**
     * Initialize a Node.
     *
     * @param humanOpponent The human (opponent) player
     * @param self The computer player
     */
    Node(Node parent, Player self, Player humanOpponent) {
        this.parent = parent;
        this.self = self;
        this.opponent = humanOpponent;

        this.children = new ArrayList<>();
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public void incrementPlays() {
        this.plays++;
    }

    public void incrementWins() {
        this.wins++;
    }

    public int getPlays() {
        return plays;
    }

    public int getWins() {
        return wins;
    }

    public Player getOpponent() {
        return opponent;
    }

    public Player getSelf() {
        return self;
    }

    public Field getMove() {
        return move;
    }

    public Node getParent() {
        return parent;
    }
}
