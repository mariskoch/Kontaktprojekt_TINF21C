package code.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {

    /**
     * Takes a String referring to a file, which will be read line by line into Strings. The Content of the file will
     * be stored and returned by a ArrayList of String.
     * @param filepath Path to *.db file to be read
     * @return ArrayList containing Strings with file content
     */
    public static ArrayList<String> readFileToMemory(String filepath) {
        BufferedReader br = null;
        String currentLine;
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
