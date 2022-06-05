package game;

public class Cell
{
    public static final boolean MINE = true;
    public static final boolean EMPTY = false;

    public final int posX, posY;

    private final Field field;

    final boolean mine;

    private int mineNeighbors;
    private boolean opened;
    private boolean flag;
    private int nearFlags;

    Cell(Field field, int posX, int posY, boolean mine)
    {
        this.field = field;
        this.posX = posX;
        this.posY = posY;
        this.mine = mine;
    }


    void setMineNeighbors(int neighbors)
    {
        mineNeighbors = neighbors;
    }


    public int getMineNeighbors()
    {
        return mineNeighbors;
    }

    public boolean isFlagSet()
    {
        return flag;
    }

    void addNearFlag()
    {
        nearFlags++;
    }

    void removeNearFlag()
    {
        if (nearFlags != 0)
            nearFlags--;
    }

    public Interact changeFlagSet()
    {
        if (field.isGameOver())
            return new Interact(true);
        if (opened || field.isGameWon())
            return new Interact(false);
        flag = !flag;
        Interact result = new Interact(this);
        field.updateNearestFlags(this, result);
        return result;
    }

    public boolean isOpened()
    {
        return opened;
    }


    public boolean isMine()
    {
        return opened && mine;
    }

    public Interact open()
    {
        if (opened)
            return new Interact(false);
        if (mine)
            return field.explosion();
        opened = true;
        flag = false;
        Interact result = new Interact(this);
        field.updateNearestCells(result, posX, posY);
        return result;
    }

    int tryToOpen(Interact result)
    {
        if (mine || opened)
            return 0;
        if(mineNeighbors != 0)
            return -1;
        opened = true;
        result.addCell(this);
        field.updateNearestCells(result, posX, posY);
        return 1;
    }

    void openExactly()
    {
        opened = true;
    }

    @Override
    public int hashCode()
    {
        return (posX << 16) | posY & 0xffff;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Cell cell))
            return false;
        return cell.posX == posX && cell.posY == posY;
    }
}