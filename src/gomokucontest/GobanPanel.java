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

    public static final int WHITE = 1;
    public static final int BLACK = 2;
    private static final String abc[] = {"A", "B", "C", "D", "E", "F", "G", "H",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"};
    private final Color boardColor = new Color(238, 211, 130);
    private final Color textColor = Color.BLACK;
    private final Color lineColor = Color.BLACK;
    private final Color whiteStoneColor = Color.WHITE;
    private final Color blackStoneColor = Color.BLACK;
    private final int pointSize = 6;
    private int offset = 30;
    private int[][] goban;
    private int[][] gobanHistory;
    private int moveCount;
    private int gobanSize;
    private int gameEnd;
    private int currentStone = BLACK;
    private int cellSize;
    private BufferedImage bufferImage;
    private Graphics2D graphics;
    private BufferedImage blackStoneImage;
    private BufferedImage whiteStoneImage;
    private BufferedImage boardTexture;
    private TexturePaint slatetp;
    public IGomokuEngine playerWHITE;
    public IGomokuEngine playerBLACK;
    public boolean playerWHITEhuman;
    public boolean playerBLACKhuman;
    private int lasti = -1;
    private int lastj = -1;

    public GobanPanel(int size) {
        super();
        this.gobanSize = size;
        try {
            blackStoneImage = ImageIO.read(this.getClass().getResource("black_stone.png"));
            whiteStoneImage = ImageIO.read(this.getClass().getResource("white_stone.png"));
            boardTexture = ImageIO.read(this.getClass().getResource("board_texture.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        goban = new int[size][size];
        gobanHistory = new int[size][size];
       
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (playerWHITEhuman || playerBLACKhuman) {
                    humanPlaceStone(me);
                }
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
        slatetp = new TexturePaint(boardTexture, new Rectangle(0, 0, 500, 500));
        clearGoban();
    }

    public void clearGoban() {
        gameEnd = 0;
        currentStone = BLACK;
        lasti = -1;
        lastj = -1;
        moveCount = 0;
        for (int i = 0; i < gobanSize; i++) {
            for (int j = 0; j < gobanSize; j++) {
                goban[i][j] = 0;
                gobanHistory[i][j] = 0;
            }
        }
    }

    public int getGobanSize() {
        return gobanSize;
    }

    public int getGobanIJ(int i, int j) {
        return goban[i][j];
    }

    public void resizePanel() {
        int newHeigh = getHeight();
        int newWidth = getWidth();
        int minSide = newHeigh > newWidth ? newWidth : newHeigh;
        cellSize = (minSide - 40) / (gobanSize + 1);
        offset = 20 + cellSize;
        bufferImage = (BufferedImage) createImage(getWidth(), getHeight());
        if (bufferImage != null) {
            graphics = (Graphics2D) bufferImage.getGraphics();
        }
    }

    private void humanPlaceStone(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();

        if (x > (offset - cellSize / 2) && y > (offset - cellSize / 2)
                && x < (offset - cellSize / 2 + gobanSize * cellSize)
                && y < (offset - cellSize / 2 + gobanSize * cellSize)) {
            x = x - (offset - cellSize / 2);
            y = y - (offset - cellSize / 2);

            int i = x / cellSize;
            int j = gobanSize - y / cellSize - 1;

            if (gameEnd == 0) {
                if (goban[i][j] == 0) {
                    if (playerBLACKhuman && playerWHITEhuman) {
                        placeStone(i, j);
                        gameEnd = GomokuContest.getInstance().isGameEnd();
                    } else if (playerBLACKhuman && !playerWHITEhuman) {
                        placeStone(i, j);
                        gameEnd = GomokuContest.getInstance().isGameEnd();
                        if (gameEnd == 0) {
                            playerWHITE.doMove(WHITE);
                            gameEnd = GomokuContest.getInstance().isGameEnd();
                        }
                    } else if (!playerBLACKhuman && playerWHITEhuman) {
                        placeStone(i, j);
                        gameEnd = GomokuContest.getInstance().isGameEnd();
                        if (gameEnd == 0) {
                            playerBLACK.doMove(BLACK);
                            gameEnd = GomokuContest.getInstance().isGameEnd();
                        }
                    }
                }
            }
            repaint();
        }
    }

    public void placeStone(int i, int j) {
        if (goban[i][j] == 0) {
            goban[i][j] = currentStone;
            moveCount++;
            gobanHistory[i][j] = moveCount;
            lasti = i;
            lastj = j;
        }
        if (currentStone == BLACK) {
            currentStone = WHITE;
        } else {
            currentStone = BLACK;
        }
    }

    public void setGameEnd(int gameEnd) {
        this.gameEnd = gameEnd;
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
        if (GomokuContest.getInstance().whiteBoard()){
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }else{
            g.setPaint(slatetp);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        g.setColor(textColor);
        for (int i = 0; i < gobanSize; i++) {
            g.drawString(abc[i], offset + (cellSize * i) - 3, offset - 5 - cellSize / 2);
            g.drawString(abc[i], offset + (cellSize * i) - 3, offset + cellSize * (gobanSize - 1) + 15 + cellSize / 2);
            g.drawString(Integer.toString(gobanSize - i), offset - 20 - cellSize / 2, offset + (cellSize * i) + 5);
            g.drawString(Integer.toString(gobanSize - i), offset + cellSize * (gobanSize - 1) + 5 + cellSize / 2, offset + (cellSize * i) + 5);
        }

        g.setColor(lineColor);
        for (int i = 0; i < gobanSize; i++) {
            g.drawLine(offset, offset + i * cellSize, offset + cellSize * (gobanSize - 1), offset + i * cellSize);
            g.drawLine(offset + i * cellSize, offset, offset + i * cellSize, offset + cellSize * (gobanSize - 1));
        }

        g.fillOval(offset + cellSize * (gobanSize - 1) / 2 - pointSize / 2, offset + cellSize * (gobanSize - 1) / 2 - pointSize / 2, pointSize, pointSize);
        //g.fillOval(offset + cellSize * (gobanSize - 1) / 2 - pointSize / 2, offset + cellSize * (gobanSize - 4) - pointSize / 2, pointSize, pointSize);
        //g.fillOval(offset + cellSize * (gobanSize - 1) / 2 - pointSize / 2, offset + cellSize * 3 - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * 3 - pointSize / 2, offset + cellSize * 3 - pointSize / 2, pointSize, pointSize);
        //g.fillOval(offset + cellSize * 3 - pointSize / 2, offset + cellSize * (gobanSize - 1) / 2 - pointSize / 2, pointSize, pointSize);
        //g.fillOval(offset + cellSize * (gobanSize - 4) - pointSize / 2, offset + cellSize * (gobanSize - 1) / 2 - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * 3 - pointSize / 2, offset + cellSize * (gobanSize - 4) - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * (gobanSize - 4) - pointSize / 2, offset + cellSize * 3 - pointSize / 2, pointSize, pointSize);
        g.fillOval(offset + cellSize * (gobanSize - 4) - pointSize / 2, offset + cellSize * (gobanSize - 4) - pointSize / 2, pointSize, pointSize);
    }

    private void drawStones(Graphics2D g) {
        int stoneSize = (int) (cellSize * 0.95);

        if (lasti != -1 && lastj != -1) {
            g.setColor(Color.RED);
            g.fillOval(offset + cellSize * lasti - cellSize / 2, offset + cellSize * (gobanSize - lastj - 1) - cellSize / 2, cellSize, cellSize);
        }
        
        if (GomokuContest.getInstance().showHistory()){
            for (int i = 0; i < gobanSize; i++) {
                for (int j = 0; j < gobanSize; j++) {
                    if (goban[i][j] == WHITE) {
                         g.setColor(whiteStoneColor);
                         g.fillOval(offset + cellSize * i - stoneSize / 2, offset + cellSize * (gobanSize - j - 1) - stoneSize / 2, stoneSize, stoneSize);
                         g.setColor(Color.BLACK);
                         g.drawOval(offset + cellSize * i - stoneSize / 2, offset + cellSize * (gobanSize - j - 1) - stoneSize / 2, stoneSize, stoneSize);
                         int w = (int)(g.getFont().getStringBounds(Integer.toString(gobanHistory[i][j]), g.getFontRenderContext()).getWidth()/2);
                         g.drawString(Integer.toString(gobanHistory[i][j]), offset + cellSize * i - w, offset + cellSize * (gobanSize - j - 1) + 5);
                    } else if (goban[i][j] == BLACK) {
                         g.setColor(blackStoneColor);
                         g.fillOval(offset + cellSize * i - stoneSize / 2, offset + cellSize * (gobanSize - j - 1) - stoneSize / 2, stoneSize, stoneSize);
                         g.setColor(Color.WHITE);
                         int w = (int)(g.getFont().getStringBounds(Integer.toString(gobanHistory[i][j]), g.getFontRenderContext()).getWidth()/2);
                         g.drawString(Integer.toString(gobanHistory[i][j]), offset + cellSize * i - w, offset + cellSize * (gobanSize - j - 1) + 5);
                    }
                }
            }            
        }else{
            for (int i = 0; i < gobanSize; i++) {
                for (int j = 0; j < gobanSize; j++) {
                    if (goban[i][j] == WHITE) {
                        g.drawImage(whiteStoneImage, offset + cellSize * i - stoneSize / 2,
                                offset + cellSize * (gobanSize - j - 1) - stoneSize / 2, stoneSize, stoneSize, null);
                    } else if (goban[i][j] == BLACK) {
                        g.drawImage(blackStoneImage, offset + cellSize * i - stoneSize / 2,
                                offset + cellSize * (gobanSize - j - 1) - stoneSize / 2, stoneSize, stoneSize, null);
                    }
                }
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
                s = "BOARD FILL";
            }
            g.drawString(s, offset + gobanSize*cellSize/2 - 145, offset + gobanSize*cellSize / 2 + 15);
            g.setFont(f);
        }
    }

}
