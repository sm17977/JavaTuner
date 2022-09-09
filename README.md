## Java Guitar Tuner (WIP)
A desktop GUI Guitar Tuner developed with Java Swing

## Program Base Functionality
* GUI Components
  * Stream audio JToggleButton
  * Guitar String JButton (for each string) 
  * Tuning (standard, drop-d etc.) JComboBox
* Read audio from system microphone in real-time 
* Write audio to byte array
* Detect guitar sound in byte array
* Classify sound in relation to string + tuning selection


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
8. Add auto and manual mode buttons
9. Code auto and manual mode in Pitch detection class
10. Make sound bar look pretty

## Resources
FlatLaf Swing UI Theme: https://www.formdev.com/flatlaf/

TarsosDSP audio processing library: https://github.com/JorenSix/TarsosDSP

