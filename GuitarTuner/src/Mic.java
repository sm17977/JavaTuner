import javax.sound.sampled.*;
import java.io.IOException;

public class Mic{
    public boolean recording;
    public TargetDataLine targetLine;
    public SourceDataLine sourceLine;
    public byte[] data;

    public Mic(){
        initDataLines();
        data = new byte[1024];
    }

    public void initDataLines(){
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100,16,2,4,44100,false );
        try{
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open();

            info = new DataLine.Info(TargetDataLine.class, format);
            targetLine = (TargetDataLine)AudioSystem.getLine(info);
            targetLine.open();

        }catch (Exception e) {
        }
    }

    public void startRecording(){
        AudioInputStream stream = new AudioInputStream(targetLine);

        sourceLine.start();
        targetLine.start();
        final int[] result = {0};
        Thread thread = new Thread(){
            @Override
            public void run() {
                while(true) {
                    targetLine.read(data, 0, data.length);
                    sourceLine.write(data, 0, data.length);
                    System.out.println(data.length);
                    try {
                        result[0] = stream.read(data);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
             }
        };
        thread.start();

        int numChannels = stream.getFormat().getChannels();
        int frameLength = (int) stream.getFrameLength();
        int[][] toReturn = new int[numChannels][frameLength];

        System.out.println(frameLength);
        System.out.println(numChannels);

        int sampleIndex = 0;

        for (int t = 0; t < data.length;) {
            for (int channel = 0; channel < numChannels; channel++) {
                int low = (int) data[t];
                t++;
                int high = (int) data[t];
                t++;
                int sample = getSixteenBitSample(high, low);
                toReturn[channel][sampleIndex] = sample;
            }
            sampleIndex++;
        }




    }
    private int getSixteenBitSample(int high, int low) {
        return (high << 8) + (low & 0x00ff);
    }
}
