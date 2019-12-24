package yunjing.bean;

/**
 * Created by Administrator on 2017/8/15.
 */

public class TableBean {
    private long eid;
    private String content;
    private boolean pic,voice,file;//用于附件

    public boolean isPic() {
        return pic;
    }

    public void setPic(boolean pic) {
        this.pic = pic;
    }

    public boolean isVoice() {
        return voice;
    }

    public void setVoice(boolean voice) {
        this.voice = voice;
    }

    public boolean isFile() {
        return file;
    }

    public void setFile(boolean file) {
        this.file = file;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
