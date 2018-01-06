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

/**
 * Implementation of the agent for Q-Learning algorithm.
 */
public class QAgent extends GamePlay {

    /**
     * The random factor for the exploration function in the training of the agent.
     */
    private static final double eps = 0.1;

    /**
     * The learning factor.
     */
    private static final double alpha = 0.3;

    /**
     * The discount factor.
     */
    private static final double gamma = 0.7;

    /**
     * Q-Learning algorithm object. Contains the learning table.
     */
    private QLearningAlgorithm algorithm;

    /**
     * ID of the current player.
     */
    private int playerID;

    /**
     * Creates a agent with given id and work mode.
     *
     * @param playerID {@link Integer} ID of the player
     * @param train    {@link Boolean} Should the algorithm train or not
     */
    public QAgent(int playerID, boolean train) {
        this.playerID = playerID;

        if (train) {
            algorithm = new QLearningAlgorithm(alpha, gamma, eps, playerID);
        } else {
            algorithm = new QLearningAlgorithm(alpha, gamma, 0, playerID);
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
