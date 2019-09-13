package nucleedinamice;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Forme 
{    
   // static Forme nd = new Forme();//nuclee dinamice
    static int nrFeaturess, nrForme;
    public  List<FA> formeLista = new ArrayList<FA>();
    int totPixeli = 0;
     
    public void getPixelsFromImage()throws IOException
    {
        BufferedImage bi=ImageIO.read(new File("reg.png"));
        int[] pixel;
        int height=bi.getHeight();
        int width =bi.getWidth();
        totPixeli =  height*width;
        System.out.println("total pixeli: "  + totPixeli);
         
        for (int y = 0; y < bi.getHeight(); y++) 
        {
            for (int x = 0; x < bi.getWidth(); x++) 
            {
                pixel = bi.getRaster().getPixel(x, y, new int[3]);
                System.out.println(pixel[0] + " " + pixel[1] + " " + pixel[2]);
               
                //scriere in fisier
                PrintWriter writer = new PrintWriter("output1.txt");
                writer.println(pixel[0] + " " + pixel[1] + " " + pixel[2]);
                writer.close(); 
            }
        }   

    }
    
    public static List<String> ListaClase (List<FA> forme)
    {
       List<String> claseLista = new ArrayList<String>();
       for(int i=0;i<forme.size(); i++) 
          if(!(claseLista.contains(forme.get(i).className)))
              claseLista.add(forme.get(i).className);
       return claseLista; //lista cu clase
    }
    
    public static List<FA> ImpartireClase(String clasa, int nrClase, List<FA> forma)
    {
        List<FA> formeClaseLista = new ArrayList<FA>();

        for (int i = 0; i < forma.size(); i++) {
            if (forma.get(i).className.equals(clasa))
            {
                formeClaseLista.add(forma.get(i) );
                nrClase++;
            }
        }
        return formeClaseLista; //lista cu forme functie de clasa
    }
    
    public static FA calculCG( List<FA> claseDiferentiate, String clasa) 
    {        
        double[] features = new double[nrFeaturess];
        for (int d = 0; d < nrFeaturess; d++) {
            features[d] = 0;
        }

        FA FAOb = new FA();
        FAOb.setFullForma(features, clasa);
        
        double suma = 0;
        for (int j = 0; j < nrFeaturess; j++) {
            suma = 0;
            for (int i = 0; i < claseDiferentiate.size(); i++) {
                FA cd = claseDiferentiate.get(i);
                suma = suma + cd.features[j];
            }
            FAOb.features[j] = suma / claseDiferentiate.size();
        }
        return FAOb;
    }

    public static double distantaEuclidiana(FA a, FA b)
    {
        double d = 0;
        for (int i = 0; i < a.features.length; i++) {
            d += (a.features[i] - b.features[i]) * (a.features[i] - b.features[i]);
        }
        return Math.sqrt(d);
    }
        
    public void citireFisier(String numeFisier) 
    {
        try {
            FileInputStream fisInput = new FileInputStream(numeFisier);
            BufferedReader br = new BufferedReader(new InputStreamReader(fisInput));
            String linie = br.readLine();

            String[] line = linie.split(" ");
            int[] dimLine = new int[line.length];
            for (int i = 0; i < line.length; i++) {
                dimLine[i] = Integer.parseInt(line[i]);
            }
           
            nrForme = totPixeli; //tot nr linii (nr formelor din fisier)
            nrFeaturess = 3;//total nr col(nr caracteristicilor din fisier)
                   
            double[] featuresArray = new double[nrFeaturess];

            while ((linie = br.readLine()) != null) {
                String[] caract = linie.split(" ");
                for (int i = 0; i < caract.length; i++) {
                    featuresArray[i] = Double.parseDouble(caract[i]);
                }

                FA forma = new FA();
                forma.setFormaFeature(featuresArray);
                formeLista.add(forma);
            }
            fisInput.close();
        } catch (IOException e) { System.err.println(e.getMessage()); }
    }
    
    public static String noileNuclee(List<FA> forme, int nrFeatures, int nrClase) 
    {
        String noileNuclee = "";
        nucleeDinamiceAlg(forme, nrFeatures, nrClase);
        for (int i = 0; i < forme.size(); i++) {
            noileNuclee += forme.get(i).getForma() + "\n";
        }
        return noileNuclee;

    }
            
    public static void nucleeDinamiceAlg(List<FA> forme, int nrFeatures, int nrClase)//clasificare formelor in cele m Clase
    {
        ArrayList<FA> nuclee = new ArrayList<FA>();
        boolean nucleeleCoincid = false;
        
        System.out.println("nr Clase: " + nrClase);
        // Initializare: alege nucleele celor m clase în mod aleator
        // din mulŃimea formelor: în acest caz, primele m forme au
        // fost alese drept nuclee 
        for (int i = 0; i < nrClase; i++) {
            nuclee.add(forme.get(i));
            System.out.println("nucleu -> " + forme.get(i).getForma() );
        }

        do {
            //*) clasifică formele în funcŃie de distanŃa
            //*) faŃă de nucleul cel mai apropiat
            for (int i = 0; i < forme.size(); i++) {
                double minDist = Integer.MAX_VALUE;
                int indexClasa = -1;
                for (int j = 0; j < nrClase; j++) {
                    double d = distantaEuclidiana(forme.get(i), nuclee.get(j));
                    if (minDist > d) {
                        minDist = d;
                        indexClasa = j;
                    }
                }
                forme.get(i).className = "clasa" + indexClasa + " -> ";
            }
            
            // calculeaza centrele de greutate ale
            // claselor nou formate 
            List<FA> cg = new ArrayList<FA>();
            List<String> clsList = new ArrayList<String>(nrClase);
            clsList = ListaClase(forme);

           for (String clasa : clsList) //foreach (String clasa in clsList)
           {
                FA ptrn = calculCG(ImpartireClase(clasa, nrClase, forme), clasa);
                cg.add(ptrn);
            }

            for (int i = 0; i < cg.size(); i++) 
            {
                System.out.println("CG " + cg.get(i).getForma());
            }

            // alege noile nuclee ca fiind cele mai apropiate
            // forme fata de centrele de greutate 
            ArrayList<FA> nucleeNoi = new ArrayList<FA>();
            for (int i = 0; i < nrClase; i++) {
                double minDist = Integer.MAX_VALUE;
                int indexForma = -1;
                for (int j = 0; j < forme.size(); j++) {
                    double d = distantaEuclidiana(cg.get(i), forme.get(j));
                    if (minDist > d) {
                        minDist = d;
                        indexForma = j;
                    }
                }
                nucleeNoi.add(forme.get(indexForma));
            }

            // verfică dacă există schimbări în nucleele
            // celor m clase 
            nucleeleCoincid = true;
            for (int i = 0; i < nrClase; i++)
            {
                for (int j = 0; j < nrFeatures; j++) 
                {
                    if (nuclee.get(i).features[j] != nucleeNoi.get(i).features[j]) 
                        nucleeleCoincid = false;
                }
            }
            nuclee = nucleeNoi;
        } while (!nucleeleCoincid);
    }
    
}
