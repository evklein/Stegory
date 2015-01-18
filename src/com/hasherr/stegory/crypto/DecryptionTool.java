package com.hasherr.stegory.crypto;

import com.hasherr.stegory.image.Pixel;
import com.hasherr.stegory.util.Utilities;

import java.awt.image.BufferedImage;

/**
 * Created by Evan on 1/4/2015.
 */
public class DecryptionTool
{
    private StringBuilder builder;
    private Pixel[][] carrierPixels;
    private int[] carrierValues;
    private int decodeCount;

    /**
     * Instantiates all global variables for the class, using data derived from the carrier image.
     * @param carrier encrypted carrier image.
     */


    /**
     * Decrypts the payload image from the carrier values. This is done by finding the RGB values for every 3 carrier
     * values in carrierValues. The binary values are then converted to integers and then formatted into an RGB color
     * code. The resulting pixel is then added to the new message.
     * @return payload image.
     */
    public BufferedImage decryptMessage(BufferedImage carrier)
    {
        initializeDecryptionTools(carrier);
        BufferedImage message = createMessageImage();

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

    /**
     * Pulls the hidden binary values out of every 3 RGB values in carrierValues. Starts at 0 using the global decodeCount
     * variable.
     * @return the decrypted hidden binary value.
     */
    private String getHiddenBinaryValue()
    {
        builder.delete(0, 8);
        String r = Utilities.integerToBinaryString(carrierValues[decodeCount++], 8);
        String g = Utilities.integerToBinaryString(carrierValues[decodeCount++], 8);
        String b = Utilities.integerToBinaryString(carrierValues[decodeCount++], 8);

        // Decryption magic.
        String hiddenBinaryValue = builder.append(r.substring(5, 8) + g.substring(6, 8) + b.substring(5, 8)).toString();
        return hiddenBinaryValue;
    }

    /**
     * Pulls the hidden binary values out of every 3 RGB values in carrierValues. Starts at whatever variable is passed
     * to count.
     * @param count the starting value of decryption within carrierValues.
     * @return
     */
    private String getHiddenBinaryValue(int count)
    {
        builder.delete(0, 8);
        String r = Utilities.integerToBinaryString(carrierValues[count++], 8);
        String g = Utilities.integerToBinaryString(carrierValues[count++], 8);
        String b = Utilities.integerToBinaryString(carrierValues[count++], 8);

        String hiddenBinaryValue = builder.append(r.substring(5, 8) + g.substring(6, 8) + b.substring(5, 8)).toString(); // This is where the magic happens.
        return hiddenBinaryValue;
    }

    /**
     * Returns the width of the target payload. This value is derived from the first 2 pixels (first 6 RGB values)
     * of the image. By combining the hidden binary values derived from these 2 pixels, a 16-bit binary value is
     * pulled and can be transformed into a 16-bit integer.
     * @return the width of the payload.
     * @see com.hasherr.stegory.util.Utilities#binaryToInteger(String)
     */
    public int getEncryptedWidth()
    {
        String[] parts = new String[2];
        parts[0] = getHiddenBinaryValue(0);
        parts[1] = getHiddenBinaryValue(3);
        return Utilities.binaryToInteger(parts[0] + parts[1]);
    }

    /**
     * Returns the height of the target payload. This value is derived from the third and fourth pixels (second 6 RGB
     * values) of the image. By combining the hidden binary values derived from these 2 pixels, a 16-bit binary value
     * is pulled and can be transformed into a 16-bit integer.
     * @return the height of the payload.
     * @see com.hasherr.stegory.util.Utilities#binaryToInteger(String)
     */
    public int getEncryptedHeight()
    {
        String[] parts = new String[2];
        parts[0] = getHiddenBinaryValue(6);
        parts[1] = getHiddenBinaryValue(9);
        return Utilities.binaryToInteger(parts[0] + parts[1]);
    }

    /**
     * Initialized various variables and arrays needed for the decryption process.
     * @param carrier the carrier image to derive the encrypted message from.
     */
    private void initializeDecryptionTools(BufferedImage carrier)
    {
        builder = new StringBuilder();

        carrierPixels = new Pixel[carrier.getWidth()][carrier.getHeight()];
        carrierValues = new int[carrier.getWidth() * carrier.getHeight() * 9]; // Carries all RGB values of the carrier.
        assignCarrierPixelValues(carrier);

        decodeCount = 0;
    }

    /**
     * Assigns all RGB values from the carrier image to an array for later use in the decryption process.
     */
    private void assignCarrierPixelValues(BufferedImage carrier)
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

    private BufferedImage createMessageImage()
    {
        return new BufferedImage(getEncryptedWidth(), getEncryptedHeight(), BufferedImage.TYPE_INT_RGB);
    }

}