import javax.swing.*;
import java.awt.event.ActionEvent;

public class StreamAction extends AbstractAction {

    Mic m;

    public StreamAction(){
        m = new Mic();



    }



    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton toggle = (JToggleButton)e.getSource();
        if(toggle.isSelected()){
            System.out.println("Streaming Started");
            m.startRecording();
            m.openMicrophone();
            m.targetThread.start();
            m.sourceThread.start();

        }
        else{
            m.targetLine.stop();
            m.targetLine.close();
            System.out.println("Streaming Ended");


        }

    }
}
