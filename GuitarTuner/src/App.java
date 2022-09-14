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

                // Setup Main JFrame and JPanel Layout
                FlatLightLaf.setup();
                JFrame mainFrame = new JFrame("JavaTuner");
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainFrame.setSize(1024, 600);
                JPanel panel = new JPanel(new MigLayout("align 50% 50%",
                        "[center]",
                        "[grow][][]"));

                // Audio Volume Bar
                AudioBar bar = new AudioBar();
                bar.setPreferredSize(new Dimension((int) (mainFrame.getWidth() * 0.8), 20));

                // Pitch Detection Panel
                CirclePanel circlePanel = new CirclePanel(300,300);
                PitchLabel pitchLabel = new PitchLabel("Pitch", SwingConstants.CENTER);
                circlePanel.setLayout(new GridBagLayout());
                circlePanel.add(pitchLabel);

                Action recordBtnAction = new RecordButtonAction(bar, pitchLabel);

                // Guitar String Buttons
                CircleButton button1 = new CircleButton("E", recordBtnAction);

                CircleButton button2 = new CircleButton("A", recordBtnAction);
                CircleButton button3 = new CircleButton("D", recordBtnAction);
                CircleButton button4 = new CircleButton("G", recordBtnAction);
                CircleButton button5 = new CircleButton("B", recordBtnAction);
                CircleButton button6 = new CircleButton("E", recordBtnAction);

                ButtonGroup btnGroup = new ButtonGroup();
                btnGroup.add(button1);
                btnGroup.add(button2);
                btnGroup.add(button3);
                btnGroup.add(button4);
                btnGroup.add(button5);
                btnGroup.add(button6);

                // Guitar Headstock Graphic
                JImage guitarImg = new JImage(new File("C:\\Users\\Sean\\Desktop\\JavaTuner\\GuitarTuner\\src\\guitarHeadstock.png"), 225, 50);
                guitarImg.setLayout(new MigLayout());

                mainFrame.setContentPane(panel);
                mainFrame.add(button1, "split, gapleft 30");
                mainFrame.add(button2, "gapleft 30");
                mainFrame.add(button3, "gapleft 30");
                mainFrame.add(button4, "gapleft 30");
                mainFrame.add(button5, "gapleft 30");
                mainFrame.add(button6, "gapleft 30, wrap");
                mainFrame.add(guitarImg, String.format("w %d!, h %d!, split", guitarImg.getWidth(), guitarImg.getHeight()));
                mainFrame.add(circlePanel, "wrap");
                mainFrame.add(bar);

                mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
                panel.setBorder(BorderFactory.createLineBorder(Color.black));
                mainFrame.setVisible(true);
            }
        });
    }
}

