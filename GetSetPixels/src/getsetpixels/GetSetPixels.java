package getsetpixels;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import javax.imageio.ImageIO;

public class GetSetPixels{
  public static void main(String args[])throws IOException{
    
      BufferedImage bi=ImageIO.read(new File("regioni.jpg"));
int[] pixel;

for (int y = 0; y < bi.getHeight(); y++) {
    for (int x = 0; x < bi.getWidth(); x++) {
        pixel = bi.getRaster().getPixel(x, y, new int[3]);
        System.out.println(pixel[0] + " " + pixel[1] + " " + pixel[2]);
        
        PrintWriter writer = new PrintWriter("regioniOutput.txt");
        writer.println(pixel[0] + " " + pixel[1] + " " + pixel[2]);
        writer.close(); 
    }
}

  }
}