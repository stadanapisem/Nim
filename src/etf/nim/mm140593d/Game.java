package etf.nim.mm140593d;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Game {
    private GameState gameState = null;

    public void gameInitialization() {
        System.out.println("New Game:\n");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.print("Choose number of heaps: ");
                int heapsNumber = Integer.parseInt(bufferedReader.readLine());
                gameState = new GameState(heapsNumber);

                break;
            } catch (IllegalStateException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println("Exception reading heaps number!");
            }
        }

        for (int i = 1; i <= gameState.getHeapsNumber(); i++) {
            while (true) {
                try {
                    System.out.printf("Choose number of objects for heap %d: ", i);
                    gameState.setObjectsNumber(i - 1, Integer.parseInt(bufferedReader.readLine()));

                    if (gameState.differentNumberOfObjects(i)) {
                        break;
                    }

                    System.err.println("Value not allowed!");
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (Exception e) {
                    System.err.println("Exception reading objects number!");
                }
            }
        }

        System.out.println("-----------------------------------------");
        System.out.println("GAME PARAMETERS: ");
        System.out.println("-----------------------------------------");
        for (int i = 0; i < gameState.getHeapsNumber(); i++) {
            System.out.printf("Heap %d: %d\n", i + 1, gameState.getObjectsNumber(i));
        }

        gamePlay();
    }

    private void gamePlay() {
        gameState.initialize();

        while (gameState.isNotFinished()) {
            gameState.checkWinCondition();

            if (!gameState.isNotFinished()) {
                break;
            }

        }

        System.out.printf("Player%d wins!!!", gameState.getWinnerPlayer() + 1);
    }
}
