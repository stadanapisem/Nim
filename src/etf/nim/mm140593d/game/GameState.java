package etf.nim.mm140593d.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class responsible with keeping the state of the game.
 */
public class GameState implements Serializable {

    /**
     * The maximum number of heaps.
     */
    private static final Integer MAX_HEAPS = 10;

    /**
     * The maximum number of objects per heap.
     */
    private static final Integer MAX_OBJECTS = 10;

    /**
     * Number of heaps for this game.
     */
    private int heapsNumber;

    /**
     * Array of number of objects per heap.
     */
    private int[] ObjectsNumber;

    /**
     * Current player id.
     */
    private int currentPlayer;

    /**
     * ID of the winning player.
     */
    private int winnerPlayer;

    /**
     * Flag representing the game running state.
     */
    private boolean notFinished;

    /**
     * Current move.
     */
    private int moveNumber;

    /**
     * Last value of objects that the corresponding player took in his last move.
     */
    private int[] lastValuePlayer;

    /**
     * Creates the game state object.
     *
     * @param heapsNumber Number of heaps in the game
     * @throws IllegalStateException Thrown if number of heaps is invalid
     */
    public GameState(int heapsNumber) throws IllegalStateException {
        if (heapsNumber > MAX_HEAPS || heapsNumber <= 0) {
            throw new IllegalStateException("Too many heaps");
        }

        this.heapsNumber = heapsNumber;
        this.ObjectsNumber = new int[heapsNumber];
        this.lastValuePlayer = new int[2];
        this.moveNumber = 0;

        for (int i = 0; i < heapsNumber; i++)
            ObjectsNumber[i] = 0;
    }

    /**
     * Method for making a deep copy of the gameState object.
     *
     * @return Deep copy of the object
     */
    @Override public GameState clone() {
        GameState newGameState = new GameState(this.heapsNumber);
        newGameState.ObjectsNumber = this.ObjectsNumber.clone();
        newGameState.lastValuePlayer = this.lastValuePlayer.clone();
        newGameState.currentPlayer = this.currentPlayer;
        newGameState.notFinished = true;
        newGameState.winnerPlayer = this.winnerPlayer;

        return newGameState;
    }

    /**
     * Sets the currentPlayer, winnerPlayer, notFinished flag and lastValuePlayer to their initial values.
     */
    public void initialize() {
        lastValuePlayer[0] = lastValuePlayer[1] = 10;
        currentPlayer = 0;
        winnerPlayer = -1;
        notFinished = true;
    }

    /**
     * Checks whether the number of objects is different on each heap (except 0).
     *
     * @param num Checks only first num heaps
     * @return {@link Boolean} True or False
     */
    public boolean differentNumberOfObjects(int num) {
        for (int i = 0; i < num - 1; i++) {
            for (int j = i + 1; j < num; j++) {
                if (ObjectsNumber[i] == ObjectsNumber[j])
                    return false;
            }
        }

        return true;
    }

    public boolean changeAndCheck(boolean justCheck, int val, int maxVal) {
        for (int i = 0; i < heapsNumber; i++) {
            if (ObjectsNumber[i] == maxVal) {
                return changeAndCheck(i, val, justCheck);
            }
        }

        return false;
    }

    /**
     * Same as {@link #changeAndCheck(int, int, boolean)}.
     *
     * @param gameMove  {@link GameMove} Move to be played
     * @param justCheck {@link Boolean} Just check or change the current state
     * @return {@link Boolean} Whether the move can be executed or not
     * @throws IllegalStateException Thrown in case the move is not valid
     */
    public boolean changeAndCheck(GameMove gameMove, boolean justCheck)
        throws IllegalStateException {
        return changeAndCheck(gameMove.getHeap(), gameMove.getObjects(), justCheck);
    }

    /**
     * Checks whether the move is valid and can be applied to the current state.
     *
     * @param idx       {@link Integer} Index of the heap
     * @param val       {@link Integer} Number of objects to take
     * @param justCheck {@link Boolean} Just check or change the state
     * @return {@link Boolean} Can be done or not
     * @throws IllegalStateException Thrown in case the move is not valid
     */
    public boolean changeAndCheck(int idx, int val, boolean justCheck)
        throws IllegalStateException {
        if (val > 2 * lastValuePlayer[(currentPlayer + 1) % 2]) {
            throw new IllegalStateException(
                "Cannot take this much (2 * last player) " + idx + " " + val + "\n" + this
                    .toString());
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

    /**
     * Checks whether the win condition is met, and sets the {@link #winnerPlayer} as well as {@link #notFinished} flag.
     */
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

    /**
     * Returns all possible moves that can be executed from the current state.
     *
     * @return {@link List} < {@link GameMove} > List of the possible moves
     */
    public List<GameMove> getAllPossibleMoves() {
        List<GameMove> possibleMoves = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j < heapsNumber; j++) {
                try {
                    if (changeAndCheck(j, i, true)) {
                        possibleMoves.add(new GameMove(j, i, getObjectsNumber(j)));
                    }
                } catch (IllegalStateException e) {
                    // Expected
                }
            }
        }

        return possibleMoves;
    }

    /**
     * Applies the move to the copy of the current state.
     *
     * @param move {@link GameMove} Move to be applied
     * @return {@link GameState} Copy of the current state with the move applied
     */
    public GameState applyMove(GameMove move) {
        GameState result = this.clone();

        result.changeAndCheck(move.getHeap(), move.getObjects(), false);
        result.checkWinCondition();

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

    @Override public boolean equals(Object obj) {
        boolean flag = true;
        GameState gameState = (GameState) obj;

        if (gameState.notFinished != this.notFinished) {
            flag = false;
        }

        Integer[] objects = Arrays.stream(this.getObjectsNumber()).boxed().toArray(Integer[]::new);
        Arrays.sort(objects, Collections.reverseOrder());

        Integer[] objects2 =
            Arrays.stream(gameState.getObjectsNumber()).boxed().toArray(Integer[]::new);
        Arrays.sort(objects2, Collections.reverseOrder());

        for (int i = 0; i < this.heapsNumber && objects[i] != 0; i++) {
            if (!objects2[i].equals(objects[i])) {
                flag = false;
            }
        }

        return flag;
    }

    @Override public int hashCode() {
        int result = 3;

        int c = this.notFinished ? 0 : 1;

        Integer[] objects = Arrays.stream(this.getObjectsNumber()).boxed().toArray(Integer[]::new);
        Arrays.sort(objects, Collections.reverseOrder());

        for (int i = 0; i < this.heapsNumber && objects[i] != 0; i++) {
            c += 11 * i + objects[i];
        }

        return 37 * result + c;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < heapsNumber; i++) {
            sb.append("Heap " + (i + 1) + ": " + ObjectsNumber[i] + "\n");
        }

        return sb.toString();
    }
}
