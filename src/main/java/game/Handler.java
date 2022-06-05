package game;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.LinkedList;
import java.util.List;


public class Handler
{

    public static final int DEFAULT_WIDTH = 10;
    public static final int DEFAULT_HEIGHT = 10;
    public static final int DEFAULT_MINES_COUNT = 10;

    private static final Gson GSON;

    private List<Score> scores = new LinkedList<>();
    private final File scoresFile;
    private final GameTimer timer;

    private String nick;

    private Field field;
    private boolean resultRecorded;

    static
    {
        GsonBuilder builder = new GsonBuilder();
        JsonDeserializer<Score> deserializer = (json, type, context) ->
        {
            JsonObject object = json.getAsJsonObject();
            String nick = object.get("nick").getAsString();
            int square = object.get("square").getAsInt();
            int time = object.get("time").getAsInt();
            return new Score(nick, square, time);
        };
        builder.registerTypeAdapter(Score.class, deserializer);
        builder.setPrettyPrinting();
        GSON = builder.create();
    }

    public Handler(GameTimer timer, File scores, String nick, int mines, int width, int height)
    {
        if (timer == null)
            throw new IllegalArgumentException("Timer cannot be null!");
        if (scores == null)
            throw new IllegalArgumentException("Scores file cannot be null!");
        if (nick == null)
            throw new IllegalArgumentException("Nick cannot be null!");
        this.timer = timer;
        scoresFile = scores;
        this.nick = nick;
        loadScores();
        restartGame(mines, width, height);
    }


    public Handler(GameTimer timer, File scores, String nick)
    {
        this(timer, scores, nick, DEFAULT_MINES_COUNT, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public int getWidth()
    {
        return field.getWidth();
    }


    public int getHeight()
    {
        return field.getHeight();
    }


    public int getMinesCount()
    {
        return field.getMinesCount();
    }


    public int getFlagsCount()
    {
        return field.getFlagsCount();
    }


    public void restartGame(int mines, int width, int height)
    {
        field = new Field(mines, width, height);
        resultRecorded = false;
        timer.stopTimer();
    }



    public Interact openCell(int x, int y)
    {
        if (field.isGameOver() || field.isGameWon())
            throw new IllegalStateException("Game ends.");
        if (x < 0 || x >= field.getWidth() || y < 0 || y >= field.getHeight())
            throw new IllegalArgumentException("Cell must be at field!");
        if (!timer.isRun())
            timer.resetTimer();
        Interact result = field.getCell(x, y).open();
        if (result.isExplode())
            timer.stopTimer();
        return result;
    }


    public Interact markCell(int x, int y)
    {
        if (field.isGameWon() || field.isGameOver())
            throw new IllegalStateException("Game ends.");
        if (x < 0 || x >= field.getWidth() || y < 0 || y >= field.getHeight())
            throw new IllegalArgumentException("Cell must be at field!");
        return field.getCell(x, y).changeFlagSet();
    }

    public boolean isGameWon()
    {
        if (field.isGameWon())
            recordResult();
        return field.isGameWon();
    }

    public boolean isGameOver()
    {

        return field.isGameOver();
    }

    public Iterable<Score> getScoreTable()
    {
        return scores;
    }


    public String getNick()
    {
        return nick;
    }


    public void setNick(String nick)
    {
        this.nick = nick;
    }


    private void recordResult()
    {
        if (resultRecorded)
            return;
        resultRecorded = true;
        timer.stopTimer();

        Score score = new Score(nick, field.getWidth() * field.getHeight() * getMinesCount() / 10 , timer.getTimePassed());

        int i = 0;
        for (; i < scores.size(); i++)
        {
            Score sc = scores.get(i);
            if (sc.time > score.time)
                break;
        }

        scores.add(i, score);
        saveScores();
    }


    private void saveScores()
    {
        try (Writer writer = new FileWriter(scoresFile))
        {
            GSON.toJson(scores, new TypeToken<LinkedList<Score>>() {}.getType(), writer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadScores()
    {
        if (!scoresFile.exists())
            return;
        try (Reader reader = new FileReader(scoresFile))
        {
            scores = GSON.fromJson(reader, new TypeToken<LinkedList<Score>>() {}.getType());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }



}