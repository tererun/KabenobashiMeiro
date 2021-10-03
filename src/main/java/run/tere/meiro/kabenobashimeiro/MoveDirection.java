package run.tere.meiro.kabenobashimeiro;

public enum MoveDirection {
    UP(0, 2),
    RIGHT(2, 0),
    DOWN(0, -2),
    LEFT(-2, 0);

    private int addX;
    private int addY;

    MoveDirection(int addX, int addY) {
        this.addX = addX;
        this.addY = addY;
    }

    public int getAddX() {
        return addX;
    }

    public int getAddY() {
        return addY;
    }
}
