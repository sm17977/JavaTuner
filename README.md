## Java Guitar Tuner (WIP)
A desktop GUI Guitar Tuner developed with Java Swing

## Program Base Functionality
* GUI Components
  * Auto Jbutton to detect string automatically
  * 6 Guitar string JButtons for individual string tuning 
  * Tuning JComboBox selection (standard, drop d, open g etc.)
* Read audio from system microphone in real-time 
* Write audio to byte array
* Detect pitch
* Classify current pitch in relation to current string + tuning selection


## To do
1. Define MVC structure :heavy_check_mark:
2. Handle Microphone Input :heavy_check_mark:
3. Implement Baseline Functionality :heavy_check_mark:
4. Create custom JComponent classes
   - Images :heavy_check_mark:
   - Circular Button :heavy_check_mark:
   - Circular Panel :heavy_check_mark:
5. Implement Pitch Detection (YIN Algorithm from TarsosDSP) :heavy_check_mark:
6. Create buttons for each guitar string :heavy_check_mark:
7. Add button animations
8. Add auto mode button :heavy_check_mark:
9. Code auto and manual mode in Pitch detection class
10. Add sound bar to visualise microphone feedback :heavy_check_mark:
11. Make sound bar look pretty
12. Add menu bar for selecting alternative tunings

## Resources
FlatLaf Swing UI Theme: https://www.formdev.com/flatlaf/

TarsosDSP audio processing library: https://github.com/JorenSix/TarsosDSP

