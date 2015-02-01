package problem;


import converter.DoubleConverter;
import converter.NonNegativeDoubleConverter;
import problem.Parameter;
import converter.PositiveIntegerConverter;
import math.CachedFunction;
import math.Function;
import org.apache.commons.math3.complex.Complex;

public class ProblemParams {

    @Parameter(name = "k0",
            converter = DoubleConverter.class)
    private double k0 = 0.0;
    @Parameter(name = "dk",
            converter = DoubleConverter.class)
    private double dk = 100;
    @Parameter(name = "tau",
            converter = NonNegativeDoubleConverter.class)
    private double tau = 1.0;
    @Parameter(name = "a",
            converter = NonNegativeDoubleConverter.class)
    private double a = 2.0;
    @Parameter(name = "z",
            converter = NonNegativeDoubleConverter.class)
    private double z = 0.0;
    @Parameter(name = "x0 для графика",
            converter = DoubleConverter.class)
    private double x0 = -1.0;
    @Parameter(name = "x1 для графика",
            converter = DoubleConverter.class)
    private double x1 = 1.0;
    @Parameter(name = "t0 для графика",
            converter = DoubleConverter.class)
    private double t0 = 0.0;
    @Parameter(name = "t1 для графика",
            converter = DoubleConverter.class)
    private double t1 = 4.0;
    @Parameter(name = "Nx",
            converter = PositiveIntegerConverter.class)
    private int Nx = 50;
    @Parameter(name = "Nt",
            converter = PositiveIntegerConverter.class)
    private int Nt = 50;
    @Parameter(name = "Шаг интегрирования Nx",
            converter = PositiveIntegerConverter.class)
    private int integrateNx = 50;
    @Parameter(name = "Шаг интегрирования Nt",
            converter = PositiveIntegerConverter.class)
    private int integrateNt = 50;
    @Parameter(name = "Шаг интегрирования Nk",
            converter = PositiveIntegerConverter.class)
    private int integrateNk = 100;
    @Parameter(name = "Шаг интегрирования Nα",
            converter = PositiveIntegerConverter.class)
    private int integrateNa = 50;

    public Function<Complex> timeComponent = new CachedFunction<>(new Function<Complex>() {

        @Override
        public Complex compute(double... x) {
            return Complex.ONE;
        }
    });

    public Function<Complex> spaceComponent = new CachedFunction<>(new Function<Complex>() {

        @Override
        public Complex compute(double... x) {
            return Complex.ONE;
        }
    });

    public double getZ() {
        return z;
    }

    public double getK0() {
        return k0;
    }

    public double getDk() {
        return dk;
    }

    public double getTau() {
        return tau;
    }

    public double getA() {
        return a;
    }

    public double getX0() {
        return x0;
    }

    public double getX1() {
        return x1;
    }

    public double getT0() {
        return t0;
    }

    public double getT1() {
        return t1;
    }

    public int getNx() {
        return Nx;
    }

    public int getNt() {
        return Nt;
    }

    public int getIntegrateNx() {
        return integrateNx;
    }

    public int getIntegrateNt() {
        return integrateNt;
    }

    public int getIntegrateNk() {
        return integrateNk;
    }

    public int getIntegrateNa() {
        return integrateNa;
    }

    public Function<Complex> getTimeComponent() {
        return timeComponent;
    }

    public Function<Complex> getSpaceComponent() {
        return spaceComponent;
    }

}
