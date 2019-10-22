package mcts;

import battleship.Player;

import java.util.ArrayList;
import java.util.List;

class Node {
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
    Node(Player self, Player humanOpponent) {
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
}
