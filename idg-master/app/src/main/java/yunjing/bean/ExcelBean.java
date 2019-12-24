package yunjing.bean;

/**
 * Created by tujingwu on 2017/8/10.
 * 可以上下左右滚动的表格bean
 */

public class ExcelBean {
    private String contentL0, contentL1, contentL2;//如果是树形状态，代表展开等级对应的内容，否则默认一级
    private boolean openL0, openL1;//是否展开1级、2级
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

    public String getContentL0() {
        return contentL0;
    }

    public void setContentL0(String contentL0) {
        this.contentL0 = contentL0;
    }

    public String getContentL1() {
        return contentL1;
    }

    public void setContentL1(String contentL1) {
        this.contentL1 = contentL1;
    }

    public String getContentL2() {
        return contentL2;
    }

    public void setContentL2(String contentL2) {
        this.contentL2 = contentL2;
    }

    public boolean isOpenL0() {
        return openL0;
    }

    public void setOpenL0(boolean openL0) {
        this.openL0 = openL0;
    }

    public boolean isOpenL1() {
        return openL1;
    }

    public void setOpenL1(boolean openL1) {
        this.openL1 = openL1;
    }
}
