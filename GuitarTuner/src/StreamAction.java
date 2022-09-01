

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class StreamAction extends AbstractAction {

    Mic m;
    AudioBar bar;
    PitchLabel label;

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
                    m = new Mic(bar, label);
                }
            });


        }
        else{
            toggle.setText("Start Recording");
            System.out.println("Streaming Ended");
            try {
                m.sourceLine.stop();
                m.sourceLine.close();
                m.targetLine.stop();
                m.targetLine.close();
                m.inputStream.close();
            } catch (IOException | NullPointerException ex) {
                Thread.currentThread().interrupt();
            }

        }
    }
}
