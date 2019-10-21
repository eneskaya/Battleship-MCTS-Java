package mcts;

class Tree {
    private Node root;

    Tree(Node root) {
        this.root = root;
    }

    public Node getRootNode() {
        return this.root;
    }
}
