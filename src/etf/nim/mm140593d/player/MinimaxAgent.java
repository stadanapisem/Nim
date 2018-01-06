package etf.nim.mm140593d.player;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GamePlay;
import etf.nim.mm140593d.game.GameState;
import etf.nim.mm140593d.minimax.MiniMax;

import java.time.Duration;
import java.time.Instant;


public class MinimaxAgent extends GamePlay {
    private MiniMax algorithm = new MiniMax();
    private int maxDepth;

    public MinimaxAgent(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override public void doMove(GameState gameState) {
        try {
            Instant start = Instant.now();

            algorithm.constructTree(gameState, maxDepth);
            GameMove bestMove = algorithm.getBestMove();

            Instant end = Instant.now();
            System.out.println(
                "Computing time: " + Duration.between(start, end).toMillis() + " ms\nTaking move: " + bestMove
                    .toString());
            gameState.changeAndCheck(bestMove, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
