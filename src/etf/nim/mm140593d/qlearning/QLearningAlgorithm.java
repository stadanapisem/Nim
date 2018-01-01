package etf.nim.mm140593d.qlearning;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GameState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

public class QLearningAlgorithm {

    private HashMap<GameState, HashMap<GameMove, Double>> Q;
    private double alpha;
    private double gamma;
    private double eps;
    private GameState lastState;
    private GameMove lastMove;
    private int playerID;

    public QLearningAlgorithm(double alpha, double gamma, double eps, int playerID) {
        Q = new HashMap<>();

        this.alpha = alpha;
        this.gamma = gamma;
        this.eps = eps;
        this.playerID = playerID;
        this.lastMove = null;
        this.lastState = null;
    }

    public GameMove getBestMove(GameState gs) {
        if (!Q.containsKey(gs)) {
            HashMap<GameMove, Double> tmp = new HashMap<>();
            gs.getAllPossibleMoves().forEach(move -> tmp.put(move, 0.0));

            Q.put(gs.clone(), tmp);
        }

        GameMove resultAction = null;
        if (Math.random() < eps) {
            HashMap<GameMove, Double> possibleMoves = Q.get(gs);
            Integer randomValue = (int) (Math.random() * possibleMoves.keySet().size());
            resultAction = (GameMove) possibleMoves.keySet().toArray()[randomValue];
        } else {
            // take best
            double max = -100000;
            for (Entry<GameMove, Double> entry : Q.get(gs).entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    resultAction = entry.getKey();
                }
            }
        }



        if (lastMove != null && lastState != null) {
            Double maxValue = Collections.max(Q.get(lastState).values());
            Double lastValue = Q.get(lastState).get(lastMove);

            lastValue += alpha * (gamma * maxValue - lastValue);
            Q.get(lastState).put(lastMove, lastValue);
        }

        lastState = gs.clone();
        lastMove = resultAction;
        return resultAction;
    }

    public void finalize(int player) {
        Double reward;

        if (player == playerID) {
            reward = 1.0;
        } else {
            reward = -1.0;
        }

        Q.get(lastState).put(lastMove, reward);
    }

    public HashMap<GameState, HashMap<GameMove, Double>> getQ() {
        return Q;
    }

    public void setQ(HashMap<GameState, HashMap<GameMove, Double>> q) {
        Q = q;
    }
}
