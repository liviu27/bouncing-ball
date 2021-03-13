import javax.swing.*;
import java.awt.*;

public class GeometricLoop {

    public static void main(String[] args) {
        JFrame appFrame = new JFrame();
        JPanel board = new Board();

        appFrame.setPreferredSize(new Dimension(700, 400));
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setContentPane(board);
        appFrame.pack();

        appFrame.setLocationRelativeTo(null);
        appFrame.setVisible(true);
    }
}


