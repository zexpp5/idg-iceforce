package newProject.company.project_manager.investment_project.voice;

import java.io.File;
import java.io.Serializable;

/**
 * Created by zsz on 2019/5/15.
 */

public class VoiceListBean implements Serializable {
    private File file;
    private int second;
    private String url;

    public VoiceListBean(File file, int second) {
        this.file = file;
        this.second = second;
    }

    public VoiceListBean(int second, String url) {
        this.second = second;
        this.url = url;
    }

    public VoiceListBean(File file, int second, String url) {
        this.file = file;
        this.second = second;
        this.url = url;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
