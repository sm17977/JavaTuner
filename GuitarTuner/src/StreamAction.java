

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StreamAction extends AbstractAction {

    Mic m;
    AudioBar bar;

    public StreamAction(AudioBar bar){
        this.bar = bar;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton toggle = (JToggleButton)e.getSource();
        if(toggle.isSelected()){
            System.out.println("Streaming Started");
            new Thread(new Mic(bar)).start();
        }
        else{
            System.out.println("Streaming Ended");
            m.sourceLine.stop();
            m.sourceLine.close();
            m.targetLine.stop();
            m.targetLine.close();
        }
    }
}
