package NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class FileTask {
    List<byte[]> data = new ArrayList<>();
    String fileName;
    String filePath;
    int id;

    public FileTask(String fileName, String filePath, int id) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.id = id;
    }

    public void addData(byte[] data) {
        this.data.add(data);
    }

    public List<byte[]> getData() {
        return data;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getId() {
        return id;
    }
}
