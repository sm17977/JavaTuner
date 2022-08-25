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
1. Define MVC structure     
2. Setup GUI components
3. Read audio using a DataLine  :heavy_check_mark:
4. Write audio into Byte array  :heavy_check_mark:
5. Implement Actions for JButtons
6. Research sound properties for classification/detection

## Resources
FlatLaf Swing UI Theme: https://www.formdev.com/flatlaf/

Deep Learning Guitar Tunings: https://pats.cs.cf.ac.uk/@archive_file?p=1159&n=final&f=1-report.pdf&SIG=75b823d516969389b9cb7a57f27877c3112ad2b8a305de05bce18c140ee58c69
