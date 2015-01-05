package com.hasherr.stegory.util;

/**
 * Created by Evan on 1/4/2015.
 */
public class Utilities
{
    // Converts a string to a binary string.
    public static String stringToBinary(int value)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Integer.toBinaryString(value));

        // Add
        while (stringBuilder.length() != 8)
        {
            String old = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append("0" + old);
        }

        return stringBuilder.toString();
    }

    // Converts a binary string to a string.
    public static String binaryToString(String binary)
    {
        return Integer.toString(Integer.parseInt(binary, 2));
    }

}
