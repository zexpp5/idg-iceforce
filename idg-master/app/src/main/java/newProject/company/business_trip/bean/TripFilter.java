package newProject.company.business_trip.bean;

/**
 * Created by selson on 2018/6/6.
 */

public class TripFilter
{
    private String tripType;
    private String remark;
    private String startDate;
    private String endDate;
    private String budget;
    private String startCity;
    private String targetCitys;
    private String apply;

    public String getApply()
    {
        return apply;
    }

    public void setApply(String apply)
    {
        this.apply = apply;
    }

    public String getTripType()
    {
        return tripType;
    }

    public void setTripType(String tripType)
    {
        this.tripType = tripType;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
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

    public String getBudget()
    {
        return budget;
    }

    public void setBudget(String budget)
    {
        this.budget = budget;
    }

    public String getStartCity()
    {
        return startCity;
    }

    public void setStartCity(String startCity)
    {
        this.startCity = startCity;
    }

    public String getTargetCitys()
    {
        return targetCitys;
    }

    public void setTargetCitys(String targetCitys)
    {
        this.targetCitys = targetCitys;
    }
}
