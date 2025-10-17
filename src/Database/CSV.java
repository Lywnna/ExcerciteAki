package Database;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSV {

    private CSV(){}

    private static final String Delimiter = ";";

    public static boolean WriteToTable(Table data) throws IOException {
        if (data == null || data.getHeader() == null)
            throw new NullPointerException("Data is null");

        StringBuilder csvBuilder = new StringBuilder();

        csvBuilder.append(String.join(Delimiter, data.getHeader())).append("\r\n");

        for (List<String> row : data.getRows())
            csvBuilder.append(String.join(Delimiter, row)).append("\r\n");

        BufferedWriter writer = new BufferedWriter(new FileWriter(data.getPath()));
        writer.write(csvBuilder.toString());

        return true;
    }

    public static Table ReadTable(String tableName) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(tableName));

        String line;
        List<String> header = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();


        boolean isHeader = true;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            List<String> cells = Arrays.stream(line.split(Delimiter))
                .map(String::trim)
                .collect(Collectors.toList());

            if (isHeader) {
                header = cells;
                isHeader = false;
            } else rows.add(cells);
        }

        return new Table(tableName, header, rows);

    }
}
