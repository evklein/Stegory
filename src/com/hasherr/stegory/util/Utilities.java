package com.hasherr.stegory.util;

/**
 * Created by Evan on 1/4/2015.
 */
public class Utilities
{
    public static final int ALPHA = 255;

    /**
     * Converts a value from a normal integer to a binary value of type String.
     * @param value value to turn from integer into binary string
     * @param numberOfBits the number of bits to return
     * @return a completed binary string
     */
    public static String integerToBinaryString(int value, int numberOfBits)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Integer.toBinaryString(value));

        // Adds 0's to the front of the binary if the string does not come out as expected.
        while (stringBuilder.length() != numberOfBits)
        {
            String old = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append("0" + old);
        }

        String finalizedBinaryValue = stringBuilder.toString();
        return finalizedBinaryValue;
    }

    public static int binaryToInteger(String binary) { return Integer.parseInt(binary, 2); }
}
