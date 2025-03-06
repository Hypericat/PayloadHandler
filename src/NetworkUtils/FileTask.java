package NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class FileTask {
    List<byte[]> data = new ArrayList<>();
    String fileDst;
    String fileSrc;
    int id;

    public FileTask(String fileSrc, String fileDst, int id) {
        this.fileDst = fileDst;
        this.fileSrc = fileSrc;
        this.id = id;
    }

    public void addData(byte[] data) {
        this.data.add(data);
    }

    public List<byte[]> getData() {
        return data;
    }

    public String getFileDst() {
        return fileDst;
    }

    public String getFileSrc() {
        return fileSrc;
    }

    public int getId() {
        return id;
    }
}
