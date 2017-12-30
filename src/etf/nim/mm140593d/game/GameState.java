package etf.nim.mm140593d.game;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private static final Integer MAX_HEAPS = 10;
    private static final Integer MAX_OBJECTS = 10;

    private int heapsNumber;
    private int[] ObjectsNumber;

    private int currentPlayer;
    private int winnerPlayer;

    private boolean notFinished;
    private int moveNumber;

    private int[] lastValuePlayer;

    public GameState(int heapsNumber) throws IllegalStateException {
        if (heapsNumber > MAX_HEAPS || heapsNumber <= 0) {
            throw new IllegalStateException("Too many heaps");
        }

        this.heapsNumber = heapsNumber;
        this.ObjectsNumber = new int[heapsNumber];
        this.lastValuePlayer = new int[2];
        this.moveNumber = 0;
    }

    @Override public GameState clone() {
        GameState newGameState = new GameState(this.heapsNumber);
        newGameState.ObjectsNumber = this.ObjectsNumber.clone();
        newGameState.lastValuePlayer = this.lastValuePlayer.clone();
        newGameState.currentPlayer = this.currentPlayer;
        newGameState.notFinished = true;
        newGameState.winnerPlayer = this.winnerPlayer;

        return newGameState;
    }

    public void initialize() {
        lastValuePlayer[0] = lastValuePlayer[1] = 10;
        currentPlayer = 0;
        winnerPlayer = -1;
        notFinished = true;
    }

    public boolean differentNumberOfObjects(int num) {
        for (int i = 0; i < num - 1; i++) {
            for (int j = i + 1; j < num; j++) {
                if (ObjectsNumber[i] == ObjectsNumber[j])
                    return false;
            }
        }

        return true;
    }

    public boolean changeAndCheck(GameMove gameMove, boolean justCheck)
        throws IllegalStateException {
        return changeAndCheck(gameMove.getHeap(), gameMove.getObjects(), justCheck);
    }

    public boolean changeAndCheck(int idx, int val, boolean justCheck)
        throws IllegalStateException {
        if (val > 2 * lastValuePlayer[(currentPlayer + 1) % 2]) {
            throw new IllegalStateException("Cannot take this much (2 * last player)");
        }

        if (idx < 0 || idx >= heapsNumber) {
            throw new IllegalStateException("Wrong heap!");
        }

        if (ObjectsNumber[idx] < val) {
            throw new IllegalStateException("You can't remove that much!");
        }

        int tmpValue = ObjectsNumber[idx];
        ObjectsNumber[idx] -= val;

        for (int i = 0; i < heapsNumber - 1; i++) {
            for (int j = i + 1; j < heapsNumber; j++) {
                if (ObjectsNumber[i] == ObjectsNumber[j] && ObjectsNumber[i] != 0) {
                    ObjectsNumber[idx] = tmpValue;
                    return false;
                }
            }
        }

        if (justCheck) {
            ObjectsNumber[idx] = tmpValue;
        } else {
            lastValuePlayer[currentPlayer] = val;
        }

        return true;
    }

    public void checkWinCondition() {
        boolean winFlag = true;

        for (int i = 0; i < heapsNumber; i++) {
            if (ObjectsNumber[i] != 0) {
                winFlag = false;
            }
        }

        /*if (winFlag) {
            winnerPlayer = (currentPlayer + 1) % 2;
            notFinished = false;
            return ;
        }

        value_loop:
        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j < heapsNumber; j++) {
                if (changeAndCheck(j, i, true)) {
                    winFlag = false;
                    break value_loop;
                }
            }
        }*/

        if (winFlag) {
            if (moveNumber == 0) {
                winnerPlayer = currentPlayer;
            } else {
                winnerPlayer = (currentPlayer + 1) % 2;
            }

            notFinished = false;
        }
    }

    public List<GameMove> getAllPossibleMoves() {
        List<GameMove> possibleMoves = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j < heapsNumber; j++) {
                try {
                    if (changeAndCheck(j, i, true)) {
                        possibleMoves.add(new GameMove(j, i));
                    }
                } catch (IllegalStateException e) {
                    // Expected
                }
            }
        }

        return possibleMoves;
    }

    public GameState applyMove(GameMove move) {
        GameState result = this.clone();

        result.changeAndCheck(move.getHeap(), move.getObjects(), false);
        result.checkWinCondition(); // ne radi kada moze da pobedi u prvom potezu

        return result;
    }

    public void changePlayer() {
        currentPlayer = (currentPlayer + 1) % 2;
    }

    public int getHeapsNumber() {
        return heapsNumber;
    }

    public Integer getObjectsNumber(int idx) {
        if (idx >= 0 && idx < heapsNumber) {
            return ObjectsNumber[idx];
        }

        return null;
    }

    public void setObjectsNumber(int idx, int value) throws IllegalStateException {
        if (idx >= 0 && idx < heapsNumber && value > 0 && value <= MAX_OBJECTS) {
            ObjectsNumber[idx] = value;
        } else {
            throw new IllegalStateException("Bad index or value");
        }
    }

    public int[] getObjectsNumber() {
        return ObjectsNumber;
    }

    public int getWinnerPlayer() {
        return winnerPlayer;
    }

    public boolean isNotFinished() {
        return notFinished;
    }

    public void increaseMoveNumber() {
        moveNumber++;
    }

    public void decreaseMoveNumber() {
        moveNumber--;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < heapsNumber; i++) {
            sb.append("Heap " + (i + 1) + ": " + ObjectsNumber[i] + "\n");
        }

        return sb.toString();
    }
}
