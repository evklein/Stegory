package com.hasherr.stegory.ui.controllers;

import javax.swing.*;

/**
 * Created by Evan on 1/21/2015.
 */
public abstract class ImageDataComponentController extends ComponentController
{
    protected JLabel imageFilePathLabel, imageWidthLabel, imageHeightLabel;

    public ImageDataComponentController(JLabel imageFilePathLabel, JLabel imageWidthLabel, JLabel imageHeightLabel)
    {
        this.imageFilePathLabel = imageFilePathLabel;
        this.imageWidthLabel = imageWidthLabel;
        this.imageHeightLabel = imageHeightLabel;
    }
}
