package math;

import java.util.HashMap;
import java.util.Map;

public class CachedFunction<T> implements Function<T> {

    private final Function<T> function;
    
    private final Map<Double, T> cache = new HashMap<>();

    public CachedFunction(Function<T> function) {
        this.function = function;
    }

    @Override
    public T compute(double... x) {
        if(x.length != 1) {
            return function.compute(x);
        }
        T value = cache.get(x[0]);
        if(value == null) {
            value = function.compute(x);
            cache.put(x[0], value);
        }
        return value;
    }

}
