import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.*;

public class Mic {
    private static AudioDispatcher dispatcher;
    private final Pitch pitch;

    private final Map<Float, String> stdTunings = Map.of(
            82f, "E2",
            110f, "A2",
            147f, "D3",
            196f, "G3",
            247f, "B3",
            330f, "E4"
    );
    private TargetDataLine targetLine;
    private AudioInputStream inputStream;

    public Mic(RecordButtonAction recordBtnAction) {
        pitch = new Pitch(recordBtnAction);
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
        int bufferSize = 6000;
        int overlap = 0;

        dispatcher = new AudioDispatcher(audioInputStream, bufferSize, overlap);
        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, sampleRate, bufferSize, pitch));
        new Thread(dispatcher).start();
    }

    public void stopRecording(){
        dispatcher.stop();
        targetLine.close();
        targetLine.stop();
    }
}
