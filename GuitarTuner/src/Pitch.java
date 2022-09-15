import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public class Pitch implements PitchDetectionHandler {
    private final int RMS_STORE_SIZE = 10;

    // For testing purposes
    private final double MIC_AMBIENCE_RMS = 0.000711;
    private final RecordButtonAction recordBtnAction;
    private final Queue<Double> previousRMSUnits;

    private static boolean manualModeSelected;

    private final Map<Float, String> stdTunings = Map.of(
            50f, "",
            82f, "E2",
            110f, "A2",
            147f, "D3",
            196f, "G3",
            247f, "B3",
            330f, "E4"
    );
    private double avgPreviousRMSUnits = 0;
    public Pitch(RecordButtonAction recordBtnAction){
        this.recordBtnAction = recordBtnAction;
        previousRMSUnits = new ArrayDeque<Double>();
    }

    // handlePitch is called after the Mic.startRecording() method, and runs continuously
    // until Mic.stopRecording() is called
    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        float min = 0;
        float closestNote = 0;
        Optional<Float> firstKey = stdTunings.keySet().stream().findFirst();
        if (firstKey.isPresent()) {
            min = firstKey.get();
        }

        if(previousRMSUnits.size() == RMS_STORE_SIZE){
            previousRMSUnits.remove();
            previousRMSUnits.add(audioEvent.getRMS());
            double sum = previousRMSUnits.stream().reduce(Double::sum).get();
            avgPreviousRMSUnits = sum / RMS_STORE_SIZE;
        }
        else{
            previousRMSUnits.add(audioEvent.getTimeStamp());
        }

        if(80 > audioEvent.getdBSPL() && audioEvent.getdBSPL() < 1200) {
            recordBtnAction.updateAudioBar((float) (audioEvent.getRMS() + MIC_AMBIENCE_RMS) * 20);
        }

        if(pitchDetectionResult.getPitch() != -1){
            double timeStamp = audioEvent.getTimeStamp();
            float pitch = pitchDetectionResult.getPitch();

            for(Float tuning: stdTunings.keySet()){
                float diff = Math.abs(tuning - (pitch*2));
                if (diff < min){
                    min = diff;
                    closestNote = tuning;
                }
            }
            recordBtnAction.updateLabel((String.format("%s\n", stdTunings.get(closestNote))));
        }
    }
}
