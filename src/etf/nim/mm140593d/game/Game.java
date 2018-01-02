package etf.nim.mm140593d.game;

import etf.nim.mm140593d.player.AlphaBetaComputerPlayer;
import etf.nim.mm140593d.player.HumanPlayer;
import etf.nim.mm140593d.player.QAgent;
import etf.nim.mm140593d.player.SimpleComputerPlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Game {
    private GameState gameState = null;
    private int playerMode[];
    private int treeDepth[];

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

        playerMode = new int[2];
        treeDepth = new int[2];
        treeDepth[0] = treeDepth[1] = 2147483647;

        while (true) {
            try {
                System.out.println("Choose Player 1");
                System.out.print("\t1. Human\n\t2. Simple Computer\n\t3. Intermediate Computer\n\t"
                    + "4. Computer Champion\nEnter a number [1 - 4]: ");
                playerMode[0] = Integer.parseInt(bufferedReader.readLine());

                break;
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

        if (playerMode[0] == 2 || playerMode[0] == 3) {
            while (true) {
                try {
                    System.out.print("Enter maximum search depth [1 - 2^31] (default is 2^31): ");
                    String line = bufferedReader.readLine();
                    if (!line.equals("")) {
                        treeDepth[0] = Integer.parseInt(line);
                    }

                    break;
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        while (true) {
            try {
                System.out.println("Choose Player 2");
                System.out.print("\t1. Human\n\t2. Simple Computer\n\t3. Intermediate Computer\n\t"
                    + "4. Computer Champion\nEnter a number [1 - 4]: ");
                playerMode[1] = Integer.parseInt(bufferedReader.readLine());

                break;
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

        if (playerMode[1] == 2 || playerMode[1] == 3) {
            while (true) {
                try {
                    System.out.print("Enter maximum search depth [1 - 2^31] (default is 2^31): ");
                    String line = bufferedReader.readLine();
                    if (!line.equals("")) {
                        treeDepth[1] = Integer.parseInt(line);
                    }

                    break;
                } catch (Exception e) {
                    e.printStackTrace(System.err);
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
        GamePlay player1 = null;
        GamePlay player2 = null;

        if (playerMode[0] == 1) {
            player1 = new HumanPlayer();
        } else if (playerMode[0] == 2) {
            player1 = new SimpleComputerPlayer(treeDepth[0]);
        } else if (playerMode[0] == 3) {
            player1 = new AlphaBetaComputerPlayer(treeDepth[0]);
        } else if (playerMode[0] == 4) {
            player1 = new QAgent(0, true);
        }

        if (playerMode[1] == 1) {
            player2 = new HumanPlayer();
        } else if (playerMode[1] == 2) {
            player2 = new SimpleComputerPlayer(treeDepth[1]);
        } else if (playerMode[1] == 3) {
            player2 = new AlphaBetaComputerPlayer(treeDepth[1]);
        } else if (playerMode[1] == 4) {
            player2 = new QAgent(1, false);
        }

        player1.loadState();
        player2.loadState();

        while (gameState.isNotFinished()) {
            gameState.checkWinCondition();

            if (!gameState.isNotFinished()) {
                break;
            }

            System.out.println("-----------------------------------------");
            System.out.println("Player 1");

            System.out.println(gameState.toString());
            player1.doMove(gameState);
            gameState.increaseMoveNumber();

            gameState.changePlayer();
            gameState.checkWinCondition();

            if (!gameState.isNotFinished()) {
                break;
            }

            System.out.println("-----------------------------------------");
            System.out.println("Player 2");

            System.out.println(gameState.toString());
            player2.doMove(gameState);

            gameState.increaseMoveNumber();
            gameState.changePlayer();
        }

        player1.saveState(gameState.getWinnerPlayer());
        player2.saveState(gameState.getWinnerPlayer());

        System.out.printf("Player %d wins!!!", gameState.getWinnerPlayer() + 1);
    }
}
