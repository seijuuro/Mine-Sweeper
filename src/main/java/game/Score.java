package game;

public class Score
{
    public final String nick;
    public final int square, time;

    Score(String nick, int square, int time)
    {
        this.nick = nick;
        this.square = square;
        this.time = time;
    }

    @Override
    public String toString()
    {
        return String.format("%s -- Score: %d, Time: %d sec", nick, square, time);
    }
}
