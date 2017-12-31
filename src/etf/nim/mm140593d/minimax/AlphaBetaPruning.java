package etf.nim.mm140593d.minimax;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GameState;

import java.util.Arrays;
import java.util.Comparator;

public class AlphaBetaPruning {
    private Tree tree;
    private int maxDepth;

    public void constructTree(GameState gameState, int maxDepth) {
        this.maxDepth = maxDepth;
        Node rootNode = new Node(gameState, true, null);
        tree = new Tree(rootNode);
        consructTree(rootNode, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private void consructTree(Node rootNode, int depth, int alpha, int beta) {
        if (depth > maxDepth) {
            int score =
                Arrays.stream(rootNode.getGameState().getObjectsNumber()).reduce((x, y) -> x ^ y)
                    .getAsInt();
            rootNode.setScore(rootNode.isMax() ? -score : score);

            return;
        }

        boolean isMaxChild = !rootNode.isMax();

        /*rootNode.getGameState().getAllPossibleMoves().forEach(gameMove -> {
            Node childNode =
                new Node(rootNode.getGameState().applyMove(gameMove), isMaxChild, gameMove);
            childNode.getGameState().increaseMoveNumber();
            childNode.getGameState().changePlayer();
            consructTree(childNode, depth + 1, alpha, beta);

            if (rootNode.isMax()) {
                alpha = Math.max(alpha, childNode.getScore());
            } else {
                beta = Math.min(beta, childNode.getScore());
            }
        });*/

        for (GameMove gameMove : rootNode.getGameState().getAllPossibleMoves()) {
            Node childNode =
                new Node(rootNode.getGameState().applyMove(gameMove), isMaxChild, gameMove);
            childNode.getGameState().increaseMoveNumber();
            childNode.getGameState().changePlayer();

            consructTree(childNode, depth + 1, alpha, beta);

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
            //rootNode.setScore(rootNode.getChildren().stream().mapToInt(Node::getScore).sum());
        }
    }

    public GameMove getBestMove() {
        Comparator<Node> scoreComparator = Comparator.comparing(Node::getScore)
            .thenComparing(node -> node.getGameMove().getObjects());

        return tree.getRoot().getChildren().stream().max(scoreComparator)
            .orElseThrow(IllegalStateException::new).getGameMove();
    }
}
