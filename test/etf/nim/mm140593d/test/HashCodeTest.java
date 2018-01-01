package etf.nim.mm140593d.test;

import etf.nim.mm140593d.game.GameState;
import org.junit.Test;

public class HashCodeTest {

    @Test
    public void test1() {

        for (int heaps = 0; heaps < 10; heaps++) {
            GameState gameState = new GameState(heaps);
            gameState.initialize();

            //for (int objs = 0; objs < 10; objs++)
        }
    }
}
