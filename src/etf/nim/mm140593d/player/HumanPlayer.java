package etf.nim.mm140593d.player;

import etf.nim.mm140593d.game.GamePlay;
import etf.nim.mm140593d.game.GameState;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HumanPlayer extends GamePlay {

    @Override public void doMove(GameState gameState) {
        while (true) {
            try {
                System.out.print("Enter the heap and value: ");

                int heap, value;
                BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(System.in));
                String line = bufferedReader.readLine();
                heap = Integer.parseInt(line.split(" ")[0]);
                value = Integer.parseInt(line.split(" ")[1]);
                if (gameState.changeAndCheck(heap - 1, value, false)) {
                    break;
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }
}
