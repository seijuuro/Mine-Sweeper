package GUI;

import game.Handler;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class SettingsPanel extends JDialog
{

    public static final String DEFAULT_NICK = "zxcursed";

    private final JTextField nick;
    private final JFormattedTextField minesCount;
    private final JFormattedTextField height;
    private final JFormattedTextField width;

    private final MainPanel frame;

    public SettingsPanel(MainPanel frame)
    {
        super(frame, "Settings", true);

        this.frame = frame;

        nick = new JTextField(DEFAULT_NICK);

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter minesFormatter = new NumberFormatter(format);
        minesFormatter.setValueClass(Integer.class);

        minesFormatter.setMaximum(400);
        minesFormatter.setMinimum(0);
        minesCount = new JFormattedTextField(minesFormatter);
        minesCount.setValue(Handler.DEFAULT_MINES_COUNT);

        NumberFormatter sizesFormatter = new NumberFormatter(format);
        sizesFormatter.setValueClass(Integer.class);

        sizesFormatter.setMaximum(20);
        sizesFormatter.setMinimum(5);
        height = new JFormattedTextField(sizesFormatter);
        height.setValue(Handler.DEFAULT_HEIGHT);
        width = new JFormattedTextField(sizesFormatter);
        width.setValue(Handler.DEFAULT_WIDTH);

        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(e -> closeSettings());

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(new JLabel("Nick:"));
        add(nick);
        add(new JLabel("Mines count:"));
        add(minesCount);
        add(new JLabel("Width:"));
        add(width);
        add(new JLabel("Height:"));
        add(height);
        add(new JLabel("Changes will be applied after restart."));
        add(closeButton);

        setSize(300, 250);
        setResizable(false);
    }

    public int getUserHeight()
    {
        return height.getValue() == null ? -1 : (int) height.getValue();
    }

    public int getUserWidth()
    {
        return  (int) width.getValue();
    }

    public int getMinesCount()
    {
        return  (int) minesCount.getValue();
    }

    public String getNick()
    {
        return nick.getText();
    }

    private void closeSettings()
    {
        int mines = getMinesCount();
        int width = getUserWidth();
        int height = getUserHeight();
        if (getNick().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please check the correctness of the entered data!", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (mines >= width * height)
        {
            JOptionPane.showMessageDialog(this, "You can't place so many mines on a field of this size!", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        setVisible(false);

        int result = JOptionPane.showConfirmDialog(frame, "Restart the game with the new settings?", "Restart?",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION)
            frame.pressRestart();
    }
}