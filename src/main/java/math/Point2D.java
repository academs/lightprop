package math;

public class Point2D {

    private final double x;
    private final double t;

    public Point2D(double x, double t) {
        this.x = x;
        this.t = t;
    }

    public double getX() {
        return x;
    }

    public double getT() {
        return t;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.t) ^ (Double.doubleToLongBits(this.t) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point2D other = (Point2D) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.t) != Double.doubleToLongBits(other.t)) {
            return false;
        }
        return true;
    }

}
