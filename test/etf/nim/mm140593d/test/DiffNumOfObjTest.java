package etf.nim.mm140593d.test;

import etf.nim.mm140593d.game.GameState;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class DiffNumOfObjTest {

    @Test(expected = IllegalStateException.class)
    public void test1() {
        GameState state = new GameState(3);

        state.setObjectsNumber(0, 11);
    }

    @Test public void test2() {
        GameState state = new GameState(3);

        state.setObjectsNumber(0, 10);
        Assert.assertEquals((Integer) 10, state.getObjectsNumber(0));
    }

    @Test(expected = IllegalStateException.class)
    public void test3() {
        GameState state = new GameState(3);

        state.setObjectsNumber(1, 0);
    }

    @Test public void test4() throws NoSuchFieldException, IllegalAccessException {
        Field objs = GameState.class.getDeclaredField("ObjectsNumber");
        objs.setAccessible(true);

        GameState gameState = new GameState(3);

        int[] objsVal = {1, 1, 0};
        objs.set(gameState, objsVal);

        Assert.assertTrue(gameState.differentNumberOfObjects(1));
    }

    @Test
    public void test5() throws NoSuchFieldException, IllegalAccessException {
        Field objs = GameState.class.getDeclaredField("ObjectsNumber");
        objs.setAccessible(true);

        GameState gameState = new GameState(3);

        int[] objsVal = {2, 0, 1};
        objs.set(gameState, objsVal);

        Assert.assertTrue(gameState.differentNumberOfObjects(1));
    }
}
