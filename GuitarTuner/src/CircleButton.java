import com.formdev.flatlaf.util.Animator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public class CircleButton extends JToggleButton {

    private final Font f = new Font("Segoe UI", Font.PLAIN, 60);
    private boolean mouseOver = false;
    private boolean mousePressed = false;
    public CircleButton(String text){
        super.setText(text);
        setFont(f);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);


        MouseAdapter mouseListener = new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent me){
                if(contains(me.getX(), me.getY())){

                    mousePressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me){
                mousePressed = false;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent me){
                mouseOver = false;
                mousePressed = false;
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent me){
                mouseOver = contains(me.getX(), me.getY());
                System.out.println(isSelected());
                repaint();
            }
        };

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }



    private int getDiameter(){
        int diameter = Math.min(getWidth(), getHeight());
        return diameter;
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
            g2.setColor(Color.RED);
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

