

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StreamAction extends AbstractAction {

    Mic m;

    public StreamAction(){
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton toggle = (JToggleButton)e.getSource();
        if(toggle.isSelected()){
            m = new Mic(new AudioBar());
            System.out.println("Streaming Started");
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
