package Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterHelper {
    private FilterHelper(){}

    public static List<Condition> parseFilter(String filter) {
        List<Condition> conditions = new ArrayList<>();
        if (filter == null || filter.trim().isEmpty()) {
            return conditions;
        }

        String[] rawConditions = filter.split(";");

        for (String rawCondition : rawConditions) {
            String trimmedCondition = rawCondition.trim();
            if (trimmedCondition.isEmpty()) continue;

            String[] parts = trimmedCondition.split(" ", 3);

            if (parts.length == 3) {
                Condition cond = new Condition();
                cond.setHeader(parts[0].trim());
                cond.setOperator(parts[1].trim());
                cond.setValue(parts[2].trim());

                // **Validation:** Check if cond.operator is one of the supported ones.
                if (List.of("=", ">", "<", ">=", "<=", "%").contains(cond.getOperator()))
                    conditions.add(cond);
                else
                    throw new RuntimeException("Invalid operation on filter to table: " + cond.getOperator());
            } else
                throw new RuntimeException("Invalid filter: " + filter);
        }
        return conditions;
    }


    public static boolean EvaluateCondition (String rowValue, String operator, String filterValue) {
        return switch (operator) {
            case "=" -> rowValue.compareTo(filterValue) == 0;
            case ">" -> rowValue.compareTo(filterValue) < 0;
            case "<" -> rowValue.compareTo(filterValue) > 0;
            case ">=" -> rowValue.compareTo(filterValue) <= 0;
            case "<=" -> rowValue.compareTo(filterValue) >= 0;
            case "%" -> rowValue.contains(filterValue);
            default -> false;
        };
    }
}