package sample.elasticclient;
import java.util.Collections;
import java.util.List;

public class DataPage<T> {

    public static final DataPage EMPTY = new DataPage(Collections.emptyList(), null, 0, 0);

    private final List<T> products;
    private final String input;
    private final int from;
    private final int size;

    public DataPage(List<T> dataElements, String input, int from, int size) {
        this.products = dataElements;
        this.input = input;
        this.from = from;
        this.size = size;
    }

    List<T> get() {
        return Collections.unmodifiableList(products);
    }

    public String getInput() {
        return input;
    }

    public int getFrom() {
        return from;
    }

    public int getSize() {
        return size;
    }
}