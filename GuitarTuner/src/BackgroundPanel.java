import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundPanel extends JPanel {

    BufferedImage bgImage;

    public BackgroundPanel(MigLayout layout){
        super(layout);
        File imgFile = new File("src/TunerBackG.png");
        try {
            bgImage = ImageIO.read(imgFile);


        }catch (IOException e){
            System.out.println("Image not found.");
        }

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, null);
    }
}
