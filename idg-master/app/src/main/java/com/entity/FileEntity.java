package com.entity;



import com.cxgz.activity.db.dao.BaseUserName;
import com.chaoxiang.base.config.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/15.
 */
public class FileEntity extends BaseUserName
{
    protected List<SDFileListEntity> annex;

    private List<SDFileListEntity> fileAnnex ;
    private List<SDFileListEntity> imgAnnex;
    private List<SDFileListEntity> voiceAnnex ;

    public List<SDFileListEntity> getFileAnnex() {
        setFiles();
        return fileAnnex;
    }

    public void setFileAnnex(List<SDFileListEntity> fileAnnex) {
        this.fileAnnex = fileAnnex;
    }

    public List<SDFileListEntity> getAnnex() {
        return annex;
    }

    public void setAnnex(List<SDFileListEntity> annex) {
        this.annex = annex;
    }

    public List<SDFileListEntity> getImgAnnex() {
            setFiles();
        return imgAnnex;
    }

    public void setImgAnnex(List<SDFileListEntity> imgAnnex) {
        this.imgAnnex = imgAnnex;
    }

    public List<SDFileListEntity> getVoiceAnnex() {
            setFiles();
        return voiceAnnex;
    }

    public void setVoiceAnnex(List<SDFileListEntity> voiceAnnex) {
        this.voiceAnnex = voiceAnnex;
    }

    /**
     * 设置文件
     */
    public void setFiles() {
        fileAnnex = new ArrayList<>();
        imgAnnex = new ArrayList<>();
        voiceAnnex = new ArrayList<>();
        for(int i=0;i<annex.size();i++) {
            SDFileListEntity ans = annex.get(i);
            SDFileListEntity entity = new SDFileListEntity();
            entity.setAnnexWay(ans.getAnnexWay());

            entity.setAndroidFilePath(ans.getAndroidFilePath());
            entity.setFileSize(ans.getFileSize());
            entity.setId(ans.getId());
            entity.setType(ans.getType());
            entity.setPath(ans.getPath());
            String srcName = annex.get(i).getSrcName();
            if(annex.get(i).getSrcName().endsWith( Constants.IMAGE_PREFIX)) {

                srcName = srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX));

                entity.setSrcName(srcName);

                imgAnnex.add(entity);
            } else if(annex.get(i).getSrcName().endsWith(Constants.RADIO_PREFIX)) {
                srcName = srcName.substring(0, srcName.indexOf(Constants.RADIO_PREFIX));
                entity.setSrcName(srcName);
                voiceAnnex.add(entity);
            } else {
                entity.setSrcName(srcName);
                fileAnnex.add(entity);
            }
        }
    }

}
