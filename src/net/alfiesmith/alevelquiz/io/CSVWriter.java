package net.alfiesmith.alevelquiz.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author AvroVulcan on 07/05/2020
 */
public class CSVWriter {

    private File file;

    public CSVWriter(File file) {
        this.file = file;

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }

    public void writeAll(List<String[]> data) {
        try (PrintWriter empty = new PrintWriter(file)){ // empty the file
            for (String[] line : data) {
                empty.println(convertToCsv(line));
            }
        } catch (IOException err) {
            err.printStackTrace();;
        }



    }

    public List<String[]> read() {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }
        } catch (IOException err) {
            err.printStackTrace();
        }

        return data;
    }

    /**
     * Will take some data ie
     * { "Alfie", "Smith", "AvroVulcan", "5", "10" }
     */
    public static String convertToCsv(String[] data) {
        return String.join(",", data);
    }


}
