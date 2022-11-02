package Game;

import java.util.ArrayList;

public class NGrid {

    public final static int LOST = -2;
    public final static int MOVE_VALID = 0;
    public final static int MOVE_INVALID = -1;
    public final static int WON = 1;
    private final NTile[] field;
    private final int totalSquares;
    private final int totalBombs;
    private final int dimension;
    private final int[] dimensions;
    private int markedBombCount;
    private final ArrayList<NTile> revealed;
    private boolean lost;
    private boolean won;
    private boolean firstMove;

    public NGrid(int[] dimensions, int bombs){
        this.dimension = dimensions.length;
        this.dimensions = dimensions;
        int tempTotal = 1;
        for(int i : dimensions){
            tempTotal *= i;
        }
        this.totalSquares = tempTotal;
        this.field = new NTile[tempTotal];
        this.totalBombs = bombs;
        this.markedBombCount = 0;
        this.revealed = new ArrayList<NTile>();
        this.lost = false;
        this.won = false;
        this.firstMove = true;
        this.genField();
    }

    private void genField(){
        int remainingBombs = this.totalBombs;
        int remainingSquares = this.totalSquares;
        for(int i = 0; i < this.totalSquares; i++){
            if (Math.random() < remainingBombs / ((double) remainingSquares)) {
                this.field[i] = new NTile(i, this.indexToCoord(i), true);
                remainingBombs--;
            } else {
                this.field[i] = new NTile(i, this.indexToCoord(i), false);
            }
            remainingSquares--;
        }
        // neighbours
        ArrayList<NTile> neighbours = new ArrayList<NTile>();
        int[][] offsets = this.genOffsets(); // calculate all possible offset for each var
        for(int i = 0; i < this.totalSquares; i++){
            neighbours.clear();
            for(int[] offset : offsets){
                this.vecAdd(offset, this.indexToCoord(i));
                if(this.isInField(offset)){
                    neighbours.add(this.field[i]);
                }
            }
            this.field[i].addNeighbours(neighbours);
        }
    }

    private void vecAdd(int[] a, int[] b){
        for(int i = 0; i < a.length; i++){
            a[i] += b[i];
        }
    }

    private boolean isInField(int[] coord){
        boolean isIn = true;
        for(int i = 0; i < this.dimension; i++){
            isIn &= (-1 < coord[i] && coord[i] < this.dimensions[i]);
        }
        return isIn;
    }

    private int[][] genOffsets(){
        int[][] offsets = new int[(int)(Math.pow(3, this.dimension)) - 1][this.dimension];
        int num;
        for(int i = 0; i < offsets.length / 2; i++){
            num = i;
            // extract all trinary digits
            for(int j = 0; j < this.dimension; j++){
                offsets[i][j] = num % 3;
                num /= 3;
            }
        }
        for(int i = offsets.length / 2 + 1; i < offsets.length + 1; i++){
            num = i;
            // extract all trinary digits
            for(int j = 0; j < this.dimension; j++){
                offsets[i - 1][j] = num % 3;
                num /= 3;
            }
        }
        return offsets;
    }

    public boolean isLost() {
        return this.lost;
    }

    public boolean isWon() {
        return this.won;
    }

    public ArrayList<NTile> getRevealed() {
        return this.revealed;
    }

    public NTile getTileAt(int index) {
        return this.field[index];
    }

    public NTile getTileAt(int[] coord) {
        return this.field[this.coordToIndex(coord)];
    }

    public int getRemainingBombCount() {
        return this.totalBombs - this.markedBombCount;
    }

    public int getRemainingUnrevealedCount() {
        return this.totalSquares - this.revealed.size();
    }

    public int move(NMove m) {
        NTile curr = this.field[m.getIndex()];
        if (m.shouldMark()) {
            if(curr.isRevealed()) {
                return MOVE_INVALID;
            }
            curr.changeMarked();
            if(curr.isMarked()){
                this.markedBombCount++;
            } else {
                this.markedBombCount--;
            }
        } else { // wanne click the tile
            if(this.firstMove){ // first move
                this.firstMove = false;
                if(curr.isBomb()){
                    this.handleFirstMove(m.getIndex());
                    return MOVE_VALID;
                }
            }
            if(curr.isBomb()){ // is bomb
                this.lost = true;
                return LOST;
            }
            if (curr.isRevealed()) { // already revealed
                return MOVE_INVALID;
            }
            this.revealSection(curr);
            return MOVE_VALID;
        }
        if(this.checkIfSolved()) {
            return WON;
        } else {
            return MOVE_VALID;
        }
    }

    private void handleFirstMove(int index) {
        this.genField();
        if (this.field[index].isBomb()) {
            this.handleFirstMove(index);
        } else {
            this.revealSection(this.field[index]);
        }
    }

    private void revealSection(NTile curr){
        curr.reveal();
        this.revealed.add(curr);
        if(curr.getCount() != 0) {
            return;
        }
        for (NTile i : curr.getNeighbours()) {
            if(i.isRevealed()) {
                continue;
            }
            if (i.getCount() == 0) {
                this.revealSection(i);
                continue;
            }
            i.reveal();
            this.revealed.add(curr);
        }
    }

    private boolean checkIfSolved(){
        for(NTile curr : this.field){
            if(curr.isBomb() ^ curr.isMarked()){
                return false;
            }
        }
        this.won = true;
        return true;
    }

    private int[] indexToCoord(int index){
        int[] coord = new int[this.dimension];
        int nextIndex;
        for(int i = 0; i < this.dimension; i++){
            coord[i] = index % this.dimensions[i];
            index /= this.dimensions[i];
        }
        return coord;
    }

    private int coordToIndex(int[] coord){
        int index = coord[0];
        for(int i = 1; i < this.dimension; i++){
            index = coord[i]+this.dimensions[i]*index;
        }
        return index;
    }
}
