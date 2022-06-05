package GUI;

import game.Cell;
import game.Handler;
import game.Interact;

import javax.swing.*;
import java.awt.*;

public class FieldPanel extends JPanel
{

    private enum State
    {
        NONE,
        WIN,
        LOSE
    }

    private static final Font STATE_FONT = new Font("TimesRoman", Font.BOLD, 42);

    private final Handler game;
    private final StatsDisplay stats;

    private CellButton[][] buttons;
    private State gameState;

    public FieldPanel(Handler game, StatsDisplay statsDisplay)
    {
        this.game = game;
        this.stats = statsDisplay;
        statsDisplay.resetGame(game.getMinesCount());
        statsDisplay.setNick(game.getNick());
        newSize(game.getWidth(), game.getHeight());
    }

    public void restartGame(int mines, int width, int height)
    {
        game.restartGame(mines, width, height);
        stats.resetGame(mines);
        newSize(width, height);
    }

    public void setNick(String nick)
    {
        game.setNick(nick);
        stats.setNick(nick);
    }

    private void newSize(int width, int height)
    {
        removeAll();
        gameState = State.NONE;
        setSize(height * CellButton.SIDE_SIZE, width * CellButton.SIDE_SIZE);
        buttons = new CellButton[width][height];
        setLayout(new GridLayout(width, height));

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                add(buttons[i][j] = new CellButton(this, i, j));
        validate();
        repaint();
    }


    void openCell(int x, int y)
    {
        if (game.isGameOver() || game.isGameWon())
            return;
        Interact result = game.openCell(x, y);
        update(result);
        if (result.isExplode())
        {
            gameState = State.LOSE;
            buttons[x][y].setBackground(Color.RED);
        }

        if (game.isGameWon())
        {
            gameState = State.WIN;
            repaint();
        }
    }

    @Override
    public void paint(Graphics graphics)
    {
        super.paint(graphics);
        if (gameState == State.NONE)
            return;

        setFont(STATE_FONT);

        if (gameState == State.WIN)
        {
            graphics.setColor(Color.GREEN);
            drawCenter(graphics, "WON!");
        }
        else if (gameState == State.LOSE)
        {
            graphics.setColor(Color.RED);
            drawCenter(graphics, "LOSE!");
        }
    }

    private void drawCenter(Graphics graphics, String text)
    {
        FontMetrics metrics = graphics.getFontMetrics(STATE_FONT);
        int textX = (getWidth() - metrics.stringWidth(text)) / 2;
        int textY = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
        int rectX = textX - 10;
        int rectY = textY - metrics.getHeight() + 10;
        int rectWidth = metrics.stringWidth(text) + 20;
        int rectHeight = metrics.getHeight();

        Color textColor = graphics.getColor();
        graphics.setColor(getBackground());
        graphics.fillRect(rectX, rectY, rectWidth, rectHeight);

        graphics.setColor(textColor);
        graphics.drawString(text, textX, textY);
    }

    void flagCell(int x, int y)
    {
        if (game.isGameWon() || game.isGameOver())
            return;

        int flags = game.getFlagsCount();
        update(game.markCell(x, y));

        if (flags > game.getFlagsCount())
            stats.removeFlag();
        else if (flags < game.getFlagsCount())
            stats.setFlag();

        if (game.isGameWon())
        {
            gameState = State.WIN;
            repaint();
        }
    }


    private void update(Interact result)
    {
        for (Cell cell : result)
            buttons[cell.posX][cell.posY].updateState(cell);

        repaint();
    }

}