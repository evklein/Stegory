package com.hasherr.stegory.ui;

import com.hasherr.stegory.crypto.DecryptionTool;
import com.hasherr.stegory.crypto.EncryptionTool;

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
    private EncryptionTool encryptionTool;
    private DecryptionTool decryptionTool;

    public MainForm() throws IOException
    {
        fileChooser = new JFileChooser();
        encryptionTool = new EncryptionTool();
        decryptionTool = new DecryptionTool();
        encryptionFileFormatButtonGroup = new ButtonGroup();
        defineButtonGroupButtons();

        ////////////////
        ///ENCRYPTION///
        ////////////////
        selectCarrierImageButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                int response = fileChooser.showOpenDialog(selectCarrierImageButton);

                if (response == JFileChooser.APPROVE_OPTION)
                {
                    File carrierFile = fileChooser.getSelectedFile();
                    carrierImagePathLabel.setText("Image path: " + carrierFile.getAbsolutePath());

                    try
                    {
                        carrier = ImageIO.read(carrierFile);
                        carrierImageWidthLabel.setText("Width: " + Integer.toString(carrier.getWidth()));
                        carrierImageHeightLabel.setText("Height: " + Integer.toString(carrier.getHeight()));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        selectMessageImageButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                int response = fileChooser.showOpenDialog(selectMessageImageButton);

                if (response == JFileChooser.APPROVE_OPTION)
                {
                    File messageFile = fileChooser.getSelectedFile();
                    messageImagePathLabel.setText("Image path: " + messageFile.getAbsolutePath());

                    try
                    {
                        message = ImageIO.read(messageFile);
                        messageImageWidthLabel.setText("Width: " + message.getWidth());
                        messageImageHeightLabel.setText("Height: " + message.getHeight());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        encryptButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                int response = fileChooser.showSaveDialog(encryptButton);
                encryptionStatusLabel.setText("Encryption Status: [PENDING]");
                if (response == JFileChooser.APPROVE_OPTION)
                {
                    String filePath = fileChooser.getSelectedFile().toString();
                    try
                    {
                        BufferedImage encryptedImage = new EncryptionTool().encryptMessage(carrier, message);
                        if (filePath.substring(filePath.length() - 4, filePath.length()).toLowerCase().equals(".png"))
                            ImageIO.write(encryptedImage, "png", fileChooser.getSelectedFile());
                        else
                            ImageIO.write(encryptedImage, "png", new File(fileChooser.getSelectedFile() + ".png"));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        encryptionStatusLabel.setText("Encryption Status: [FAILED]");
                    }

                    encryptionStatusLabel.setText("Encryption Status: [COMPLETED]");
                }
            }
        });

        ////////////////
        ///DECRYPTION///
        ////////////////
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
                        decryptionPayloadWidthLabel.setText("Payload width: " + decryptionTool.getEncryptedWidth());
                        decryptionPayloadHeightLabel.setText("Payload height: " + decryptionTool.getEncryptedHeight());
                        
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
                        ImageIO.write(message, "png", new File(fileChooser.getSelectedFile().getAbsolutePath()));
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
        JFrame frame = new JFrame("Stegory v0.2");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(450, frame.getHeight());
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
