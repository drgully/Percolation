/*-----------------------------------------------------------------------------
 *  Author:        David Gully
 *  Written:       June 15, 2014
 *
 *  Compilation:   javac PercolationStats.java
 *  Execution:     java PercolationStats <grid dimension> <number of tests>
 *  
 *  Creates two dimensional grid of closed sites.
 *  Opens sites, connects them and tests for percolation.
 * 
 *  % java PercolationStats 200 100
 *  mean                    = 0.5923647500000001 
 *  stddev                  = 0.00943328253479737 
 *  95% confidence interval = 0.5905158266231798, 0.5942136733768204 
 *  
 *---------------------------------------------------------------------------*/

public class PercolationStats
{
    int N, T;            // dimensions and number of tests
    double[] percolated; // array to store count of opened sites per test
    
    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T)
    {
        this.N = N;
        this.T = T;
        percolated = new double[T];
        for(int i = 0; i < T; i++)
        {
            Percolation obj = new Percolation(N);
            int count = 0;            
            while(!obj.percolates())
            {
                int row = StdRandom.uniform(N)+1;
                int col = StdRandom.uniform(N)+1;
                obj.open(row, col);
                count++;
            }
            percolated[i] = (double)obj.getOpened() / (N*N);
        }
        System.out.println("mean                    = " + mean());
        System.out.println("stddev                  = " + stddev());
        System.out.println("95% confidence interval = " + confidenceLo() + ", " + confidenceHi());
    }
    
    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(percolated);
    }
    
    // sample standard deviation of percolation threshold
    public double stddev()
    {
        return StdStats.stddev(percolated);
    }
    
    // returns lower bound of the 95% confidence interval
    public double confidenceLo()
    {
        return mean() - (1.96 * (stddev() / Math.sqrt(T)));
    }
    
    // returns upper bound of the 95% confidence interval
    public double confidenceHi()
    {
        return mean() + (1.96 * (stddev() / Math.sqrt(T)));
    }
    
    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
    }
}