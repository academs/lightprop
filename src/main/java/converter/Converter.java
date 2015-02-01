package converter;

public interface Converter {

    String asString(Object o);

    Object asObject(String str) throws IllegalArgumentException;
}
