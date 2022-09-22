    import javax.swing.*;
    import java.awt.event.ActionEvent;
    import java.util.ArrayList;
    import java.util.Objects;

    public class RecordButtonAction extends AbstractAction {
        private final AudioBar bar;
        private final PitchLabel label;
        private final Mic m;
        private ArrayList<JToggleButton> buttons;


        public RecordButtonAction(AudioBar bar, PitchLabel label, ArrayList<JToggleButton> buttons){
            this.bar = bar;
            this.label = label;
            this.buttons = buttons;
            m = new Mic(RecordButtonAction.this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SwitchButton autoBtn = null;
            JToggleButton toggle = (JToggleButton)e.getSource();
            if(toggle.isSelected() && !toggle.getName().equals("Auto")){
                for(JToggleButton btn : buttons) {
                    if(btn.getName().equals("Auto")) {
                        autoBtn = (SwitchButton) btn;
                        autoBtn.setEnabled(true);
                        autoBtn.setSelected(false);
                        autoBtn.timer.start();
                    }
                    else if(!btn.getName().equals(toggle.getName())){
                        btn.setSelected(false);
                        btn.setEnabled(true);
                    }
                }
            }
            else if (toggle.isSelected() && toggle.getName().equals("Auto")){

                for(JToggleButton btn : buttons) {
                    if(btn.getName().equals("Auto")) {
                        autoBtn = (SwitchButton) btn;
                        autoBtn.setEnabled(false);
                    }

                    if (!btn.getName().equals("Auto")) {
                        btn.setSelected(false);
                        btn.setEnabled(true);
                    }

                }
                updateLabel("");
            }
        }
        public void updateLabel(String str){
            label.setText(str);
        }
        public void updateAudioBar(float rms) {
            bar.setAmplitude(rms);
        }
    }
