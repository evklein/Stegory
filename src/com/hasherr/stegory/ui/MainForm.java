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
    private JTextField widthField;
    private JTextField heightField;
    private JButton decryptButton;
    private JLabel carrierImageWidthLabel;
    private JLabel carrierImageHeightLabel;
    private JLabel messageImageWidthLabel;
    private JLabel messageImageHeightLabel;
    private JLabel encryptionStatusLabel;

    private BufferedImage carrier, message;
    private JFileChooser fileChooser;

    public MainForm() throws IOException
    {
        fileChooser = new JFileChooser();

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
                        BufferedImage encryptedImage = new EncryptionTool(carrier, message).encryptMessage();
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
                    try
                    {
                        carrier = ImageIO.read(new File(fileChooser.getSelectedFile().getAbsolutePath()));
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
                    int width = Integer.parseInt(widthField.getText());
                    int height = Integer.parseInt(heightField.getText());

                    try
                    {
                        message = new DecryptionTool(carrier, width, height).decodeMessage();
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

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("Stegory v0.2");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(400, 290);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
