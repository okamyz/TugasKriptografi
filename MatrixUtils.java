package quizkriptografi;

public class MatrixUtils {
    // Function to calculate the determinant of a 2x2 matrix
    public static int determinant2x2(int[][] matrix) {
        return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % 26;
    }

    // Function to find the inverse of a 2x2 matrix
    public static int[][] inverse2x2(int[][] matrix) {
        int det = determinant2x2(matrix);
        if (det == 0) throw new IllegalArgumentException("Matrix is not invertible.");

        // Calculate the modular inverse of the determinant
        int modInverse = modularInverse(det, 26);
        if (modInverse == -1) throw new IllegalArgumentException("Matrix is not invertible in mod 26.");

        return new int[][]{
            {(matrix[1][1] * modInverse) % 26, (-matrix[0][1] * modInverse + 26) % 26},
            {(-matrix[1][0] * modInverse + 26) % 26, (matrix[0][0] * modInverse) % 26}
        };
    }

    // Function to find the modular inverse using the Extended Euclidean Algorithm
    private static int modularInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1; // No modular inverse
    }

    // Function to find the inverse of a 4x4 matrix
    public static int[][] inverse(int[][] matrix) {
        // Implement the inverse calculation for a 4x4 matrix if necessary.
        // This is a more complex operation; consider using a library for robust handling.
        throw new UnsupportedOperationException("4x4 matrix inversion not implemented.");
    }
}
