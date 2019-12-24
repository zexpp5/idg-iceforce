package newProject.utils;

import android.content.Context;

import com.chaoxiang.base.utils.MyToast;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanIceProject;
import newProject.company.invested_project.bean.City;
import newProject.company.invested_project.bean.Province;
import newProject.company.invested_project.bean.TypeBean;
import newProject.company.project_manager.investment_project.bean.GroupListBean;

/**
 * Created by selson on 2019/5/13.
 */

public class HttpHelperUtils
{
    private HttpHelperUtils()
    {
    }

    public static class HttpHelperUtilsHelper
    {
        private static final HttpHelperUtils httpHtlper = new HttpHelperUtils();
    }

    public static synchronized HttpHelperUtils getInstance()
    {
        return HttpHelperUtilsHelper.httpHtlper;
    }

    //返回字典所需
    public void getType(final Context mContext, boolean isNeedPd, String typeString, final InputListener mListener)
    {
        final List<TypeBean.DataBeanX.DataBean> dataBeanList = new ArrayList<>();
        final List<BeanIceProject> icApprovedList = new ArrayList<>();
        ListHttpHelper.getProjectType(mContext, isNeedPd, typeString, new SDRequestCallBack(TypeBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(mContext, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                TypeBean typeBean = (TypeBean) responseInfo.getResult();
                if (typeBean != null && typeBean.getStatus() == 200)
                {
                    dataBeanList.clear();
                    dataBeanList.addAll(typeBean.getData().getData());
                    for (int i = 0; i < dataBeanList.size(); i++)
                    {
                        icApprovedList.add(new BeanIceProject(i, dataBeanList.get(i).getCodeKey(), dataBeanList.get(i)
                                .getCodeNameZhCn()));
                    }
                    mListener.onData(icApprovedList);
                }
            }
        });
    }

    //获取城市需要的字典
    public void getCityType(final Context mContext, boolean isNeedPd, String typeString, final InputListener2 mListener)
    {
        final List<TypeBean.DataBeanX.DataBean> dataBeanList = new ArrayList<>();
        final List<Province> provinceList = new ArrayList<>();
        ListHttpHelper.getProjectType(mContext, isNeedPd, typeString, new SDRequestCallBack(TypeBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(mContext, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                TypeBean typeBean = (TypeBean) responseInfo.getResult();
                if (typeBean != null && typeBean.getStatus() == 200)
                {
                    dataBeanList.clear();
                    dataBeanList.addAll(typeBean.getData().getData());
                    for (int i = 0; i < dataBeanList.size(); i++)
                    {
                        Province province = new Province();
                        province.id = Integer.parseInt(dataBeanList.get(i).getCodeKey());
                        province.name = dataBeanList.get(i).getCodeNameZhCn();
                        province.citys = new ArrayList<>();
                        for (int j = 0; j < dataBeanList.get(i).getChildren().size(); j++)
                        {
                            City city = new City();
                            city.id = Integer.parseInt(dataBeanList.get(i).getChildren().get(j).getCodeKey());
                            city.name =dataBeanList.get(i).getChildren().get(j).getCodeNameZhCn();
                            province.citys.add(city);
                        }
                        provinceList.add(province);
                    }
                    mListener.onData(provinceList);
                }
            }
        });
    }

    //群组
    public void getGroupListType(final Context mContext, final InputListener mListener)
    {
        final List<GroupListBean.DataBeanX.DataBean> groupLists = new ArrayList<>();
        final List<BeanIceProject> icApprovedList = new ArrayList<>();
        ListHttpHelper.getGroupListData(mContext, new SDRequestCallBack(GroupListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                GroupListBean bean = (GroupListBean) responseInfo.getResult();
                if (bean != null && bean.getData() != null)
                {
                    if (bean.getData().getData() != null && bean.getData().getData().size() > 0)
                    {
                        groupLists.clear();
                        groupLists.addAll(bean.getData().getData());
                        for (int i = 0; i < groupLists.size(); i++)
                        {
                            icApprovedList.add(new BeanIceProject(i, groupLists.get(i).getDeptId(), groupLists.get(i)
                                    .getDeptName()));
                        }
                    }
                }
                mListener.onData(icApprovedList);
            }
        });
    }

    public interface InputListener
    {
        void onData(List<BeanIceProject> icApprovedList);
    }

    public interface InputListener2
    {
        void onData(List<Province> provinceList);
    }

    private InputListener mListener;

    public void setInputListener(InputListener listener)
    {
        this.mListener = listener;
    }
}
