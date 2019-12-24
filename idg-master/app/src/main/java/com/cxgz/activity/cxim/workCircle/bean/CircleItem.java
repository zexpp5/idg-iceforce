package com.cxgz.activity.cxim.workCircle.bean;

import android.text.TextUtils;

import com.cxgz.activity.cxim.bean.TuiGuangBean;
import com.entity.administrative.employee.Annexdata;

import java.util.List;


public class CircleItem extends BaseBean
{
    public final static String TYPE_URL = "1";
    public final static String TYPE_IMG = "2";
    public final static String TYPE_VIDEO = "3";

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String content;
    private String createTime;
    private String type;//1:链接  2:图片 3:视频
    private String linkImg;
    private String linkTitle;
    private List<String> photos;
    private List<FavortItem> favorters;
    private List<CommentItem> comments;
    //新增附件列表
    private List<Annexdata> annexList;
    //新增推广地址列表
    private TuiGuangBean tuiGuangBean;

    private User user;
    private String videoUrl;
    private String videoImgUrl;

    private String bgImgUrl;

    private boolean isExpand;

    private String contentTitle;

    private String startDate;
    private String endDate;

    //金额
    private double money;

    private int total;

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public double getMoney()
    {
        return money;
    }

    public void setMoney(double money)
    {
        this.money = money;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public TuiGuangBean getTuiGuangBean()
    {
        return tuiGuangBean;
    }

    public void setTuiGuangBean(TuiGuangBean tuiGuangBean)
    {
        this.tuiGuangBean = tuiGuangBean;
    }

    public String getBgImgUrl()
    {
        return bgImgUrl;
    }

    public void setBgImgUrl(String bgImgUrl)
    {
        this.bgImgUrl = bgImgUrl;
    }

    public String getContentTitle()
    {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle)
    {
        this.contentTitle = contentTitle;
    }

    public List<Annexdata> getAnnexList()
    {
        return annexList;
    }

    public void setAnnexList(List<Annexdata> annexList)
    {
        this.annexList = annexList;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public List<FavortItem> getFavorters()
    {
        return favorters;
    }

    public void setFavorters(List<FavortItem> favorters)
    {
        this.favorters = favorters;
    }

    public List<CommentItem> getComments()
    {
        return comments;
    }

    public void setComments(List<CommentItem> comments)
    {
        this.comments = comments;
    }

    public String getLinkImg()
    {
        return linkImg;
    }

    public void setLinkImg(String linkImg)
    {
        this.linkImg = linkImg;
    }

    public String getLinkTitle()
    {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle)
    {
        this.linkTitle = linkTitle;
    }

    public List<String> getPhotos()
    {
        return photos;
    }

    public void setPhotos(List<String> photos)
    {
        this.photos = photos;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getVideoUrl()
    {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl)
    {
        this.videoUrl = videoUrl;
    }

    public String getVideoImgUrl()
    {
        return videoImgUrl;
    }

    public void setVideoImgUrl(String videoImgUrl)
    {
        this.videoImgUrl = videoImgUrl;
    }

    public void setExpand(boolean isExpand)
    {
        this.isExpand = isExpand;
    }

    public boolean isExpand()
    {
        return this.isExpand;
    }

    public boolean hasFavort()
    {
        if (favorters != null && favorters.size() > 0)
        {
            return true;
        }
        return false;
    }

    public boolean hasComment()
    {
        if (comments != null && comments.size() > 0)
        {
            return true;
        }
        return false;
    }

    public String getCurUserFavortId(String curUserId)
    {
        String favortid = "";
        if (!TextUtils.isEmpty(curUserId) && hasFavort())
        {
            for (FavortItem item : favorters)
            {
                if (curUserId.equals(item.getUser().getId()))
                {
                    favortid = item.getId();
                    return favortid;
                }
            }
        }
        return favortid;
    }
}
