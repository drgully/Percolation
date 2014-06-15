/*-----------------------------------------------------------------------------
 *  Author:        David Gully
 *  Written:       June 15, 2014
 *
 *  Compilation:   javac Percolation.java
 *  Execution:     java PercolationStats <grid dimension> <number of tests>
 *  
 *  Creates two dimensional grid of closed sites.
 *  Opens sites, connects them and tests for percolation.
 * 
 *---------------------------------------------------------------------------*/

public class Percolation
{
    int[][] grid;    // site layout in two dimensions
    boolean[] open;  // array of open sites
    int N;           // dimension of sites
    int opened;      // count of opened sites
    WeightedQuickUnionUF helper;
        
    // create N-by-N grid, with all sites blocked
    public Percolation(int N)
    {
        this.N = N;
        opened = 0;
        int count = 0; // use for labeling grid
        grid = new int[N][N];
        open = new boolean[N*N];
        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < N; j++)
            {
                grid[i][j] = count;
                open[count++] = false;
            }
        }
        helper = new WeightedQuickUnionUF(N*N+2);
    }
    
    // open site (row i, column j) if it is not already
    public void open(int i, int j)
    {
        if(i > 0 && i <= grid.length && j > 0 && j <= grid.length)
        {
            if(!isOpen(i, j))
            {
                open[grid[i-1][j-1]] = true;
                opened++;
                // find surrounding open sites and connect
                if((grid[i-1][j-1] / N) != 0) // has site above
                {
                    if(open[grid[i-2][j-1]] == true)
                    {
                        helper.union(grid[i-1][j-1], grid[i-2][j-1]);
                    }
                }
                else // is on top row
                {
                    helper.union(grid[i-1][j-1], N*N); // connect top imaginary site
                }
                if((grid[i-1][j-1] % N) != (N - 1)) // has site to right
                {
                    if(open[grid[i-1][j]] == true)
                    {
                        helper.union(grid[i-1][j-1], grid[i-1][j]);
                    }
                }
                if((grid[i-1][j-1] / N) != (N - 1)) // has site below
                {
                    if(open[grid[i][j-1]] == true)
                    {
                        helper.union(grid[i-1][j-1], grid[i][j-1]);
                    }
                }
                else // is on bottom row
                {
                    helper.union(grid[i-1][j-1], N*N+1); // connect bottom imaginary site
                }
                if((grid[i-1][j-1] % N) != 0) // has site to left
                {
                    if(open[grid[i-1][j-2]] == true)
                    {
                        helper.union(grid[i-1][j-1],grid[i-1][j-2]);
                    }
                }
            }
        }
        else
        {
            throw new IndexOutOfBoundsException();
        }
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j)
    {
        boolean retval = false; 
        if(i > 0 && i <= grid.length && j > 0 && j <= grid.length)
        {
            retval = (open[grid[i-1][j-1]] == true);
        }
        else
        {
            throw new IndexOutOfBoundsException();
        }
        return retval;
    }
    
    // is site (row i, column j) full?
    public boolean isFull(int i, int j)
    {
        boolean retval = false; 
        if(i > 0 && i <= grid.length && j > 0 && j <= grid.length)
        {
            // true if site is connected to top imaginary site
            retval = helper.connected(grid[i-1][j-1], N*N);
        }
        return retval;
    }
    
    // does the system percolate?
    public boolean percolates()
    {
        return helper.connected(N*N, N*N+1);
    }
    
    // get count of opened
    public int getOpened()
    {
        return opened;
    }
}