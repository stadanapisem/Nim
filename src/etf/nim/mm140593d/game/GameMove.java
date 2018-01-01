package etf.nim.mm140593d.game;

import java.io.Serializable;

public class GameMove implements Serializable {
    private int heap;
    private int objects;

    public GameMove(int heap, int objects) {
        this.heap = heap;
        this.objects = objects;
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

    @Override public String toString() {
        return "Heap: " + (heap + 1) + " objs: " + objects;
    }
}
