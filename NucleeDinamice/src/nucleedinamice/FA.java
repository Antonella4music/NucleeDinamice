package nucleedinamice;

public class FA
{
    public double[] features;
    public String className;
    
    public FA()
    {
        this.features = null;
        this.className = "";
    }
    
    public FA(double[] feature, String clasa) //intorc o lista de liste (ca sa am departajat pe clasa din care face parte obiectul)
    {
      features = new double[feature.length];
      setFullForma(feature, clasa); 
    }
   
    public void setFullForma(double[] feature, String clasa)
    {        
        features = new double[feature.length];
        className = "";
        className = clasa;
        for(int i = 0; i< feature.length;i++) 
           features[i] = feature[i];
    }
        
    public void setFormaFeature(double[] feature)
    {        
        features = new double[feature.length];
        for(int i = 0; i< feature.length;i++)
           features[i] = feature[i];
    }

    public String getForma() 
    {
        String text = "";
        text += className;
        
        for(int i = 0; i < features.length ;i++) 
            text += features[i] + " ";            
            
        return text;
    }
        
}
