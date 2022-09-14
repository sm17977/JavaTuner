import javax.swing.*;
import java.awt.*;

// Using implementation from Radiodef's stackoverflow answer
// Link: https://stackoverflow.com/questions/26574326/how-to-calculate-the-level-amplitude-db-of-audio-signal-in-java

public class AudioBar extends JComponent {

    private int meterWidth = 10;
    private int meterHeight = 100;

    private float amp = 0f;

    public void setAmplitude(float amp) {
        this.amp = Math.abs(amp);
        repaint();
    }

    public void setMeterWidth(int meterWidth) {
        this.meterWidth = meterWidth;
    }
    public void setMeterHeight(int meterHeight) {
        this.meterHeight = meterHeight;
    }

    @Override
    protected void paintComponent(Graphics g) {

        int w = Math.min(meterWidth, getWidth());
        int h = Math.min(meterHeight, getHeight());
        int x = getWidth() / 2 - w / 2;
        int y = 0;

        g.setColor(Color.WHITE);
        g.fillRect(x, y, w, h);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, w - 1, h - 1);

        int a = Math.round(amp * (w + 2));
        g.setColor(Color.GREEN);
        g.fillRect(x + 1, y + 1,  a,  h - 2);

    }


    @Override
    public Dimension getPreferredSize() {
        Dimension pref = super.getPreferredSize();
        pref.width = meterWidth;
        pref.height = meterHeight;
        return pref;
    }

    @Override
    public void setPreferredSize(Dimension pref) {
        super.setPreferredSize(pref);
        setMeterWidth(pref.width);
        setMeterHeight(pref.height);
    }
}