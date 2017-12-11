package etf.nim.mm140593d;

public class GameState {

    private static final Integer MAX_HEAPS = 10;
    private static final Integer MAX_OBJECTS = 10;

    private int heapsNumber;
    private int[] ObjectsNumber;

    private int currentPlayer;
    private int winnerPlayer;

    private boolean notFinished;

    private int[] lastValuePlayer;

    public GameState(int heapsNumber) throws IllegalStateException {
        if (heapsNumber > MAX_HEAPS || heapsNumber <= 0) {
            throw new IllegalStateException("Too many heaps");
        }

        this.heapsNumber = heapsNumber;
        this.ObjectsNumber = new int[heapsNumber];
        this.lastValuePlayer = new int[2];
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

    public boolean changeAndCheck(int idx, int val, boolean justCheck) throws IllegalStateException {
        if (val > 2 * lastValuePlayer[(currentPlayer + 1) % 2]) {
            throw new IllegalStateException("Cannot take this much (2 * last player)");
        }

        int tmpValue = ObjectsNumber[idx];
        ObjectsNumber[idx] = val;

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

        if (winFlag) {
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
        }

        if (winFlag) {
            winnerPlayer = (currentPlayer + 1) % 2;
            notFinished = false;
        }
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

    public int getWinnerPlayer() {
        return winnerPlayer;
    }

    public boolean isNotFinished() {
        return notFinished;
    }
}
