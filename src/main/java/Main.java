import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {
        JFrame appFrame = new JFrame();
        JPanel board = new Board();

        appFrame.setPreferredSize(new Dimension(700, 400));
        appFrame.setResizable(false);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setContentPane(board);
        appFrame.pack();

        appFrame.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                board.requestFocusInWindow();
            }
        });

        appFrame.setLocationRelativeTo(null);
        appFrame.setVisible(true);
    }
}


