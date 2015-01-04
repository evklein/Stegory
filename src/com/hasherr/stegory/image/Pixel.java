package com.hasherr.stegory.image;

import java.awt.*;

/**
 * Created by Evan on 1/4/2015.
 */
public class Pixel
{
    int x, y;
    Color color;

    public Pixel()
    {
        x = 0;
        y = 0;
        color = new Color(255, 255, 255);
    }

    public Pixel(int x, int y)
    {
        this.x = x;
        this.y = y;
        color = new Color(255, 255, 255);
    }

    public Pixel(int x, int y, int r, int g, int b)
    {
        this.x = x;
        this.y = y;
        color = new Color(r, g, b);
    }

    public Pixel(int x, int y, int rgbCode)
    {
        this.x = x;
        this.y = y;

        color = rgbTypeIntToColor(rgbCode);
    }

    public Pixel(int rgbCode)
    {
        this.x = 0;
        this.y = 0;

        color = rgbTypeIntToColor(rgbCode);
    }

    private Color rgbTypeIntToColor(int rgbCode)
    {
        return new Color((rgbCode >> 16) & 0xFF, (rgbCode >> 8) & 0xFF, (rgbCode) & 0xFF);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Color getColor()
    {
        return color;
    }

}