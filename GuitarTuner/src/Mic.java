//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.text.DecimalFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.SwingUtilities;

public class Mic implements Runnable {
    public boolean recording;
    private static final float NORMALIZATION_FACTOR_2_BYTES = 32768.0F;
    public boolean isRecording;
    public AudioFormat format;
    public TargetDataLine targetLine;
    public SourceDataLine sourceLine;
    public byte[] data;
    public DecimalFormat df;
    public AudioBar bar;

    public Mic(AudioBar bar) {
        this.bar = bar;
        this.isRecording = false;
        this.initDataLines();
        this.data = new byte[2048];
        this.df = new DecimalFormat("#.00");
    }

    public void initDataLines() {
        format = new AudioFormat(44100.0F, 16, 2, true, false);

        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, this.format);
            sourceLine = (SourceDataLine)AudioSystem.getLine(info);
            sourceLine.open();

            info = new DataLine.Info(TargetDataLine.class, this.format);
            targetLine = (TargetDataLine)AudioSystem.getLine(info);
            targetLine.open();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        new AudioInputStream(this.targetLine);
        this.sourceLine.start();
        this.targetLine.start();
        float[] samples = new float[data.length/2];
        float lastPeak = 0.0F;


        for(int b; (b = targetLine.read(data, 0, data.length)) > -1;) {
            this.sourceLine.write(this.data, 0, this.data.length);
            for(int i = 0, s = 0; i < b;) {
                int sample = 0;

                sample |= data[i++] & 0xFF; // (reverse these two lines
                sample |= data[i++] << 8;   //  if the format is big endian)

                // normalize to range of +/-1.0f
                samples[s++] = sample / 32768f;
            }

            float rms = 0f;
            float peak = 0f;

            for(float sample : samples) {

                float abs = Math.abs(sample);
                if(abs > peak) {
                    peak = abs;
                }

                rms += sample * sample;
            }

            rms = (float)Math.sqrt(rms / samples.length);
            if (lastPeak > peak) {
                peak = lastPeak * 0.875f;
            }

            lastPeak = peak;
            updateAudioBar(rms, peak);
            System.out.println("" + rms + " " + peak);
        }

    }

    public void updateAudioBar(final float rms, final float peak) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                bar.setPeak(peak);
                bar.setAmplitude(rms);
            }
        });
    }

}
