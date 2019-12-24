package yunjing.http;

import com.entity.administrative.employee.Annexdata;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class FileSubmitBean implements Serializable {
    private int status;
    private List<Annexdata> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Annexdata> getData() {
        return data;
    }

    public void setData(List<Annexdata> data) {
        this.data = data;
    }
}
