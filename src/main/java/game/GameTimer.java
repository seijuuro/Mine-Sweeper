package game;


import java.util.Timer;
import java.util.TimerTask;


public abstract class GameTimer
{

    private final Timer timer = new Timer(true);

    private long begin;
    private HiddenTimer timerTask;
    private boolean run;

    public int getTimePassed()
    {
        return begin == 0 ? 0 : (int) ((System.currentTimeMillis() - begin) / 1000);
    }

    public boolean isRun()
    {
        return run;
    }

    protected abstract void updateTimer(int currTime);

    void resetTimer()
    {
        stopTimer();
        run = true;
        begin = System.currentTimeMillis();
        timer.schedule(timerTask = new HiddenTimer(), 0, 1000);
    }


    void stopTimer()
    {
        if (timerTask != null)
            timerTask.cancel();
        run = false;
    }

    private class HiddenTimer extends TimerTask
    {

        @Override
        public void run()
        {
            updateTimer(getTimePassed());
        }
    }

}