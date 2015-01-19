package com.hasherr.stegory.ui.controllers;

import com.hasherr.stegory.ui.controllers.tools.ImageDetails;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Evan on 1/19/2015.
 */
public class SelectMessageImageButtonController extends ComponentController implements ImageDetails
{
    private String filePath;
    private int width, height;

    @Override
    public void actionPerformed(ActionEvent event)
    {
        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION)
        {
            File messageFile = fileChooser.getSelectedFile();
            filePath = messageFile.getAbsolutePath();

            try
            {
                BufferedImage message = ImageIO.read(messageFile);
                width = message.getWidth();
                height = message.getHeight();
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
