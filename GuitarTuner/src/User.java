import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class User{
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
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100,16,2,4,44100,false );
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if(!AudioSystem.isLineSupported(info)){
                System.out.println("Line not supported");
            }
            final TargetDataLine line = (TargetDataLine)AudioSystem.getLine(info);
            line.open();
            System.out.println("Recording...");
            line.start();

            Thread thread = new Thread(){
                public void run(){
                    AudioInputStream audioStream = new AudioInputStream(line);
                    File audioFile = new File("record.wav");
                    try {
                        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Stopped recording.");
                }
            };
            thread.start();
            Thread.sleep(5000);
            line.stop();
            line.close();
            System.out.println("Sound test finished.");



        } catch (LineUnavailableException | InterruptedException e) {

        }


    }




}
