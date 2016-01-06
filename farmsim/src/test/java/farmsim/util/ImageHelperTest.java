package farmsim.util;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * Tests for ImageHelper class
 */
public class ImageHelperTest {
    /**
     * Test to check if images actually change at all when edited
     */
    @Test
    public void checkChanged() {
        try {
            File file = new File(
                    this.getClass().getResource("/ground/grass.png").toURI());
            Assert.assertTrue("File does not exist", file.exists());
            Assert.assertTrue("File cannot be read", file.canRead());
            BufferedImage bufferedOriginal = ImageIO.read(file);
            Image awtOriginal = (Image) bufferedOriginal;

            Image awt;
            BufferedImage buffered;

            // test awt image hue
            awt = (Image) ImageIO.read(file);
            Assert.assertFalse("awt image edit hue has no impact",
                    same(ImageHelper.toBufferedImage(awtOriginal),
                            ImageHelper.editHue(awt, (float) 0.5)));
            // test awt image saturation
            awt = (Image) ImageIO.read(file);
            Assert.assertFalse("awt image edit saturation has no impact",
                    same(ImageHelper.toBufferedImage(awtOriginal),
                            ImageHelper.editSaturation(awt, (float) 0.5)));
            // test awt image brightness
            awt = (Image) ImageIO.read(file);
            Assert.assertFalse("awt image edit brightness has no impact",
                    same(ImageHelper.toBufferedImage(awtOriginal),
                            ImageHelper.editBrightness(awt, (float) 0.5)));

            // test buffered image hue
            buffered = ImageIO.read(file);
            Assert.assertFalse("buffered image edit hue has no impact",
                    same(bufferedOriginal,
                            ImageHelper.editHue(buffered, (float) 0.5)));
            // test buffered image saturation
            buffered = ImageIO.read(file);
            Assert.assertFalse("buffered image edit saturation has no impact",
                    same(bufferedOriginal,
                            ImageHelper.editSaturation(buffered, (float) 0.5)));
            // test buffered image brightness
            buffered = ImageIO.read(file);
            Assert.assertFalse("buffered image edit brightness has no impact",
                    same(bufferedOriginal,
                            ImageHelper.editBrightness(buffered, (float) 0.5)));

        } catch (IOException e) {
            Assert.assertFalse("Exception loading image (known formats: " + Arrays.toString(ImageIO.getReaderFormatNames()) + "): " + e.toString(),
                    e instanceof IOException);
        } catch (URISyntaxException e) {
			Assert.fail("URISyntaxException: " + e.getMessage());
		}
    }

    /**
     * Test to check if images become brighter when they should
     */
    @Test
    public void checkBrighter() throws IOException {
        try {
            File file = new File(
                    this.getClass().getResource("/ground/grass.png").toURI());
            Assert.assertNotNull("Image file is null", file);
            BufferedImage bufferedOriginal = ImageIO.read(file);
            Image awtOriginal = (Image) bufferedOriginal;

            Image awt;
            BufferedImage buffered;

            // test awt image brightness
            awt = (Image) ImageIO.read(file);
            Assert.assertTrue("awt image edit not increasing brightness",
                    more(ImageHelper.editBrightness(awt, (float) 0.5),
                            ImageHelper.toBufferedImage(awtOriginal), 2));

            // test buffered image brightness
            buffered = ImageIO.read(file);
            Assert.assertTrue("buffered image edit not increasing brightness",
                    more(ImageHelper.editBrightness(buffered, (float) 0.5),
                            bufferedOriginal, 2));

        } catch (IOException e) {
            Assert.assertFalse("Exception loading image" + e.toString(),
                    e instanceof IOException);
        } catch (URISyntaxException e) {
            Assert.fail("URISyntaxException: " + e.getMessage());
        }
    }

    /**
     * Test to check if images become darker when they should
     */
    @Test
    public void checkDarker() {
        try {
            File file = new File(
                    this.getClass().getResource("/ground/grass.png").toURI());
            BufferedImage bufferedOriginal = ImageIO.read(file);
            Image awtOriginal = (Image) bufferedOriginal;

            Image awt;
            BufferedImage buffered;

            // test awt image brightness
            awt = (Image) ImageIO.read(file);
            Assert.assertTrue("awt image edit not decreasing brightness",
                    less(ImageHelper.editBrightness(awt, (float) -0.5),
                            ImageHelper.toBufferedImage(awtOriginal), 2));

            // test buffered image brightness
            buffered = ImageIO.read(file);
            Assert.assertTrue("buffered image edit not decreasing brightness",
                    less(ImageHelper.editBrightness(buffered, (float) -0.5),
                            bufferedOriginal, 2));

        } catch (IOException e) {
            Assert.assertFalse("Exception loading image" + e.toString(),
                    e instanceof IOException);
        } catch (URISyntaxException e) {
            Assert.fail("URISyntaxException: " + e.getMessage());
        }
    }

    /**
     * Test to check if images become more saturated when they should
     */
    @Test
    public void checkMoreSat() {
        try {
            File file = new File(
                    this.getClass().getResource("/ground/grass.png").toURI());
            BufferedImage bufferedOriginal = ImageIO.read(file);
            Image awtOriginal = (Image) bufferedOriginal;

            Image awt;
            BufferedImage buffered;

            // test awt image saturation
            awt = (Image) ImageIO.read(file);
            Assert.assertTrue("awt image edit not increasing saturation",
                    more(ImageHelper.editSaturation(awt, (float) 0.5),
                            ImageHelper.toBufferedImage(awtOriginal), 1));

            // test buffered image saturation
            buffered = ImageIO.read(file);
            Assert.assertTrue("buffered image edit not increasing saturation",
                    more(ImageHelper.editSaturation(buffered, (float) 0.5),
                            bufferedOriginal, 1));

        } catch (IOException e) {
            Assert.assertFalse("Exception loading image" + e.toString(),
                    e instanceof IOException);
        } catch (URISyntaxException e) {
            Assert.fail("URISyntaxException: " + e.getMessage());
        }
    }

    /**
     * Test to check if images become less saturated when they should
     */
    @Test
    public void checkLessSat() {
        try {
            File file = new File(
                    this.getClass().getResource("/ground/grass.png").toURI());
            BufferedImage bufferedOriginal = ImageIO.read(file);
            Image awtOriginal = (Image) bufferedOriginal;

            Image awt;
            BufferedImage buffered;

            // test awt image saturation
            awt = (Image) ImageIO.read(file);
            Assert.assertTrue("awt image edit not decreasing saturation",
                    less(ImageHelper.editSaturation(awt, (float) -0.5),
                            ImageHelper.toBufferedImage(awtOriginal), 1));

            // test buffered image saturation
            buffered = ImageIO.read(file);
            Assert.assertTrue("buffered image edit not decreasing saturation",
                    less(ImageHelper.editSaturation(buffered, (float) -0.5),
                            bufferedOriginal, 1));

        } catch (IOException e) {
            Assert.assertFalse("Exception loading image" + e.toString(),
                    e instanceof IOException);
        } catch (URISyntaxException e) {
            Assert.fail("URISyntaxException: " + e.getMessage());
        }
    }

    /**
     * Test to check if images become higher hue when they should
     */
    @Test
    public void checkMoreHue() {
        try {
            File file = new File(
                    this.getClass().getResource("/ground/grass.png").toURI());
            BufferedImage bufferedOriginal = ImageIO.read(file);

            Image awt1;
            Image awt2;
            BufferedImage buffered1;
            BufferedImage buffered2;

            // test awt image hue
            awt1 = (Image) ImageIO.read(file);
            awt2 = (Image) ImageIO.read(file);
            Assert.assertTrue("awt image edit not increasing hue",
                    more(ImageHelper.editHue(awt1, (float) 0.9),
                            ImageHelper.editHue(awt2, (float) 0.1), 0));

            // test buffered image hue
            buffered1 = ImageIO.read(file);
            buffered2 = ImageIO.read(file);
            Assert.assertTrue("buffered image edit not increasing hue",
                    more(ImageHelper.editHue(buffered1, (float) 0.9),
                            ImageHelper.editHue(buffered2, (float) 0.1), 0));

        } catch (IOException e) {
            Assert.assertFalse("Exception loading image" + e.toString(),
                    e instanceof IOException);
        } catch (URISyntaxException e) {
            Assert.fail("URISyntaxException: " + e.getMessage());
        }
    }

    /**
     * Test to check if images become lower hue when they should
     */
    @Test
    public void checkLessHue() {
        try {
            File file = new File(
                    this.getClass().getResource("/ground/grass.png").toURI());
            BufferedImage bufferedOriginal = ImageIO.read(file);

            Image awt1;
            Image awt2;
            BufferedImage buffered1;
            BufferedImage buffered2;

            // test awt image hue
            awt1 = (Image) ImageIO.read(file);
            awt2 = (Image) ImageIO.read(file);
            Assert.assertTrue("awt image edit not increasing hue",
                    less(ImageHelper.editHue(awt1, (float) 0.1),
                            ImageHelper.editHue(awt2, (float) 0.9), 0));

            // test buffered image hue
            buffered1 = ImageIO.read(file);
            buffered2 = ImageIO.read(file);
            Assert.assertTrue("buffered image edit not increasing hue",
                    less(ImageHelper.editHue(buffered1, (float) 0.1),
                            ImageHelper.editHue(buffered2, (float) 0.9), 0));

        } catch (IOException e) {
            Assert.assertFalse("Exception loading image" + e.toString(),
                    e instanceof IOException);
        } catch (URISyntaxException e) {
            Assert.fail("URISyntaxException: " + e.getMessage());
        }
    }

    /**
     * Test that a nullPointException is thrown when it should be
     */
    @Test(expected = NullPointerException.class)
    public void nullImagesTest() {
        Image awt = null;
        BufferedImage buffered = null;

        // test awt image hue
        ImageHelper.editHue(awt, (float) 0.5);
        // test awt image saturation
        ImageHelper.editSaturation(awt, (float) 0.5);
        // test awt image brightness
        ImageHelper.editBrightness(awt, (float) 0.5);

        // test buffered image hue
        ImageHelper.editHue(buffered, (float) 0.5);
        // test buffered image saturation
        ImageHelper.editSaturation(buffered, (float) 0.5);
        // test buffered image brightness
        ImageHelper.editBrightness(buffered, (float) 0.5);
    }

    /**
     * Test that a nullPointException is thrown when it should be
     */
    @Test
    public void badPercentTest() {
        try {
            int tests = 0;
            int exceptions = 0;

            File file = new File(
                    this.getClass().getResource("/ground/grass.png").toURI());
            BufferedImage bufferedOriginal = ImageIO.read(file);

            Image awt;
            BufferedImage buffered;

            // test awt image hue - too large
            awt = (Image) ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editHue(awt, (float) 5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue("Illegal arguments accepted for awt edit hue",
                    tests == exceptions);
            // test awt image hue - too small
            awt = (Image) ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editHue(awt, (float) -5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue("Illegal arguments accepted for awt edit hue",
                    tests == exceptions);

            // test awt image saturation - too large
            awt = (Image) ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editSaturation(awt, (float) 5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue(
                    "Illegal arguments accepted for awt edit saturation",
                    tests == exceptions);
            // test awt image saturation - too small
            awt = (Image) ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editSaturation(awt, (float) -5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue(
                    "Illegal arguments accepted for awt edit saturation",
                    tests == exceptions);

            // test awt image brightness - too large
            awt = (Image) ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editBrightness(awt, (float) 5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue(
                    "Illegal arguments accepted for awt edit brightness",
                    tests == exceptions);
            // test awt image brightness - too small
            awt = (Image) ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editBrightness(awt, (float) -5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue(
                    "Illegal arguments accepted for awt edit brightness",
                    tests == exceptions);

            // test buffered image hue - too large
            buffered = ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editHue(buffered, (float) 5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue(
                    "Illegal arguments accepted for buffered edit hue",
                    tests == exceptions);
            // test buffered image hue - too small
            buffered = ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editHue(buffered, (float) -5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue(
                    "Illegal arguments accepted for buffered edit hue",
                    tests == exceptions);

            // test buffered image saturation - too large
            buffered = ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editSaturation(buffered, (float) 5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue(
                    "Illegal arguments accepted for buffered edit saturation",
                    tests == exceptions);
            // test buffered image saturation - too small
            buffered = ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editSaturation(buffered, (float) -5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue(
                    "Illegal arguments accepted for buffered edit saturation",
                    tests == exceptions);

            // test buffered image brightness - too large
            buffered = ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editBrightness(buffered, (float) 5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue(
                    "Illegal arguments accepted for buffered edit brightness",
                    tests == exceptions);
            // test buffered image brightness - too small
            buffered = ImageIO.read(file);
            try {
                tests++;
                ImageHelper.editBrightness(buffered, (float) -5);
            } catch (IllegalArgumentException e) {
                exceptions++;
            }
            Assert.assertTrue(
                    "Illegal arguments accepted for buffered edit brightness",
                    tests == exceptions);

        } catch (IOException e) {
            Assert.assertFalse("Exception loading image" + e.toString(),
                    e instanceof IOException);
        } catch (URISyntaxException e) {
            Assert.fail("URISyntaxException: " + e.getMessage());
        }
    }

    /**
     * Check if two images are the same
     * 
     * @param image1 First image to check
     * @param image2 Second image to check
     * @return true if images are same size, and corresponding pixels have the
     *         same RGBA false otherwise
     */
    public boolean same(BufferedImage image1, BufferedImage image2) {
        if ((image1.getHeight() != image2.getHeight())
                || (image1.getWidth() != image2.getWidth())) {
            return false;
        }
        for (int i = 0; i < image1.getHeight(); i++) {
            for (int j = 0; j < image1.getWidth(); j++) {
                Color pixel1 = new Color(image1.getRGB(i, j), true);
                Color pixel2 = new Color(image2.getRGB(i, j), true);
                if (!pixel1.equals(pixel2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if the property of one image is greater than that of the other
     * 
     * @param image1 First image to check
     * @param image2 Second image to check
     * @param property The property to check - 0 for Hue, 1 for Saturation, 2
     *        for Brightness
     * @return true if image1's property is greater than image2's for every
     *         pixel false otherwise
     */
    public boolean more(BufferedImage image1, BufferedImage image2,
            int property) {
        if ((image1.getHeight() != image2.getHeight())
                || (image1.getWidth() != image2.getWidth())) {
            return false;
        }
        for (int i = 0; i < image1.getHeight(); i++) {
            for (int j = 0; j < image1.getWidth(); j++) {
                Color pixel1 = new Color(image1.getRGB(i, j), true);
                Color pixel2 = new Color(image2.getRGB(i, j), true);
                float[] hsb1 = Color.RGBtoHSB(pixel1.getRed(),
                        pixel1.getGreen(), pixel1.getBlue(), null);
                float[] hsb2 = Color.RGBtoHSB(pixel2.getRed(),
                        pixel2.getGreen(), pixel2.getBlue(), null);

                if (hsb1[property] <= hsb2[property]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if the property of one image is less than that of the other
     * 
     * @param image1 First image to check
     * @param image2 Second image to check
     * @param property The property to check - 0 for Hue, 1 for Saturation, 2
     *        for Brightness
     * @return true if image1's property is less than image2's for every pixel
     *         false otherwise
     */
    public boolean less(BufferedImage image1, BufferedImage image2,
            int property) {
        if ((image1.getHeight() != image2.getHeight())
                || (image1.getWidth() != image2.getWidth())) {
            return false;
        }
        for (int i = 0; i < image1.getHeight(); i++) {
            for (int j = 0; j < image1.getWidth(); j++) {
                Color pixel1 = new Color(image1.getRGB(i, j), true);
                Color pixel2 = new Color(image2.getRGB(i, j), true);
                float[] hsb1 = Color.RGBtoHSB(pixel1.getRed(),
                        pixel1.getGreen(), pixel1.getBlue(), null);
                float[] hsb2 = Color.RGBtoHSB(pixel2.getRed(),
                        pixel2.getGreen(), pixel2.getBlue(), null);

                if (hsb1[property] >= hsb2[property]) {
                    return false;
                }
            }
        }
        return true;
    }
}
