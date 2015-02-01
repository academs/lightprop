package math;

public enum ComplexPlotMode {

    IM("Мнимая часть"),
    RE("Действительная часть"),
    ABS("Абсолютное значение");

    private final String name;

    private ComplexPlotMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
