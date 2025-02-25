import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FloatFileManager {

    private static final String FILE_PATH = "float_data.txt";

    // Method to save a float to a file
    public static void saveScore(double number) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(String.valueOf(number));
            System.out.println("Saved: " + number);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read a float from the file
    public static double readScore() {
        try {
            String data = new String(Files.readAllBytes(Paths.get(FILE_PATH))).trim();
            return (double) Double.parseDouble(data);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return -1; // Return an invalid value if an error occurs
        }
    }

    // Method to modify the float in the file
    public static void modifyScore(double newValue) {
        saveScore(newValue); // Overwrite with the new value
        System.out.println("Modified to: " + newValue);
    }

    // public static void main(String[] args) {
    //     // Example usage
    //     saveFloat(12.5f); // Save 12.5 in the file
    //     System.out.println("Read: " + readFloat()); // Read and print the saved value

    //     modifyFloat(20.75f); // Modify the value to 20.75
    //     System.out.println("Read after modification: " + readFloat()); // Verify modification
    // }
}
