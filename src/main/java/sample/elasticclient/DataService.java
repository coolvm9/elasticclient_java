package sample.elasticclient;

import java.io.IOException;

public interface DataService {
    DataElement findById(String id) throws IOException;

    DataPage<DataElement> search(String query) throws IOException;

    DataPage<DataElement> next(DataPage page) throws IOException;

    void save(DataElement product) throws IOException;

}
