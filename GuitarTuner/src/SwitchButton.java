import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwitchButton extends JToggleButton {
    public Timer animationTimer;
    private float switchPosition;
    private final float switchSpeed = 0.4f;

    public SwitchButton(Action action){
        super.setAction(action);
        super.setOpaque(false);
        super.setName("Auto");
        setBackground(Color.RED);
        setPreferredSize(new Dimension(50, 25));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchPosition = 2;

        animationTimer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isSelected()){
                    int switchAnimationEndPosition = getWidth() - getHeight();
                    if(switchPosition < switchAnimationEndPosition){
                        switchPosition += switchSpeed;
                        repaint();
                    }
                    else{
                        animationTimer.stop();
                        switchPosition = switchAnimationEndPosition;
                        repaint();
                    }
                }
                else{
                    int switchAnimationEndPosition = 2;
                    if (switchPosition > switchAnimationEndPosition) {
                        switchPosition -= switchSpeed;
                        repaint();
                    }
                    else{
                        animationTimer.stop();
                        switchPosition = switchAnimationEndPosition;
                        repaint();
                    }
                }
            }
        });
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationTimer.start();
            }
        });
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
        g2.fillOval((int) switchPosition, 2, height-4, height-4);

    }
    private float getAlpha(){
        float width = getWidth() - getHeight();
        float alpha = (switchPosition - 2) / width;
        if (alpha < 0){
            alpha =0 ;
        }
        if (alpha > 1){
            alpha = 1;
        }
        return  alpha;
    }
}

