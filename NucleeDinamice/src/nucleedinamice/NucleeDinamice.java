package nucleedinamice;
import java.io.IOException;

public class NucleeDinamice
{      
    public static void main(String[] args) 
    {
        Forme fOb = new Forme();
        
        //get pixels from image
       // try{
       // fOb.getPixelsFromImage();
        
        //nuclee dinamice
        fOb.citireFisier("output.txt");
        System.out.println(fOb.noileNuclee(fOb.formeLista, fOb.nrFeaturess, 1));
        
       // }catch(IOException e){System.out.println(e.getMessage());}
    }
}
