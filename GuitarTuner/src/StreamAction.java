import javax.swing.*;
import java.awt.event.ActionEvent;

public class StreamAction extends AbstractAction {
    private Mic m;
    private final AudioBar bar;
    private final PitchLabel label;

    public StreamAction(AudioBar bar,  PitchLabel label){
        this.bar = bar;
        this.label = label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton toggle = (JToggleButton)e.getSource();
        if(toggle.isSelected()){
            toggle.setText("Stop Recording");
            System.out.println("Streaming Started");
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    m = new Mic(StreamAction.this);
                }
            });
        }
        else{
            toggle.setText("Start Recording");
            System.out.println("Recording Ended");
            m.stopRecording();
            updateLabel("Stopped Recording");
        }
    }

    public void updateLabel(String str){
        label.setText(str);
    }

    public void updateAudioBar(float rms, float peak) {
        bar.setPeak(peak);
        bar.setAmplitude(rms);
    }
}
