import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class User extends JFrame {
    File audioInput;


    public static void main(String[] args) {
        User u = new User();

        System.out.println("Welcome to my Guitar Tuner!");
        System.out.println("Press Enter to start sound test...");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        u.openMicrophone();





    }

    public void openMicrophone(){
        try{
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, )
        }


        TargetDataLine line;
        AudioFormat format = null;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if(!AudioSystem.isLineSupported(info)){
            //handle error or smth
        }
        try{
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
        } catch (LineUnavailableException e) {
            System.out.println(e.getMessage());
        }

    }




}
