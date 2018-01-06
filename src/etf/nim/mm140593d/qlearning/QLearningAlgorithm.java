package etf.nim.mm140593d.qlearning;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Implementation of the Q-Learning algorithm.
 */
public class QLearningAlgorithm implements Serializable {

    /**
     * The learning table of States -> (Action, Reward).
     */
    private HashMap<GameState, HashMap<GameMove, Double>> Q;

    /**
     * The learning parameter.
     */
    private double alpha;

    /**
     * The discount factor (how much this move affects the last one).
     */
    private double gamma;

    /**
     * Random factor in the exploration function.
     */
    private double eps;

    /**
     * The last state of the game.
     */
    private GameState lastState;

    /**
     * The action (move) last taken.
     */
    private GameMove lastMove;

    /**
     * The id of the player.
     */
    private int playerID;

    /**
     * Creates the QLearningAlgorithm object.
     *
     * @param alpha    {@link #alpha}
     * @param gamma    {@link #gamma}
     * @param eps      {@link #eps}
     * @param playerID {@link #playerID}
     */
    public QLearningAlgorithm(double alpha, double gamma, double eps, int playerID) {
        Q = new HashMap<>();

        this.alpha = alpha;
        this.gamma = gamma;
        this.eps = eps;
        this.playerID = playerID;
        this.lastMove = null;
        this.lastState = null;
    }

    /**
     * Returns the best action (move) to take in the current game state.
     *
     * @param gs {@link GameState} Current state of the game
     * @return {@link GameMove} Action to take
     */
    public GameMove getBestMove(GameState gs) {

        // If the table does not contain the entry for this game state, create it.
        if (!Q.containsKey(gs)) {
            HashMap<GameMove, Double> tmp = new HashMap<>();
            gs.getAllPossibleMoves().forEach(move -> tmp.put(move, 0.0));

            Q.put(gs.clone(), tmp);
        }

        // The exploration function. With a certain probability choose a random move.
        GameMove resultAction = null;
        if (Math.random() < eps) {
            HashMap<GameMove, Double> possibleMoves = Q.get(gs);

            while (true) {
                try {
                    Integer randomValue = (int) (Math.random() * possibleMoves.keySet().size());
                    resultAction = (GameMove) possibleMoves.keySet().toArray()[randomValue];

                    if (gs.changeAndCheck(true, resultAction.getObjects(),
                        resultAction.getNumberOnHeap())) {
                        break;
                    }
                } catch (IllegalStateException e) {
                    // Expected
                }
            }
        } else {
            // This should always execute if not in training mode.
            // Get the move with the maximum reward.
            double max = -10000;
            for (Entry<GameMove, Double> entry : Q.get(gs).entrySet()) {
                try {
                    if (entry.getValue() >= max && gs
                        .changeAndCheck(true, entry.getKey().getObjects(),
                            entry.getKey().getNumberOnHeap())) {
                        max = entry.getValue();
                        resultAction = entry.getKey();
                    }
                } catch (IllegalStateException e) {
                    // Expected, in case this move can not be executed in current state.
                }
            }

        }

        // The update function.
        if (lastMove != null && lastState != null) {
            Double maxValue = Collections.max(Q.get(gs).values());
            Double lastValue = Q.get(lastState).get(lastMove);
            Double reward =
                Arrays.stream(gs.getObjectsNumber()).reduce((x, y) -> x ^ y).getAsInt() != 0
                    ? -1.0 : 0.0;

            lastValue += alpha * (reward + gamma * maxValue - lastValue);
            Q.get(lastState).put(lastMove, lastValue);
        }

        lastState = gs.clone();
        lastMove = resultAction;
        return resultAction;
    }

    /**
     * Function called after the game is done, only to add a reward to the last action taken.
     *
     * @param player ID of the winner player
     */
    public void finalize(int player) {
        Double reward;

        if (player == playerID) {
            reward = 1.0;
        } else {
            reward = -1.0;
        }

        if (lastState != null && lastMove != null) {
            Q.get(lastState).put(lastMove, reward);
        }
    }

    /**
     * Serializes the Table object into file.
     *
     * @param oos {@link ObjectOutputStream} Output stream
     * @throws IOException Thrown in case of stream error
     */
    public void saveTable(ObjectOutputStream oos) throws IOException {
        oos.writeInt(Q.size());

        Q.forEach((key, value) -> {
            try {
                oos.writeObject(key);
                oos.writeObject(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Deserialize the table object.
     *
     * @param ois {@link ObjectInputStream} Input stream
     * @throws IOException            Thrown in case of stream error
     * @throws ClassNotFoundException Thrown in case of wrong cast
     */
    public void loadTable(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        int mapSize = ois.readInt();

        for (int i = 0; i < mapSize; i++) {
            GameState gs = (GameState) ois.readObject();
            HashMap<GameMove, Double> map = (HashMap<GameMove, Double>) ois.readObject();
            Q.put(gs, map);
        }
    }
}
