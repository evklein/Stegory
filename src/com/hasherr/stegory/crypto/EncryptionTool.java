package com.hasherr.stegory.crypto;

import com.hasherr.stegory.image.Pixel;
import com.hasherr.stegory.util.Utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Evan on 1/4/2015.
 */
public class EncryptionTool
{
    private BufferedImage carrier, message;
    private Pixel[][] carrierPixels, messagePixels;
    private int[] messageValues;

    public EncryptionTool(BufferedImage carrier, BufferedImage message) throws IOException
    {
        this.carrier = carrier;
        this.message = message;
        carrierPixels = new Pixel[carrier.getWidth()][carrier.getHeight()];
        messagePixels = new Pixel[message.getWidth()][message.getHeight()];
        messageValues = new int[message.getWidth() * message.getHeight() * 9];

        createPixelArrays();
    }

    public BufferedImage encryptMessage() throws IOException
    {
        int width = carrier.getWidth();
        int height = carrier.getHeight();
        int count = 0;
        String[] messageDimensions = getMessageDimensions();

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], messageValues[count++]);
                if (x == 0 && y <= 3)
                    carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], Integer.parseInt(Utilities.binaryToString(messageDimensions[y])));

                int color = (Utilities.ALPHA << 24) | (carrierPixels[x][y].getColor().getRed() << 16) |
                            (carrierPixels[x][y].getColor().getGreen() << 8) |
                             carrierPixels[x][y].getColor().getBlue();
                carrier.setRGB(x, y, color);
            }
        }

        return carrier;
    }

    private Pixel encryptPixel(Pixel carrier, int message)
    {
        String messageBinary = Utilities.integerToBinary(message, 8);

        String red = Utilities.integerToBinary(carrier.getColor().getRed(), 8);
        String green = Utilities.integerToBinary(carrier.getColor().getGreen(), 8);
        String blue = Utilities.integerToBinary(carrier.getColor().getBlue(), 8);

        int r = hideBits(red.substring(0, 5), messageBinary.substring(0, 3));
        int g = hideBits(green.substring(0, 6), messageBinary.substring(3, 5));
        int b = hideBits(blue.substring(0, 5), messageBinary.substring(5, 8));

        return new Pixel(carrier.getX(), carrier.getY(), r, g, b);
    }

    // Uses chopped bits to hide values.
    private int hideBits(String carrierValue, String messageBinarySection)
    {
        int value = Integer.parseInt(Utilities.binaryToString(carrierValue + messageBinarySection));
        return value;
    }

    // Encrypts message width and height into the first four pixels of the carrier in order to allow for easier, more automatic encryption.
    private String[] getMessageDimensions()
    {
        String width = Utilities.integerToBinary(message.getWidth(), 16);
        String height = Utilities.integerToBinary(message.getHeight(), 16);

        String widthOne = width.substring(0, 8);
        String widthTwo = width.substring(8, 16);
        String heightOne = height.substring(0, 8);
        String heightTwo = height.substring(8, 16);
        String[] splitValues = { widthOne, widthTwo, heightOne, heightTwo };
        return splitValues;
    }

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
