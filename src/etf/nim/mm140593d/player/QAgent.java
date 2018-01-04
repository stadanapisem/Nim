package etf.nim.mm140593d.player;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GamePlay;
import etf.nim.mm140593d.game.GameState;
import etf.nim.mm140593d.qlearning.QLearningAlgorithm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.Instant;

public class QAgent extends GamePlay {

    private static final double eps = 0.5;
    private static final double alpha = 0.3;
    private static final double gamma = 0.9;
    private QLearningAlgorithm algorithm;
    private int playerID;
    private boolean train;

    public QAgent(int playerID, boolean train) {
        this.playerID = playerID;
        this.train = train;

        if (this.train) {
            algorithm = new QLearningAlgorithm(alpha, gamma, eps, playerID);
        } else {
            algorithm = new QLearningAlgorithm(alpha, gamma, 1.0, playerID);
        }
    }

    @Override public void doMove(GameState gameState) {
        try {
            Instant start = Instant.now();

            GameMove bestMove = algorithm.getBestMove(gameState);

            Instant end = Instant.now();
            System.out.println(
                "Computing time: " + Duration.between(start, end).toMillis() + " ms\nTaking move: "
                    + bestMove.toString());
            gameState.changeAndCheck(false, bestMove.getObjects(), bestMove.getNumberOnHeap());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override public void saveState(int playerID) {
        algorithm.finalize(playerID);

        try (ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream("train_data" + this.playerID + ".dat"))) {
            algorithm.saveTable(oos);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override public void loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(
            new FileInputStream("train_data" + playerID + ".dat"))) {

            algorithm.loadTable(ois);
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
