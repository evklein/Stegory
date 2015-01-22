package com.hasherr.stegory.ui.forms;

import com.hasherr.stegory.crypto.DecryptionTool;
import com.hasherr.stegory.crypto.EncryptionTool;
import com.hasherr.stegory.ui.controllers.encryption.EncryptionButtonController;
import com.hasherr.stegory.ui.controllers.encryption.SelectImageUIController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
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

    private BufferedImage carrier, message;
    private JFileChooser fileChooser;
    private ButtonGroup encryptionFileFormatButtonGroup;


    public MainForm() throws IOException
    {
        fileChooser = new JFileChooser();
        encryptionFileFormatButtonGroup = new ButtonGroup();
        defineButtonGroupButtons();

        SelectImageUIController carrierSelectionController = new SelectImageUIController(carrierImagePathLabel,
                carrierImageWidthLabel, carrierImageHeightLabel);
        SelectImageUIController messageSelectionController = new SelectImageUIController(messageImagePathLabel,
                messageImageWidthLabel, messageImageHeightLabel);
        EncryptionButtonController encryptionButtonController = new EncryptionButtonController(carrierSelectionController,
                messageSelectionController);

        // Encryption buttons & controllers.
        selectCarrierImageButton.addActionListener(carrierSelectionController);
        selectMessageImageButton.addActionListener(messageSelectionController);
        encryptButton.addActionListener(encryptionButtonController);

        // Decryption buttons & controllers.
        selectCarrierImage.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                int response = fileChooser.showOpenDialog(selectCarrierImage);

                if (response == JFileChooser.APPROVE_OPTION)
                {
                    decryptionCarrierImagePath.setText("Image path: " + fileChooser.getSelectedFile());
                    try
                    {
                        carrier = ImageIO.read(new File(fileChooser.getSelectedFile().getAbsolutePath()));
                        decryptionCarrierImageWidth.setText("Image width: " + carrier.getWidth());
                        decryptionCarrierImageHeight.setText("Image height: " + carrier.getHeight());
//                        decryptionPayloadWidthLabel.setText("Payload width: " + decryptionTool.getEncryptedWidth());
//                        decryptionPayloadHeightLabel.setText("Payload height: " + decryptionTool.getEncryptedHeight());
                        
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        decryptButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                int response = fileChooser.showSaveDialog(decryptButton);

                if (response == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        message = decryptionTool.decryptMessage(carrier);
                        ImageIO.write(message, "JPG", new File(fileChooser.getSelectedFile().getAbsolutePath()));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void defineButtonGroupButtons()
    {
        encryptionFileFormatButtonGroup.add(PNG24RadioButton);
        encryptionFileFormatButtonGroup.add(JPEGRadioButton);
        encryptionFileFormatButtonGroup.add(BMPRadioButton);
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("Stegory v0.35");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(450, frame.getHeight());
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
