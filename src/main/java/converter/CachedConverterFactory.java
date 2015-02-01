package converter;

import java.util.HashMap;
import java.util.Map;

public class CachedConverterFactory {

    private static final Map<Class<? extends Converter>, Converter> cache
            = new HashMap<Class<? extends Converter>, Converter>();

    public static Converter newConveter(Class<? extends Converter> clazz)
            throws InstantiationException, IllegalAccessException {
        if (!cache.containsKey(clazz)) {
            cache.put(clazz, clazz.newInstance());
        }
        return cache.get(clazz);
    }

}
