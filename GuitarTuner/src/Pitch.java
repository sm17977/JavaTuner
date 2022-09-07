import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;

import java.util.ArrayDeque;
import java.util.Queue;

public class Pitch implements PitchDetectionHandler {
    private final int RMS_STORE_SIZE = 10;
    private final double MIC_AMBIENCE_RMS = 0.000711;
    private final RecordButtonAction recordBtnAction;
    private final Queue<Double> previousRMSUnits;
    private double avgPreviousRMSUnits = 0;
    public Pitch(RecordButtonAction recordBtnAction){
        this.recordBtnAction = recordBtnAction;
        previousRMSUnits = new ArrayDeque<Double>();
    }

    // handlePitch is called after the Mic.startRecording() method, and runs continuously
    // until Mic.stopRecording() is called
    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        if(previousRMSUnits.size() == RMS_STORE_SIZE){
            previousRMSUnits.remove();
            previousRMSUnits.add(audioEvent.getRMS());
            double sum = previousRMSUnits.stream().reduce(Double::sum).get();
            avgPreviousRMSUnits = sum / RMS_STORE_SIZE;
        }
        else{
            previousRMSUnits.add(audioEvent.getTimeStamp());
        }

        recordBtnAction.updateAudioBar((float) (avgPreviousRMSUnits + MIC_AMBIENCE_RMS) * 5);

        if(pitchDetectionResult.getPitch() != -1){
            double timeStamp = audioEvent.getTimeStamp();
            float pitch = pitchDetectionResult.getPitch();
            recordBtnAction.updateLabel((String.format("Pitch detected at %.2fs: %.2fHz\n", timeStamp, pitch*2)));
        }
    }
}
