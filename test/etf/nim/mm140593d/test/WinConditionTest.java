package etf.nim.mm140593d.test;

import etf.nim.mm140593d.game.GameState;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class WinConditionTest {

    @Test
    public void test1() {
        GameState gameState = new GameState(3);
        gameState.initialize();
        gameState.setObjectsNumber(0, 1);
        gameState.setObjectsNumber(1, 2);
        gameState.setObjectsNumber(2, 3);

        gameState.checkWinCondition();
        Assert.assertTrue(gameState.isNotFinished());
    }

    @Test
    public void test2() throws NoSuchFieldException, IllegalAccessException {
        Field objs = GameState.class.getDeclaredField("ObjectsNumber");
        objs.setAccessible(true);

        GameState gameState = new GameState(3);
        gameState.initialize();

        int[] objsVal = {0, 0, 0};
        objs.set(gameState, objsVal);

        gameState.checkWinCondition();
        Assert.assertFalse(gameState.isNotFinished());
    }

    @Test
    public void test3() throws NoSuchFieldException, IllegalAccessException {
        Field objs = GameState.class.getDeclaredField("ObjectsNumber");
        objs.setAccessible(true);

        GameState gameState = new GameState(3);
        gameState.initialize();

        int[] objsVal = {0, 0, 1};
        objs.set(gameState, objsVal);

        gameState.checkWinCondition();
        Assert.assertTrue(gameState.isNotFinished());
    }

}
