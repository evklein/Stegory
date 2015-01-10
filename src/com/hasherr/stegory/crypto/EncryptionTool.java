package com.hasherr.stegory.crypto;

import com.hasherr.stegory.image.Pixel;
import com.hasherr.stegory.util.Utilities;

import java.awt.image.BufferedImage;

/**
 * Created by Evan on 1/4/2015.
 */
public class EncryptionTool
{
    private BufferedImage carrier, message;
    private Pixel[][] carrierPixels, messagePixels;
    private int[] messageValues;

    /**
     * Constructs a new EncryptionTool using specified carrier and message images.
     * @param carrier carrier image
     * @param message message image
     */
    public EncryptionTool(BufferedImage carrier, BufferedImage message)
    {
        this.carrier = carrier;
        this.message = message;
        carrierPixels = new Pixel[carrier.getWidth()][carrier.getHeight()];
        messagePixels = new Pixel[message.getWidth()][message.getHeight()];
        messageValues = new int[message.getWidth() * message.getHeight() * 9];

        createPixelArrays();
    }

    /**
     * Goes through every pixel in the carrier image and encrypts them with RGB values from the message.
     * @return new image, encrypted with payload
     */
    public BufferedImage encryptMessage()
    {
        int count = 0;

        for (int x = 0; x < carrier.getWidth(); x++)
        {
            for (int y = 0; y < carrier.getHeight(); y++)
            {
                carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], messageValues[count++]);
                encryptDimensionalData(x, y);

                int color = (Utilities.ALPHA << 24) |
                            (carrierPixels[x][y].getColor().getRed() << 16) |
                            (carrierPixels[x][y].getColor().getGreen() << 8) |
                            (carrierPixels[x][y].getColor().getBlue());
                carrier.setRGB(x, y, color);
            }
        }

        return carrier;
    }

    /**
     * Encrypts the dimensions of the image message inside of the first four pixels of the carrier image. Since a
     * dimension can be drastically bigger than an 8-bit integer (max 255), this data must use 16-bit integers (max
     * 65536). Because the method encryptPixel() can only encrypt an 8-bit integer into a pixel, these new 16-bit values
     * must be split into two pieces and then be placed into two different pixels.
     * @param x the x value to place the new pixel at
     * @param y the y value to place the new pixel at
     */
    private void encryptDimensionalData(int x, int y)
    {
        String widthBinary = Utilities.integerToBinaryString(message.getWidth(), 16);
        String heightBinary = Utilities.integerToBinaryString(message.getHeight(), 16);

        if (x == 0)
        {
            if (y == 0)
                carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], Integer.parseInt(widthBinary.substring(0, 8), 2));
            else if (y == 1)
                carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], Integer.parseInt(widthBinary.substring(8, 16), 2));
            else if (y == 2)
                carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], Integer.parseInt(heightBinary.substring(0,8), 2));
            else if (y == 3)
                carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], Integer.parseInt(heightBinary.substring(8, 16), 2));
        }
    }

    /**
     * Hides a single 8-bit RGB code inside of a designated pixel.
     * @param carrierPixel pixel to encrypt data with
     * @param messageBits message to encrypt
     * @return encrypted pixel
     */
    private Pixel encryptPixel(Pixel carrierPixel, int messageBits)
    {
        String messageBinary = Utilities.integerToBinaryString(messageBits, 8);

        String red = Utilities.integerToBinaryString(carrierPixel.getColor().getRed(), 8);
        String green = Utilities.integerToBinaryString(carrierPixel.getColor().getGreen(), 8);
        String blue = Utilities.integerToBinaryString(carrierPixel.getColor().getBlue(), 8);

        int r = hideBinaryValue(red.substring(0, 5), messageBinary.substring(0, 3));
        int g = hideBinaryValue(green.substring(0, 6), messageBinary.substring(3, 5));
        int b = hideBinaryValue(blue.substring(0, 5), messageBinary.substring(5, 8));

        return new Pixel(carrierPixel.getX(), carrierPixel.getY(), r, g, b);
    }

    /**
     * Uses chopped bits to hide values, turns it from binary into an integer.
     * @param carrierValue carrier value to encrypt
     * @param messageBinarySection data to encrypt inside of carrierValue
     * @return encrypted value
     */
    private int hideBinaryValue(String carrierValue, String messageBinarySection)
    {
        int value = Utilities.binaryToInteger(carrierValue + messageBinarySection);
        return value;
    }


    /**
     * Creates arrays for the carrier image pixel count, the message image count, and all the RGB values within the
     * message pixels.
     */
    private void createPixelArrays()
    {
        int count = 0;

        // Define all pixels for the carrier image.
        for (int x = 0; x < carrier.getWidth(); x++)
            for (int y = 0; y < carrier.getHeight(); y++)
                carrierPixels[x][y] = new Pixel(x, y, carrier.getRGB(x, y));

        // Define all pixels for the message image.
        for (int x = 0; x < message.getWidth(); x++)
        {
            for (int y = 0; y < message.getHeight(); y++)
            {
                messagePixels[x][y] = new Pixel(x, y, message.getRGB(x, y));
                messageValues[count++] = messagePixels[x][y].getColor().getRed();
                messageValues[count++] = messagePixels[x][y].getColor().getGreen();
                messageValues[count++] = messagePixels[x][y].getColor().getBlue();
            }
        }
    }
}
