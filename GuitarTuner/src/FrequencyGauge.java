import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class FrequencyGauge extends JPanel {
    private static final int DEGREES_PER_TICK = 10;
    private final Ellipse2D gaugeBackground;
    private final Dimension preferredSize;
    private float dialAngle;
    private float targetDialAngle;

    public FrequencyGauge(int width, int height){
        this.gaugeBackground = new Ellipse2D.Double(0, 0, width, height);
        this.preferredSize = new Dimension(width, height);
        setOpaque(false);
        dialAngle = 0;
        targetDialAngle = 0;

        // Rotate dial
        Timer timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dialAngle == targetDialAngle) {
                    return;
                }
                if (dialAngle < targetDialAngle) {
                    dialAngle += 1;
                } else {
                    dialAngle -= 1;
                }
                repaint();
            }
        });
        timer.start();

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(hints);
        g2.setStroke(new BasicStroke(3));

        double panelCenterX = getWidth() / 2f;
        double panelCenterY = getHeight() / 2f;
        double panelRadius = getWidth() / 2f;

        drawBackground(g2);
        drawGaugeBorder(g2);
        drawIntervalTicks(g2, panelRadius, panelCenterX, panelCenterY);

        double dialCenterDiameter = 30f;
        double dialCenterRadius = dialCenterDiameter / 2f;

        drawDial(g2, dialCenterRadius, dialCenterDiameter, panelCenterX, panelCenterY);

        g2.setColor(Color.BLACK);
    }

    public void drawBackground(Graphics2D g2){
        g2.setColor(Color.WHITE);
        g2.fill(gaugeBackground);
    }

    public void drawGaugeBorder(Graphics2D g2){
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(2, 2, 296, 296);
    }

    public void drawIntervalTicks(Graphics2D g2, double panelRadius, double panelCenterX, double panelCenterY){

        g2.setStroke(new BasicStroke(1));
        for(int theta = 0; theta <= 360; theta += DEGREES_PER_TICK){
            if(theta >= 180) {
                // Calculate point on a circle using x = r * cos(theta), y = r * cos(theta)
                Point2D pointOnCircle = new Point((int) ((panelRadius * Math.cos(Math.toRadians(theta))) + panelRadius), (int) ((panelRadius * Math.sin(Math.toRadians(theta))) + panelRadius));
                g2.drawLine((int) (pointOnCircle.getX()), (int) (pointOnCircle.getY()), (int) panelCenterX, (int) panelCenterY);
            }
        }
        // Draw a white oval over the center of the gauge to cutoff tick line length, fix this in future using vectors?
        g2.setColor(Color.WHITE);
        g2.fillOval((int)panelRadius - 140, (int)panelRadius - 140, 280, 280);
    }

    public void drawDial(Graphics2D g2, double dialCenterRadius, double dialCenterDiameter, double panelCenterX, double panelCenterY){

        g2.setColor(Color.RED);
        Ellipse2D dialCenter = new Ellipse2D.Double(panelCenterX - dialCenterRadius, panelCenterY - dialCenterRadius, dialCenterDiameter, dialCenterDiameter);
        g2.fill(dialCenter);

        double armLength = 80;

        Point2D leftBasePoint = new Point2D.Double(dialCenter.getX(), dialCenter.getY() + dialCenterRadius);
        Point2D rightBasePoint = new Point2D.Double(dialCenter.getX() + dialCenterDiameter, dialCenter.getY() + dialCenterRadius);
        Point2D topPoint = new Point2D.Double(rightBasePoint.getX() - dialCenterRadius, dialCenter.getY() + dialCenterRadius - armLength);
        Polygon dialArm = new Polygon(new int[]{(int) leftBasePoint.getX(), (int) rightBasePoint.getX(), (int) topPoint.getX()},
                new int[]{(int) leftBasePoint.getY(), (int) rightBasePoint.getY(), (int) topPoint.getY()}, 3);

        int maxDialAngle = 90;
        if(targetDialAngle > maxDialAngle){
            targetDialAngle = maxDialAngle;
        }
        int minDialAngle = -90;
        if(targetDialAngle < minDialAngle){
            targetDialAngle = minDialAngle;
        }

        AffineTransform transform = new AffineTransform();
        transform.translate(panelCenterX, panelCenterY);
        transform.rotate(Math.toRadians(dialAngle));
        transform.translate(-panelCenterX, -panelCenterY);
        Shape dial = transform.createTransformedShape(dialArm);

        g2.fill(dial);
    }

    public void setTargetAngle(float angle){
        this.targetDialAngle = angle;
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }
}
