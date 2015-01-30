package gomokucontest;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GobanPanel extends JPanel {

    public Color boardColor = new Color(238, 211, 130);
    public Color textColor = Color.BLACK;
    public Color lineColor = Color.BLACK;
    public Color whiteStoneColor = Color.WHITE;
    public Color blackStoneColor = Color.BLACK;
    private final int pointSize = 6;
    public static final int WHITE = 1;
    public static final int BLACK = 2;
    public int[][] goban;
    public int currentStone = BLACK;
    private BufferedImage bufferImage;
    private Graphics2D graphics;
    private int size;
    private int offset = 30;
    private int cellSize;
    private int gameEnd;

    private BufferedImage blackStoneImage;
    private BufferedImage whiteStoneImage;
    private BufferedImage boardTexture;
    private TexturePaint slatetp;

    private final String abc[] = {"A", "B", "C", "D", "E", "F", "G", "H",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S"};

    public GobanPanel(int size) {
        super();
        this.size = size;

        try {
            blackStoneImage = ImageIO.read(this.getClass().getResource("black_stone.png"));
            whiteStoneImage = ImageIO.read(this.getClass().getResource("white_stone.png"));
            boardTexture = ImageIO.read(this.getClass().getResource("board_texture.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        goban = new int[size][size];
        clearGoban();

        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                placeStone(me);
            }
        });

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizePanel();
                repaint();
            }
        });

        bufferImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        graphics = (Graphics2D) bufferImage.getGraphics();
    }

    public int getGobanSize() {
        return size;
    }

    public void resizePanel() {
        int newHeigh = getHeight();
        int newWidth = getWidth();
        int minSide = newHeigh > newWidth ? newWidth : newHeigh;
        cellSize = (minSide - 40) / (size + 1);
        offset = 20 + cellSize;
        bufferImage = (BufferedImage) createImage(getWidth(), getHeight());
        if (bufferImage != null) {
            graphics = (Graphics2D) bufferImage.getGraphics();
        }
    }

    public void placeStone(MouseEvent me) {
        if (gameEnd != 0) {
            clearGoban();
            repaint();
            return;
        }

        int x = me.getX();
        int y = me.getY();

        if (x > (offset - cellSize / 2) && y > (offset - cellSize / 2)
                && x < (offset - cellSize / 2 + size * cellSize)
                && y < (offset - cellSize / 2 + size * cellSize)) {
            x = x - (offset - cellSize / 2);
            y = y - (offset - cellSize / 2);

            int i = x / cellSize;
            int j = size - y / cellSize - 1;
            if (goban[i][j] == 0) {
                goban[i][j] = currentStone;
                //if (currentStone == BLACK) currentStone = WHITE;
                //else currentStone = BLACK;
                gameEnd = GomokuContest.getInstance().isGameEnd();
                if (gameEnd == 0) {
                    GomokuContest.getInstance().doMove(WHITE);
                }
                gameEnd = GomokuContest.getInstance().isGameEnd();
                repaint();
            }
        }
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBoard(graphics);
        drawStones(graphics);
        drawEndGame(graphics);
        g.drawImage(bufferImage, 0, 0, null);
    }

    private void drawBoard(Graphics2D g) {
        // g.setColor(boardColor);
        // g.fillRect(0, 0, this.getWidth(), this.getHeight());
        slatetp = new TexturePaint(boardTexture, new Rectangle(0, 0, 500, 500));

        g.setPaint(slatetp);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(textColor);
        for (int i = 0; i < size; i++) {
            g.drawString(abc[i], offset + (cellSize * i) - 3, offset - 5 - cellSize / 2);
            g.drawString(abc[i], offset + (cellSize * i) - 3, offset + cellSize * (size - 1) + 15 + cellSize / 2);
            g.drawString(Integer.toString(size - i), offset - 20 - cellSize / 2, offset + (cellSize * i) + 5);
            g.drawString(Integer.toString(size - i), offset + cellSize * (size - 1) + 5 + cellSize / 2, offset + (cellSize * i) + 5);
        }

        g.setColor(lineColor);
        for (int i = 0; i < size; i++) {
            g.drawLine(offset, offset + i * cellSize, offset + cellSize * (size - 1), offset + i * cellSize);
            g.drawLine(offset + i * cellSize, offset, offset + i * cellSize, offset + cellSize * (size - 1));
        }

        g.fillOval(offset + cellSize * (size - 1) / 2 - pointSize / 2, offset + cellSize * (size - 1) / 2 - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * (size - 1) / 2 - pointSize / 2, offset + cellSize * (size - 4) - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * (size - 1) / 2 - pointSize / 2, offset + cellSize * 3 - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * 3 - pointSize / 2, offset + cellSize * 3 - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * 3 - pointSize / 2, offset + cellSize * (size - 1) / 2 - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * (size - 4) - pointSize / 2, offset + cellSize * (size - 1) / 2 - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * 3 - pointSize / 2, offset + cellSize * (size - 4) - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * (size - 4) - pointSize / 2, offset + cellSize * 3 - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * (size - 4) - pointSize / 2, offset + cellSize * (size - 4) - pointSize / 2, pointSize, pointSize);
    }

    private void drawStones(Graphics2D g) {
        int stoneSize = (int) (cellSize * 0.95);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (goban[i][j] == WHITE) {
                    g.drawImage(whiteStoneImage, offset + cellSize * i - stoneSize / 2,
                            offset + cellSize * (size - j - 1) - stoneSize / 2, stoneSize, stoneSize, null);
                    /*g.setColor(whiteStoneColor);
                     g.fillOval(offset + cellSize * i - stoneSize / 2, offset + cellSize * (size - j - 1) - stoneSize / 2, stoneSize, stoneSize);
                     g.setColor(Color.BLACK);
                     g.drawOval(offset + cellSize * i - stoneSize / 2, offset + cellSize * (size - j - 1) - stoneSize / 2, stoneSize, stoneSize);*/
                } else if (goban[i][j] == BLACK) {
                    /*g.setColor(blackStoneColor);
                     g.fillOval(offset + cellSize * i - stoneSize / 2, offset + cellSize * (size - j - 1) - stoneSize / 2, stoneSize, stoneSize);*/
                    g.drawImage(blackStoneImage, offset + cellSize * i - stoneSize / 2,
                            offset + cellSize * (size - j - 1) - stoneSize / 2, stoneSize, stoneSize, null);
                }
            }
        }
    }

    public void clearGoban() {
        gameEnd = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                goban[i][j] = 0;
            }
        }
    }

    private void drawEndGame(Graphics g) {
        if (gameEnd != 0) {
            Font f = g.getFont();
            g.setColor(Color.BLACK);
            g.setFont(new Font("Verdana", Font.PLAIN, 48));
            String s;
            if (gameEnd == WHITE) {
                s = "WHITE WIN";
            } else if (gameEnd == BLACK) {
                s = "BLACK WIN";
            } else {
                s = "BOARD IF FILL";
            }
            g.drawString(s, this.getWidth() / 2 - 145, this.getHeight() / 2 + 15);
            g.setFont(f);
        }
    }

}
