import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PitchLabel extends JLabel {

    public PitchLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setOpaque(false);
        Font f = new Font("SansSerif", Font.PLAIN, 80);
        this.setFont(f);

    }
    public void setFontSize(int size) {
        super.setFont(new Font("SansSerif", Font.PLAIN, size));
    }
}
