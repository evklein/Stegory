package com.hasherr.stegory.crypto;

import com.hasherr.stegory.core.Utilities;
import com.hasherr.stegory.image.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Evan on 1/4/2015.
 */
public class EncryptionTool
{
    private BufferedImage carrier;
    private BufferedImage message;
    private Pixel[][] carrierPixels;
    private Pixel[][] messagePixels;
    private int[] messageValues;

    private StringBuilder redValueBuilder;
    private StringBuilder blueValueBuilder;
    private StringBuilder greenValueBuilder;

    public EncryptionTool(BufferedImage carrier, BufferedImage message) throws IOException
    {
        this.carrier = carrier;
        this.message = message;
        carrierPixels = new Pixel[carrier.getWidth()][carrier.getHeight()];
        messagePixels = new Pixel[message.getWidth()][message.getHeight()];
        messageValues = new int[message.getWidth() * message.getHeight() * 9];

        redValueBuilder = new StringBuilder();
        blueValueBuilder = new StringBuilder();
        greenValueBuilder = new StringBuilder();

        createPixelArrays();
    }

    public void encodeMessage(String imagePath) throws IOException
    {
        int width = carrier.getWidth();
        int height = carrier.getHeight();
        int alpha = 255;
        int count = 0;

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                carrierPixels[x][y] = encodePixel(carrierPixels[x][y], messageValues[count++]);
                int color = (alpha << 24) | (carrierPixels[x][y].getColor().getRed() << 16) | (carrierPixels[x][y].getColor().getGreen() << 8)
                        | carrierPixels[x][y].getColor().getBlue();
                carrier.setRGB(x, y, color);
            }
        }

        ImageIO.write(carrier, "PNG", new File(imagePath));
    }

    private Pixel encodePixel(Pixel carrier, int message)
    {
        String messageBinary = Utilities.stringToBinary(message);

        clearBuilderValues();
        String red = Utilities.stringToBinary(carrier.getColor().getRed());
        String green = Utilities.stringToBinary(carrier.getColor().getGreen());
        String blue = Utilities.stringToBinary(carrier.getColor().getBlue());

        // Hide values HERE.
        redValueBuilder.append(red.substring(0, 5) + messageBinary.substring(0, 3));
        greenValueBuilder.append(green.substring(0, 6) + messageBinary.substring(3, 5));
        blueValueBuilder.append(blue.substring(0, 5) + messageBinary.substring(5, 8));

        int rVal = Integer.parseInt(Utilities.binaryToString(redValueBuilder.toString()));
        int gVal = Integer.parseInt(Utilities.binaryToString(greenValueBuilder.toString()));
        int bVal = Integer.parseInt(Utilities.binaryToString(blueValueBuilder.toString()));

        return new Pixel(carrier.getX(), carrier.getY(), rVal, gVal, bVal);
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

    private void clearBuilderValues()
    {
        int length = 8;
        redValueBuilder.delete(0, length);
        greenValueBuilder.delete(0, length);
        blueValueBuilder.delete(0, length);
    }
}
