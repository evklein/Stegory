package com.hasherr.stegory.crypto;

import com.hasherr.stegory.image.Pixel;
import com.hasherr.stegory.util.Utilities;

import java.awt.image.BufferedImage;
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

    public DecryptionTool(BufferedImage carrier)
    {
        this.carrier = carrier;
        builder = new StringBuilder();

        carrierPixels = new Pixel[carrier.getWidth()][carrier.getHeight()];
        carrierValues = new int[carrier.getWidth() * carrier.getHeight() * 9]; // Carries all RGB values of the carrier.
        assignCarrierPixels();

        // Decoding tool-related variables.

        System.out.println(getHiddenDimensions(1));
        System.out.println(getHiddenDimensions(2));
        message = new BufferedImage(getHiddenDimensions(1), getHiddenDimensions(2), BufferedImage.TYPE_INT_RGB);
        System.out.println(message.getWidth());
        decodeCount = 0;
    }

    public BufferedImage decryptMessage() throws IOException
    {
        for (int x = 0; x < message.getWidth(); x++)
        {
            for (int y = 0; y < message.getHeight(); y++)
            {
                int hiddenRedValue = Integer.parseInt(Utilities.binaryToString(getHiddenBinaryValue()));
                int hiddenGreenValue = Integer.parseInt(Utilities.binaryToString(getHiddenBinaryValue()));
                int hiddenBlueValue = Integer.parseInt(Utilities.binaryToString(getHiddenBinaryValue()));

                int color = (Utilities.ALPHA << 24) | (hiddenRedValue << 16) | (hiddenGreenValue << 8) | hiddenBlueValue;
                message.setRGB(x, y, color);
            }
        }

        return message;
    }

    private String getHiddenBinaryValue()
    {
        builder.delete(0, 8);
        String r = Utilities.integerToBinary(carrierValues[decodeCount++], 8);
        String g = Utilities.integerToBinary(carrierValues[decodeCount++], 8);
        String b = Utilities.integerToBinary(carrierValues[decodeCount++], 8);

        String hiddenBinaryValue = builder.append(r.substring(5, 8) + g.substring(6, 8) + b.substring(5, 8)).toString(); // This is where the magic happens.
        return hiddenBinaryValue;
    }

    private int getHiddenDimensions(int option)
    {
        int specialCount = 0;
        String[] binaryValues = new String[2];
        String[] splitBinaryValues = new String[4];

        for (int i = 0; i < 4; i++)
        {
            builder.delete(0, 8);
            String r = Utilities.integerToBinary(carrierValues[specialCount++], 8);
            String g = Utilities.integerToBinary(carrierValues[specialCount++], 8);
            String b = Utilities.integerToBinary(carrierValues[specialCount++], 8);

            String hiddenBinaryValue = builder.append(r.substring(5, 8) + g.substring(6, 8) + b.substring(5, 8)).toString();
            splitBinaryValues[i] = hiddenBinaryValue;
        }
        binaryValues[0] = splitBinaryValues[0] + splitBinaryValues[1]; // WIDTH
        binaryValues[1] = splitBinaryValues[2] + splitBinaryValues[3]; // HEIGHT

        
        if (option == 1)
            return Integer.parseInt(Utilities.binaryToString(binaryValues[0]));
        return Integer.parseInt(Utilities.binaryToString(binaryValues[1]));
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