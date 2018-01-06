package etf.nim.mm140593d.player;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GamePlay;
import etf.nim.mm140593d.game.GameState;
import etf.nim.mm140593d.minimax.AlphaBetaPruning;

import java.time.Duration;
import java.time.Instant;

public class AlphaBetaAgent extends GamePlay {
    private AlphaBetaPruning algorithm = new AlphaBetaPruning();
    private int maxDepth;

    public AlphaBetaAgent(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public void doMove(GameState gameState) {
        try {
            Instant start = Instant.now();

            algorithm.constructTree(gameState, maxDepth);
            GameMove bestMove = algorithm.getBestMove();

            Instant end = Instant.now();
            System.out.println(
                "Coputing time: " + Duration.between(start, end).toMillis() + " ms\nTaking move: " + bestMove
                    .toString());
            gameState.changeAndCheck(bestMove, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
