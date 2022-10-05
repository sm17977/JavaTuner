import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JImage extends JPanel{
    BufferedImage img;
    BufferedImage imgShadow;
    Dimension size;
    private int x = 0;
    private int y = 0;
    Color red;

    public JImage(File file, int w_margin, int h_margin){
        //setBorder(BorderFactory.createLineBorder(Color.black));
        setOpaque(false);
        red = new Color(1f, 0f, 0f, 0f);
        setBackground(Color.ORANGE);
        try {
            img = ImageIO.read(file);
            imgShadow = ImageIO.read(file);
            colorImage(imgShadow);

            size = getImageDimension(file);
            setSize(size.width + w_margin, size.height + h_margin);
            x = (getWidth() - size.width) / 2;
            y = (getHeight() - size.height) / 2;
        }catch (IOException e){
            System.out.println("Image not found.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(imgShadow, x+5, y+5, null, null);
        g2.drawImage(img, x, y, null, null);
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

    private static BufferedImage colorImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster raster = image.getRaster();

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                int[] pixels = raster.getPixel(xx, yy, (int[]) null);
                pixels[0] = 0;
                pixels[1] = 0;
                pixels[2] = 0;
                raster.setPixel(xx, yy, pixels);
            }
        }
        return image;
    }
}
