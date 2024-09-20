package quizkriptografi;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CipherApp {
    private JFrame frame;
    private JTextArea inputTextArea, outputTextArea;
    private JTextField keyField;
    private JComboBox<String> cipherComboBox;

    public static void main(String[] args) {
        CipherApp app = new CipherApp();
        app.createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Cipher GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel keyLabel = new JLabel("Key:");
        keyLabel.setBounds(10, 20, 80, 25);
        panel.add(keyLabel);

        keyField = new JTextField(20);
        keyField.setBounds(100, 20, 160, 25);
        panel.add(keyField);

        JLabel cipherLabel = new JLabel("Cipher:");
        cipherLabel.setBounds(10, 60, 80, 25);
        panel.add(cipherLabel);

        String[] cipherOptions = {"Vigenère Cipher", "Playfair Cipher", "Hill Cipher"};
        cipherComboBox = new JComboBox<>(cipherOptions);
        cipherComboBox.setBounds(100, 60, 160, 25);
        panel.add(cipherComboBox);

        JButton loadFileButton = new JButton("Load File");
        loadFileButton.setBounds(10, 100, 120, 25);
        panel.add(loadFileButton);

        loadFileButton.addActionListener(e -> loadFile());

        JButton encryptButton = new JButton("Encrypt");
        encryptButton.setBounds(150, 100, 120, 25);
        panel.add(encryptButton);

        encryptButton.addActionListener(e -> encrypt());

        JButton decryptButton = new JButton("Decrypt");
        decryptButton.setBounds(290, 100, 120, 25);
        panel.add(decryptButton);

        decryptButton.addActionListener(e -> decrypt());

        inputTextArea = new JTextArea();
        inputTextArea.setBounds(10, 140, 450, 80);
        panel.add(inputTextArea);

        outputTextArea = new JTextArea();
        outputTextArea.setBounds(10, 240, 450, 80);
        panel.add(outputTextArea);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                inputTextArea.read(reader, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void encrypt() {
        String inputText = inputTextArea.getText();
        String key = keyField.getText();
        String cipher = (String) cipherComboBox.getSelectedItem();
        String outputText = "";

        if (cipher.equals("Vigenère Cipher")) {
            outputText = CipherUtils.encryptVigenere(inputText, key);
        } else if (cipher.equals("Playfair Cipher")) {
            outputText = CipherUtils.encryptPlayfair(inputText, key);
        } else if (cipher.equals("Hill Cipher")) {
            int[][] keyMatrix = generateKeyMatrix(key);
            outputText = CipherUtils.encryptHill(inputText, keyMatrix);
        }

        outputTextArea.setText(outputText);
    }

    private void decrypt() {
        String inputText = inputTextArea.getText();
        String key = keyField.getText();
        String cipher = (String) cipherComboBox.getSelectedItem();
        String outputText = "";

        if (cipher.equals("Vigenère Cipher")) {
            outputText = CipherUtils.decryptVigenere(inputText, key);
        } else if (cipher.equals("Playfair Cipher")) {
            outputText = CipherUtils.decryptPlayfair(inputText, key);
        } else if (cipher.equals("Hill Cipher")) {
            int[][] keyMatrix = generateKeyMatrix(key);
            outputText = CipherUtils.decryptHill(inputText, keyMatrix);
        }

        outputTextArea.setText(outputText);
    }

    private int[][] generateKeyMatrix(String key) {
        // Assuming the key is a 16-character string for a 4x4 matrix
        int[][] keyMatrix = new int[4][4];
        key = key.toUpperCase().replaceAll("[^A-Z]", ""); // Remove non-alphabetic characters

        // Ensure the key has exactly 16 characters
        if (key.length() < 16) {
            while (key.length() < 16) {
                key += 'X'; // Padding with 'X'
            }
        } else if (key.length() > 16) {
            key = key.substring(0, 16); // Truncate to 16 characters
        }

        // Fill the key matrix
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                keyMatrix[i][j] = key.charAt(i * 4 + j) - 'A'; // Convert to integer
            }
        }

        return keyMatrix;
    }
}
