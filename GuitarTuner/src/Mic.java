import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Mic implements PitchDetectionHandler {
    private TargetDataLine targetLine;
    private AudioInputStream inputStream;
    private final StreamAction streamAction;
    private static AudioDispatcher dispatcher;
    private float lastPeak = 0f;

    public Mic(StreamAction streamAction) {
        this.streamAction = streamAction;
        this.initDataLines();
        this.startRecording();
    }
    private void initDataLines() {
        AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);

        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            targetLine = (TargetDataLine)AudioSystem.getLine(info);
            targetLine.open();
            inputStream = new AudioInputStream(targetLine);

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private void startRecording()  {
        JVMAudioInputStream audioInputStream = new JVMAudioInputStream(inputStream);
        targetLine.start();

        float sampleRate = 44100;
        int bufferSize = 1024;
        int overlap = 0;

        dispatcher = new AudioDispatcher(audioInputStream, bufferSize, overlap);
        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, sampleRate, bufferSize, this));
        new Thread(dispatcher).start();
    }

    public void stopRecording(){
        dispatcher.stop();
        targetLine.close();
        targetLine.stop();
    }

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        audioEvent.isSilence(0.5);
        float[] samples = audioEvent.getFloatBuffer();
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
        streamAction.updateAudioBar((float)audioEvent.getRMS(), peak);

        if(pitchDetectionResult.getPitch() != -1){
            double timeStamp = audioEvent.getTimeStamp();
            float pitch = pitchDetectionResult.getPitch();
            //float probability = pitchDetectionResult.getProbability();
            streamAction.updateLabel((String.format("Pitch detected at %.2fs: %.2fHz\n", timeStamp, pitch)));
        }
    }
}
