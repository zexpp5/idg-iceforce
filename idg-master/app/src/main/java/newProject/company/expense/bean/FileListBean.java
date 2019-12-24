package newProject.company.expense.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * author: Created by aniu on 2018/6/20.
 */

public class FileListBean implements Parcelable{
    private String fileName;
    private String fileUrl;
    @SerializedName("id")
    private String idX;
    private String type;

    protected FileListBean(Parcel in) {
        fileName = in.readString();
        fileUrl = in.readString();
        idX = in.readString();
        type = in.readString();
    }

    public static final Creator<FileListBean> CREATOR = new Creator<FileListBean>() {
        @Override
        public FileListBean createFromParcel(Parcel in) {
            return new FileListBean(in);
        }

        @Override
        public FileListBean[] newArray(int size) {
            return new FileListBean[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getIdX() {
        return idX;
    }

    public void setIdX(String idX) {
        this.idX = idX;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(fileUrl);
        dest.writeString(idX);
        dest.writeString(type);
    }
}
