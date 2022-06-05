import GUI.MainPanel;

import java.io.File;

public class Main
{

    private static final File SCORES_FILE = new File("scores.json");

    public static void main(String[] args)
    {
        new MainPanel(SCORES_FILE);
    }
}