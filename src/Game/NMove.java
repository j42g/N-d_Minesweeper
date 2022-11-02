package Game;

public class NMove {

    private final int index;
    private final boolean mark;

    public NMove(int index, boolean mark){
        this.index = index;
        this.mark = mark;
    }

    public NMove(NTile a, boolean mark){
        this.index = a.getIndex();
        this.mark = mark;
    }

    public int getIndex(){
        return this.index;
    }

    public boolean shouldMark(){
        return this.mark;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Move){
            return this.index == ((NMove)o).getIndex();
        } else {
            return false;
        }
    }
}
