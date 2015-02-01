package converter;

public class PositiveDoubleConverter extends DoubleConverter {

    private static final String ERROR_MSG = "Значение должно быть положительным "
            + "числом двойной точности в виде 0.1234, .1234, 1.234e-1";

    @Override
    public Object asObject(String str) throws IllegalArgumentException {
        double res = (Double) super.asObject(str);
        if (res <= 0) {
            throw new IllegalArgumentException(ERROR_MSG);
        }
        return res;
    }
}
