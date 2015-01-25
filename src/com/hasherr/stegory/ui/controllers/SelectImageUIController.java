package com.hasherr.stegory.ui.controllers;

import com.hasherr.stegory.ui.controllers.parent.ImageDataComponentController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Evan on 1/19/2015.
 */
public class SelectImageUIController extends ImageDataComponentController
{
    private BufferedImage image;

    public SelectImageUIController(JLabel imageFilePathLabel, JLabel imageWidthLabel, JLabel imageHeightLabel)
    {
        super(imageFilePathLabel, imageWidthLabel, imageHeightLabel);
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION)
        {
            File imageFile = fileChooser.getSelectedFile();
            imageFilePathLabel.setText("Path: " + imageFile.getAbsolutePath());

            try
            {
                image = ImageIO.read(fileChooser.getSelectedFile());
                imageWidthLabel.setText("Width: " + image.getWidth());
                imageHeightLabel.setText("Height: " + image.getHeight());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public BufferedImage getImage()
    {
        return image;
    }
}
