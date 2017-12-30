package etf.nim.mm140593d.minimax;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GameState;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class MiniMax {
    private Tree tree;
    private int maxDepth;

    public void constructTree(GameState gameState, int maxDepth) {
        this.maxDepth = maxDepth;
        Node rootNode = new Node(gameState, true, null);
        tree = new Tree(rootNode);
        constructTree(rootNode, 0);
    }

    public void constructTree(Node rootNode, int depth) {
        boolean isMaxChild = !rootNode.isMax();

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
            int score =
                Arrays.stream(rootNode.getGameState().getObjectsNumber()).reduce((x, y) -> x ^ y)
                    .getAsInt();
            rootNode.setScore(rootNode.isMax() ? -score : score);

            return;
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
