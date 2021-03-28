import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Board extends JPanel {

    public static double speedX = 3.0d;
    public static double speedY = 1.1d;
    public static final int CIRCLE_RADIUS = 20;
    public static final int FPS = 60;


    private int rectangleX1;
    private int rectangleY1;
    private int rectangleX2;
    private int rectangleY2;
    private double centerCircleX;
    private double centerCircleY;
    private boolean paused = true;
    private int scorePlayer1;
    private int scorePlayer2;
    private Dimension boardSize;
    private Executor keyWorker = Executors.newFixedThreadPool(2);

    public Board() {
        new Thread(() -> {
            while (true) {
                boardSize = getSize();

                if (centerCircleX == 0 && centerCircleY == 0) {
                    centerCircleX = boardSize.width / 2;
                    centerCircleY = boardSize.height / 2;
                    repaint();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                rectangleX1 = 5;
                if (rectangleY1 == 0) {
                    rectangleY1 = boardSize.height / 2 - 40;
                }
                rectangleX2 = boardSize.width - 10;
//
                if (rectangleY2 == 0) {
                    rectangleY2 = boardSize.height / 2 - 40;
                }


                while (paused) {
                    synchronized (Board.this) {
                        try {
                            Board.this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                final int width = getSize().width;
                final int height = getSize().height;

                centerCircleX += speedX;
                if (centerCircleX > width - CIRCLE_RADIUS) {
                    centerCircleX = width - CIRCLE_RADIUS;
                    speedX = -speedX;
                    scorePlayer1++;
                } else if (centerCircleX < CIRCLE_RADIUS) {
                    centerCircleX = CIRCLE_RADIUS;
                    speedX = -speedX;
                    scorePlayer2++;
                }

                centerCircleY += speedY;
                if (centerCircleY > height - CIRCLE_RADIUS) {
                    centerCircleY = height - CIRCLE_RADIUS;
                    speedY = -speedY;
                } else if (centerCircleY < CIRCLE_RADIUS) {
                    centerCircleY = CIRCLE_RADIUS;
                    speedY = -speedY;
                }

                repaint();

                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                synchronized (Board.this) {
                    paused = !paused;
                    Board.this.notify();
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyWorker.execute(() -> {
                    System.out.println("start");
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        rectangleY2 -= 3;
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        rectangleY2 += 3;
                    } else if (e.getKeyCode() == KeyEvent.VK_W) {
                        rectangleY1 -= 3;
                    } else if (e.getKeyCode() == KeyEvent.VK_S) {
                        rectangleY1 += 3;
                    }
                    System.out.println("end");
                });
            }
        });

        setBorder(BorderFactory.createLineBorder(Color.blue));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (centerCircleX == 0 && centerCircleY == 0) {
            return;
        }
        g.fillRect(rectangleX1, rectangleY1, 5, 80);
        g.fillRect(rectangleX2, rectangleY2, 5, 80);
        drawCircleByCenter(g, (int) centerCircleX, (int) centerCircleY);

        g.setColor(Color.black);
        g.drawString("x = " + (int) centerCircleX + "  |  y = " + (int) centerCircleY + "  |  Speed_x = " + speedX + "  |  Speed_y = " + speedY, 5, 15);
        g.drawString("Player 1  " + scorePlayer1 + ":" + scorePlayer2 + "  Player 2", boardSize.width / 2 - 60, boardSize.height - 5);
    }

    void drawCircleByCenter(Graphics g, int x, int y) {
        g.setColor(Color.BLUE);
        g.fillOval(x - CIRCLE_RADIUS, y - CIRCLE_RADIUS, 2 * CIRCLE_RADIUS, 2 * CIRCLE_RADIUS);
    }
}
