import org.apache.commons.math4.legacy.linear.ArrayRealVector;
import org.apache.commons.math4.legacy.linear.RealVector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class FrequencyGauge extends JPanel {
    private static final int DEGREES_PER_TICK = 15;
    private final Ellipse2D gaugeBackground;
    private final Ellipse2D gaugeForeground;
    private final Dimension preferredSize;
    private float dialAngle;
    private float targetDialAngle;

    public FrequencyGauge(int width, int height){
        this.gaugeBackground = new Ellipse2D.Double(0.0, 0.0, width, height);
        this.gaugeForeground = new Ellipse2D.Double(2.5, 2.5, width - 5.0, height - 5.0);
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
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        hints.add(new RenderingHints(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE));
        hints.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC));

        g2.setRenderingHints(hints);
        g2.setStroke(new BasicStroke(1));

        double panelCenterX = getWidth() / 2f;
        double panelCenterY = getHeight() / 2f;
        double panelRadius = getWidth() / 2f;

        drawBackground(g2);
        drawIntervalTicks(g2, panelRadius, panelCenterX, panelCenterY);

        double dialCenterDiameter = 30f;
        double dialCenterRadius = dialCenterDiameter / 2f;

        drawDial(g2, dialCenterRadius, dialCenterDiameter, panelCenterX, panelCenterY);

        g2.setColor(Color.BLACK);
    }

    public void drawBackground(Graphics2D g2){
        g2.setColor(Color.BLACK);
        g2.fill(gaugeBackground);
        g2.setColor(Color.WHITE);
        g2.fill(gaugeForeground);
        //g2.fillOval((int) (gaugeBackground.getX() + 10 / 2) ,(int) (gaugeBackground.getY() + 10 / 2), (int) gaugeBackground.getWidth() - 10, (int) gaugeBackground.getHeight() - 10);
    }

    public void drawIntervalTicks(Graphics2D g2, double panelRadius, double panelCenterX, double panelCenterY){

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));

        int tickLength = 20;
        int labelDistFromTick = 35;

        for(int theta = 0; theta <= 360; theta += DEGREES_PER_TICK){

            if(theta >= 180) {

                // Calculate point on a circle using x = r * cos(theta), y = r * cos(theta)
                Point2D pointOnCircle = new Point2D.Double((panelRadius * Math.cos(Math.toRadians(theta)) + panelRadius), (panelRadius * Math.sin(Math.toRadians(theta))) + panelRadius);

                double tickX = (panelRadius - tickLength) * Math.cos(Math.toRadians(theta));
                double tickY = (panelRadius - tickLength) * Math.sin(Math.toRadians(theta));
                g2.draw(new Line2D.Double(pointOnCircle.getX(), pointOnCircle.getY(), panelCenterX + tickX, panelCenterY + tickY));

                FontMetrics fontMetrics = g2.getFontMetrics(getFont());
                double centeredTextX = (fontMetrics.stringWidth(Integer.toString(theta)) / 2f);
                double centeredTextY = (fontMetrics.getHeight() / 4f);

                double labelX = (panelRadius - labelDistFromTick) * Math.cos(Math.toRadians(theta));
                double labelY = (panelRadius - labelDistFromTick) * Math.sin(Math.toRadians(theta));

                g2.drawString(Integer.toString(theta), (int) ((panelCenterX + labelX) - centeredTextX), (int) ((panelCenterY + labelY) + centeredTextY));
            }
        }
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
