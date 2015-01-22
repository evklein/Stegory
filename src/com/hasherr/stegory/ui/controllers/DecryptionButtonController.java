package com.hasherr.stegory.ui.controllers;

import com.hasherr.stegory.crypto.DecryptionTool;
import com.hasherr.stegory.ui.controllers.parent.ComponentController;
import com.hasherr.stegory.ui.controllers.parent.ExtensionChecker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Evan on 1/22/2015.
 */
public class DecryptionButtonController extends ComponentController implements ExtensionChecker
{
    private DecryptionTool decryptionTool;
    private BufferedImage carrier, message;
    private SelectImageUIController decryptionSelectionController;

    public DecryptionButtonController(SelectImageUIController decryptionSelectionController)
    {
        decryptionTool = new DecryptionTool();
        this.decryptionSelectionController = decryptionSelectionController;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        int response = fileChooser.showSaveDialog(null);

        if (response == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                message = decryptionTool.decryptMessage(decryptionSelectionController.getImage());
                ImageIO.write(message, "JPG", new File(fileChooser.getSelectedFile().getAbsolutePath()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String checkExtension(String initialValue)
    {
        if (initialValue.substring(initialValue.length() - 4, initialValue.length()).toLowerCase().equals(".png"))
            return initialValue;
        return initialValue + ".png";
    }
}
