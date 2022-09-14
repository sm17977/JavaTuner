    import javax.swing.*;
    import java.awt.event.ActionEvent;

    public class RecordButtonAction extends AbstractAction {
        private final AudioBar bar;
        private final PitchLabel label;
        private Mic m;

        public RecordButtonAction(AudioBar bar,  PitchLabel label){
            this.bar = bar;
            this.label = label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JToggleButton toggle = (JToggleButton)e.getSource();
            if(toggle.isSelected()){
                System.out.println("Streaming Started");
                m = new Mic(RecordButtonAction.this);
            }
            else{
                System.out.println("Recording Ended");
                m.stopRecording();
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
