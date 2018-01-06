package etf.nim.mm140593d.test;

import etf.nim.mm140593d.game.GamePlay;
import etf.nim.mm140593d.game.GameState;
import etf.nim.mm140593d.player.AlphaBetaAgent;
import etf.nim.mm140593d.player.QAgent;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parallelized.class) public class GamePlayQAgentTest {
    private Integer heapsNumber;

    public GamePlayQAgentTest(Integer heapsNumber) {
        this.heapsNumber = heapsNumber;
    }

    @Parameterized.Parameters public static Collection heaps() {
        return Arrays.asList(new Object[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
    }

    @Ignore @Test public void test() {
        GamePlay player1 = new QAgent(0, false);
        GamePlay player2 = new AlphaBetaAgent(3);

        GameState gameState = new GameState(heapsNumber);

        player1.loadState();
        player2.loadState();
        Integer game = 0;
        int[] wins = new int[2];

        List<int[]> data = GenerateCombinations.generate(heapsNumber);
        System.setOut(new PrintStream(new OutputStream() {
            @Override public void write(int i) throws IOException {

            }
        }));

        for (int[] objValues : data) {
            gameState.initialize();
            for (int i = 0; i < objValues.length; i++) {
                gameState.setObjectsNumber(i, objValues[i]);
            }

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

            game++;
            // System.err.println(game++ + " winner: " + (gameState.getWinnerPlayer() + 1));
            wins[gameState.getWinnerPlayer()]++;

            //player1.saveState(gameState.getWinnerPlayer());
            // player2.saveState(gameState.getWinnerPlayer());
        }

        System.err.println("---------------------------------");
        System.err.println(
            heapsNumber + "\nplayer1: " + ((double) wins[0] / game) + " player2: " + (
                (double) wins[1] / game));
    }
}
