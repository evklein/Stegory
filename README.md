# README #

### What is Stegory? ###

* Stegory is a steganographic tool used to hide images or messages inside of other images.
* Currently still in the startup stage of development, since no real 'releases' have been released as of yet.

### Who do I talk to? ###

* Stegory is headed by me, Evan Klein.
* You can reach me by using my contact page at http://hasherr.com/contact-me/

### Future Plans ###

* Add support for multiple image types (BMP, TGA, RAW)
* Splash Screen with official logo and information

## Notes ##

<h4>JPEG</h4>
<p>After multiple tests and some online research, JPEG Steganography will not be included within Stegory. Due to the
compression of JPEG images, color values become very distorted. Encryption works well enough, with the program spitting
out an encrypted image that also happens to have quite a bit of loss due to the compression. While I have no doubt that
other languages may have the functionality to do this due to having a native 'Bitmap' class where the values can just be
copied over, I am not going to attempt to solve JPEG Steganography due to the fact that I would have to modify the
compression method itself in order to make decryption work. The payoff would not be worth it due to the distortion from
compression becoming greater and greater every time the image is encrypted/decrypted.</p>

<h4>Grayscale</h4>
<p>It turns out that using grayscale messaging for LSB encryption or variants of it <b>does not work</b>. This is due to
the fact that grayscale relies on all the RGB values of any given pixel being the same.  
