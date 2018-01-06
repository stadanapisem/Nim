package etf.nim.mm140593d.game;

import java.io.Serializable;

public class GameMove implements Serializable {
    private int heap;
    private int objects;
    private int numberOnHeap;

    public GameMove(int heap, int objects, int numberOnHeap) {
        this.heap = heap;
        this.objects = objects;
        this.numberOnHeap = numberOnHeap;
    }

    public int getHeap() {
        return heap;
    }

    public void setHeap(int heap) {
        this.heap = heap;
    }

    public int getObjects() {
        return objects;
    }

    public void setObjects(int objects) {
        this.objects = objects;
    }

    public int getNumberOnHeap() {
        return numberOnHeap;
    }

    @Override
    public boolean equals(Object obj) {
        boolean flag = true;
        GameMove gameMove = (GameMove) obj;

        if (objects != gameMove.objects) {
            flag = false;
        }

        if (numberOnHeap != gameMove.numberOnHeap) {
            flag = false;
        }

        return flag;
    }

    @Override
    public int hashCode() {
        return 31 * (objects + 11 * numberOnHeap);
    }

    @Override public String toString() {
        return "From " + numberOnHeap + " taking " + objects;
    }
}
