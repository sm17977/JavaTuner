import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.*;

import java.text.DecimalFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.SwingUtilities;

public class Mic implements PitchDetectionHandler {
    public AudioFormat format;
    public TargetDataLine targetLine;
    public SourceDataLine sourceLine;
    public AudioInputStream inputStream;
    public byte[] data;
    public DecimalFormat df;
    public AudioBar bar;
    public PitchLabel pitchLabel;

    public Mic(AudioBar bar, PitchLabel pitchLabel) {
        this.bar = bar;
        this.pitchLabel = pitchLabel;
        this.initDataLines();
        this.run();
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

            inputStream = new AudioInputStream(targetLine);

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public void run()  {
        JVMAudioInputStream audioInputStream = new JVMAudioInputStream(inputStream);

        sourceLine.start();
        targetLine.start();

        float sampleRate = 44100;
        int bufferSize = 1024;
        int overlap = 0;

        AudioDispatcher dispatcher = new AudioDispatcher(audioInputStream, bufferSize, overlap);
        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, sampleRate, bufferSize, this));
        new Thread(dispatcher,"Audio dispatching").start();
    }

    public void updateAudioBar(final float rms, final float peak) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                bar.setPeak(peak);
                bar.setAmplitude(rms);
            }
        });
    }

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        audioEvent.isSilence(1);
        float[] samples = audioEvent.getFloatBuffer();
        float lastPeak = 0f;
        float peak = 0f;

        for(float sample : samples) {
            float abs = Math.abs(sample);
            if (abs > peak) {
                peak = abs;
            }
        }
        if (lastPeak > peak) {
            peak = lastPeak * 0.875f;
        }

        lastPeak = peak;
        updateAudioBar((float)audioEvent.getRMS(), peak);

        if(pitchDetectionResult.getPitch() != -1){
            double timeStamp = audioEvent.getTimeStamp();
            float pitch = pitchDetectionResult.getPitch();
            float probability = pitchDetectionResult.getProbability();
            double rms = audioEvent.getRMS() * 100;
            pitchLabel.setText(String.format("Pitch detected at %.2fs: %.2fHz\n", timeStamp,pitch));
        }
    }
}
