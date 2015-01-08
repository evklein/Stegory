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
    private StringBuilder builder;
    private BufferedImage carrier, message;
    private Pixel[][] carrierPixels;
    private int[] carrierValues;

    public DecryptionTool(BufferedImage carrier)
    {
        this.carrier = carrier;
        builder = new StringBuilder();

        carrierPixels = new Pixel[carrier.getWidth()][carrier.getHeight()];
        carrierValues = new int[carrier.getWidth() * carrier.getHeight() * 9]; // Carries all RGB values of the carrier.
        assignCarrierPixelValues();

        System.out.println("W: " + getEncryptedWidth());
        System.out.println("H: " + getEncryptedHeight());
        message = new BufferedImage(getEncryptedWidth(), getEncryptedHeight(), BufferedImage.TYPE_INT_RGB);
    }

    Integer decodeCount = 0;
    public BufferedImage decryptMessage() throws IOException
    {

        for (int x = 0; x < message.getWidth(); x++)
        {
            for (int y = 0; y < message.getHeight(); y++)
            {
                int hiddenRedValue = Utilities.binaryToInteger(getHiddenBinaryValue());
                int hiddenGreenValue = Utilities.binaryToInteger(getHiddenBinaryValue());
                int hiddenBlueValue = Utilities.binaryToInteger(getHiddenBinaryValue());

                int color = (Utilities.ALPHA << 24) | (hiddenRedValue << 16) | (hiddenGreenValue << 8) | hiddenBlueValue;
                message.setRGB(x, y, color);
            }
        }

        return message;
    }

    int dCount = 0;
    private String getHiddenBinaryValue()
    {
        builder.delete(0, 8);
        String r = Utilities.integerToBinaryString(carrierValues[dCount++], 8);
        String g = Utilities.integerToBinaryString(carrierValues[dCount++], 8);
        String b = Utilities.integerToBinaryString(carrierValues[dCount++], 8);

        String hiddenBinaryValue = builder.append(r.substring(5, 8) + g.substring(6, 8) + b.substring(5, 8)).toString(); // This is where the magic happens.
        return hiddenBinaryValue;
    }

    private String getHiddenBinaryValue(int count)
    {
        builder.delete(0, 8);
        System.out.println(count);
        String r = Utilities.integerToBinaryString(carrierValues[count++], 8);
        String g = Utilities.integerToBinaryString(carrierValues[count++], 8);
        String b = Utilities.integerToBinaryString(carrierValues[count++], 8);
        System.out.println(count);

        String hiddenBinaryValue = builder.append(r.substring(5, 8) + g.substring(6, 8) + b.substring(5, 8)).toString(); // This is where the magic happens.
        return hiddenBinaryValue;
    }

//    private int getHiddenDimensions(int option)
//    {
////        int specialCount = 0;
////        String[] binaryValues = new String[2];
////        String[] splitBinaryValues = new String[4];
////
////        for (int i = 0; i < 4; i++)
////        {
////            builder.delete(0, 8);
////            String r = Utilities.integerToBinaryString(carrierValues[specialCount++], 8);
////            String g = Utilities.integerToBinaryString(carrierValues[specialCount++], 8);
////            String b = Utilities.integerToBinaryString(carrierValues[specialCount++], 8);
////
////            String hiddenBinaryValue = builder.append(r.substring(5, 8) + g.substring(6, 8) + b.substring(5, 8)).toString();
////            splitBinaryValues[i] = hiddenBinaryValue;
////        }
////        binaryValues[0] = splitBinaryValues[0] + splitBinaryValues[1]; // WIDTH
////        binaryValues[1] = splitBinaryValues[2] + splitBinaryValues[3]; // HEIGHT
////
////
////        if (option == 1)
////            return Utilities.binaryToInteger(binaryValues[0]);
////        return Utilities.binaryToInteger(binaryValues[1]);
//
//
//
//        int count = 0;
//        String[] binaryValues = new String[4];
//
//        for (int i = 0; i < 4; i++)
//            binaryValues[i] = getHiddenBinaryValue(count);
//
//        int width = Utilities.binaryToInteger(binaryValues[0] + binaryValues[1]);
//        int height = Utilities.binaryToInteger(binaryValues[2] + binaryValues[2]);
//
//
//        return 0;
//    }

    private int getEncryptedWidth()
    {
        int count = 0;
        String[] parts = new String[2];

        parts[0] = getHiddenBinaryValue(0);
        parts[1] = getHiddenBinaryValue(3);

        System.out.println(parts[1] + parts[0]);
        return Utilities.binaryToInteger(parts[0] + parts[1]);
    }

    private int getEncryptedHeight()
    {
        int count = 6;
        String[] parts = new String[2];

        parts[0] = getHiddenBinaryValue(6);
        parts[1] = getHiddenBinaryValue(9);

        return Utilities.binaryToInteger(parts[0] + parts[1]);
    }

    private void assignCarrierPixelValues()
    {
        int count = 0;
        for (int x = 0; x < carrier.getWidth(); x++)
        {
            for (int y = 0; y < carrier.getHeight(); y++)
            {
                carrierPixels[x][y] = new Pixel(x, y, carrier.getRGB(x, y));
                carrierValues[count++] = carrierPixels[x][y].getColor().getRed();
                carrierValues[count++] = carrierPixels[x][y].getColor().getGreen();
                carrierValues[count++] = carrierPixels[x][y].getColor().getBlue();
            }
        }
    }
}