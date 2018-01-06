package etf.nim.mm140593d.minimax;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GameState;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Implements Alpha Beta pruning algorithm.
 */
public class AlphaBetaPruning {
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
        consructTree(rootNode, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Creates a node in the game tree.
     *
     * @param rootNode {@link Node}
     * @param depth    {@link Integer} Depth of the current node
     * @param alpha    {@link Integer} Score of children won't be less then alpha
     * @param beta     {@link Integer} Score of children won't be greater then beta
     */
    private void consructTree(Node rootNode, int depth, int alpha, int beta) {
        if (depth > maxDepth) {
            // If the maxDepth is reached calculate the heuristics function.
            int score =
                Arrays.stream(rootNode.getGameState().getObjectsNumber()).reduce((x, y) -> x ^ y)
                    .getAsInt();
            rootNode.setScore(rootNode.isMax() ? -score : score);

            return;
        }

        boolean isMaxChild = !rootNode.isMax();

        for (GameMove gameMove : rootNode.getGameState().getAllPossibleMoves()) {
            Node childNode =
                new Node(rootNode.getGameState().applyMove(gameMove), isMaxChild, gameMove);
            childNode.getGameState().increaseMoveNumber();
            childNode.getGameState().changePlayer();

            consructTree(childNode, depth + 1, alpha, beta);

            // No use to develop further child nodes if current has score greater than alpha and current node is min.
            // Or if the score is smaller than beta and node is max.
            if ((rootNode.isMax() && childNode.getScore() < alpha) || (!rootNode.isMax()
                && childNode.getScore() > beta)) {
                break;
            }

            rootNode.addChild(childNode);

            if (rootNode.isMax()) {
                alpha = Math.max(alpha, childNode.getScore());
            } else {
                beta = Math.min(beta, childNode.getScore());
            }
        }

        if (rootNode.getChildren().size() == 0) {
            rootNode.setScore(rootNode.isMax() ? -1 : 1);
        } else {
            Comparator<Node> scoreComparator = Comparator.comparing(Node::getScore);
            rootNode.setScore(rootNode.getChildren().stream()
                .max(rootNode.isMax() ? scoreComparator : scoreComparator.reversed())
                .orElseThrow(IllegalStateException::new).getScore());
        }
    }

    /**
     * After the tree is constructed, this method will return the best move for the current game state.
     *
     * @return {@link GameMove} Best move for the current state.
     */
    public GameMove getBestMove() {
        Comparator<Node> scoreComparator = Comparator.comparing(Node::getScore)
            .thenComparing(node -> node.getGameMove().getObjects());

        return tree.getRoot().getChildren().stream().max(scoreComparator)
            .orElseThrow(IllegalStateException::new).getGameMove();
    }
}
