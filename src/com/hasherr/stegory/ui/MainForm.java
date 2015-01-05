package com.hasherr.stegory.ui;

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

    private BufferedImage carrier, message;

    public MainForm() throws IOException
    {
        selectCarrierImageButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(selectCarrierImageButton);

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    File carrierFile = fileChooser.getSelectedFile();
                    carrierImagePathLabel.setText(carrierFile.getAbsolutePath());

                    try
                    {
                        carrier = ImageIO.read(carrierFile);
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
                JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(selectMessageImageButton);

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    File messageFile = fileChooser.getSelectedFile();
                    messageImagePathLabel.setText(messageFile.getAbsolutePath());

                    try
                    {
                        message = ImageIO.read(messageFile);
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
                JFileChooser fileChooser = new JFileChooser();
                int response = fileChooser.showSaveDialog(encryptButton);

                if (response == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        BufferedImage encryptedImage = new EncryptionTool(carrier, message).encryptMessage();
                        ImageIO.write(encryptedImage, "png", fileChooser.getSelectedFile());
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
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(300, 250);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
