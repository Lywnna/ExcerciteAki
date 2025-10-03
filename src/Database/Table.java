package Database;

import java.util.List;

public class Table {
    private final String Path;
    private final String Name;
    private final List<String> Header;
    private final List<List<String>> Rows;

    public Table(String name, List<String> header, List<List<String>> rows) {
        Path = "" + name + ",csv";
        Name = name;
        Header = header;
        Rows = rows;
    }

    public String getPath(){
        return Path;
    }

    public String getName() {
        return Name;
    }

    public List<String> getHeader() {
        return Header;
    }

    public List<List<String>> getRows() {
        return Rows;
    }

}
