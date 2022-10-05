import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                // Setup Main JFrame and JPanel Layout
                FlatLightLaf.setup();
                JFrame mainFrame = new JFrame("JavaTuner");
                mainFrame.setResizable(false);
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainFrame.setSize(1024, 600);

                JMenuBar menuBar = new JMenuBar();
                JMenu menu = new JMenu("Guitar Tunings");

                ButtonGroup tuningSelectionGroup = new ButtonGroup();
                JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("Standard");
                rbMenuItem.setSelected(true);
                tuningSelectionGroup.add(rbMenuItem);
                menu.add(rbMenuItem);

                rbMenuItem = new JRadioButtonMenuItem("Drop D");
                tuningSelectionGroup.add(rbMenuItem);
                menu.add(rbMenuItem);
                menuBar.add(menu);

                BackgroundPanel panel =  new BackgroundPanel(new MigLayout("align 50% 50%",
                        "[center]",
                        "20[grow][][]"));

                // Audio Volume Bar
                AudioBar bar = new AudioBar();
                bar.setPreferredSize(new Dimension((int) (mainFrame.getWidth() * 0.8), 20));

                // Pitch Detection Panel
                CirclePanel circlePanel = new CirclePanel(300,300);
                circlePanel.setLayout(new BorderLayout());
                PitchLabel pitchLabel = new PitchLabel("Pitch", 0);
                circlePanel.add(pitchLabel, BorderLayout.SOUTH);

                // Auto button
                SwitchButton autoBtn = new SwitchButton(null);

                // Guitar String Buttons
                CircleButton string1Btn = new CircleButton("E2", null, "S1");
                CircleButton string2Btn = new CircleButton("A2", null, "S2");
                CircleButton string3Btn = new CircleButton("D3", null, "S3");
                CircleButton string4Btn = new CircleButton("G3", null, "S4");
                CircleButton string5Btn = new CircleButton("B3", null, "S5");
                CircleButton string6Btn = new CircleButton("E4", null, "S6");

                JPanel autoBtnPanel = new JPanel(new GridBagLayout());
                autoBtnPanel.setOpaque(false);
                JLabel autoLabel = new JLabel("Auto  ");
                autoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

                autoBtnPanel.add(autoLabel);
                autoBtnPanel.add(autoBtn);

                ArrayList<JToggleButton> btnGroup = new ArrayList<>();
                btnGroup.add(string1Btn);
                btnGroup.add(string2Btn);
                btnGroup.add(string3Btn);
                btnGroup.add(string4Btn);
                btnGroup.add(string5Btn);
                btnGroup.add(string6Btn);
                btnGroup.add(autoBtn);

                Action recordBtnAction = new ButtonController(bar, pitchLabel, btnGroup, circlePanel);

                for(JToggleButton btn : btnGroup){
                    btn.addActionListener(recordBtnAction);
                }

                autoBtn.animationTimer.start();
                autoBtn.setSelected(true);
                autoBtn.setEnabled(false);

                // Guitar Headstock Graphic
                JImage guitarImg = new JImage(new File("src/guitarHeadstock.png"), 225, 50);
                guitarImg.setLayout(new MigLayout());

                mainFrame.setContentPane(panel);
                mainFrame.add(string1Btn, "split, gapleft 80");
                mainFrame.add(string2Btn, "gapleft 30");
                mainFrame.add(string3Btn, "gapleft 30");
                mainFrame.add(string4Btn, "gapleft 30");
                mainFrame.add(string5Btn, "gapleft 30");
                mainFrame.add(string6Btn, "gapleft 30");
                mainFrame.add(autoBtnPanel, "gapleft 35, wrap");
                mainFrame.add(guitarImg, String.format("w %d!, h %d!, split", guitarImg.getWidth(), guitarImg.getHeight()));
                mainFrame.add(circlePanel, "wrap");
                mainFrame.add(bar);

                mainFrame.setJMenuBar(menuBar);

                mainFrame.getContentPane().setBackground(Color.ORANGE);
                panel.setBorder(BorderFactory.createLineBorder(Color.black));
                mainFrame.setVisible(true);
            }
        });
    }
}

