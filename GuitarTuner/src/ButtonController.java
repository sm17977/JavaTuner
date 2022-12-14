    import javax.swing.*;
    import java.awt.event.ActionEvent;
    import java.util.ArrayList;

    public class ButtonController extends AbstractAction {
        private final AudioBar bar;
        private final PitchLabel label;
        private final ArrayList<JToggleButton> buttons;
        private FrequencyGauge frequencyGauge;

        public ButtonController(AudioBar bar, PitchLabel label, ArrayList<JToggleButton> buttons, FrequencyGauge frequencyGauge){
            this.bar = bar;
            this.label = label;
            this.buttons = buttons;
            this.frequencyGauge = frequencyGauge;
            Mic m = new Mic(ButtonController.this);
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
                        autoBtn.animationTimer.start();
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
                frequencyGauge.setTargetAngle(-90);
            }
            else {
                frequencyGauge.setTargetAngle(freq);
            }
        }
    }
