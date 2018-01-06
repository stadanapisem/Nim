package etf.nim.mm140593d.test;

import etf.nim.mm140593d.game.GameMove;
import org.junit.Assert;
import org.junit.Test;

public class HashCodeGameMoveTest {

    @Test public void testRandom1() {
        GameMove gameMove = new GameMove(3, 1, 5);
        GameMove gameMove1 = new GameMove(6, 1, 5);

        Assert.assertEquals(gameMove.hashCode(), gameMove1.hashCode());
        Assert.assertTrue(gameMove.equals(gameMove1));
    }

    @Test public void testRandom2() {
        GameMove gameMove = new GameMove(1, 1, 5);
        GameMove gameMove1 = new GameMove(2, 2, 5);

        Assert.assertNotEquals(gameMove.hashCode(), gameMove1.hashCode());
        Assert.assertFalse(gameMove.equals(gameMove1));
    }

    @Test public void testRandom3() {
        GameMove gameMove = new GameMove(1, 1, 5);
        GameMove gameMove1 = new GameMove(1, 1, 7);

        Assert.assertNotEquals(gameMove.hashCode(), gameMove1.hashCode());
        Assert.assertFalse(gameMove.equals(gameMove1));
    }
}
