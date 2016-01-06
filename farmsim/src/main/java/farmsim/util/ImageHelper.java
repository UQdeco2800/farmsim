package farmsim.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javafx.embed.swing.SwingFXUtils;

/**
 * Methods to tint anc colourise an image
 *
 * @author matthew-lake
 */
public class ImageHelper {

    /** Empty private constructor to keep sonar happy
     */
    private ImageHelper() {

    }

    /**
     * Edit the hue, saturation or brightness of an image according by a
     * percentage
     *
     * @param img The image to be edited
     * @param property The property ('h','s','b') to be edited
     * @param percent If editing hue, the degree of the shift as a percentage of
     *        240 degrees. If editing saturation or brightness, the change in
     *        that value as a percentage of the difference between the current
     *        value and the max
     * @return An edited BufferedImage
     */
    public static BufferedImage edit(Image img, String property,
            float percent) {
        BufferedImage buffer = toBufferedImage(img);
        int height = buffer.getHeight();
        int width = buffer.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixel = new Color(buffer.getRGB(x, y), true);

                int a = pixel.getAlpha(); // alpha
                int r = pixel.getRed(); // red
                int g = pixel.getGreen(); // green
                int b = pixel.getBlue(); // blue

                float[] hsb = Color.RGBtoHSB(r, g, b, null);

                switch (property) {
                    case "h":
                        hsb[0] = (float) 0.6667 * (percent);
                        pixel = colorFromHSB(hsb[0], hsb[1], hsb[2]);
                        pixel = new Color(pixel.getRed(), pixel.getGreen(),
                                pixel.getBlue(), a);
                        break;
                    case "s":
                        hsb[1] += (1.0 - hsb[1]) * percent;
                        pixel = colorFromHSB(hsb[0], hsb[1], hsb[2]);
                        pixel = new Color(pixel.getRed(), pixel.getGreen(),
                                pixel.getBlue(), a);
                        break;
                    case "b":
                        hsb[2] += (1.0 - hsb[2]) * percent;
                        pixel = colorFromHSB(hsb[0], hsb[1], hsb[2]);
                        pixel = new Color(pixel.getRed(), pixel.getGreen(),
                                pixel.getBlue(), a);
                        break;
                    default:
                        // Do nothing, invalid input
                }

                buffer.setRGB(x, y, pixel.getRGB());
            }
        }
        return buffer;
    }

    /**
     * Make colour from HSB
     * @param hue
     *          hue to use
     * @param sat
     *          saturation to use
     * @param bright
     *          brightness to use
     * @return
     *          color with hue hue, saturation sat, and brightness bright
     */
    private static Color colorFromHSB(float hue, float sat, float bright) {
        return new Color(Color.HSBtoRGB(hue, sat, bright), true);
    }

    /**
     * Edit the hue of an awt image
     * 
     * @param img The awt image to edit
     * @param percent The degree of the shift as a percentage of 240 degrees.
     *        Must be in range [0,1]
     * @return The edited BufferedImage
     */
    public static BufferedImage editHue(Image img, float percent) {
        if ((percent < 0) || (percent > 1)) {
            throw new IllegalArgumentException();
        }
        return edit(img, "h", percent);
    }

    /**
     * Edit the hue of a buffered image
     * 
     * @param img The buffered image to edit
     * @param percent The degree of the shift as a percentage of 240 degrees.
     *        Must be in range [0,1]
     * @return The edited BufferedImage
     */
    public static BufferedImage editHue(BufferedImage img, float percent) {
        if ((percent < 0) || (percent > 1)) {
            throw new IllegalArgumentException();
        }
        return edit(img, "h", percent);
    }

    /**
     * Edit the hue of a javafx image
     * 
     * @param img The javafx image to edit
     * @param percent The degree of the shift as a percentage of 240 degrees.
     *        Must be in range [0,1]
     * @return The edited BufferedImage
     */
    public static BufferedImage editHue(javafx.scene.image.Image img,
            float percent) {
        if ((percent < 0) || (percent > 1)) {
            throw new IllegalArgumentException();
        }
        BufferedImage buffer = new BufferedImage((int) img.getWidth(),
                (int) img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Image awt = SwingFXUtils.fromFXImage(img, buffer);

        return edit(awt, "h", percent);
    }

    /**
     * Edit the saturation of an awt image
     * 
     * @param img The awt image to edit
     * @param percent the change in saturation as a percentage of the difference
     *        between the current value and the max. Must be in range [-1,1]
     * @return The edited BufferedImage
     */
    public static BufferedImage editSaturation(Image img, float percent) {
        if ((percent < -1) || (percent > 1)) {
            throw new IllegalArgumentException();
        }
        return edit(img, "s", percent);
    }

    /**
     * Edit the saturation of a buffered image
     * 
     * @param img The buffered image to edit
     * @param percent the change in saturation as a percentage of the difference
     *        between the current value and the max. Must be in range [-1,1]
     * @return The edited BufferedImage
     */
    public static BufferedImage editSaturation(BufferedImage img,
            float percent) {
        if ((percent < -1) || (percent > 1)) {
            throw new IllegalArgumentException();
        }
        return edit(img, "s", percent);
    }

    /**
     * Edit the saturation of a javafx image
     * 
     * @param img The javafx image to edit
     * @param percent the change in saturation as a percentage of the difference
     *        between the current value and the max. Must be in range [-1,1]
     * @return The edited BufferedImage
     */
    public static BufferedImage editSaturation(javafx.scene.image.Image img,
            float percent) {
        if ((percent < -1) || (percent > 1)) {
            throw new IllegalArgumentException();
        }
        BufferedImage buffer = new BufferedImage((int) img.getWidth(),
                (int) img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Image awt = SwingFXUtils.fromFXImage(img, buffer);

        return edit(awt, "s", percent);
    }

    /**
     * Edit the brightness of an awt image
     * 
     * @param img The awt image to edit
     * @param percent the change in brightness as a percentage of the difference
     *        between the current value and the max. Must be in range [-1,1]
     * @return The edited BufferedImage
     */
    public static BufferedImage editBrightness(Image img, float percent) {
        if ((percent < -1) || (percent > 1)) {
            throw new IllegalArgumentException();
        }
        return edit(img, "b", percent);
    }

    /**
     * Edit the brightness of a buffered image
     * 
     * @param img The buffered image to edit
     * @param percent the change in brightness as a percentage of the difference
     *        between the current value and the max. Must be in range [-1,1]
     * @return The edited BufferedImage
     */
    public static BufferedImage editBrightness(BufferedImage img,
            float percent) {
        if ((percent < -1) || (percent > 1)) {
            throw new IllegalArgumentException();
        }
        return edit(img, "b", percent);
    }

    /**
     * Edit the brightness of a javafx image
     * 
     * @param img The javafx image to edit
     * @param percent the change in brightness as a percentage of the difference
     *        between the current value and the max. Must be in range [-1,1]
     * @return The edited BufferedImage
     */
    public static BufferedImage editBrightness(javafx.scene.image.Image img,
            float percent) {
        if ((percent < -1) || (percent > 1)) {
            throw new IllegalArgumentException();
        }
        BufferedImage buffer = new BufferedImage((int) img.getWidth(),
                (int) img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Image awt = SwingFXUtils.fromFXImage(img, buffer);

        return edit(awt, "b", percent);
    }

    /**
     * Blend two buffered images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image 
     * N.B. Images must be the same size.
     * 
     * @param img1 The first image to blend
     * @param img2 The second image to blend
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The blended image
     */
    public static BufferedImage blend(BufferedImage img1, BufferedImage img2,
            double bias) {
        if ((img1.getHeight() != img2.getHeight())
                || (img1.getWidth() != img2.getWidth())) {
            throw new IllegalArgumentException("Images must be the same size");
        }
        BufferedImage buffer = new BufferedImage((int) img1.getWidth(),
                (int) img1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int height = buffer.getHeight();
        int width = buffer.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixel1 = new Color(img1.getRGB(x, y), true);
                Color pixel2 = new Color(img2.getRGB(x, y), true);

                Color pixel = new Color(
                        (int) (pixel1.getRed() * (1 - bias)
                                + pixel2.getRed() * bias),
                        (int) (pixel1.getGreen() * (1 - bias)
                                + pixel2.getGreen() * bias),
                        (int) (pixel1.getBlue() * (1 - bias)
                                + pixel2.getBlue() * bias),
                        (int) (pixel1.getAlpha() * (1 - bias)
                                + pixel2.getAlpha() * bias));

                buffer.setRGB(x, y, pixel.getRGB());
            }
        }
        return buffer;
    }

    /**
     * Blend a bufferd image and an awt image together by averaging the rgba
     * values of their pixels - useful for applying masks/overlays to an image
     * N.B. Images must be the same size.
     * 
     * @param img1 The first image to blend
     * @param img2 The second image to blend
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The blended image
     */
    public static BufferedImage blend(BufferedImage img1, Image img2,
            double bias) {
        BufferedImage buffer2 = toBufferedImage(img2);

        return blend(img1, buffer2, bias);
    }

    /**
     * Blend a bufferd image and a javafx image together by averaging the rgba
     * values of their pixels - useful for applying masks/overlays to an image
     * N.B. Images must be the same size.
     * 
     * @param img1 The first image to blend
     * @param img2 The second image to blend
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The blended image
     */
    public static BufferedImage blend(BufferedImage img1,
            javafx.scene.image.Image img2, double bias) {
        BufferedImage buffer2 = SwingFXUtils.fromFXImage(img2, null);

        return blend(img1, buffer2, bias);
    }

    /**
     * Blend two awt images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image N.B. Images must
     * be the same size.
     * 
     * @param img1 The first image to blend
     * @param img2 The second image to blend
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The blended image
     */
    public static BufferedImage blend(Image img1, Image img2, double bias) {
        BufferedImage buffer1 = toBufferedImage(img1);
        BufferedImage buffer2 = toBufferedImage(img2);

        return blend(buffer1, buffer2, bias);
    }

    /**
     * Blend two awt images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image N.B. Images must
     * be the same size.
     * 
     * @param img1 The first image to blend
     * @param img2 The second image to blend
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The blended image
     */
    public static BufferedImage blend(Image img1, javafx.scene.image.Image img2,
            double bias) {
        BufferedImage buffer1 = toBufferedImage(img1);
        BufferedImage buffer2 = SwingFXUtils.fromFXImage(img2, null);

        return blend(buffer1, buffer2, bias);
    }

    /**
     * Blend two javafx images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image N.B. Images must
     * be the same size.
     * 
     * @param img1 The first image to blend
     * @param img2 The second image to blend
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The blended image
     */
    public static BufferedImage blend(javafx.scene.image.Image img1,
            javafx.scene.image.Image img2, double bias) {
        BufferedImage buffer1 = SwingFXUtils.fromFXImage(img1, null);
        BufferedImage buffer2 = SwingFXUtils.fromFXImage(img2, null);

        return blend(buffer1, buffer2, bias);
    }

    /**
     * add two buffered images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image 
     * N.B. Images must be the same size.
     *
     * @param img1 The first image to add
     * @param img2 The second image to add
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The added image
     */
    public static BufferedImage add(BufferedImage img1, BufferedImage img2,
                                      double bias) {
        if ((img1.getHeight() != img2.getHeight())
                || (img1.getWidth() != img2.getWidth())) {
            throw new IllegalArgumentException("Images must be the same size");
        }
        BufferedImage buffer = new BufferedImage((int) img1.getWidth(),
                (int) img1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int height = buffer.getHeight();
        int width = buffer.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixel1 = new Color(img1.getRGB(x, y), true);
                Color pixel2 = new Color(img2.getRGB(x, y), true);

                Color pixel = new Color(
                    Math.min((int) (pixel1.getRed() + pixel2.getRed() * bias), 255),
                    Math.min((int) (pixel1.getGreen() + pixel2.getGreen() * bias), 255),
                    Math.min((int) (pixel1.getBlue() + pixel2.getBlue() * bias), 255),
                    Math.min((int) (pixel1.getAlpha() + pixel2.getAlpha() * bias), 255));

                buffer.setRGB(x, y, pixel.getRGB());
            }
        }
        return buffer;
    }

    /**
     * add a bufferd image and an awt image together by averaging the rgba
     * values of their pixels - useful for applying masks/overlays to an image
     * N.B. Images must be the same size.
     *
     * @param img1 The first image to add
     * @param img2 The second image to add
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The added image
     */
    public static BufferedImage add(BufferedImage img1, Image img2,
                                      double bias) {
        BufferedImage buffer2 = toBufferedImage(img2);

        return add(img1, buffer2, bias);
    }

    /**
     * add a bufferd image and a javafx image together by averaging the rgba
     * values of their pixels - useful for applying masks/overlays to an image
     * N.B. Images must be the same size.
     *
     * @param img1 The first image to add
     * @param img2 The second image to add
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The added image
     */
    public static BufferedImage add(BufferedImage img1,
                                      javafx.scene.image.Image img2, double bias) {
        BufferedImage buffer2 = SwingFXUtils.fromFXImage(img2, null);

        return add(img1, buffer2, bias);
    }

    /**
     * add two awt images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image N.B. Images must
     * be the same size.
     *
     * @param img1 The first image to add
     * @param img2 The second image to add
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The added image
     */
    public static BufferedImage add(Image img1, Image img2, double bias) {
        BufferedImage buffer1 = toBufferedImage(img1);
        BufferedImage buffer2 = toBufferedImage(img2);

        return add(buffer1, buffer2, bias);
    }

    /**
     * add two awt images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image N.B. Images must
     * be the same size.
     *
     * @param img1 The first image to add
     * @param img2 The second image to add
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The added image
     */
    public static BufferedImage add(Image img1, javafx.scene.image.Image img2,
                                      double bias) {
        BufferedImage buffer1 = toBufferedImage(img1);
        BufferedImage buffer2 = SwingFXUtils.fromFXImage(img2, null);

        return add(buffer1, buffer2, bias);
    }

    /**
     * add two javafx images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image N.B. Images must
     * be the same size.
     *
     * @param img1 The first image to add
     * @param img2 The second image to add
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The added image
     */
    public static BufferedImage add(javafx.scene.image.Image img1,
                                      javafx.scene.image.Image img2, double bias) {
        BufferedImage buffer1 = SwingFXUtils.fromFXImage(img1, null);
        BufferedImage buffer2 = SwingFXUtils.fromFXImage(img2, null);

        return add(buffer1, buffer2, bias);
    }

    /**
     * subtract two buffered images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image 
     * N.B. Images must be the same size.
     *
     * @param img1 The first image to subtract
     * @param img2 The second image to subtract
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The subtracted image
     */
    public static BufferedImage subtract(BufferedImage img1, BufferedImage img2,
                                    double bias) {
        if ((img1.getHeight() != img2.getHeight())
                || (img1.getWidth() != img2.getWidth())) {
            throw new IllegalArgumentException("Images must be the same size");
        }
        BufferedImage buffer = new BufferedImage((int) img1.getWidth(),
                (int) img1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int height = buffer.getHeight();
        int width = buffer.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixel1 = new Color(img1.getRGB(x, y), true);
                Color pixel2 = new Color(img2.getRGB(x, y), true);

                Color pixel = new Color(
                        Math.max((int) (pixel1.getRed() - pixel2.getRed() * bias), 0),
                        Math.max((int) (pixel1.getGreen() - pixel2.getGreen() * bias), 0),
                        Math.max((int) (pixel1.getBlue() - pixel2.getBlue() * bias), 0),
                        Math.max((int) (pixel1.getAlpha() - pixel2.getAlpha() * bias), 0));

                buffer.setRGB(x, y, pixel.getRGB());
            }
        }
        return buffer;
    }

    /**
     * subtract a bufferd image and an awt image together by averaging the rgba
     * values of their pixels - useful for applying masks/overlays to an image
     * N.B. Images must be the same size.
     *
     * @param img1 The first image to subtract
     * @param img2 The second image to subtract
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The subtracted image
     */
    public static BufferedImage subtract(BufferedImage img1, Image img2,
                                    double bias) {
        BufferedImage buffer2 = toBufferedImage(img2);

        return subtract(img1, buffer2, bias);
    }

    /**
     * subtract a bufferd image and a javafx image together by averaging the rgba
     * values of their pixels - useful for applying masks/overlays to an image
     * N.B. Images must be the same size.
     *
     * @param img1 The first image to subtract
     * @param img2 The second image to subtract
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The subtracted image
     */
    public static BufferedImage subtract(BufferedImage img1,
                                    javafx.scene.image.Image img2, double bias) {
        BufferedImage buffer2 = SwingFXUtils.fromFXImage(img2, null);

        return subtract(img1, buffer2, bias);
    }

    /**
     * subtract two awt images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image N.B. Images must
     * be the same size.
     *
     * @param img1 The first image to subtract
     * @param img2 The second image to subtract
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The subtracted image
     */
    public static BufferedImage subtract(Image img1, Image img2, double bias) {
        BufferedImage buffer1 = toBufferedImage(img1);
        BufferedImage buffer2 = toBufferedImage(img2);

        return subtract(buffer1, buffer2, bias);
    }

    /**
     * subtract two awt images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image N.B. Images must
     * be the same size.
     *
     * @param img1 The first image to subtract
     * @param img2 The second image to subtract
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The subtracted image
     */
    public static BufferedImage subtract(Image img1, javafx.scene.image.Image img2,
                                    double bias) {
        BufferedImage buffer1 = toBufferedImage(img1);
        BufferedImage buffer2 = SwingFXUtils.fromFXImage(img2, null);

        return subtract(buffer1, buffer2, bias);
    }

    /**
     * subtract two javafx images together by averaging the rgba values of their
     * pixels - useful for applying masks/overlays to an image N.B. Images must
     * be the same size.
     *
     * @param img1 The first image to subtract
     * @param img2 The second image to subtract
     * @param bias The bias of the interpolation - 0 for completely img1 to 1
     *        for completely img2
     * @return The subtracted image
     */
    public static BufferedImage subtract(javafx.scene.image.Image img1,
                                    javafx.scene.image.Image img2, double bias) {
        BufferedImage buffer1 = SwingFXUtils.fromFXImage(img1, null);
        BufferedImage buffer2 = SwingFXUtils.fromFXImage(img2, null);

        return subtract(buffer1, buffer2, bias);
    }
    
    /**
     * Converts an awt Image to a BufferedImage
     *
     * @param img The awt Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage buffer = new BufferedImage(img.getWidth(null),
                img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D graphics = buffer.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        // Return the buffered image
        return buffer;
    }

    /**
     * Make a deep copy of a buffered image
     * 
     * @param image The image to copy
     * @return A completely new copy
     */
    public static BufferedImage deepCopy(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
