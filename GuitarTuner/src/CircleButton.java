import com.formdev.flatlaf.util.Animator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public class CircleButton extends JToggleButton  {
    public CircleButton(String text, Action action, String name){
        setName(name);
        setAction(action);
        setFont(new Font("Segoe UI", Font.PLAIN, 60));
        setText(text);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private int getDiameter(){
        return Math.min(getWidth(), getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        int diameter = getDiameter();
        int radius = diameter/2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(hints);

        if(isSelected()){
            g2.setColor(Color.ORANGE.darker());
            setEnabled(false);
        }

        g2.fillOval(getWidth()/2 - radius, getHeight()/2 - radius, diameter, diameter);

        FontMetrics metrics = g2.getFontMetrics();
        int stringW = metrics.stringWidth(getText());
        int stringH = metrics.getHeight();
        g2.setColor(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString(getText(), getWidth()/2 - stringW/2, getHeight()/2 + stringH/4);
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics metrics = getGraphics().getFontMetrics(getFont());
        int minDiameter = 10 + Math.max(metrics.stringWidth(getText()), metrics.getHeight());
        return new Dimension(minDiameter, minDiameter);
    }
}

