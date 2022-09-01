import javax.swing.*;
import java.awt.*;
public class PitchLabel extends JLabel {
    Font f = new Font("Segoe UI", Font.PLAIN, 22);

    public PitchLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        this.setFont(f);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
    }
}
