    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.util.ArrayList;
    import java.util.Objects;

    public class RecordButtonAction extends AbstractAction {
        private final AudioBar bar;
        private final PitchLabel label;
        private final Mic m;
        private ArrayList<JToggleButton> buttons;
        private CirclePanel circlePanel;

        public RecordButtonAction(AudioBar bar, PitchLabel label, ArrayList<JToggleButton> buttons, CirclePanel circlePanel){
            this.bar = bar;
            this.label = label;
            this.buttons = buttons;
            this.circlePanel = circlePanel;
            m = new Mic(RecordButtonAction.this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SwitchButton autoBtn = null;
            JToggleButton currentBtnPressed = (JToggleButton)e.getSource();

            // A guitar string button is selected
            if(currentBtnPressed.isSelected() && !currentBtnPressed.getName().equals("Auto")){
                for(JToggleButton btn : buttons) {
                    // Deselect (Turn off) auto mode button
                    if(btn.getName().equals("Auto")) {
                        autoBtn = (SwitchButton) btn;
                        autoBtn.setEnabled(true);
                        autoBtn.setSelected(false);
                        autoBtn.timer.start();
                    }
                    // Turn off all the other guitar string buttons
                    else if(!btn.getName().equals(currentBtnPressed.getName())){
                        btn.setSelected(false);
                        btn.setEnabled(true);
                    }
                }
            }

            // The auto mode button is selected
            else if (currentBtnPressed.isSelected() && currentBtnPressed.getName().equals("Auto")){
                // Auto mode button is disabled when selected, the button is
                // automatically re-selected when the user clicks on a guitar string button
                autoBtn = (SwitchButton) currentBtnPressed;
                autoBtn.setEnabled(false);
                // Deselect all guitar string buttons
                for(JToggleButton btn : buttons) {
                    if (!btn.getName().equals("Auto")) {
                        btn.setSelected(false);
                        btn.setEnabled(true);
                    }
                }
            }

            // Set pitch detection mode to state of auto mode button
            if(autoBtn != null){
                Pitch.autoModeSelected = autoBtn.isSelected();
            }
        }
        public void updateLabel(String str){
            label.setText(str);
        }
        public void updateAudioBar(float rms) {
            bar.setAmplitude(rms);
        }
        public ArrayList<JToggleButton> getButtons() {
            return buttons;
        }

        public void updatePitchDial(float freq){
            if(freq < -30){
                circlePanel.setTargetAngle(-90);
            }
            else {
                circlePanel.setTargetAngle(freq);
            }
        }
    }
