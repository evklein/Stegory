package com.hasherr.stegory.core;

import com.hasherr.stegory.crypto.DecryptionTool;
import com.hasherr.stegory.crypto.EncryptionTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Evan on 11/23/2014.
 */
public class Starter
{
    public static void main(String[] args) throws IOException
    {
        BufferedImage carrier = ImageIO.read(new File("images/carrier.png"));
        BufferedImage message = ImageIO.read(new File("images/message.png"));
        new EncryptionTool(carrier, message).encryptMessage("images/new.png");
        new DecryptionTool(ImageIO.read(new File("images/new.png")), message.getWidth(), message.getHeight()).decodeMessage("images/decode.png");
    }
}
