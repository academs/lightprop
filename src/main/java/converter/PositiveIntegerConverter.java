package converter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PositiveIntegerConverter implements Converter {

    private static final String ERROR_MSG = "Значение должно быть положительным "
            + "целым числом";

    @Override
    public String asString(Object o) {
        if (o instanceof Integer) {
            return Integer.toString((Integer) o);
        }
        Logger.getLogger(PositiveIntegerConverter.class.getName())
                .log(Level.SEVERE, "Ошибка в валидаторе",
                        new IllegalArgumentException("Object: " + o));
        return "###";
    }

    @Override
    public Object asObject(String str) throws IllegalArgumentException {
        try {
            int res = Integer.parseInt(str);
            if (res <= 0) {
                throw new IllegalArgumentException(ERROR_MSG);
            }
            return res;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(ERROR_MSG);
        } catch (Exception ex) {
            Logger.getLogger(PositiveIntegerConverter.class.getName()).log(Level.INFO, "Ошибка в валидаторе", ex);
        }
        throw new IllegalArgumentException("Попробуйте ввести значение заново");
    }
}
