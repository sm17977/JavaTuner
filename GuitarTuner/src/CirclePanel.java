import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class CirclePanel extends JPanel {
    private final Ellipse2D circleBackground;
    private final Dimension preferredSize;
    private float dialAngle;
    private float targetDialAngle;

    private static final int DEGREES_PER_TICK = 10;

    public CirclePanel(int width, int height){
        this.circleBackground = new Ellipse2D.Double(0, 0, width, height);
        this.preferredSize = new Dimension(width, height);
        setOpaque(false);
        dialAngle = 0;
        targetDialAngle = 0;
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

    public void setTargetAngle(float angle){
        this.targetDialAngle = angle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(hints);
        g2.setStroke(new BasicStroke(3));

        // Draw circle panel background
        g2.setColor(Color.WHITE);
        g2.fill(circleBackground);

        // Create and draw dial center
        double panelCenterX = getWidth() / 2f;
        double panelCenterY = getHeight() / 2f;
        double dialCenterDiameter = 30;
        double dialCenterRadius = dialCenterDiameter/2;

        // Draw border interval ticks
        g2.setColor(Color.BLACK);
        g2.drawOval(3, 3, 294, 294);
       // g2.drawLine();
        double panelRadius = getWidth() / 2f;
        g2.setStroke(new BasicStroke(1));

        for(int theta = 0; theta <= 360; theta += DEGREES_PER_TICK){
            if(theta >= 180) {
                // Calculate point on a circle using x = r * cos(theta), y = r * cos(theta)
                Point2D pointOnCircle = new Point((int) ((panelRadius * Math.cos(Math.toRadians(theta))) + panelRadius), (int) ((panelRadius * Math.sin(Math.toRadians(theta))) + panelRadius));
                // Add radius because the origin of the panel is not the center, and it needs to be for this formula
                Line2D line = new Line2D.Double(1.0, 1.0, 1.0, 1.0);
                g2.drawLine((int) (pointOnCircle.getX()), (int) (pointOnCircle.getY()), (int) panelCenterX, (int) panelCenterY);
            }
        }

        g2.setColor(Color.WHITE);
        g2.fillOval((int)panelRadius - 140, (int)panelRadius - 140, 280, 280);



        g2.setColor(Color.RED);
        Ellipse2D dialCenter = new Ellipse2D.Double(panelCenterX - dialCenterRadius, panelCenterY - dialCenterRadius, dialCenterDiameter, dialCenterDiameter);
        g2.fill(dialCenter);

        // Create and draw dial arm
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

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g2.drawString("-10", getWidth() / 5f, getHeight() / 5f);
        g2.drawString("+10", getWidth() - (getWidth() / 5f) - 40, getHeight() / 5f);
        g2.drawString("0", (getWidth() / 2f) - 5, (getHeight() / 5f) - 30);
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }
}
