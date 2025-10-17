package Database;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Database {
    private Database() {}

    public static Table SelectAll(String tableName) throws IOException {
        return CSV.ReadTable(tableName);
    }

    public static Table Select(String tableName, String... headers) throws IOException {
        Table table = SelectAll(tableName);

        Arrays.stream(headers).filter(currentHeader -> !table.getHeader().contains(currentHeader)).forEach(table::DeleteHeader);

        return table;
    }

    /**
     * Evaluates a single filter condition against a row's value.
     *
     * @param tableName The CSV file name
     * @param filter The filter string format [header operation filter], the space is important
     * @param headers Headers of the table you want to select
     * @return A filtered table
     */
    public static Table SelectFiltered(String tableName, String filter, String... headers) throws IOException {
        Table table = Select(tableName, headers);
        List<Condition> conditions= FilterHelper.parseFilter(filter);
        int index = 0;

        for(List<String> Row : table.getRows()){
            boolean rowMatchesFilter = true;

            for (Condition cond : conditions) {
                String rowValue = Row.get(table.GetHeaderIndex(cond.getHeader()));

                if (!FilterHelper.EvaluateCondition(rowValue, cond.getOperator(), cond.getValue())) {
                    rowMatchesFilter = false;
                    break;
                }
            }

            if(!rowMatchesFilter)
                table.DeleteRow(index);

            index++;
        }
        return table;
    }

    public static Table SelectAll(String tableName, String filter) throws IOException {
        Table table = SelectAll(tableName);
        return SelectFiltered(tableName, filter, table.getHeader().toString());
    }
}
