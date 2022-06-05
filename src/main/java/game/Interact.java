package game;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Interact implements Iterable<Cell>
{
    private final Set<Cell> changed = new HashSet<>();
    private final boolean explode;

    Interact(boolean isExplode)
    {
        explode = isExplode;
    }

    Interact(Cell init)
    {
        explode = false;
        changed.add(init);
    }

    void addCell(Cell cell)
    {
        changed.add(cell);
    }

    public boolean isExplode() {
        return explode;
    }

    @Override
    public Iterator<Cell> iterator()
    {
        return new ResultIterator();
    }

    private final class ResultIterator implements Iterator<Cell>
    {
        private final Iterator<Cell> iterator = changed.iterator();

        @Override
        public boolean hasNext()
        {
            return iterator.hasNext();
        }

        @Override
        public Cell next()
        {
            return iterator.next();
        }
    }
}