package Utils;

import java.util.Scanner;

public class Reader {
    private Reader() {};

    public static<T> T Read(Class<T> type)
    {
        Scanner sc = new Scanner(System.in);
        String value = sc.nextLine();

        if (type == String.class)
            return type.cast(value);

        if (type == Integer.class)
            return type.cast(Integer.parseInt(value));

        if (type == Double.class)
            return type.cast(Double.parseDouble(value));

        if (type == Float.class)
            return type.cast(Double.parseDouble(value));

        if (type == Boolean.class)
            return type.cast(Double.parseDouble(value));

        if (type == Long.class)
            return type.cast(Double.parseDouble(value));

        throw new UnsupportedOperationException("Type " + type.getSimpleName() + " is not supported for reading.");
    }
}
