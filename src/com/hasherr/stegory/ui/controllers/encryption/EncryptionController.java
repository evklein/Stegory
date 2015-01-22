package com.hasherr.stegory.ui.controllers.encryption;

import com.hasherr.stegory.crypto.EncryptionTool;
import com.hasherr.stegory.ui.controllers.ComponentController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Evan on 1/21/2015.
 */
public class EncryptionController extends ComponentController implements ExtensionChecker
{
    private EncryptionTool encryptionTool;
    private SelectImageUIController carrierController, messageController;

    public EncryptionController(EncryptionTool encryptionTool, SelectImageUIController carrierController,
                SelectImageUIController messageController)
    {
        this.encryptionTool = encryptionTool;
        this.carrierController = carrierController;
        this.messageController = messageController;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        int response = fileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION)
        {
            String filePath = fileChooser.getSelectedFile().toString();
            try
            {
                BufferedImage encryptedImage = encryptionTool.encryptMessage(carrierController.getImage(),
                        messageController.getImage());
                String finalizedFinalPath = checkExtension(filePath);
                ImageIO.write(encryptedImage, "PNG", new File(finalizedFinalPath));
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
