package sample.elasticclient;

import com.opencsv.bean.CsvBindByPosition;

public class DataElement {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @CsvBindByPosition(position = 0)
    private String id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @CsvBindByPosition(position = 1)
    private String text;
}
