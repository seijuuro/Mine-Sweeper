package GUI;

import game.Cell;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;


public class CellButton extends JButton
{

    public static final int SIDE_SIZE = 35;
    private static final Font TEXT_FONT = new Font("TimesRoman", Font.BOLD, 20);
    private static final Border RAISED_BORDER = BorderFactory.createRaisedBevelBorder();
    private static final Border LOWERED_BORDER = BorderFactory.createLoweredBevelBorder();
    private static final Border EMPTY_BORDER = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    private static Icon FLAG_ICON;
    private static Icon MINE_ICON;

    static
    {
        try
        {
            FLAG_ICON = new ImageIcon(ImageIO.read(CellButton.class.getResource("/flag1.png"))
                    .getScaledInstance(SIDE_SIZE, SIDE_SIZE, Image.SCALE_DEFAULT));
            MINE_ICON = new ImageIcon(ImageIO.read(CellButton.class.getResource("/mine3.png"))
                    .getScaledInstance(SIDE_SIZE, SIDE_SIZE, Image.SCALE_DEFAULT));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private final int posX, posY;
    private final FieldPanel field;

    public CellButton(FieldPanel field, int x, int y)
    {
        if (FLAG_ICON == null || MINE_ICON == null)
            throw new RuntimeException("Images not loaded!");
        posX = x;
        posY = y;
        this.field = field;
        addMouseListener(new Listener());
        setBorder(RAISED_BORDER);
        setBackground(Color.GRAY);
    }


    void updateState(Cell cell)
    {
        if (cell.isOpened())
        {
            if (cell.isMine())
            {
                setBackground(cell.isFlagSet() ? Color.GREEN : Color.YELLOW);
                setIcon(MINE_ICON);
            }
            else if (cell.getMineNeighbors() > 0)
            {
                 setIcon(null);
                setText(String.valueOf(cell.getMineNeighbors()));
                setFont(TEXT_FONT);
                setForeground(getNumberColor(cell.getMineNeighbors()));
            }
            setBorder(EMPTY_BORDER);
        }
        else if (cell.isFlagSet() && getIcon() != FLAG_ICON)
            setIcon(FLAG_ICON);
        else if (!cell.isFlagSet() && getIcon() == FLAG_ICON)
            setIcon(null);


        else if (!cell.isMine())
            setBackground(Color.GRAY);
    }


    private Color getNumberColor(int number)
    {
        return switch (number) {
            case 1 -> new Color(12, 36, 252);
            case 2 -> new Color(14, 126, 18);
            case 3 -> new Color(251, 13, 28);
            case 4 -> new Color(21, 2, 78);
            case 5 -> new Color(61, 12, 22);
            case 6 -> new Color(122, 210, 193);
            case 7 -> Color.BLACK;
            case 8 -> Color.YELLOW;
            default -> null;
        };
    }


    private final class Listener implements MouseListener
    {

        private void mouseClick(MouseEvent event)
        {
            if (getBorder() == EMPTY_BORDER)
                return;

            if (event.getButton() == MouseEvent.BUTTON1 && getIcon() != FLAG_ICON)
                field.openCell(posX, posY);
            else if (event.getButton() == MouseEvent.BUTTON3)
                field.flagCell(posX, posY);
        }

        @Override
        public void mousePressed(MouseEvent event)
        {
            if (event.getButton() == MouseEvent.BUTTON1 && getBorder() != EMPTY_BORDER && getIcon() != FLAG_ICON)
                setBorder(LOWERED_BORDER);
        }

        @Override
        public void mouseReleased(MouseEvent event)
        {
            if (event.getButton() == MouseEvent.BUTTON1 && getBorder() != EMPTY_BORDER)
                setBorder(RAISED_BORDER);
            if (event.getX() <= getWidth() && event.getY() <= getHeight())
                mouseClick(event);
        }

        @Override
        public void mouseEntered(MouseEvent event){}


        @Override
        public void mouseExited(MouseEvent event){}


        @Override
        public void mouseClicked(MouseEvent event){}

    }

}