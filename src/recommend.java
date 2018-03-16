import java.io.File;
import java.util.*;
import javafx.util.Pair;

public class recommend{
    public static void main(String[] args) {
        data d = new data();
        int[][] movieUserData = d.generateData();
        
        // try{
        //     Formatter ratingData = new Formatter("D:\\ratings.txt");

        //     for(int i=0;i<d.numMovies;i++){
        //         for(int j=0;j<d.numUsers;j++){
        //             ratingData.format("%s, ", movieUserData[i][j]);
        //         }
        //         ratingData.format("\n");
        //     }
        //     ratingData.close();
        // }
        // catch(Exception ex){
        //     ex.printStackTrace();
        // }

        // System.out.println();
        // System.out.println();

        FeatureNParameter fp = new FeatureNParameter(d.numMovies, d.numUsers);
        // for(int i=0;i<d.numMovies;i++){
        //     for(int j=0;j<100;j++){
        //         System.out.print(fp.X[i][j] + " ");
        //     }
        //     System.out.println();
        // }

        // System.out.println();
        // System.out.println();
        
        // for(int i=0;i<d.numUsers;i++){
        //     for(int j=0;j<100;j++){
        //         System.out.print(fp.Theta[i][j] + " ");
        //     }
        //     System.out.println();
        // }

        recommendationSystem rs = new recommendationSystem();
        
        int NUM_ITERATIONS = 4;
        double LAMBDA = 0.03;
        double ALPHA = 0.3;
        rs.gradientDescent(fp.X, fp.Theta, movieUserData, NUM_ITERATIONS, LAMBDA, ALPHA, d.numMovies, d.numUsers);
        
        /**
         * Calculating predictions!
         */
        double[][] thetaTrans = rs.transpose(fp.Theta, d.numUsers, 100);
        double[][] predictions = new double[d.numMovies][d.numUsers];
        for(int i=0;i<d.numMovies;i++){
            for(int j=0;j<d.numUsers;j++){
                for(int k=0;k<100;k++){
                    predictions[i][j] += (fp.X[i][k]*thetaTrans[k][j]); 
                }
                //System.out.print(predictions[i][j]+" ");
            }  
            //System.out.println();  
        }    

        /**
         * Changing each prediction value to positive and less than or equal to 5
         */
        for(int i=0;i<d.numMovies;i++){
            for(int j=0;j<d.numUsers;j++){
                predictions[i][j] = Math.abs(predictions[i][j]);
                predictions[i][j] = predictions[i][j]>5?5:predictions[i][j];
            }   
        }

        /** 
         * Error calculation 
        */
        double error = 0;
        for(int i=0;i<d.numMovies;i++){
            for(int j=0;j<d.numUsers;j++){
                if(movieUserData[i][j] != 0)
                    error += Math.abs(predictions[i][j]-movieUserData[i][j])/5;
            }  
        }
        //Average Error
        error /= (d.numMovies*d.numUsers);
        // Percentage error
        error *= 100;
        System.out.println("Error percentage: "+error+" :(");
    }
}