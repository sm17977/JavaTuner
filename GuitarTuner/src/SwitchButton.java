import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

public class SwitchButton extends JComponent {
    private Timer timer;
    private float location;
    private boolean selected;
    private boolean mouseOver;
    private float speed = 0.4f;
    private boolean isAuto;


    public SwitchButton(){
        this.isAuto = isAuto;
        setBackground(Color.RED);
        setPreferredSize(new Dimension(50, 25));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        location = 2;

        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isSelected()){
                    int endLocation = getWidth() - getHeight();
                    if(location < endLocation){
                        location += speed;
                        repaint();
                    }
                    else{
                        timer.stop();
                        location = endLocation;
                        repaint();
                    }
                }
                else{
                    int endlocation = 2;
                    if (location > endlocation) {
                        location -= speed;
                        repaint();
                    }
                    else{
                        timer.stop();
                        location = endlocation;
                        repaint();
                    }
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseOver = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseOver = false;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    if(mouseOver){
                        selected = !selected;
                        timer.start();
                    }
                }
            }
        });
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
        timer.start();
    }



    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        float alpha = getAlpha();
        if(alpha < 1){
            g2.setColor(Color.gray);
            g2.fillRoundRect(0, 0, width, height, 25, 25);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(getBackground());
        g2.fillRoundRect(0,0, width, height, 25, 25);
        g2.setColor(getForeground());
        g2.setComposite(AlphaComposite.SrcOver);
        g2.fillOval((int) location, 2, height-4, height-4);
        super.paint(g2);
    }
    private float getAlpha(){
        float width = getWidth() - getHeight();
        float alpha = (location - 2) / width;
        if (alpha < 0){
            alpha =0 ;
        }
        if (alpha > 1){
            alpha = 1;
        }
        return  alpha;
    }
}

