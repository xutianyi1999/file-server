package common.entity;

import java.io.Serializable;

public class Message implements Serializable {

    private Type type;
    private String path;
    private FileBlock fileBlock;
    private String[] fileList;
    private Code code;

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

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

    public String[] getFileList() {
        return fileList;
    }

    public void setFileList(String[] fileList) {
        this.fileList = fileList;
    }
}
