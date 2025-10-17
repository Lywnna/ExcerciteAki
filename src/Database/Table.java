package Database;

import java.util.List;

public class Table {
    private final String Path;
    private final String Name;
    private final List<String> Header;
    private final List<List<String>> Rows;

    public Table(String name, List<String> header, List<List<String>> rows) {
        Path = System.getProperty("java.home") + name + ".csv";
        Name = name;
        Header = header;
        Rows = rows;
    }

    public String getPath(){ return Path; }
    public String getName() { return Name; }
    public List<String> getHeader() { return Header; }
    public List<List<String>> getRows() { return Rows; }

    public int GetHeaderIndex(String header){ return Header.indexOf(header); }
    public String GetHeader(int index){ return Header.get(index); }
    public List<String> GetRows(int index) { return Rows.get(index); }
    public String GetElement(int column, int row) { return Rows.get(row).get(column); }

    public int CountHeaders(){ return Header.size(); }
    public int CountRows(){ return Rows.size(); }

    public void DeleteRow(int index){
        Rows.remove(index);
    }

    public void DeleteHeader(int index){
        Header.remove(index);
    }
    public void DeleteHeader(String header){
        Header.remove(header.indexOf(header));
    }
}
