package Database;

import java.io.IOException;

public class Database {
    private Database() {}
    public static Table SelectAll(String tableName) throws IOException {
        Table t = CSV.ReadTable(tableName);
        if (t == null) {

        }
    }
}
