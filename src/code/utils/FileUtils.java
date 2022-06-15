package code.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {
    public static ArrayList<String> readFileToMemory(String filepath) {
        BufferedReader br = null;
        String currentLine = null;
        ArrayList<String> fileContents = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(filepath));
            while ((currentLine = br.readLine()) != null) {
                fileContents.add(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContents;
    }
}
