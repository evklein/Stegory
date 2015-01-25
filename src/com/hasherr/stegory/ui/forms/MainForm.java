package com.hasherr.stegory.ui.forms;

import com.hasherr.stegory.ui.controllers.DecryptionButtonController;
import com.hasherr.stegory.ui.controllers.EncryptionButtonController;
import com.hasherr.stegory.ui.controllers.SelectImageUIController;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Evan on 1/4/2015.
 */
public class MainForm
{
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JPanel encryptTab;
    private JPanel decryptTab;
    private JButton selectCarrierImageButton;
    private JButton selectMessageImageButton;
    private JButton encryptButton;
    private JLabel carrierImagePathLabel;
    private JLabel messageImagePathLabel;
    private JButton selectCarrierImage;
    private JButton decryptButton;
    private JLabel carrierImageWidthLabel;
    private JLabel carrierImageHeightLabel;
    private JLabel messageImageWidthLabel;
    private JLabel messageImageHeightLabel;
    private JLabel encryptionStatusLabel;
    private JLabel decryptionCarrierImagePath;
    private JLabel decryptionCarrierImageWidth;
    private JLabel decryptionCarrierImageHeight;
    private JLabel decryptionPayloadWidthLabel;
    private JLabel decryptionPayloadHeightLabel;
    private JLabel decryptionStatusLabel;
    private JPanel encryptionExportFileFormatPanel;
    private JRadioButton PNG24RadioButton;
    private JRadioButton JPEGRadioButton;
    private JRadioButton BMPRadioButton;
    private JProgressBar progressBar1;
    private JProgressBar progressBar2;
    private ButtonGroup encryptionFileFormatButtonGroup;

    /**
     * Initializes a new MainForm and initializes other parts of the class, such as the buttons and their controller
     * groups, as well as various button groupings.
     */
    public MainForm()
    {
        defineButtonGroups();
        defineControllerStructure();
    }

    /**
     * Define button groups for various radio buttons.
     */
    private void defineButtonGroups()
    {
        encryptionFileFormatButtonGroup = new ButtonGroup();
        encryptionFileFormatButtonGroup.add(PNG24RadioButton);
        encryptionFileFormatButtonGroup.add(JPEGRadioButton);
        encryptionFileFormatButtonGroup.add(BMPRadioButton);
    }

    /**
     * Assigns each necessary component their specialized controller to control their behavior.
     */
    private void defineControllerStructure()
    {
        SelectImageUIController carrierSelectionController = new SelectImageUIController(carrierImagePathLabel,
                carrierImageWidthLabel, carrierImageHeightLabel);
        SelectImageUIController messageSelectionController = new SelectImageUIController(messageImagePathLabel,
                messageImageWidthLabel, messageImageHeightLabel);
        EncryptionButtonController encryptionButtonController = new EncryptionButtonController(carrierSelectionController,
                messageSelectionController);
        SelectImageUIController decryptionCarrierSelectionController = new SelectImageUIController(decryptionCarrierImagePath,
                decryptionCarrierImageWidth, decryptionCarrierImageHeight);
        DecryptionButtonController decryptionButtonController = new DecryptionButtonController(decryptionCarrierSelectionController);

        // Encryption buttons & controllers.
        selectCarrierImageButton.addActionListener(carrierSelectionController);
        selectMessageImageButton.addActionListener(messageSelectionController);
        encryptButton.addActionListener(encryptionButtonController);
        selectCarrierImage.addActionListener(decryptionCarrierSelectionController);
        decryptButton.addActionListener(decryptionButtonController);
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("Stegory v0.37");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(450, frame.getHeight());
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
