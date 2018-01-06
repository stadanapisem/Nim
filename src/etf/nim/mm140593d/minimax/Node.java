package etf.nim.mm140593d.minimax;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a node in the game tree for the MiniMax and AlphaBetaPruning.
 */
public class Node {

    /**
     * The game state for which this node is created.
     */
    private GameState gameState;

    /**
     * Game move that lead to this state.
     */
    private GameMove gameMove;

    /**
     * Score of the node.
     */
    private int score;

    /**
     * Is it the min or max node.
     */
    private boolean isMax;

    /**
     * Children of the node.
     */
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
