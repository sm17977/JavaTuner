import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

public class Mic{
    File audioInput;
    public boolean recording;
    public TargetDataLine targetLine;
    public SourceDataLine sourceLine;

    public Thread sourceThread;
    public Thread targetThread;
    public byte[] data;



    public Mic(){
        recording = false;
    }

    public void openMicrophone(){
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100,16,2,4,44100,false );
        try{
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open();

            info = new DataLine.Info(TargetDataLine.class, format);
            targetLine = (TargetDataLine)AudioSystem.getLine(info);
            targetLine.open();

            System.out.println("Recording...");

            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            sourceThread = new Thread(){
                public void run() {
                    sourceLine.start();
                    while(true){
                        sourceLine.write(out.toByteArray(), 0, out.size());


                    }
                }
            };
            targetThread = new Thread(){
                public void run() {
                    targetLine.start();
                    data = new byte[targetLine.getBufferSize() / 5];
                    int readBytes;
                    while(true){
                        readBytes = targetLine.read(data, 0, data.length);
                        out.write(data, 0, readBytes);

                    }
                }
            };





        } catch (LineUnavailableException  e) {
        }
    }




    public void startRecording(){
        recording = true;
    }

    public void endRecording(){
        recording = false;
    }




}
