import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                FlatLightLaf.setup();
                JFrame mainFrame = new JFrame("JavaTuner");
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainFrame.setSize(1024, 600);


                JPanel panel = new JPanel(new MigLayout("", "[grow][grow][grow][grow]", "[grow][grow]"));
                panel.setBorder(new EmptyBorder(25, 50, 25, 50));

                AudioBar bar = new AudioBar();
                bar.setPreferredSize(new Dimension(20, 100));

                CirclePanel circlePanel = new CirclePanel(300,300);
                PitchLabel pitchLabel = new PitchLabel("Pitch", SwingConstants.CENTER);
                circlePanel.setLayout(new GridBagLayout());
                circlePanel.add(pitchLabel);

                CircleButton buttonA = new CircleButton("A");
                CircleButton buttonE = new CircleButton("E");



                Action recordBtnAction = new RecordButtonAction(bar, pitchLabel);
                recordBtnAction.putValue(Action.NAME, "Auto");

                JToggleButton recordBtn = new JToggleButton(recordBtnAction);

                JImage guitarImg = new JImage(new File("C:\\Users\\Sean\\Desktop\\JavaTuner\\GuitarTuner\\src\\guitarHeadstock.png"), 225, 150);
                guitarImg.setLayout(new MigLayout());


                mainFrame.setContentPane(panel);
                mainFrame.add(buttonA);
                mainFrame.add(buttonE, "wrap");
                mainFrame.add(guitarImg, String.format("w %d!, h %d!", guitarImg.getWidth(), guitarImg.getHeight()));
                mainFrame.add(circlePanel);
                mainFrame.add(recordBtn);
                mainFrame.add(bar);

                mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
                panel.setBorder(BorderFactory.createLineBorder(Color.black));
                mainFrame.setVisible(true);
            }
        });
    }
}

