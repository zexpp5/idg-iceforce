package newProject.view;

/**
 * Created by selson on 2018/4/27.
 */

public class DialogTextFilter
{
    String titleString;
    String contentString;
    String yesString;
    String noString;
    String yesColor;
    String noColor;
    String projName;
    String projFileType;
    boolean isNeedBottom;
    int haveBtn;

    public String getProjName()
    {
        return projName;
    }

    public void setProjName(String projName)
    {
        this.projName = projName;
    }

    public String getProjFileType()
    {
        return projFileType;
    }

    public void setProjFileType(String projFileType)
    {
        this.projFileType = projFileType;
    }

    public int getHaveBtn()
    {
        return haveBtn;
    }

    public void setHaveBtn(int haveBtn)
    {
        this.haveBtn = haveBtn;
    }

    public boolean isNeedBottom()
    {
        return isNeedBottom;
    }

    public void setNeedBottom(boolean needBottom)
    {
        isNeedBottom = needBottom;
    }

    public String getTitleString()
    {
        return titleString;
    }

    public void setTitleString(String titleString)
    {
        this.titleString = titleString;
    }

    public String getContentString()
    {
        return contentString;
    }

    public void setContentString(String contentString)
    {
        this.contentString = contentString;
    }

    public String getYesString()
    {
        return yesString;
    }

    public void setYesString(String yesString)
    {
        this.yesString = yesString;
    }

    public String getNoString()
    {
        return noString;
    }

    public void setNoString(String noString)
    {
        this.noString = noString;
    }

    public String getYesColor()
    {
        return yesColor;
    }

    public void setYesColor(String yesColor)
    {
        this.yesColor = yesColor;
    }

    public String getNoColor()
    {
        return noColor;
    }

    public void setNoColor(String noColor)
    {
        this.noColor = noColor;
    }
}
