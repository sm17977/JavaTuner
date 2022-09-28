import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;

import javax.swing.*;
import java.math.BigDecimal;
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

    public static boolean autoModeSelected;

    private final Map<Float, String> stdTunings = Map.of(
            50.0f, "",
            82.41f, "E2",
            110.0f, "A2",
            146.83f, "D3",
            196.0f, "G3",
            246.94f, "B3",
            329.63f, "E4"
    );
    private double avgPreviousRMSUnits = 0;
    public Pitch(RecordButtonAction recordBtnAction){
        this.recordBtnAction = recordBtnAction;
        previousRMSUnits = new ArrayDeque<Double>();
        autoModeSelected = true;
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

        if(pitchDetectionResult.getPitch() != -1){;
            double timeStamp = audioEvent.getTimeStamp();
            float pitch = pitchDetectionResult.getPitch();
            System.out.println(pitch*2);

            // Detect the guitar string with the closest freq to the audio freq
            if(autoModeSelected){
                recordBtnAction.updatePitchDial(pitch);
                float diff = 0;
                for(Float tuning: stdTunings.keySet()){
                    diff = Math.abs(tuning - (pitch*2));
                    if (diff < min){
                        min = diff;
                        closestNote = tuning;
                    }
                }

                recordBtnAction.updateLabel((String.format("%s\n", stdTunings.get(closestNote))));
            }
            // Detect how the close audio freq is to selected string freq
            else{
                JToggleButton currentSelectedString = getSelectedBtn();
                String currentGuitarString = currentSelectedString.getText();
                float targetFrequency = 0;
                for(Map.Entry<Float, String> tunings : stdTunings.entrySet()){
                    if(tunings.getValue().equals(currentGuitarString)){
                        targetFrequency = tunings.getKey();
                        break;
                    }
                }
                float diff = pitch*2 - targetFrequency;
                if(Math.abs(diff) > 10){
                    recordBtnAction.updateLabel("");
                }
                else{
                    recordBtnAction.updateLabel(round(diff, 2));
                }
            }
        }
    }
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private JToggleButton getSelectedBtn(){
        JToggleButton selectedBtn = null;
        for(JToggleButton btn : recordBtnAction.getButtons()){
            if(btn.isSelected()){
                selectedBtn = btn;
                break;
            }
        }
        return selectedBtn;
    }
}
