package etf.nim.mm140593d.minimax;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GameState;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private GameState gameState;
    private GameMove gameMove;
    private int score;
    private boolean isMax;
    private List<Node> children;

    public Node(GameState gs, boolean isMax, GameMove gm) {
        this.isMax = isMax;
        this.gameState = gs;
        this.gameMove = gm;
        this.children = new ArrayList<>();
    }

    void addChild(Node child) {
        children.add(child);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isMax() {
        return isMax;
    }

    public void setMax(boolean max) {
        isMax = max;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameMove getGameMove() {
        return gameMove;
    }
}
