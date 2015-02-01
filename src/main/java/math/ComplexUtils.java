package math;

import org.apache.commons.math3.complex.Complex;

public class ComplexUtils {

    public static Function<Double> real(final Function<Complex> function) {
        return new Function<Double>() {

            @Override
            public Double compute(double... x) {
                return function.compute(x).getReal();
            }
        };
    }

    public static Function<Double> imag(final Function<Complex> function) {
        return new Function<Double>() {

            @Override
            public Double compute(double... x) {
                return function.compute(x).getImaginary();
            }
        };
    }

    public static Function<Double> abs(final Function<Complex> function) {
        return new Function<Double>() {

            @Override
            public Double compute(double... x) {
                return function.compute(x).abs();
            }
        };
    }
    
    public static Function<Double> useMode(Function<Complex> function, ComplexPlotMode mode) {
        switch (mode) {
            case RE:
                return real(function);
            case IM:
                return imag(function);
            case ABS:
                return abs(function);
            default:
                throw new AssertionError();
        }
    }

}
