import javax.swing.*;
import java.awt.*;

public class App {


    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("JavaTuner");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(640, 300);

        JButton startStream = new JButton("Start Stream");
        JButton endStream = new JButton("End Stream");

        mainFrame.add(startStream, BorderLayout.WEST);
        mainFrame.add(endStream, BorderLayout.EAST);

        mainFrame.setVisible(true);

    }



}
