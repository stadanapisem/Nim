package etf.nim.mm140593d.player;

import etf.nim.mm140593d.game.GameMove;
import etf.nim.mm140593d.game.GamePlay;
import etf.nim.mm140593d.game.GameState;
import etf.nim.mm140593d.minimax.AlphaBetaPruning;

import java.time.Duration;
import java.time.Instant;

/**
 * Agent that plays the game using the AlphaBeta pruning algorithm.
 */
public class AlphaBetaAgent extends GamePlay {
    private AlphaBetaPruning algorithm = new AlphaBetaPruning();
    private int maxDepth;

    /**
     * Creates the agent object with the maximum search depth.
     *
     * @param maxDepth Maximum search depth
     */
    public AlphaBetaAgent(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    /**
     * Method that plays the move. Firstly, it constructs the game tree (up to maxDepth) and
     * then finds the best move to take.
     *
     * @param gameState Current state of the game
     */
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
