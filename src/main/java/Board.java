import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel {

    public static double speedX = 3.0d;
    public static double speedY = 1.1d;
    public static final int CIRCLE_RADIUS = 20;
    public static final int FPS = 60;

    private double centerCircleX;
    private double centerCircleY;
    private boolean paused = true;

    public Board() {

        new Thread(() -> {
            while (true) {
                System.out.println("w " + Thread.currentThread());
                if (centerCircleX == 0 && centerCircleY == 0) {
                    final Dimension boardSize = getSize();
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
                } else if (centerCircleX < CIRCLE_RADIUS) {
                    centerCircleX = CIRCLE_RADIUS;
                    speedX = -speedX;
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (centerCircleX == 0 && centerCircleY == 0) {
            return;
        }
        drawCircleByCenter(g, (int) centerCircleX, (int) centerCircleY);
        g.setColor(Color.black);
        g.drawString("x = " + (int) centerCircleX + " |  y = " + (int) centerCircleY + " |  Speed_x = " + speedX + " |  Speed_y = " + speedY, 10, 10);
    }

    void drawCircleByCenter(Graphics g, int x, int y) {
        System.out.println("p " + Thread.currentThread());
        g.setColor(Color.BLUE);
        g.fillOval(x - CIRCLE_RADIUS, y - CIRCLE_RADIUS, 2 * CIRCLE_RADIUS, 2 * CIRCLE_RADIUS);
    }
}
