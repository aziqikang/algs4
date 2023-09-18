import edu.princeton.cs.algs4.Picture;

/**
 *
 * Seam Carving
 *
 * Image resizing by removing least-energy seams
 */
public class SeamCarver {

    private static final double BORDER_ENERGY = 1000.00;
    private int[][] pictureRGB;
    private double[][] energyArrray;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("null picture argument");
        }

        pictureRGB = new int[picture.height()][picture.width()];
        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                pictureRGB[y][x] = picture.getRGB(x, y);
            }
        }

        energyArrray = new double[picture.height()][picture.width()];
        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                energyArrray[y][x] = energy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(pictureRGB[0].length, pictureRGB.length);
        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                picture.setRGB(x, y, pictureRGB[y][x]);
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        return energyArrray[0].length;
    }

    // height of current picture
    public int height() {
        return energyArrray.length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!(0 <= x && x < width() && 0 <= y && y < height())) {
            throw new IllegalArgumentException("pixel is out of range");
        }
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return BORDER_ENERGY;
        }
        return Math.sqrt(xGradient(x, y) + yGradient(x, y));
    }

    // calculates square of the x-gradient
    private double xGradient(int x, int y) {
        int color1 = pictureRGB[y][x + 1];
        int color2 = pictureRGB[y][x - 1];

        double rDifference = ((color2 >> 16) & 0xFF) - ((color1 >> 16) & 0xFF);
        double gDifference = ((color2 >> 8) & 0xFF) - ((color1 >> 8) & 0xFF);
        double bDifference = (color2 & 0xFF) - (color1 & 0xFF);

        return rDifference * rDifference + gDifference * gDifference + bDifference * bDifference;
    }

    // calculates square of the y-gradient
    private double yGradient(int x, int y) {
        int color1 = pictureRGB[y + 1][x];
        int color2 = pictureRGB[y - 1][x];

        double rDifference = ((color2 >> 16) & 0xFF) - ((color1 >> 16) & 0xFF);
        double gDifference = ((color2 >> 8) & 0xFF) - ((color1 >> 8) & 0xFF);
        double bDifference = (color2 & 0xFF) - (color1 & 0xFF);

        return rDifference * rDifference + gDifference * gDifference + bDifference * bDifference;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposeEnergyArray();
        int[] horizontalSeam = findVerticalSeam();
        transposeEnergyArray();
        return horizontalSeam;
    }

    private void transposeEnergyArray() {
        double[][] newEnergyArray = new double[width()][height()];
        for (int y = 0; y < width(); y++) {
            for (int x = 0; x < height(); x++) {
                newEnergyArray[y][x] = energyArrray[x][y];
            }
        }
        energyArrray = newEnergyArray;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo
                = new double[height()][width()];      // stores cumulative energy from top of pic to a pixel
        int[][] edgeTo
                = new int[height()][width()];            // stores x value of adjacent vertex in row above
        for (int y = 1; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                distTo[y][x] = Integer.MAX_VALUE;
            }
        }
        for (int x = 0; x < width(); x++) {
            distTo[0][x] = BORDER_ENERGY;
        }

        // set edgeTo to -1 for all vertices in the first row
        for (int x = 0; x < width(); x++) {
            edgeTo[0][x] = -1;
        }

        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                relaxEdges(x, y, distTo, edgeTo);
            }
        }

        // find the shortest distTo of the pixels in row y-1, find the path that leads to this pixel
        int currentX = 0;
        double minDist = distTo[height() - 1][currentX];
        for (int x = 1; x < width(); x++) {
            if (distTo[height() - 1][x] < minDist) {
                currentX = x;
                minDist = distTo[height() - 1][x];
            }
        }

        int[] verticalSeam = new int[height()];
        for (int y = height() - 1; y >= 0; y--) {
            verticalSeam[y] = currentX;
            currentX = edgeTo[y][currentX];
        }

        return verticalSeam;
    }

    // finds x-values of vertically adjacent vertices
    private void relaxEdges(int x, int y, double[][] distTo, int[][] edgeTo) {
        int[] adjacent;
        if (x == 0 && x == width() - 1) {
            adjacent = new int[] { 0 };
        }
        else if (x == width() - 1) {
            adjacent = new int[] { x - 1, x };
        }
        else if (x == 0) {
            adjacent = new int[] { 0, 1 };
        }
        else {
            adjacent = new int[] { x - 1, x, x + 1 };
        }

        for (int v : adjacent) {
            if (distTo[y + 1][v] > distTo[y][x] + energyArrray[y + 1][v]) {
                edgeTo[y + 1][v] = x;
                distTo[y + 1][v] = distTo[y][x] + energyArrray[y + 1][v];
            }
        }
    }

    private void checkHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width()) {
            throw new IllegalArgumentException("invalid seam length");
        }

        int previousY = seam[0];
        for (int y : seam) {
            if (!(y >= 0 && y < height())) {
                throw new IllegalArgumentException("invalid seam");
            }
            if (y - previousY > 1 || y - previousY < -1) {
                throw new IllegalArgumentException("invalid seam");
            }
            previousY = y;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkHorizontalSeam(seam);
        int[][] newPictureRGB = new int[height() - 1][width()];

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < seam[x]; y++) {
                newPictureRGB[y][x] = pictureRGB[y][x];
            }
            for (int y = seam[x]; y < height() - 1; y++) {
                newPictureRGB[y][x] = pictureRGB[y + 1][x];
            }
        }

        pictureRGB = newPictureRGB;

        double[][] newEnergyArray = new double[height() - 1][width()];

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < seam[x]; y++) {
                newEnergyArray[y][x] = energyArrray[y][x];
            }
            for (int y = seam[x]; y < height() - 1; y++) {
                newEnergyArray[y][x] = energyArrray[y + 1][x];
            }
        }

        energyArrray = newEnergyArray;

        // update energy along seam
        for (int x = 0; x < width(); x++) {
            if (seam[x] > 0) {
                energyArrray[seam[x] - 1][x] = energy(x, seam[x] - 1);
            }
            if (seam[x] < height()) {
                energyArrray[seam[x]][x] = energy(x, seam[x]);
            }
        }
    }

    private void checkVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height()) {
            throw new IllegalArgumentException("invalid seam length");
        }

        int previousX = seam[0];
        for (int x : seam) {
            if (!(x >= 0 && x < width())) {
                throw new IllegalArgumentException("invalid seam");
            }
            if (x - previousX > 1 || x - previousX < -1) {
                throw new IllegalArgumentException("invalid seam");
            }
            previousX = x;
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkVerticalSeam(seam);
        int[][] newPictureRGB = new int[height()][width() - 1];

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < seam[y]; x++) {
                newPictureRGB[y][x] = pictureRGB[y][x];
            }
            for (int x = seam[y]; x < width() - 1; x++) {
                newPictureRGB[y][x] = pictureRGB[y][x + 1];
            }
        }

        pictureRGB = newPictureRGB;


        double[][] newEnergyArray = new double[height()][width() - 1];

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < seam[y]; x++) {
                newEnergyArray[y][x] = energyArrray[y][x];
            }
            for (int x = seam[y]; x < width() - 1; x++) {
                newEnergyArray[y][x] = energyArrray[y][x + 1];
            }
        }

        energyArrray = newEnergyArray;

        // update energy along seam
        for (int y = 0; y < height(); y++) {
            if (seam[y] > 0) {
                energyArrray[y][seam[y] - 1] = energy(seam[y] - 1, y);
            }
            if (seam[y] < width()) {
                energyArrray[y][seam[y]] = energy(seam[y], y);
            }
        }
    }


    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        picture.show();
    }

}
