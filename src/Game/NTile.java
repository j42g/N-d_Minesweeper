package Game;

import java.util.ArrayList;

public class NTile {

    private final int index;
    private final int[] coords;
    private NTile[] neighbours;
    private boolean revealed;
    private boolean marked;
    private final boolean bomb;
    private int count;

    public NTile(int index, int[] coords, boolean bomb){
        this.index = index;
        this.coords = coords;
        this.revealed = false;
        this.marked = false;
        this.bomb = bomb;
        this.count = 0;
    }

    boolean isBomb(){
        return this.bomb;
    }

    void addNeighbours(ArrayList<NTile> neighbours){
        for(int i = 0; i < neighbours.size(); i++){
            this.neighbours[i] = neighbours.get(i);
            this.count++;
        }
    }

    public int getCount() {
        return this.count;
    }

    public boolean isMarked() {
        return false;
    }

    public void reveal(){
        this.revealed = true;
    }

    public boolean isRevealed() {
        return false;
    }

    public int getIndex(){
        return this.index;
    }

    public int[] getCoords() {
        return this.coords;
    }

    public NTile[] getNeighbours() {
        return this.neighbours;
    }

    public void changeMarked() {
        this.marked = !this.marked;
    }
}
