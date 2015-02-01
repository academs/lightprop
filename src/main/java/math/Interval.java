package math;

public final class Interval {

    private final double start;
    private final double end;
    private final int N;

    public Interval(double start, double end, int N) {
        if (start > end) {
            throw new IllegalArgumentException("error start(" + start + ") > end(" + end + ")");
        }
        if (N <= 0) {
            throw new IllegalArgumentException("N must be >0");
        }
        this.start = start;
        this.end = end;
        this.N = N;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public int getN() {
        return N;
    }

    public double getStep() {
        return (end - start) / N;
    }

}
