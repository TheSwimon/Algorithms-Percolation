import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private final WeightedQuickUnionUF gridUfOriginal;
    private final WeightedQuickUnionUF gridUfHelper;
    private final boolean[] sites;
    private final int gridLength;
    private int openSitesAmount;
 

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n <= 0)
            throw new IllegalArgumentException("Grid size has to be above 0");

        // +2 for virtual top and virtual bottom sites (VTS at 0 index and VBS at n+1 index)
        gridUfOriginal = new WeightedQuickUnionUF(n * n + 2);
        gridUfHelper = new WeightedQuickUnionUF(n * n + 1);
        sites = new boolean[n*n+1];
        gridLength = n;
    }

     // opens the site (row, col) if it is not open already
     public void open(int row, int col) {
        if (row<1 || row>gridLength || col < 1 || col > gridLength) throw new IllegalArgumentException("row and col indices are outside of the available range: 1 to " + gridLength);
        
        if (isOpen(row, col))
             return;

         int siteIndex = convertToIndex(row, col);
         sites[siteIndex] = true;
         openSitesAmount++;

         if (siteIndex <= gridLength) {
             gridUfOriginal.union(siteIndex, 0);
             gridUfHelper.union(siteIndex, 0);
         }

         if (siteIndex > gridLength*gridLength-gridLength) {
             gridUfOriginal.union(siteIndex, gridLength*gridLength+1);
         }

         // left neighboring site
         if (siteIndex % gridLength != 1 && isOpen(row, col - 1)) {
             int leftNeighborIndex = siteIndex - 1;
             gridUfOriginal.union(siteIndex, leftNeighborIndex);
             gridUfHelper.union(siteIndex, leftNeighborIndex);
         }
         
         // right neighboring site
         if (siteIndex % gridLength != 0 && isOpen(row, col + 1)) {
             int rightNeighborIndex = siteIndex + 1;
             gridUfOriginal.union(siteIndex, rightNeighborIndex);
             gridUfHelper.union(siteIndex, rightNeighborIndex);
         }

         // top neighboring site
         if (siteIndex > gridLength && isOpen(row - 1, col)) {
             int topNeighborIndex = siteIndex - gridLength;
             gridUfOriginal.union(siteIndex, topNeighborIndex);
             gridUfHelper.union(siteIndex, topNeighborIndex);
         }

         // bottom neighboring site
         if (siteIndex <= gridLength * gridLength - gridLength && isOpen(row +1, col) ) {
             int bottomNeighborIndex = siteIndex + gridLength;
             gridUfOriginal.union(siteIndex, bottomNeighborIndex);
             gridUfHelper.union(siteIndex, bottomNeighborIndex);
         }
     }
    
      // is the site (row, col) open?
      public boolean isOpen(int row, int col) {
          if (row < 1 || row > gridLength || col < 1 || col > gridLength)
              throw new IllegalArgumentException(
                      "row and col indices are outside of the available range: 1 to " + gridLength);

          int index = convertToIndex(row, col);

          return sites[index];
      }

      // is the site (row, col) full?
      public boolean isFull(int row, int col) {
          if (row < 1 || row > gridLength || col < 1 || col > gridLength) {
             throw new IllegalArgumentException(
                      "row and col indices are outside of the available range: 1 to " + gridLength);
        }
             
        




          var index = convertToIndex(row, col);
          return gridUfHelper.find(index) == gridUfHelper.find(0);
    }

      // returns the number of open sites
      public int numberOfOpenSites() {
          return openSitesAmount;
      }


      // does the system percolate?
      public boolean percolates() {
          return gridUfOriginal.find(0) == gridUfOriginal.find(gridLength*gridLength + 1);
      }
    
       // test client (optional)
       public static void main(String[] args) {

           Percolation percolation = new Percolation(10);

           percolation.open(1, 1);
           System.out.println(percolation.isOpen(1, 1) ? "site (1,1) is open" : "site is closed");
           percolation.open(1, 2);
           System.out.println(percolation.isOpen(1, 2) ? "site (1,2) is open" : "site is closed");
           percolation.open(2, 1);
           System.out.println(percolation.isOpen(1, 2) ? "site (2,1) is open" : "site is closed");

           System.out.println(percolation.gridUfOriginal.find(percolation.convertToIndex(1, 1)) == percolation.gridUfOriginal.find(percolation.convertToIndex(1, 2))
                   && percolation.gridUfOriginal.find(percolation.convertToIndex(1, 2)) == percolation.gridUfOriginal.find(percolation.convertToIndex(2, 1)) ? "sites: (1,1); (1,2) and (2,1) are connected" : "sites are not connected");
    }


       private int convertToIndex(int row, int col) {
           return (row - 1) * gridLength + col;
       }
      
}
