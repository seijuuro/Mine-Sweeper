package GUI;

import game.Handler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class MainPanel extends JFrame
{

    private final FieldPanel gameField;
    private final StatsDisplay gameStats;
    private final SettingsPanel settingsPanel = new SettingsPanel(this);
    private final RecordsPanel recordsPanel;

    public MainPanel(File scores)
    {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints cn = new GridBagConstraints();
        setLayout(layout);

        gameStats = new StatsDisplay();
        Handler game = new Handler(gameStats.getTimer(), scores, SettingsPanel.DEFAULT_NICK);
        gameField = new FieldPanel(game, gameStats);
        recordsPanel = new RecordsPanel(this, game);

        resizeWindow();

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(null);
        gamePanel.add(gameField);
        cn.gridx = 0;
        cn.gridy = 0;
        cn.gridwidth = game.getWidth();
        cn.gridheight = game.getHeight();
        cn.weightx = gameField.getWidth() / (double) getWidth();
        cn.weighty = 1;
        cn.anchor = GridBagConstraints.CENTER;
        cn.fill = GridBagConstraints.BOTH;
        cn.insets = new Insets(5, 5, 0, 0);
        layout.setConstraints(gamePanel, cn);
        add(gamePanel);


        JButton buttonRestart = new JButton("Restart");
        buttonRestart.addMouseListener(new Press(this::pressRestart));
        cn.gridx = game.getWidth();
        cn.gridy = 0;
        cn.gridheight = 1;
        cn.gridwidth = 4;
        cn.weightx = 0;
        cn.weighty = 0;
        cn.insets = new Insets(0, 10, 0, 10);
        layout.setConstraints(buttonRestart, cn);
        add(buttonRestart);

        JButton buttonSettings = new JButton("Settings");
        buttonSettings.addMouseListener(new Press(this::openSettings));
        cn.gridy = 1;
        layout.setConstraints(buttonSettings, cn);
        add(buttonSettings);


        JButton buttonRecords = new JButton("Records");
        buttonRecords.addMouseListener(new Press(this::openRecords));
        cn.gridy = 2;
        layout.setConstraints(buttonRecords, cn);
        add(buttonRecords);


        cn.gridy = 3;
        layout.setConstraints(gameStats, cn);
        add(gameStats);

        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    void pressRestart()
    {
        gameField.restartGame(settingsPanel.getMinesCount(), settingsPanel.getUserWidth(), settingsPanel.getUserHeight());
        gameField.setNick(settingsPanel.getNick());
        resizeWindow();
    }

    private void openSettings()
    {
        settingsPanel.setVisible(true);
    }

    private void openRecords()
    {
        recordsPanel.setVisible(true);
    }


    private void resizeWindow()
    {
        setSize(gameField.getWidth() + gameStats.getMaxWidth() + 35, gameField.getHeight() + 45);
        setMinimumSize(new Dimension(getWidth(), getHeight()));
    }

    private record Press(PressListener listener) implements MouseListener {

        @Override
            public void mouseReleased(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1)
                    listener.press();
            }

            @Override
            public void mouseEntered(MouseEvent event) {
            }

            @Override
            public void mouseExited(MouseEvent event) {
            }

            @Override
            public void mouseClicked(MouseEvent event) {
            }

            @Override
            public void mousePressed(MouseEvent event) {
            }

        }

    private interface PressListener
    {
        void press();
    }

}