package etf.nim.mm140593d.player;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GamePlay;
import etf.nim.mm140593d.game.GameState;
import etf.nim.mm140593d.qlearning.QLearningAlgorithm;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

public class QAgent extends GamePlay {

    private static final double eps = 0.1;
    private static final double alpha = 0.3;
    private static final double gamma = 0.9;

    private QLearningAlgorithm algorithm;
    private int playerID;

    public QAgent(int playerID) {
        algorithm = new QLearningAlgorithm(alpha, gamma, eps, playerID);
        this.playerID = playerID;
    }

    @Override public void doMove(GameState gameState) {
        try {
            Instant start = Instant.now();

            GameMove bestMove = algorithm.getBestMove(gameState);

            Instant end = Instant.now();
            System.out.println(
                "Computing time: " + Duration.between(start, end).toMillis() + " ms\nTaking move: "
                    + bestMove.toString());
            gameState.changeAndCheck(bestMove, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public void saveState(int playerID) {
        algorithm.finalize(playerID);

        try {
            new ObjectOutputStream(new FileOutputStream("train_data" + playerID + ".dat"))
                .writeObject(algorithm.getQ());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void loadState() {
        try {
            HashMap<GameState, HashMap<GameMove, Double>> obj =
                (HashMap<GameState, HashMap<GameMove, Double>>) new ObjectInputStream(
                    new FileInputStream("train_data" + playerID + ".dat")).readObject();
            algorithm.setQ(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
