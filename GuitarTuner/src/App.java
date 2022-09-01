import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class App {


    public static void main(String[] args) {
        FlatLightLaf.setup();
        JFrame mainFrame = new JFrame("JavaTuner");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(640, 300);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(25, 50, 25, 50));

        AudioBar bar = new AudioBar();
        bar.setPreferredSize(new Dimension(20, 100));
        panel.add(bar, BorderLayout.EAST);

        Action streamAction = new StreamAction(bar);
        streamAction.putValue(Action.NAME, "Start Streaming");

        JToggleButton startStream = new JToggleButton(streamAction);
        mainFrame.setContentPane(panel);
        mainFrame.add(startStream, BorderLayout.WEST);

        mainFrame.setVisible(true);

    }
}
