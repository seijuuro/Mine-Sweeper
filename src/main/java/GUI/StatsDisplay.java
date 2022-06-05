package GUI;

import game.GameTimer;

import javax.swing.*;
import java.awt.*;


public class StatsDisplay extends Label implements Runnable
{

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 14);

    private final GUITimer timer;

    private int mines;
    private int flags;
    private String nick;

    private volatile int currentTime;

    public StatsDisplay()
    {
        timer = new GUITimer();
        setFont(FONT);
    }

    public void resetGame(int newMines)
    {
        mines = newMines;
        flags = 0;

        currentTime = 0;

        updateDisplay();
    }


    public void setNick(String nick)
    {
        this.nick = nick;
        updateDisplay();
    }


    public void setFlag()
    {
        flags++;
        updateDisplay();
    }


    public void removeFlag()
    {
        if (flags != 0)
            flags--;
        updateDisplay();
    }

    public GUITimer getTimer()
    {
        return timer;
    }


    public int getMaxWidth()
    {
        return getFontMetrics(getFont()).stringWidth(formInfo(mines, 1000));
    }


    private void updateDisplay()
    {
        int time;

        time = currentTime;

        String text = formInfo(flags, time);
        setText(text);

        FontMetrics metrics = getFontMetrics(getFont());
        setSize(metrics.stringWidth(text), metrics.getHeight());
    }


    private String formInfo(int flags, int time)
    {
        return String.format("%s: flags %d/%d, %d sec.", nick, flags, mines, time);
    }

    @Override
    public void run()
    {
        updateDisplay();
    }

    private class GUITimer extends GameTimer
    {

        @Override
        protected void updateTimer(int currTime)
        {
            currentTime = currTime;

            SwingUtilities.invokeLater(StatsDisplay.this);
        }
    }
}