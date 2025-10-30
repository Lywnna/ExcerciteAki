package Database;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final String Path;
    private final String Name;
    private final List<String> Header;
    private final List<List<String>> Rows;

    public Table(String name, List<String> header, List<List<String>> rows) {
        // CORRIGIDO: Caminho relativo ao projeto
        Path = "src/Tables/" + name + ".csv";
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
        int idx = Header.indexOf(header);
        if (idx != -1) {
            Header.remove(idx);
            // Remover a coluna de todas as linhas
            for (List<String> row : Rows) {
                if (idx < row.size()) {
                    row.remove(idx);
                }
            }
        }
    }

    // NOVO: Adicionar linha
    public void AddRow(List<String> row) {
        if (row.size() == Header.size()) {
            Rows.add(new ArrayList<>(row));
        } else {
            throw new IllegalArgumentException("Row size doesn't match header size");
        }
    }

    public void log(){
        for (String col : Header) {
            System.out.print(col + " | "); // CORRIGIDO: print ao invés de println
        }
        System.out.println(); // Nova linha após header

        for (List<String> row : Rows) {
            for(String r : row) {
                System.out.print(r + " | "); // CORRIGIDO: print ao invés de println
            }
            System.out.println(); // Nova linha após cada row
        }
    }
}
