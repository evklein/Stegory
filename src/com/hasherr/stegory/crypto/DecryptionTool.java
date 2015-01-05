package com.hasherr.stegory.crypto;

import com.hasherr.stegory.util.Utilities;
import com.hasherr.stegory.image.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Evan on 1/4/2015.
 */
public class DecryptionTool
{
    private BufferedImage carrier, message;
    private Pixel[][] carrierPixels;
    private int[] carrierValues;
    private int decodeCount;

    StringBuilder builder;

    public DecryptionTool(BufferedImage carrier, int messageWidth, int messageHeight)
    {
        this.carrier = carrier;
        message = new BufferedImage(messageWidth, messageHeight, BufferedImage.TYPE_INT_RGB);

        carrierPixels = new Pixel[carrier.getWidth()][carrier.getHeight()];
        carrierValues = new int[carrier.getWidth() * carrier.getHeight() * 9];
        assignCarrierPixels();

        // Decoding tool-related variables.
        builder = new StringBuilder();
        decodeCount = 0;
    }

    public void decodeMessage(String filePath) throws IOException
    {
        int alpha = 255;
        for (int x = 0; x < message.getWidth(); x++)
        {
            for (int y = 0; y < message.getHeight(); y++)
            {
                int hiddenRedValue = Integer.parseInt(Utilities.binaryToString(getHiddenValue()));
                int hiddenGreenValue = Integer.parseInt(Utilities.binaryToString(getHiddenValue()));
                int hiddenBlueValue = Integer.parseInt(Utilities.binaryToString(getHiddenValue()));

                int color = (alpha << 24) | (hiddenRedValue << 16) | (hiddenGreenValue << 8) | hiddenBlueValue;
                message.setRGB(x, y, color);
            }
        }

        ImageIO.write(message, "PNG", new File(filePath));
    }

    private String getHiddenValue()
    {
        builder.delete(0, 8);
        String r = Utilities.stringToBinary(carrierValues[decodeCount++]);
        String g = Utilities.stringToBinary(carrierValues[decodeCount++]);
        String b = Utilities.stringToBinary(carrierValues[decodeCount++]);

        String hiddenBinaryValue = builder.append(r.substring(5, 8) + g.substring(6, 8) + b.substring(5, 8)).toString();
        return hiddenBinaryValue;
    }

    private void assignCarrierPixels()
    {
        int pixelCount = 0;
        for (int x = 0; x < carrier.getWidth(); x++)
        {
            for (int y = 0; y < carrier.getHeight(); y++)
            {
                carrierPixels[x][y] = new Pixel(x, y, carrier.getRGB(x, y));
                carrierValues[pixelCount++] = carrierPixels[x][y].getColor().getRed();
                carrierValues[pixelCount++] = carrierPixels[x][y].getColor().getGreen();
                carrierValues[pixelCount++] = carrierPixels[x][y].getColor().getBlue();
            }
        }
    }
}