import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

class FeatureNParameter{
    double[][] X, Theta;

    public FeatureNParameter(int numMovies, int numUsers){
        X = new double[numMovies][100];
        Theta = new double[numUsers][100];
        Random r = new Random();

        DecimalFormat df = new DecimalFormat("#.##");

        for(int i=0;i<numMovies;i++){
            for(int j=0;j<100;j++){
                X[i][j] = -1+2*(r.nextDouble());
                X[i][j] = Double.parseDouble(df.format(X[i][j]));
            }
        }

        for(int i=0;i<numUsers;i++){
            for(int j=0;j<100;j++){
                Theta[i][j] = -1+2*(r.nextDouble());
                Theta[i][j] =  Double.parseDouble(df.format(Theta[i][j]));
            }
        }
    }
} 