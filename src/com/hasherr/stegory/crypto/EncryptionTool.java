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
        int count = 0;
        String widthBinary = Utilities.integerToBinaryString(message.getWidth(), 16);
        String heightBinary = Utilities.integerToBinaryString(message.getHeight(), 16);

        System.out.println("W1: " + widthBinary.substring(0, 8));
        System.out.println("W2: " + widthBinary.substring(8, 16));

        for (int x = 0; x < carrier.getWidth(); x++)
        {
            for (int y = 0; y < carrier.getHeight(); y++)
            {
                carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], messageValues[count++]);
                if (x == 0 && y <= 3)
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

                int color = (Utilities.ALPHA << 24) | (carrierPixels[x][y].getColor().getRed() << 16) |
                            (carrierPixels[x][y].getColor().getGreen() << 8) |
                             carrierPixels[x][y].getColor().getBlue();
                carrier.setRGB(x, y, color);
            }
        }

        return carrier;
    }

    private Pixel encryptPixel(Pixel carrier, int messageBits)
    {
        String messageBinary = Utilities.integerToBinaryString(messageBits, 8);

        String red = Utilities.integerToBinaryString(carrier.getColor().getRed(), 8);
        String green = Utilities.integerToBinaryString(carrier.getColor().getGreen(), 8);
        String blue = Utilities.integerToBinaryString(carrier.getColor().getBlue(), 8);

        int r = hideBinaryValue(red.substring(0, 5), messageBinary.substring(0, 3));
        int g = hideBinaryValue(green.substring(0, 6), messageBinary.substring(3, 5));
        int b = hideBinaryValue(blue.substring(0, 5), messageBinary.substring(5, 8));

        return new Pixel(carrier.getX(), carrier.getY(), r, g, b);
    }

    // Uses chopped bits to hide values.
    private int hideBinaryValue(String carrierValue, String messageBinarySection)
    {
        int value = Utilities.binaryToInteger(carrierValue + messageBinarySection);
        return value;
    }

    // Encrypts message width and height into the first four pixels of the carrier in order to allow for easier, more automatic encryption.
//    private int getMessageDimension(DimensionParts part)
//    {
//        String width = Utilities.integerToBinaryString(message.getWidth(), 16);
//        String height = Utilities.integerToBinaryString(message.getHeight(), 16);
//        String binaryValue = "";
//
//        switch (part)
//        {
//            case WIDTH_ONE:
//                binaryValue = width.substring(0, 8);
//                break;
//            case WIDTH_TWO:
//                binaryValue = width.substring(8, 16);
//                break;
//            case HEIGHT_ONE:
//                binaryValue = height.substring(0, 8);
//                break;
//            case HEIGHT_TWO:
//                binaryValue = height.substring(8, 16);
//                break;
//        }
//
//        return Utilities.binaryToInteger(binaryValue);
//    }
//
//    private void encryptSpecialPixels(int x, int y)
//    {
//        switch (y)
//        {
//            case 0:
//                carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], getMessageDimension(DimensionParts.WIDTH_ONE));
//                break;
//            case 1:
//                carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], getMessageDimension(DimensionParts.WIDTH_TWO));
//                break;
//            case 2:
//                carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], getMessageDimension(DimensionParts.HEIGHT_ONE));
//                break;
//            case 3:
//                carrierPixels[x][y] = encryptPixel(carrierPixels[x][y], getMessageDimension(DimensionParts.HEIGHT_TWO));
//                break;
//        }
//    }

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

    private enum DimensionParts
    {
        WIDTH_ONE, WIDTH_TWO,
        HEIGHT_ONE, HEIGHT_TWO
    }
}
