package com.hasherr.stegory.ui.controllers.parent;

import com.hasherr.stegory.crypto.DecryptionTool;
import com.hasherr.stegory.crypto.EncryptionTool;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by Evan on 1/19/2015.
 */
public abstract class ComponentController implements ActionListener
{
    protected EncryptionTool encryptionTool;
    protected DecryptionTool decryptionTool;
    protected JFileChooser fileChooser;

    public ComponentController()
    {
        encryptionTool = new EncryptionTool();
        decryptionTool = new DecryptionTool();
        fileChooser = new JFileChooser();
    }


}
