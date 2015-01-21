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
public class SelectCarrierImageButtonController extends ImageDataComponentController
{
    public SelectCarrierImageButtonController(JLabel imageFilePathLabel, JLabel imageWidthLabel, JLabel imageHeightLabel)
    {
        super(imageFilePathLabel, imageWidthLabel, imageHeightLabel);
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION)
        {
            File carrierFile = fileChooser.getSelectedFile();
            imageFilePathLabel.setText("Path: " + carrierFile.getAbsolutePath());

            try
            {
                BufferedImage carrier = ImageIO.read(carrierFile);
                imageWidthLabel.setText("Width: " + carrier.getWidth());
                imageHeightLabel.setText("Height: " + carrier.getHeight());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
