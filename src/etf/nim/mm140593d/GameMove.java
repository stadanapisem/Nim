package etf.nim.mm140593d;

public class GameMove {
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
}
