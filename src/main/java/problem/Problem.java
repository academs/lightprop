package problem;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;
import static math.FourierTransformer.backTransform;
import static math.FourierTransformer.transform;
import java.util.HashMap;
import java.util.Map;
import math.Function;
import math.Interval;
import math.Point2D;
import org.apache.commons.math3.complex.Complex;

public class Problem {

    public static final double C = 1;

    private final Function<Complex> spaceComponent;

    private final Function<Complex> timeComponent;

    private final Interval xiInterval;
    private final Interval tInterval;
    private final Interval kInterval;
    private final Interval alphaInterval;

    public Problem(ProblemParams params) {
        this.spaceComponent = params.getSpaceComponent();
        this.timeComponent = params.getTimeComponent();
        this.xiInterval = new Interval(-params.getA(), params.getA(),
                params.getIntegrateNx());
        this.tInterval = new Interval(0.0, params.getTau(),
                params.getIntegrateNt());
        this.kInterval = new Interval(params.getK0() - params.getDk(),
                params.getK0() + params.getDk(), params.getIntegrateNk());
        this.alphaInterval = new Interval(-1.0, 1.0, params.getIntegrateNa());
    }

    // возвращает функцию-результат от трёх переменных
    // [x, t, z]
    public Function<Complex> sovle() {
        // т.к. всё написано в функциональном стиле и вычисления ленивые, то 
        // хотелось бы как-то проконтролировать объём вычислений
        final long[] counters = {0, 0, 0, 0};
        // Преобразования для получения спектра для временной компоненты:
        final Function<Complex> At = transform(timeComponent, tInterval, "At");
        // преобразование для получения "спектра" для пространственной компоненты
        final Function<Complex> As = transform(spaceComponent, xiInterval, "As");
        // подынтегральная функция для преобразования по k:
        final Function<Complex> f1 = new Function<Complex>() {

            // [alpha, k]
            @Override
            public Complex compute(double... x) {
                if (x.length != 2) {
                    throw new IllegalArgumentException("Dimension must be 2");
                }
                counters[0]++;
                return At.compute(C * x[1]).multiply(As.compute(x[0] * x[1]));
            }
        };
        // подынтегральная функция для преобразования по alpha
        final Function<Complex> f0 = new Function<Complex>() {

            // [x, t, z, alpha]
            @Override
            public Complex compute(final double... x) {
                if (x.length != 4) {
                    throw new IllegalArgumentException("Dimension must be 4");
                }
                counters[1]++;
                return backTransform(new Function<Complex>() {

                    // типа частиное применение
                    @Override
                    public Complex compute(double... x2) {
                        return f1.compute(x[3], x2[0]);
                    }
                }, kInterval, "f0").compute(x[3] * x[0] + C * x[1] + x[2] * sqrt(1 - pow(x[3], 2)));
            }
        };
        // результирующая функция
        // (x,t)
        Function<Complex> res = new Function<Complex>() {

            private final Map<Point2D, Complex> cache = new HashMap<>();
            private double z = Double.NaN;

            // [x, t, z]
            @Override
            public Complex compute(final double... x) {
                if (x.length != 3) {
                    throw new IllegalArgumentException("Dimension must be 3");
                }
                if (x[2] != z) {
                    cache.clear();
                    this.z = x[2];
                }
                Point2D arg = new Point2D(x[0], x[1]);
                if (!cache.containsKey(arg)) {
                    counters[2]++;
                    Complex res = transform(new Function<Complex>() {

                        @Override
                        public Complex compute(double... x2) {
                            return f0.compute(x[0], x[1], x[2], x2[0]);
                        }
                    }, alphaInterval, "res").compute(0);
                    cache.put(arg, res);
                }
                return cache.get(arg);
            }
        };
        // тестовое вычисление
        res.compute(0.0, 0.0, 0.0);

        // код, полезный для тестирования преобразования Фурье
//        res = new Function<Complex>() {
//
//            @Override
//            public Complex compute(double... x) {
//                return At.compute(x[0]).multiply(As.compute(x[1]));
//            }
//        };
//        res.compute(0.0, 0.0, 0.0);
        System.out.println("counters[f1]: " + counters[0]
                + "; counters[f0]: " + counters[1]
                + "; counters[res]: " + counters[2]);

        return res;
    }

}
