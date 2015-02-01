package math;

import org.apache.commons.math3.complex.Complex;

public class FourierTransformer {

    public static Function<Complex> transform(final Function<Complex> function,
            final Interval interval, final String name) {

        return new CachedFunction<>(new Function<Complex>() {

            // TODO сделать через FFT
            @Override
            public Complex compute(double... x) {
                if(x.length != 1) {
                    throw new IllegalArgumentException("One dimension function requred");
                }
                Complex res = Complex.ZERO;
                double h = interval.getStep();
                double arg = interval.getStart();
                for (int i = 0; i < interval.getN(); i++) {
                    res = res.add(function.compute(arg)
                            .multiply(Complex.I.multiply(-arg * x[0]).exp()));
                    arg += h;
                }
                return res.multiply(h);
            }
        });
    }
    
    public static Function<Complex> backTransform(final Function<Complex> function,
            final Interval interval, final String name) {

        return new CachedFunction<>(new Function<Complex>() {

            // TODO сделать через FFT
            @Override
            public Complex compute(double... x) {
                if(x.length != 1) {
                    throw new IllegalArgumentException("One dimension function requred");
                }
                Complex res = Complex.ZERO;
                double h = interval.getStep();
                double arg = interval.getStart();
                for (int i = 0; i < interval.getN(); i++) {
                    res = res.add(function.compute(arg)
                            .multiply(Complex.I.multiply(arg * x[0]).exp()));
                    arg += h;
                }
                return res.multiply(h);
            }
        });
    }    
}