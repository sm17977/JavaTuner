import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public class Pitch implements PitchDetectionHandler {
    private final int RMS_STORE_SIZE = 10;
    private final double MIC_AMBIENCE_RMS = 0.000711;
    private final int DIAL_SENSITIVITY = 20;
    private final ButtonController buttonController;
    private final Queue<Double> previousRMSUnits;
    public static boolean autoModeSelected;
    private final Map<Float, String> standardTuningFreqs = Map.of(
            50.0f, "",
            82.41f, "E2",
            110.0f, "A2",
            146.83f, "D3",
            196.0f, "G3",
            246.94f, "B3",
            329.63f, "E4"
    );
    private double avgPreviousRMSUnits = 0;
    public Pitch(ButtonController buttonController){
        this.buttonController = buttonController;
        previousRMSUnits = new ArrayDeque<>();
        autoModeSelected = true;
    }

    // handlePitch is called after the Mic.startRecording() method, and runs continuously
    // until Mic.stopRecording() is called
    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        float min = 0;
        float closestNote = 0;
        Optional<Float> firstKey = standardTuningFreqs.keySet().stream().findFirst();
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
            buttonController.updateAudioBar((float) (audioEvent.getRMS() + MIC_AMBIENCE_RMS) * 20);
        }

        if(pitchDetectionResult.getPitch() != -1){
            double timeStamp = audioEvent.getTimeStamp();
            float pitch = pitchDetectionResult.getPitch();



            // Detect the guitar string with the closest freq to the audio freq
            if(autoModeSelected){
                float diff = 0;
                for(Float tuning: standardTuningFreqs.keySet()){
                    diff = Math.abs(tuning - (pitch*2));
                    if (diff < min){
                        min = diff;
                        closestNote = tuning;
                    }
                }
                buttonController.updatePitchDial(diff * 10);
                buttonController.updateLabel((String.format("%s\n", standardTuningFreqs.get(closestNote))));
            }
            // Detect how the close audio freq is to selected string freq
            else{
                JToggleButton currentSelectedString = getSelectedBtn();
                String currentGuitarString = currentSelectedString.getText();
                float targetFrequency = 0;
                for(Map.Entry<Float, String> tunings : standardTuningFreqs.entrySet()){
                    if(tunings.getValue().equals(currentGuitarString)){
                        targetFrequency = tunings.getKey();
                        break;
                    }
                }
                float diff = pitch*2 - targetFrequency;
                System.out.println("diff: " + diff * DIAL_SENSITIVITY);
                buttonController.updateLabel(standardTuningFreqs.get(targetFrequency));
                buttonController.updatePitchDial(diff * DIAL_SENSITIVITY);
            }
        }
    }

    private JToggleButton getSelectedBtn(){
        JToggleButton selectedBtn = null;
        for(JToggleButton btn : buttonController.getButtons()){
            if(btn.isSelected()){
                selectedBtn = btn;
                break;
            }
        }
        return selectedBtn;
    }
}
