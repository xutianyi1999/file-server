package common.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

    private Type type;
    private String path;
    private FileBlock fileBlock;
    private ArrayList<FileInfo> fileInfoList;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileBlock getFileBlock() {
        return fileBlock;
    }

    public void setFileBlock(FileBlock fileBlock) {
        this.fileBlock = fileBlock;
    }

    public ArrayList<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(ArrayList<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }
}
