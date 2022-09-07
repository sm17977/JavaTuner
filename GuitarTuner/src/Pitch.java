import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;

import java.util.ArrayDeque;
import java.util.Queue;

public class Pitch implements PitchDetectionHandler {
    private final StreamAction streamAction;
    private final Queue<Double> count;
    private double average = 0;
    public Pitch(StreamAction streamAction){
        this.streamAction = streamAction;
        count = new ArrayDeque<Double>();
    }

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        audioEvent.isSilence(0.5);
        float[] samples = audioEvent.getFloatBuffer();

        //System.out.println(String.format("%2f: ", audioEvent.getRMS() + 0.5));

        if(count.size() == 10){
            count.remove();
            count.add(audioEvent.getRMS());
            double sum = count.stream().reduce(Double::sum).get();
            average = sum / 10;
            System.out.println(String.format("%2f: ", average));
            //System.out.println(String.format("%2f: ", Collections.min(count)));
        }
        else{
            count.add(audioEvent.getTimeStamp());
        }

        streamAction.updateAudioBar((float) (average + 0.000711) * 5);

        if(pitchDetectionResult.getPitch() != -1){
            double timeStamp = audioEvent.getTimeStamp();
            float pitch = pitchDetectionResult.getPitch();
            //float probability = pitchDetectionResult.getProbability();
            streamAction.updateLabel((String.format("Pitch detected at %.2fs: %.2fHz\n", timeStamp, pitch*2)));
        }

    }
}
