package etf.nim.mm140593d.minimax;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GameState;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implements the MiniMax algorithm.
 */
public class MiniMax {
    private Tree tree;
    private int maxDepth;

    /**
     * Creates the root node and delegates the construction of the rest of the game tree.
     *
     * @param gameState {@link GameState} Current game state
     * @param maxDepth  {@link Integer} Maximum depth of the game tree
     */
    public void constructTree(GameState gameState, int maxDepth) {
        this.maxDepth = maxDepth;
        Node rootNode = new Node(gameState, true, null);
        tree = new Tree(rootNode);
        constructTree(rootNode, 0);
    }

    /**
     * Constructs the game tree to the predetermined depth.
     *
     * @param rootNode {@link Node} Root node for the current function iteration
     * @param depth    {@link Integer} Current node depth
     */
    public void constructTree(Node rootNode, int depth) {
        boolean isMaxChild = !rootNode.isMax();

        // If the maxDepth is not reached and game is not finished, create a node for every
        // possible move for the current game state.
        if (rootNode.getGameState().isNotFinished() && depth <= maxDepth) {
            rootNode.getGameState().getAllPossibleMoves().forEach(gameMove -> {
                Node childNode =
                    new Node(rootNode.getGameState().applyMove(gameMove), isMaxChild, gameMove);
                childNode.getGameState().increaseMoveNumber();

                childNode.getGameState().changePlayer();
                rootNode.addChild(childNode);

                constructTree(childNode, depth + 1);
            });
        } else if (depth > maxDepth) {
            // If maxDepth is reached, calculate the heuristic function for the this node's score
            int score =
                Arrays.stream(rootNode.getGameState().getObjectsNumber()).reduce((x, y) -> x ^ y)
                    .getAsInt();
            rootNode.setScore(rootNode.isMax() ? -score : score);

            return;
        }

        // If node is leaf, score can only be win or lose.
        if (rootNode.getChildren().size() == 0) {
            rootNode.setScore(rootNode.isMax() ? -1 : 1);
        } else {
            // Score of the current node is minimum or maximum of it's children.
            Comparator<Node> scoreComparator = Comparator.comparing(Node::getScore);
            rootNode.setScore(rootNode.getChildren().stream()
                .max(rootNode.isMax() ? scoreComparator : scoreComparator.reversed())
                .orElseThrow(IllegalStateException::new).getScore());
        }
    }

    /**
     * After the tree is constructed, find the best move for the current game state.
     * @return {@link GameMove} Best possible move
     */
    public GameMove getBestMove() {
        Comparator<Node> scoreComparator = Comparator.comparing(Node::getScore)
            .thenComparing(node -> node.getGameMove().getObjects());

        return tree.getRoot().getChildren().stream().max(scoreComparator)
            .orElseThrow(IllegalStateException::new).getGameMove();
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        Queue<Node> queue = new LinkedList<>();

        queue.add(tree.getRoot());

        for (int i = 0; !queue.isEmpty(); i++) {
            sb.append("level " + i + "\n");

            Node current = queue.poll();
            sb.append(current.getGameState().toString() + "\n");

            current.getChildren().forEach(node -> queue.add(node));

        }

        return sb.toString();
    }
}
