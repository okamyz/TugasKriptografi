package quizkriptografi;




//NAME  :Fareza Ahmad Kurniawan
//NIM   : 4611422159
public class CipherUtils {

    //for Vigenere Model
    public static String encryptVigenere(String text, String key) {
        StringBuilder result = new StringBuilder();
        key = key.toUpperCase();
        text = text.toUpperCase();

        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z') continue;
            result.append((char)((c + key.charAt(j) - 2 * 'A') % 26 + 'A'));
            j = ++j % key.length();
        }
        return result.toString();
    }

    public static String decryptVigenere(String text, String key){
        StringBuilder result = new StringBuilder();
        key = key.toUpperCase();
        text = text.toUpperCase();

        for (int i = 0, j = 0; i < text.length(); i++){
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z') continue;
            result.append((char)((c -  key.charAt(j) + 26) % 26 + 'A'));
            j = ++j % key.length();
        }
        return result.toString();
    }

    //for Playfair Cipher model
    private static char[][] generatePlayfairMatrix(String key){
        char[][] matrix =  new char[5][5];
        boolean[] letterUsed = new boolean[26];
        key = key.toUpperCase().replaceAll("J","I");

        int index = 0;
        for (char c : key.toCharArray()) {
            if(!letterUsed[c - 'A']) {
                matrix[index / 5][index % 5] = c;
                letterUsed[c - 'A'] = true;
                index++;
            }
        }

        for (char c = 'A'; c<='Z'; c++){
            if(c == 'J') continue; // 'J' di Skip karena matriksnya berukuran 5x5
            if(!letterUsed[c - 'A']){
                matrix[index / 5][index % 5] = c;
                letterUsed[c - 'A'] = true;
                index++;
            }
        }
        return matrix;
    }

    private static String processPlayfair(char[][] matrix, String text, boolean encrypt){
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase().replaceAll("J", "I");

        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = (i + 1 < text.length()) ? text.charAt(i + 1) : 'X';

            int[] posA = findPosition(matrix, a);
            int[] posB = findPosition(matrix, b);

            if (posA[0] == posB[0]) {
                result.append(matrix[posA[0]][(posA[1] + (encrypt ? 1 : 4)) % 5]);
                result.append(matrix[posB[0]][(posB[1] + (encrypt ? 1 : 4)) % 5]);
            } else if (posA[1] == posB[1]) {
                result.append(matrix[(posA[0] + (encrypt ? 1 : 4)) % 5][posA[1]]);
                result.append(matrix[(posB[0] + (encrypt ? 1 : 4)) % 5][posB[1]]);
            } else {
                result.append(matrix[posA[0]][posB[1]]);
                result.append(matrix[posB[0]][posA[1]]);
            }
        }
        return result.toString();
    }

    private static int[] findPosition(char[][] matrix, char c) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public static String encryptPlayfair(String text, String key) {
        char[][] matrix = generatePlayfairMatrix(key);
        return processPlayfair(matrix, text, true);
    }

    public static String decryptPlayfair(String text, String key) {
        char[][] matrix = generatePlayfairMatrix(key);
        return processPlayfair(matrix, text, false);
    }

    //for Hill Cipher (4x4 Matrix)
    public static String encryptHill(String text, int[][] keyMatrix) {
        int[][] textMatrix = createTextMatrix(text, keyMatrix.length);
        int[][] encryptedMatrix = multiplyMatrices(textMatrix, keyMatrix);
        return convertMatrixToText(encryptedMatrix);
    }

    public static String decryptHill(String text, int[][] keyMatrix) {
        int[][] inverseKeyMatrix = MatrixUtils.inverse(keyMatrix);
        int[][] textMatrix = createTextMatrix(text, keyMatrix.length);
        int[][] decryptedMatrix = multiplyMatrices(textMatrix, inverseKeyMatrix);
        return convertMatrixToText(decryptedMatrix);
    }

    private static int[][] createTextMatrix(String text, int size) {
        // Pad text with 'X' if necessary
        while (text.length() % size != 0) {
            text += 'X';
        }

        int[][] matrix = new int[text.length() / size][size];

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                matrix[i / size][i % size] = c - 'A';
            }
        }
        return matrix;
    }

    private static int[][] multiplyMatrices(int[][] a, int[][] b) {
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;

        int[][] result = new int[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] = (result[i][j] + a[i][k] * b[k][j]) % 26; // Modulo 26 for letters
                }
            }
        }
        return result;
    }

    private static String convertMatrixToText(int[][] matrix) {
        StringBuilder result = new StringBuilder();
        for (int[] row : matrix) {
            for (int value : row) {
                result.append((char) (value + 'A'));
            }
        }
        return result.toString();
    }
}



