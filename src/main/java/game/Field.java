package game;

import java.util.Random;

public class Field
{
    protected final Cell[][] field;
    protected int minesCount;
    private final Random random;
    private boolean explode;
    private boolean isWon;
    private int flags;


    public Field(int mines, int width, int height)
    {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Width and height must be greater than zero!");
        if (mines <= 0)
            throw new IllegalArgumentException("Mines count must be positive!");
        if (width * height <= mines)
            throw new IllegalArgumentException("Cannot place " + mines + " mines to " + width + "x" + height + "field.");

        field = new Cell[width][height];
        random = new Random();
        this.minesCount = mines;
    }

    private void init(int startX, int startY)
    {
        randomGenerate(startX, startY);

        for (int i = 0; i < field.length; i++)
            for (int j = 0; j < field[i].length; j++)
                field[i][j].setMineNeighbors(countMinesAround(i, j));
    }

    public int getMinesCount()
    {
        return minesCount;
    }

    public Cell getCell(int posX, int posY)
    {
        Cell cell = field[posX][posY];
        if (cell == null) {
            init(posX, posY);
            cell = field[posX][posY];
        }
        return cell;
    }

    public boolean isGameWon()
    {
        if (isWon)
            return true;
        if (explode)
            return false;

        if (field[0][0] == null)
            return false;

        for (Cell[] line : field)
            for (Cell cell : line)
                if (!cell.isOpened() && !cell.isFlagSet())
                    return false;
        return isWon = true;
    }

    public boolean isGameOver()
    {
        return explode;
    }


    public int getWidth()
    {
        return field.length;
    }


    public int getHeight()
    {
        return field[0].length;
    }


    public int getFlagsCount()
    {
        return flags;
    }

    protected void randomGenerate(int startX, int startY)
    {
        field[startX][startY] = new Cell(this, startX, startY, Cell.EMPTY);

        for (int placed = 0; placed < minesCount; placed++)
        {
            int x = random.nextInt(getWidth());
            int y = random.nextInt(getHeight());
            if (field[x][y] != null)
                while (field[x][y] != null)
                {

                    y++;
                    if (y == getHeight())
                    {
                        y = 0;
                        if (++x == getWidth())
                            x = 0;
                    }
                }
            field[x][y] = new Cell(this, x, y, Cell.MINE);
        }

        for (int i = 0; i < getWidth(); i++)
            for (int j = 0; j < getHeight(); j++)
                if (field[i][j] == null)
                    field[i][j] = new Cell(this, i, j, Cell.EMPTY);
    }


    void updateNearestCells(Interact result, int x, int y)
    {
        if(field[x][y].getMineNeighbors() != 0)
        {
            field[x][y].openExactly();
            result.addCell(field[x][y]);
            return;
        }
        if (x != 0)
            if (field[x - 1][y].tryToOpen(result) == -1)
            {
                field[x - 1][y].openExactly();
                result.addCell(field[x - 1][y]);
            }

        if (x + 1 != getWidth())
            if (field[x + 1][y].tryToOpen(result) == -1)
            {
                field[x + 1][y].openExactly();
                result.addCell(field[x + 1][y]);
            }
        if (y != 0)
            if (field[x][y - 1].tryToOpen(result) == -1)
            {
                field[x][y - 1].openExactly();
                result.addCell(field[x][y - 1]);
            }
        if (y + 1 != getHeight())
            if (field[x][y + 1].tryToOpen(result) == -1)
            {
                field[x][y + 1].openExactly();
                result.addCell(field[x][y + 1]);
            }
    }

    Interact explosion()
    {
        explode = true;
        Interact result = new Interact(true);
        for (Cell[] line : field)
            for (Cell cell : line)
            {
                cell.openExactly();
                result.addCell(cell);
            }
        return result;
    }

    void updateNearestFlags(Cell cell, Interact result)
    {
        flags += cell.isFlagSet() ? 1 : -1;

        for (int i = cell.posX - 1; i <= cell.posX + 1; i++)
            if (i >= 0 && i < getWidth())
                for (int j = cell.posY - 1; j <= cell.posY + 1; j++)
                    if (j >= 0 && j < getHeight())
                    {
                        if (cell.isFlagSet())
                            field[i][j].addNearFlag();
                        else
                            field[i][j].removeNearFlag();
                        result.addCell(field[i][j]);
                    }
    }


    private int countMinesAround(int x, int y)
    {
        if (field[x][y].mine)
            return -1;

        int around = 0;

        for (int i = x - 1; i <= x + 1; i++)
            if (i >= 0 && i < getWidth())
                for (int j = y - 1; j <= y + 1; j++)
                    if (j >= 0 && j < getHeight())
                        around += field[i][j].mine ? 1 : 0;

        return around;
    }
}