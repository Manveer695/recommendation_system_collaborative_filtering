import java.util.*;
import java.lang.*;
import javafx.util.Pair;

class recommendationSystem{
    double costFn(double[][] X, double[][] Theta, int[][] Y, double lambda, int numMovies, int numUsers){
        double cost;

        double xSquareSum = 0, thetaSquareSum = 0, errorTermSquare = 0;

        double[][] predictions = new double[numMovies][numUsers];

        double[][] thetaTrans = transpose(Theta, numUsers, 100);
        for(int i=0;i<numMovies;i++){
            for(int j=0;j<numUsers;j++){
                for(int k=0;k<100;k++){
                    predictions[i][j] += (X[i][k]*thetaTrans[k][j]); 
                }
                if(Y[i][j] != 0)
                {
                    errorTermSquare += ((predictions[i][j] - Y[i][j])*(predictions[i][j] - Y[i][j]));
                }
            }    
        }

        for(int i=0;i<numMovies;i++){
            for(int j=0;j<100;j++){
                xSquareSum += (X[i][j]*X[i][j]);
            }
        }

        for(int i=0;i<numUsers;i++){
            for(int j=0;j<100;j++){
                thetaSquareSum += (Theta[i][j]*Theta[i][j]);
            }
        }

        cost = (errorTermSquare+lambda*(xSquareSum+thetaSquareSum))/2;
        return cost;
    }

    double[][] transpose(double[][] matrix, int rows, int cols){
        double[][] transMat = new double[cols][rows]; 
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                transMat[j][i] = matrix[i][j];
            }
        }
        return transMat;
    }

    // First is x_grad and second is theta_grad    
    Pair<double[][], double[][]> gradients(double[][] X, double[][] Theta, int[][] Y, double lambda, double alpha, int numMovies, int numUsers){
        
        double[][] x_grad = new double[numMovies][100], theta_grad = new double[numUsers][100];
        
        // x_grad
        int[] userRated = new int[numUsers];
        int cnt;

        for(int i=0;i<numMovies;i++){
            cnt = 0;
            for(int j=0;j<numUsers;j++)
            {
                if(Y[i][j] != 0)
                    userRated[cnt++] = j;
            }

            int[] tempY = new int[cnt];
            for(int j=0;j<cnt;j++)
            {
                tempY[j] = Y[i][userRated[j]];
            }

            double[][] tempTheta = new double[cnt][100];
            for(int z=0;z<cnt;z++){
                for(int j=0;j<100;j++){
                    tempTheta[z][j] = Theta[userRated[z]][j];                    
                }
            }

            double[] x_grad_row_1 = new double[cnt];
            double[][] tempThetaTrans = transpose(tempTheta, cnt, 100);
            for(int j=0;j<cnt;j++){
                for(int z=0;z<100;z++){
                    x_grad_row_1[j] += (X[i][z]*tempThetaTrans[z][j]); 
                }
                x_grad_row_1[j] -= ((double)tempY[j]);
            }

            double[] x_grad_row_2 = new double[100];
            for(int j=0;j<100;j++){
                for(int z=0;z<cnt;z++){
                    x_grad_row_2[j] += (x_grad_row_1[z]*tempTheta[z][j]); 
                }
                x_grad_row_2[j] += (lambda*X[i][j]);
                x_grad_row_2[j] = x_grad_row_2[j]*alpha;
            }

            x_grad[i] = x_grad_row_2.clone();            
        }
        userRated = null;

        // theta_grad
        int[] moviesRated = new int[numMovies];
        
        for(int i=0;i<numUsers;i++){
            cnt = 0;
            for(int j=0;j<numMovies;j++)
            {
                if(Y[j][i] != 0)
                    moviesRated[cnt++] = j;
            }

            // vertical
            int[] tempY = new int[cnt];
            
            for(int j=0;j<cnt;j++)
            {
                tempY[j] = Y[moviesRated[j]][i];
            }

            double[][] tempX = new double[cnt][100];
            for(int z=0;z<cnt;z++){
                for(int j=0;j<100;j++){
                    tempX[z][j] = X[moviesRated[z]][j];                    
                }
            }

            // vertical
            double[] theta_grad_row_1 = new double[cnt];
        
            double[][] thetaTrans = transpose(Theta, numUsers, 100);
            for(int j=0;j<cnt;j++){
                for(int z=0;z<100;z++){
                    theta_grad_row_1[j] += (tempX[j][z]*thetaTrans[z][i]); 
                }
                theta_grad_row_1[j] -= ((double)tempY[j]);
            }

            double[] theta_grad_row_2 = new double[100];
            // making theta_grad_row_1 horizontal in mind and hence skipping transpose :p

            for(int j=0;j<100;j++){
                for(int z=0;z<cnt;z++){
                    theta_grad_row_2[j] += (theta_grad_row_1[z]*tempX[z][j]); 
                }
                theta_grad_row_2[j] += (lambda*Theta[i][j]);
                theta_grad_row_2[j] = theta_grad_row_2[j]*alpha;
            }

            theta_grad[i] = theta_grad_row_2.clone();            
        }

        return new Pair<>(x_grad,theta_grad);
    }

    double gradientDescent(double[][] x, double[][] theta, int[][] movieUserData, int iterations, double lambda, double alpha,
    int numMovies, int numUsers)
    {
        double cost=0;
        for(int i=0;i<iterations;i++){
            cost = costFn(x, theta, movieUserData, lambda, numMovies, numUsers);
            System.out.println(cost);

            Pair<double[][],double[][]> grads = gradients(x, theta, movieUserData, lambda, alpha, numMovies, numUsers);
            double[][] x_grad = grads.getKey();
            double[][] theta_grad = grads.getValue();

            // simultaneously applying gradient descent to x and theta
            for(int z=0;z<numMovies;z++){
                for(int j=0;j<100;j++){
                    x[z][j] -= x_grad[z][j];
                }
            }

            for(int z=0;z<numUsers;z++){
                for(int j=0;j<100;j++){
                    theta[z][j] -= theta_grad[z][j];
                }
            }
        }
        return cost;
    } 
} 