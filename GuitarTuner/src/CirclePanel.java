import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CirclePanel extends JPanel {

    private final Ellipse2D circle;
    private Dimension preferredSize;

    public CirclePanel(int width, int height){
        this.circle = new Ellipse2D.Double(0, 0, width, height);
        this.preferredSize = new Dimension(width, height);
        setOpaque(false);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(hints);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.WHITE);
        g2.fill(circle);
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }
}
