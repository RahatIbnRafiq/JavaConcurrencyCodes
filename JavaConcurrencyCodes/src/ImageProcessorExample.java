import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class ImageProcessor {

    private String sourceImageLocation;
    private String destinationImageLocation;

    private BufferedImage originalImage;
    private BufferedImage resultImage;
    ImageProcessor(String sourceImageLocation, String destinationImageLocation) throws IOException {
        this.sourceImageLocation = sourceImageLocation;
        this.destinationImageLocation = destinationImageLocation;
        this.originalImage = ImageIO.read(new File(this.sourceImageLocation));
        this.resultImage = new BufferedImage(this.originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

    }

    private void recolorPixel(int x, int y) {
        int rgb = this.originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);


        int newRed;
        int newGreen;
        int newBlue;


        if(isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }
        int newRGB = createRGB(newRed, newGreen, newBlue);
        setRGB(x,y,newRGB);
    }

    private void recolorImage(int leftCorner, int topCorner, int width, int height) {
        for(int x = leftCorner ; x < leftCorner + width && x < this.originalImage.getWidth() ; x++) {
            for(int y = topCorner ; y < topCorner + height && y < this.originalImage.getHeight() ; y++) {
                recolorPixel(x , y);
            }
        }
    }

    private void writeImageFile() throws IOException {
        File outputFile = new File(destinationImageLocation);
        ImageIO.write(this.resultImage, "jpg", outputFile);
    }

    public void recolorSingleThread() throws IOException {
        recolorImage(0, 0, originalImage.getWidth(), originalImage.getHeight());
        writeImageFile();
    }

    public void recolorMultiThread(int numberOfThreads) {
        ArrayList<Thread> threads = new ArrayList<>();
        int width = this.originalImage.getWidth();
        int height = this.originalImage.getHeight() / numberOfThreads;
        for(int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int xOrigin = 0;
                    int yOrigin = height * threadMultiplier;
                    recolorImage(xOrigin, yOrigin, width, height);
                }
            });
            threads.add(thread);
        }

        for(Thread thread: threads) {
            thread.start();
        }

        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    private void setRGB(int x, int y, int rgb) {
        this.resultImage.getRaster().setDataElements(x, y, this.resultImage.getColorModel().getDataElements(rgb, null));
    }

    private int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }

    private int getGreen(int rgb) {
        return rgb & 0x0000FF00 >> 8;
    }

    private int getRed(int rgb) {
        return rgb & 0x00FF0000 >> 16;
    }

    private boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs( green - blue) < 30;
    }

    public int createRGB(int red, int green, int blue) {
        int rgb = 0;
        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;
        rgb |= 0xFF000000;
        return rgb;
    }
}

public class ImageProcessorExample {
    public static void main(String[] args)  throws IOException{
        long startTime = System.currentTimeMillis();
        ImageProcessor imageProcessor = new ImageProcessor("./resources/many-flowers.jpg", "./resources/processed-flowers.jpg");
        //imageProcessor.recolorSingleThread();
        imageProcessor.recolorMultiThread(4);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(String.valueOf(duration));
    }
}
