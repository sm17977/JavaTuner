import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class App {


    public static void main(String[] args) {
        FlatLightLaf.setup();
        JFrame mainFrame = new JFrame("JavaTuner");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(640, 300);

        Action streamAction = new StreamAction();
        streamAction.putValue(Action.NAME, "Start Streaming");

        JToggleButton startStream = new JToggleButton(streamAction);
        mainFrame.add(startStream, BorderLayout.WEST);

        mainFrame.setVisible(true);

    }



}
