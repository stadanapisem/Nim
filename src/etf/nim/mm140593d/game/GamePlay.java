package etf.nim.mm140593d.game;

/**
 * Abstract class that defines the required contract between players and the game.
 */
public abstract class GamePlay {
    public abstract void doMove(GameState gameState);

    public void saveState(int playerID) {

    }

    public void loadState() {

    }
}
