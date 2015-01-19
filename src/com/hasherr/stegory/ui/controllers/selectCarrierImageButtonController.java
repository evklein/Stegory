package com.hasherr.stegory.ui.controllers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Evan on 1/19/2015.
 */
public class selectCarrierImageButtonController extends ComponentController
{
    private String filePath;
    private int width, height;

    @Override
    public void actionPerformed(ActionEvent event)
    {
        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION)
        {
            File carrierFile = fileChooser.getSelectedFile();
            filePath = carrierFile.getAbsolutePath();

            try
            {
                BufferedImage carrier = ImageIO.read(carrierFile);
                width = carrier.getWidth();
                height = carrier.getHeight();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String getFilePath()
    {
        return filePath;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
