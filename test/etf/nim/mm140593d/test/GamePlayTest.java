package etf.nim.mm140593d.test;

import etf.nim.mm140593d.game.GamePlay;
import etf.nim.mm140593d.game.GameState;
import etf.nim.mm140593d.player.AlphaBetaAgent;
import etf.nim.mm140593d.player.QAgent;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GamePlayTest {

    public static final int heapsNumber = 2;

    /**
     * First player wins <=> xor of all objects is not 0
     *
     * @param objs
     * @return
     */
    private boolean isNonZero(int[] objs) {
        return Arrays.stream(objs).reduce((x, y) -> x ^ y).getAsInt() != 0;
    }

    @Test public void test() {
        GamePlay player1 = new QAgent(0, false);
        GamePlay player2 = new AlphaBetaAgent(6);

        GameState gameState = new GameState(heapsNumber);

        player1.loadState();
        player2.loadState();
        Integer game = 0;
        Integer didnt = 0;
        int[] wins = new int[2];

        List<int[]> data = GenerateCombinations.generate(heapsNumber);

        for (int[] objValues : data) {
            gameState.initialize();
            for (int i = 0; i < objValues.length; i++) {
                gameState.setObjectsNumber(i, objValues[i]);
            }

            boolean isNonZero = isNonZero(objValues);

            while (gameState.isNotFinished()) {
                gameState.checkWinCondition();

                if (!gameState.isNotFinished()) {
                    break;
                }
                player1.doMove(gameState);
                gameState.increaseMoveNumber();

                gameState.changePlayer();
                gameState.checkWinCondition();

                if (!gameState.isNotFinished()) {
                    break;
                }

                player2.doMove(gameState);

                gameState.increaseMoveNumber();
                gameState.changePlayer();
            }

            System.out.println(game++ + " winner: " + (gameState.getWinnerPlayer() + 1));
            wins[gameState.getWinnerPlayer()]++;

            player1.saveState(gameState.getWinnerPlayer());
            player2.saveState(gameState.getWinnerPlayer());

            if (isNonZero && gameState.getWinnerPlayer() != 0) {
                //System.err.println("DIDN'T WIN!");
                didnt++;
            }
        }

        System.out.println("---------------------------------");
        System.out.println(
            "player1: " + ((double) wins[0] / game) + " player2: " + ((double) wins[1] / game));
        System.out.println("DIDNT " + didnt);
    }
}
