package GUI;

import game.Score;
import game.Handler;

import javax.swing.*;

public class RecordsPanel extends JDialog
{

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final Handler gameHandler;

    public RecordsPanel(JFrame frame, Handler gameHandler)
    {
        super(frame, "Records", true);
        this.gameHandler = gameHandler;
        JList<String> list = new JList<>(listModel);
        JScrollPane scroll = new JScrollPane(list);
        add(scroll);
        updateList();
        setSize(300, 200);
    }

    @Override
    public void setVisible(boolean visible)
    {
        updateList();
        super.setVisible(visible);
    }

    private void updateList()
    {
        listModel.clear();
        int pos = 0;
        for (Score score : gameHandler.getScoreTable())
        {
            listModel.addElement(++pos + ". " + score.toString());
        }
    }

}