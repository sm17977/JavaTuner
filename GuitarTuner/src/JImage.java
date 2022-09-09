import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JImage extends JPanel{
    Image img;
    Dimension size;

    private int x = 0;
    private int y = 0;
    Color red;


    public JImage(File file, int w_margin, int h_margin){
        //setBorder(BorderFactory.createLineBorder(Color.black));
        red = new Color(1f, 0f, 0f, 0f);
        setBackground(Color.LIGHT_GRAY);
        try {
            img = ImageIO.read(file);
            size = getImageDimension(file);
            setSize(size.width + w_margin, size.height + h_margin);
            System.out.println("JPanel width: "  + getWidth());
            System.out.println("JPanel height: "  + getHeight());
            System.out.println("Image width: "  + size.width);
            System.out.println("Image height: "  + size.height);
            x = (getWidth() - size.width) / 2;
            y = (getHeight() - size.height) / 2;
        }catch (IOException e){
            System.out.println("Image not found.");
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, x, y, Color.LIGHT_GRAY, null);
    }

    public static Dimension getImageDimension(File file) throws IOException{

        int pos = file.getName().lastIndexOf(".");
        if (pos == -1)
            throw new IOException("No extension for file: " + file.getAbsolutePath());
        String suffix = file.getName().substring(pos + 1);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        while(iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(file);
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                return new Dimension(width, height);
            } catch (IOException e) {
                System.out.println("Error reading: " + file.getAbsolutePath() +  e);
            } finally {
                reader.dispose();
            }
        }

        throw new IOException("Not a known image file: " + file.getAbsolutePath());
    }
}
