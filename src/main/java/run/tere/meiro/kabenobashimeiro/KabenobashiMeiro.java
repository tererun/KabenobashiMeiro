package run.tere.meiro.kabenobashimeiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class KabenobashiMeiro {
    public static void main(String[] args) {
        new KabenobashiMeiro().run();
    }

    private final int maxX;
    private final int maxY;

    private int[][] field;
    private List<Structure> nodes;
    private List<Structure> path;

    public KabenobashiMeiro() {
        this.maxX = 15;
        this.maxY = 15;
        this.field = new int[this.maxX][this.maxY];
        this.nodes = new ArrayList<>();
        this.path = new ArrayList<>();
        for (int x = 0; x < this.maxX; x++) {
            for (int y = 0; y < this.maxY; y++) {
                Structure structure = new Structure(x, y);
                if (x == 0 || y == 0 || x == (this.maxX - 1) || y == (this.maxY - 1)) {
                    this.field[x][y] = 1;
                } else if (x % 2 == 0 && y % 2 == 0) {
                    this.nodes.add(structure);
                    this.field[x][y] = 1;
                } else {
                    this.field[x][y] = 0;
                }
            }
        }
    }

    public void run() {
        makeWall();
        for (int y = 0; y < this.maxY; y++) {
            for (int x = 0; x < this.maxX; x++) {
                int value = field[x][y];
                String print;
                if (value == 0) {
                    print = "   ";
                } else if (value == 1) {
                    print = "@@ ";
                } else {
                    print = "?? ";
                }
                System.out.print(print);
            }
            System.out.print("\n");
        }
    }

    public void makeWall() {
        if (this.nodes.size() <= 0) return;
        Collections.shuffle(this.nodes);
        Structure structure = this.nodes.get(0);
        this.path.add(structure);
        this.nodes.remove(structure);
        move();
    }

    public void move() {
        Structure structure = this.path.get(this.path.size() - 1);
        List<MoveDirection> canMoveDirections = getCanMoveDirections(structure);
        if (canMoveDirections.size() <= 0) {
            this.path.remove(structure);
            this.nodes.add(structure);
            move();
            return;
        }
        Collections.shuffle(canMoveDirections);
        MoveDirection moveDirection = canMoveDirections.get(0);
        Structure nextMoveStructure = new Structure(structure.getX() + moveDirection.getAddX(), structure.getY() + moveDirection.getAddY());
        Structure nodeStructure = this.getNodeStructure(nextMoveStructure.getX(), nextMoveStructure.getY());
        //System.out.println(structure.getX() + ", " + structure.getY());
        //System.out.println("nextMoveStructure = " + nextMoveStructure.getX() + ", " + nextMoveStructure.getY());
        //if (nodeStructure == null) System.out.println("null");
        if (nodeStructure != null) {
            this.nodes.remove(nodeStructure);
            this.path.add(nextMoveStructure);
            move();
            return;
        } else if (this.field[nextMoveStructure.getX()][nextMoveStructure.getY()] == 1) {
            for (Structure wallStructure : this.path) {
                int x = wallStructure.getX();
                int y = wallStructure.getY();
                field[x][y] = 1;
                //System.out.println("x = " + x);
                //System.out.println("y = " + y);
                int index = this.path.indexOf(wallStructure);
                //System.out.println("index = " + index);
                if (index == 0) {
                    int beforeX = nextMoveStructure.getX();
                    int beforeY = nextMoveStructure.getY();
                    int spaceX = beforeX;
                    int spaceY = beforeY;
                    if (beforeX != x) spaceX = Math.min(nextMoveStructure.getX(), wallStructure.getX()) + 1;
                    if (beforeY != y) spaceY = Math.min(nextMoveStructure.getY(), wallStructure.getY()) + 1;
                    //System.out.println(spaceX + ", " + spaceY);
                    field[spaceX][spaceY] = 1;
                    if (spaceX % 2 != 0 && spaceY % 2 != 0) System.out.println(1);
                } else {
                    int beforeIndex = index - 1;
                    Structure beforeStructure = this.path.get(beforeIndex);
                    int beforeX = beforeStructure.getX();
                    int beforeY = beforeStructure.getY();
                    int spaceX = beforeX;
                    int spaceY = beforeY;
                    if (beforeX != x) spaceX = Math.min(beforeStructure.getX(), wallStructure.getX()) + 1;
                    if (beforeY != y) spaceY = Math.min(beforeStructure.getY(), wallStructure.getY()) + 1;
                    //System.out.println(spaceX + ", " + spaceY);
                    field[spaceX][spaceY] = 1;
                    if (spaceX % 2 != 0 && spaceY % 2 != 0) System.out.println(2);
                }
            }
            this.path.clear();
            if (this.nodes.size() == 0) {
                System.out.println("終わり");
            } else {
                makeWall();
                return;
            }
        }
        //System.out.println("なんでお前ここにいるの");
    }

    public List<MoveDirection> getMoveDirections() {
        return new CopyOnWriteArrayList<>(List.of(MoveDirection.values()));
    }

    public List<MoveDirection> getCanMoveDirections(Structure structure) {
        List<MoveDirection> moveDirections = this.getMoveDirections();
        for (MoveDirection moveDirection : moveDirections) {
            Structure testStructure = new Structure(structure.getX() + moveDirection.getAddX(), structure.getY() + moveDirection.getAddY());
            if (this.getPathStructure(testStructure.getX(), testStructure.getY()) != null) {
                moveDirections.remove(moveDirection);
            }
            //if (testStructure.getX() < 0 || testStructure.getY() < 0 ||testStructure.getX() >= this.maxX || testStructure.getY() >= this.maxY) moveDirections.remove(moveDirection);
        }
        return moveDirections;
    }

    private Structure getNodeStructure(int x, int y) {
        for (Structure structure : this.nodes) {
            if (structure.getX() == x && structure.getY() == y) return structure;
        }
        return null;
    }

    private Structure getPathStructure(int x, int y) {
        for (Structure structure : this.path) {
            if (structure.getX() == x && structure.getY() == y) return structure;
        }
        return null;
    }
}
