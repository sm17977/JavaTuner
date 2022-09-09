import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PitchLabel extends JLabel {
    private final Font f = new Font("Segoe UI", Font.PLAIN, 80);

    public PitchLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setOpaque(false);
        this.setFont(f);
    }

    public void setFontSize(int size) {
        super.setFont(new Font("Segoe UI", Font.PLAIN, size));
    }

    @Override
    public void setText(String text) {
        super.setText(text);
    }



}
