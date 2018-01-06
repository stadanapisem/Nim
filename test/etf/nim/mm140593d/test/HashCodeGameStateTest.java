package etf.nim.mm140593d.test;

import etf.nim.mm140593d.game.GameState;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

public class HashCodeGameStateTest {

    @Test
    public void testHashCodeOfClone() {
        for (int heaps = 1; heaps <= 10; heaps++) {
            List<int[]> subsets = GenerateCombinations.generate(heaps);

            for (int i = 0; i < subsets.size(); i++) {
                GameState gameState = new GameState(heaps);
                gameState.initialize();

                for (int k = 0; k < subsets.get(i).length; k++) {
                    gameState.setObjectsNumber(k, subsets.get(i)[k]);
                }

                Assert.assertEquals(gameState.hashCode(), gameState.clone().hashCode());
                Assert.assertTrue(gameState.equals(gameState.clone()));
            }
        }
    }

    @Test
    public void testRandom1() throws NoSuchFieldException, IllegalAccessException {

        GameState gameState = new GameState(3);
        gameState.setObjectsNumber(0, 1);
        gameState.setObjectsNumber(1, 2);
        gameState.setObjectsNumber(2, 6);

        Field objs = GameState.class.getDeclaredField("ObjectsNumber");
        objs.setAccessible(true);
        GameState gameState1 = new GameState(4);
        objs.set(gameState1, new int[] {6, 2, 1, 0});

        Assert.assertEquals(gameState.hashCode(), gameState1.hashCode());
        Assert.assertTrue(gameState.equals(gameState1));
    }

    @Test
    public void testRandom2() throws NoSuchFieldException, IllegalAccessException {

        GameState gameState = new GameState(5);
        gameState.setObjectsNumber(0, 1);
        gameState.setObjectsNumber(1, 2);
        gameState.setObjectsNumber(2, 6);
        gameState.setObjectsNumber(3, 9);
        gameState.setObjectsNumber(4, 4);

        Field objs = GameState.class.getDeclaredField("ObjectsNumber");
        objs.setAccessible(true);
        GameState gameState1 = new GameState(10);
        objs.set(gameState1, new int[] {6, 2, 1, 0, 9, 0, 0, 0, 0, 4});

        Assert.assertEquals(gameState.hashCode(), gameState1.hashCode());
        Assert.assertTrue(gameState.equals(gameState1));
    }
}
