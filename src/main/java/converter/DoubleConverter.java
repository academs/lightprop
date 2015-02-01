package converter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DoubleConverter implements Converter {

    private static final String ERROR_MSG = "Значение должно быть "
            + "числом двойной точности в виде 0.1234, .1234, 1.234e-1";

    @Override
    public String asString(Object o) {
        if (o instanceof Double) {
            return Double.toString((Double) o);
        }
        Logger.getLogger(DoubleConverter.class.getName())
                .log(Level.SEVERE, "Ошибка в валидаторе",
                        new IllegalArgumentException("Object: " + o));
        return "###";
    }

    @Override
    public Object asObject(String str) throws IllegalArgumentException {
        try {
            double res = Double.parseDouble(str);            
            return res;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(ERROR_MSG);
        } catch (Exception ex) {
            Logger.getLogger(DoubleConverter.class.getName()).log(Level.INFO, "Ошибка в валидаторе", ex);
        }
        throw new IllegalArgumentException("Попробуйте ввести значение заново");
    }

}
