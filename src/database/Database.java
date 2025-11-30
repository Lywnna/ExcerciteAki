package database;

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

        // CORRIGIDO: Lógica invertida
        for (String currentHeader : table.getHeader()) {
            boolean shouldKeep = Arrays.asList(headers).contains(currentHeader);
            if (!shouldKeep) {
                table.DeleteHeader(currentHeader);
            }
        }

        return table;
    }

    public static Table SelectFiltered(String tableName, String filter, String... headers) throws IOException {
        Table table = Select(tableName, headers);
        List<Condition> conditions = FilterHelper.parseFilter(filter);

        if(table.CountRows() <= 0) return table;

        // CORRIGIDO: Iterar de trás para frente para evitar problemas ao remover
        for(int i = table.CountRows() - 1; i >= 0; i--){
            List<String> row = table.GetRows(i);
            boolean rowMatchesFilter = true;

            for (Condition cond : conditions) {
                int headerIndex = table.GetHeaderIndex(cond.getHeader());
                if (headerIndex == -1) continue;

                String rowValue = row.get(headerIndex);

                if (!FilterHelper.EvaluateCondition(rowValue, cond.getOperator(), cond.getValue())) {
                    rowMatchesFilter = false;
                    break;
                }
            }

            if(!rowMatchesFilter) {
                table.DeleteRow(i);
            }
        }
        return table;
    }

    public static Table SelectAll(String tableName, String filter) throws IOException {
        Table table = SelectAll(tableName);
        String[] allHeaders = table.getHeader().toArray(new String[0]);
        return SelectFiltered(tableName, filter, allHeaders);
    }

    // NOVO: Método para inserir dados
    public static void Insert(String tableName, String... values) throws IOException {
        Table table = SelectAll(tableName);

        if (values.length != table.CountHeaders()) {
            throw new IllegalArgumentException("Number of values doesn't match number of columns");
        }

        table.AddRow(Arrays.asList(values));
        CSV.WriteToTable(table);
    }

    // NOVO: Método para atualizar dados
    public static void Update(String tableName, String filter, String headerToUpdate, String newValue) throws IOException {
        Table table = SelectAll(tableName);
        List<Condition> conditions = FilterHelper.parseFilter(filter);

        int updateColumnIndex = table.GetHeaderIndex(headerToUpdate);
        if (updateColumnIndex == -1) {
            throw new IllegalArgumentException("Header not found: " + headerToUpdate);
        }

        for(List<String> row : table.getRows()){
            boolean rowMatchesFilter = true;

            for (Condition cond : conditions) {
                int headerIndex = table.GetHeaderIndex(cond.getHeader());
                if (headerIndex == -1) continue;

                String rowValue = row.get(headerIndex);

                if (!FilterHelper.EvaluateCondition(rowValue, cond.getOperator(), cond.getValue())) {
                    rowMatchesFilter = false;
                    break;
                }
            }

            if(rowMatchesFilter) {
                row.set(updateColumnIndex, newValue);
            }
        }

        CSV.WriteToTable(table);
    }
}
