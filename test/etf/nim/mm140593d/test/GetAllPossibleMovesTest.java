package etf.nim.mm140593d.test;

import etf.nim.mm140593d.game.GameState;
import org.junit.Test;

import java.lang.reflect.Field;

public class GetAllPossibleMovesTest {

    @Test
    public void simple() {
        GameState gameState = new GameState(3);
        gameState.initialize();
        gameState.setObjectsNumber(0, 1);
        gameState.setObjectsNumber(1, 2);
        gameState.setObjectsNumber(2, 3);

        gameState.getAllPossibleMoves().forEach(gameMove -> System.out.println(gameMove.toString()));
    }

    @Test
    public void notSoSimple() throws NoSuchFieldException, IllegalAccessException {
        Field objs = GameState.class.getDeclaredField("ObjectsNumber");
        objs.setAccessible(true);

        GameState gameState = new GameState(3);
        gameState.initialize();

        int[] objsVal = {0, 1, 3};
        objs.set(gameState, objsVal);

        gameState.getAllPossibleMoves().forEach(gameMove -> System.out.println(gameMove.toString()));
    }

    @Test
    public void test3() throws NoSuchFieldException, IllegalAccessException {
        Field objs = GameState.class.getDeclaredField("ObjectsNumber");
        objs.setAccessible(true);

        GameState gameState = new GameState(3);
        gameState.initialize();

        int[] objsVal = {4, 3, 1};
        objs.set(gameState, objsVal);

        gameState.getAllPossibleMoves().forEach(gameMove -> System.out.println(gameMove.toString()));
    }

    @Test
    public void test4() throws NoSuchFieldException, IllegalAccessException {
        Field objs = GameState.class.getDeclaredField("ObjectsNumber");
        objs.setAccessible(true);

        GameState gameState = new GameState(3);
        gameState.initialize();

        int[] objsVal = {7, 2, 1};
        objs.set(gameState, objsVal);

        gameState.getAllPossibleMoves().forEach(gameMove -> System.out.println(gameMove.toString()));
    }
}
