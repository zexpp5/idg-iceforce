package newProject.company.invested_project.editinfo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import org.jaaksi.pickerview.dataset.OptionDataSet;
import org.jaaksi.pickerview.picker.BasePicker;
import org.jaaksi.pickerview.picker.OptionPicker;
import org.jaaksi.pickerview.widget.DefaultCenterDecoration;
import org.jaaksi.pickerview.widget.PickerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanBaseInfo;
import newProject.company.invested_project.bean.BeanIceProject;
import newProject.company.invested_project.bean.BeanPostBaseInfo;
import newProject.company.invested_project.bean.City;
import newProject.company.invested_project.bean.Province;
import newProject.company.project_manager.investment_project.PublicUserListActivity;
import newProject.company.project_manager.investment_project.bean.NewGroupListBean;
import newProject.company.project_manager.investment_project.bean.PPIndustryListBean;
import newProject.company.project_manager.investment_project.bean.PublicUserListBean;
import newProject.utils.HttpHelperUtils;
import newProject.view.CustomTopBar;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.utils.BaseDialogUtils;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogFragmentProject;
import yunjing.view.StatusTipsView;


/**
 * Created by selson on 2019/5/7.
 * ice-项目-基础资料-编辑
 */
public class ActivityBaseInfo extends BaseActivity
{
    @Bind(R.id.tv_project_name)
    FontEditext tvProjectName;
    @Bind(R.id.tv_city)
    FontTextView tvCity;
    @Bind(R.id.tv_industry_classification)
    FontTextView tvIndustryClassification;
    @Bind(R.id.tv_company_people_number)
    FontEditext tvCompanyPeopleNumber;
    @Bind(R.id.tv_industry_group)
    FontTextView tvIndustryGroup;
    @Bind(R.id.tv_partner)
    FontTextView tvPartner;
    @Bind(R.id.tv_project_preside)
    FontTextView tvProjectPreside;
    @Bind(R.id.tv_group_number)
    FontTextView tvGroupNumber;
    @Bind(R.id.tv_contributer)
    FontTextView tvContributer;
    @Bind(R.id.tv_investigate_preside)
    FontTextView tvInvestigatePreside;
    @Bind(R.id.tv_financing_round)
    FontEditext tvFinancingRound;
    @Bind(R.id.tv_project_lawyer)
    FontTextView tvProjectLawyer;
    @Bind(R.id.tv_source_person)
    FontEditext tvSourcePerson;
    @Bind(R.id.tv_source_way)
    FontTextView tvSourceWay;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.view_loading)
    StatusTipsView viewLoading;
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.ll_save)
    LinearLayout llSave;
    @Bind(R.id.tv_director_has)
    FontTextView tvDirectorHas;
    @Bind(R.id.tv_director_info)
    FontTextView tvDirectorInfo;
    @Bind(R.id.tv_observer)
    FontTextView tvObserver;
    @Bind(R.id.tv_esg)
    FontTextView tvEsg;

    private String mEid = "";
    private String userName = "";

    private OptionPicker mPicker;
    //负责合伙人
    private int mRequestCode_partners = 101;
    private List<PublicUserListBean.DataBeanX.DataBean> partnersLists = new ArrayList<>();
    private int mRequestCode_projManagers = 102;
    private List<PublicUserListBean.DataBeanX.DataBean> projManagersLists = new ArrayList<>();
    private int mRequestCode_projTeams = 103;
    private List<PublicUserListBean.DataBeanX.DataBean> projTeamsLists = new ArrayList<>();
    private int mRequestCode_contributer = 104;
    private List<PublicUserListBean.DataBeanX.DataBean> contributerLists = new ArrayList<>();
    private int mRequestCode_projDDManagers = 105;
    private List<PublicUserListBean.DataBeanX.DataBean> projDDManagersLists = new ArrayList<>();
    private int mRequestCode_projLawyers = 106;
    private List<PublicUserListBean.DataBeanX.DataBean> projLawyersLists = new ArrayList<>();

    private int mRequestCode_projDirectors= 107;
    private List<PublicUserListBean.DataBeanX.DataBean> projDirectorList = new ArrayList<>();

    private int mRequestCode_projObservers= 108;
    private List<PublicUserListBean.DataBeanX.DataBean> projObserversList= new ArrayList<>();


    private String provinceId = "", cityId = "";

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_info_base_main;
    }

    BeanBaseInfo.DataBeanX.DataBean baseInfoBean = null;

    @Override
    protected void init()
    {
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mEid = bundle.getString(Constants.PROJECT_EID, "");
            baseInfoBean = (BeanBaseInfo.DataBeanX.DataBean) bundle.getSerializable("baseInfoBean");
        }
        if (baseInfoBean != null)
        {
            setBaseInfo(baseInfoBean);
        }

        tvSave.setText("完成");
        titleBar.setMidText("基础资料编辑");
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        getCityData();
    }

    private String projName = "";
    private String projId = "";
    private String projNameEn = "";
    private String inDate = "";
    private String comIndu = "";
    private String headCity = "";
    private String comPhase = "";
    private String currentRound = "";
    private String projManagers = "";  //项目负责人
    private String projPartners = "";
    private String projSour = "";
    private String projTeams = "";
    private String projSourPer = "";
    private String projDDManagers = "";
    private String indusGroup = "";
    private String seatFlag = "";
    private String projDirectors = "";
    private String projObservers = "";
    private String projLawyers = "";
    private String headCount = "";
    private String esgFlag = "";
    private String stsId = "";
    private String contributer = "";

    private void postAjudgeBaseInfo()
    {
        projName = tvProjectName.getText().toString();
        headCity = cityId;
        projId = mEid;
        userName = loginUserAccount;
        //行业分类已有
        headCount = tvCompanyPeopleNumber.getText().toString();//人数
        //行业小组已有
        if (partnersLists.size() > 0) //负责合伙人
        {
            StringBuffer sbPr = new StringBuffer();
            for (int i = 0; i < partnersLists.size(); i++)
                sbPr.append(partnersLists.get(i).getUserId() + ",");
            projPartners = sbPr.substring(0, sbPr.length() - 1);
        }

        if (projManagersLists.size() > 0) //项目负责人
        {
            StringBuffer sbPr = new StringBuffer();
            for (int i = 0; i < projManagersLists.size(); i++)
                sbPr.append(projManagersLists.get(i).getUserId() + ",");
            projManagers = sbPr.substring(0, sbPr.length() - 1);
        }

        if (projTeamsLists.size() > 0) //小组成员
        {
            StringBuffer sbPr = new StringBuffer();
            for (int i = 0; i < projTeamsLists.size(); i++)
                sbPr.append(projTeamsLists.get(i).getUserId() + ",");
            projTeams = sbPr.substring(0, sbPr.length() - 1);
        }

        if (contributerLists.size() > 0) //contributer
        {
            StringBuffer sbPr = new StringBuffer();
            for (int i = 0; i < contributerLists.size(); i++)
                sbPr.append(contributerLists.get(i).getUserId() + ",");
            contributer = sbPr.substring(0, sbPr.length() - 1);
        }

        if (projDDManagersLists.size() > 0) //尽调负责人
        {
            StringBuffer sbPr = new StringBuffer();
            for (int i = 0; i < projDDManagersLists.size(); i++)
                sbPr.append(projDDManagersLists.get(i).getUserId() + ",");
            projDDManagers = sbPr.substring(0, sbPr.length() - 1);
        }
        if (projLawyersLists.size() > 0) //律师
        {
            StringBuffer sbPr = new StringBuffer();
            for (int i = 0; i < projLawyersLists.size(); i++)
                sbPr.append(projLawyersLists.get(i).getUserId() + ",");
            projLawyers = sbPr.substring(0, sbPr.length() - 1);
        }
        if (projDirectorList.size() > 0) //
        {
            StringBuffer sbPr = new StringBuffer();
            for (int i = 0; i < projDirectorList.size(); i++)
                sbPr.append(projDirectorList.get(i).getUserId() + ",");
            projDirectors = sbPr.substring(0, sbPr.length() - 1);
        }

        if (projObserversList.size() > 0) //
        {
            StringBuffer sbPr = new StringBuffer();
            for (int i = 0; i < projObserversList.size(); i++)
                sbPr.append(projObserversList.get(i).getUserId() + ",");
            projObservers = sbPr.substring(0, sbPr.length() - 1);
        }

        currentRound = tvFinancingRound.getText().toString();//融资轮次
        projSourPer = tvSourcePerson.getText().toString();

        BeanPostBaseInfo beanPostBaseInfo = new BeanPostBaseInfo();

        beanPostBaseInfo.setProjName(projName);
        beanPostBaseInfo.setProjId(projId);
        beanPostBaseInfo.setProjNameEn(projNameEn);
        beanPostBaseInfo.setInDate(inDate);
        beanPostBaseInfo.setComIndu(comIndu);
        beanPostBaseInfo.setHeadCity(headCity);
        beanPostBaseInfo.setComPhase(comPhase);
        beanPostBaseInfo.setCurrentRound(currentRound);
        beanPostBaseInfo.setProjManagers(projManagers);
        beanPostBaseInfo.setProjPartners(projPartners);
        beanPostBaseInfo.setProjSour(projSour);
        beanPostBaseInfo.setProjTeams(projTeams);
        beanPostBaseInfo.setProjSourPer(projSourPer);
        beanPostBaseInfo.setProjDDManagers(projDDManagers);
        beanPostBaseInfo.setIndusGroup(indusGroup);
        beanPostBaseInfo.setSeatFlag(seatFlag);
        beanPostBaseInfo.setProjDirectors(projDirectors);
        beanPostBaseInfo.setProjObservers(projObservers);
        beanPostBaseInfo.setProjLawyers(projLawyers);
        beanPostBaseInfo.setHeadCount(headCount);
        beanPostBaseInfo.setEsgFlag(esgFlag);
        beanPostBaseInfo.setStsId(stsId);
        beanPostBaseInfo.setContributer(contributer);
        beanPostBaseInfo.setUsername(userName);

        if (StringUtils.empty(projName))
        {
            MyToast.showToast(ActivityBaseInfo.this, getResources().getString(R.string.hint_iceforce_baseInfo1));
            return;
        }
        if (StringUtils.empty(projId))
        {
            MyToast.showToast(ActivityBaseInfo.this, getResources().getString(R.string.hint_iceforce_baseInfo2));
            return;
        }
        if (StringUtils.empty(comIndu))
        {
            MyToast.showToast(ActivityBaseInfo.this, getResources().getString(R.string.hint_iceforce_baseInfo3));
            return;
        }
        if (StringUtils.empty(indusGroup))
        {
            MyToast.showToast(ActivityBaseInfo.this, getResources().getString(R.string.hint_iceforce_baseInfo4));
            return;
        }
        postBaseInfo(beanPostBaseInfo);
    }

    private void postBaseInfo(BeanPostBaseInfo beanPostBaseInfo)
    {
        ListHttpHelper.postEdtBaseInfo(ActivityBaseInfo.this, SDGson.toJson(beanPostBaseInfo), new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(ActivityBaseInfo.this, "修改失败");
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                MyToast.showToast(ActivityBaseInfo.this, "修改成功");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void setBaseInfo(BeanBaseInfo.DataBeanX.DataBean baseInfoBean)
    {
        if (StringUtils.notEmpty(baseInfoBean.getProjName()))
            tvProjectName.setText(baseInfoBean.getProjName());

        if (StringUtils.notEmpty(baseInfoBean.getHeadCityStr()))
            tvCity.setText(baseInfoBean.getHeadCityStr());  //城市。

        if (StringUtils.notEmpty(baseInfoBean.getComInduStr()))
            tvIndustryClassification.setText(baseInfoBean.getComInduStr());

        if (StringUtils.notEmpty(baseInfoBean.getHeadCount()))
            tvCompanyPeopleNumber.setText(baseInfoBean.getHeadCount());

        if (StringUtils.notEmpty(baseInfoBean.getIndusGroupStr()))
            tvIndustryGroup.setText(baseInfoBean.getIndusGroupStr());

        if (StringUtils.notEmpty(baseInfoBean.getProjPartnerNames()))
            tvPartner.setText(baseInfoBean.getProjPartnerNames());

        if (StringUtils.notEmpty(baseInfoBean.getProjManagerNames()))
            tvProjectPreside.setText(baseInfoBean.getProjManagerNames());

        if (StringUtils.notEmpty(baseInfoBean.getProjTeamNames()))
            tvGroupNumber.setText(baseInfoBean.getProjTeamNames()); //小组成员

        if (StringUtils.notEmpty(baseInfoBean.getContributerNames()))
            tvContributer.setText(baseInfoBean.getContributerNames());  //Contributer

        if (StringUtils.notEmpty(baseInfoBean.getProjDDManagerNames()))
            tvInvestigatePreside.setText(baseInfoBean.getProjDDManagerNames()); //尽调负责人

        if (StringUtils.notEmpty(baseInfoBean.getCurrentRound()))
            tvFinancingRound.setText(baseInfoBean.getCurrentRound());    //融资轮次

        if (StringUtils.notEmpty(baseInfoBean.getProjLawyerNames()))
            tvProjectLawyer.setText(baseInfoBean.getProjLawyerNames());

        if (StringUtils.notEmpty(baseInfoBean.getProjSourPer()))
            tvSourcePerson.setText(baseInfoBean.getProjSourPer());

        if (StringUtils.notEmpty(baseInfoBean.getProjSourStr()))
            tvSourceWay.setText(baseInfoBean.getProjSourStr());    //来源渠道

        if (StringUtils.notEmpty(baseInfoBean.getProjDirectorNames()))  //董事席位
            tvDirectorInfo.setText(baseInfoBean.getProjDirectorNames());

        if (StringUtils.notEmpty(baseInfoBean.getProjObserverNames()))  //观察员
            tvObserver.setText(baseInfoBean.getProjObserverNames());

        if (StringUtils.notEmpty(baseInfoBean.getSeatFlag()))
        {
            if (baseInfoBean.getSeatFlag().equalsIgnoreCase("Y"))
                tvDirectorHas.setText("是");
            else
                tvDirectorHas.setText("否");
        } else
        {

        }

        if (StringUtils.notEmpty(baseInfoBean.getEsgFlag()))
        {
            if (baseInfoBean.getEsgFlag().equalsIgnoreCase("Y"))
                tvEsg.setText("是");
            else
                tvEsg.setText("否");
        } else
        {

        }

        projName = baseInfoBean.getProjName();
        projId = baseInfoBean.getProjId();
        projNameEn = baseInfoBean.getProjNameEn();
//        inDate = baseInfoBean.;
        comIndu = baseInfoBean.getComIndu();
        headCity = baseInfoBean.getHeadCity();
        comPhase = baseInfoBean.getComPhase();
        currentRound = baseInfoBean.getCurrentRound();
        projManagers = baseInfoBean.getProjManagers();  //项目负责人
        projPartners = baseInfoBean.getProjPartners();
        projSour = baseInfoBean.getProjSour();
        projTeams = baseInfoBean.getProjTeams();
        projSourPer = baseInfoBean.getProjSourPer();
        projDDManagers = baseInfoBean.getProjDDManagers();
        indusGroup = baseInfoBean.getIndusGroup();
        seatFlag = baseInfoBean.getSeatFlag();
        projDirectors = baseInfoBean.getProjDirectors();
        projObservers = baseInfoBean.getProjObservers();
        projLawyers = baseInfoBean.getProjLawyers();
        headCount = baseInfoBean.getHeadCount();
        esgFlag = baseInfoBean.getEsgFlag();
        stsId = baseInfoBean.getStsId();
        contributer = baseInfoBean.getContributer();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_city, R.id.tv_industry_classification, R.id.tv_industry_group, R.id.tv_partner, R.id.tv_project_preside,
            R.id.tv_group_number, R.id.tv_contributer, R.id.tv_investigate_preside, R.id.tv_project_lawyer, R.id
            .tv_source_way, R.id.tv_save, R.id.tv_director_has, R.id.tv_director_info, R.id.tv_observer, R.id.tv_esg})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_city:
                DisplayUtil.hideInputSoft(ActivityBaseInfo.this);
                showCity();
                break;
            case R.id.tv_industry_classification:
                //行业分类
//                ShowIndustryTypeDialog();
                getIndustryData();
                break;
            case R.id.tv_industry_group:
//                ShowIndustryGroupTypeDialog();
                getGroupDataNew();
                break;
            case R.id.tv_partner:
                Intent intent = new Intent(ActivityBaseInfo.this, PublicUserListActivity.class);
                intent.putExtra("userLists", (Serializable) partnersLists);
                intent.putExtra("ids", projPartners);
                intent.putExtra("type", "PARTNER");
                intent.putExtra("mTitle", "负责合伙人");
                startActivityForResult(intent, mRequestCode_partners);
                break;
            case R.id.tv_project_preside:
                Intent intent2 = new Intent(ActivityBaseInfo.this, PublicUserListActivity.class);
                intent2.putExtra("userLists", (Serializable) projManagersLists);
                intent2.putExtra("ids", projManagers);
                intent2.putExtra("mTitle", "项目负责人");
                intent2.putExtra("at", true);
                startActivityForResult(intent2, mRequestCode_projManagers);
                break;
            case R.id.tv_group_number:
                Intent intent3 = new Intent(ActivityBaseInfo.this, PublicUserListActivity.class);
                intent3.putExtra("userLists", (Serializable) projTeamsLists);
                intent3.putExtra("ids", projTeams);
                intent3.putExtra("mTitle", "小组成员");
                intent3.putExtra("at", true);
                startActivityForResult(intent3, mRequestCode_projTeams);
                break;
            case R.id.tv_contributer:
                Intent intent4 = new Intent(ActivityBaseInfo.this, PublicUserListActivity.class);
                intent4.putExtra("userLists", (Serializable) contributerLists);
                intent4.putExtra("ids", contributer);
                intent4.putExtra("mTitle", "Contributer");
                intent4.putExtra("at", true);
                startActivityForResult(intent4, mRequestCode_contributer);
                break;
            case R.id.tv_investigate_preside:
                Intent intent5 = new Intent(ActivityBaseInfo.this, PublicUserListActivity.class);
                intent5.putExtra("userLists", (Serializable) projDDManagersLists);
                intent5.putExtra("ids", projDDManagers);
                intent5.putExtra("type", "DDMANAGER");
                intent5.putExtra("mTitle", "尽调负责人");
                startActivityForResult(intent5, mRequestCode_projDDManagers);
                break;
            case R.id.tv_project_lawyer:
                Intent intent6 = new Intent(ActivityBaseInfo.this, PublicUserListActivity.class);
                intent6.putExtra("userLists", (Serializable) projLawyersLists);
                intent6.putExtra("ids", projLawyers);
                intent6.putExtra("type", "LAWYER");
                intent6.putExtra("mTitle", "项目律师");
                startActivityForResult(intent6, mRequestCode_projLawyers);
                break;
            case R.id.tv_source_way:
                ShowSourceDialog();
                break;
            case R.id.tv_save:
                postAjudgeBaseInfo();
                break;
            //是否席位
            case R.id.tv_director_has:
                showDirector();
                break;
            //董事信息
            case R.id.tv_director_info:
                Intent intent7 = new Intent(ActivityBaseInfo.this, PublicUserListActivity.class);
                intent7.putExtra("userLists", (Serializable) projDirectorList);
                intent7.putExtra("ids", projDirectors);
                intent7.putExtra("type", "");
                intent7.putExtra("mTitle", "董事信息");
                intent7.putExtra("at", true);
                startActivityForResult(intent7, mRequestCode_projDirectors);
                break;
            //观察员
            case R.id.tv_observer:
                Intent intent8 = new Intent(ActivityBaseInfo.this, PublicUserListActivity.class);
                intent8.putExtra("userLists", (Serializable) projObserversList);
                intent8.putExtra("ids", projObservers);
                intent8.putExtra("type", "");
                intent8.putExtra("mTitle", "观察员席位");
                intent8.putExtra("at", true);
                startActivityForResult(intent8, mRequestCode_projObservers);
                break;
            //ESG政策
            case R.id.tv_esg:
                showESG();
                break;
        }
    }

    private void getCityData()
    {
        HttpHelperUtils.getInstance().getCityType(ActivityBaseInfo.this, true, Constants.TYPE_PROJECT_CITY, new HttpHelperUtils
                .InputListener2()
        {
            @Override
            public void onData(List<Province> provinceList)
            {
                final DefaultCenterDecoration decoration = new DefaultCenterDecoration(ActivityBaseInfo.this);
                decoration.setMargin(0, 0, 0, 0);
                mPicker = new OptionPicker.Builder(ActivityBaseInfo.this, 2, new OptionPicker.OnOptionSelectListener()
                {
                    @Override
                    public void onOptionSelect(OptionPicker picker, int[] selectedPosition, OptionDataSet[] selectedOptions)
                    {
                        System.out.println("selectedPosition = " + Arrays.toString(selectedPosition));
                        String text;
                        Province province = (Province) selectedOptions[0];
                        provinceId = province.getValue();
                        City city = (City) selectedOptions[1];
                        if (city == null)
                        {
                            cityId = null;
                            text = province.name;
                        } else
                        {
                            cityId = city.getValue();
                            if (city.name != null)
                            {
                                text = city.name;
                            } else
                            {
                                text = "";
                            }
                        }
                        tvCity.setText(text);
                    }
                }).setInterceptor(new BasePicker.Interceptor()
                {
                    @Override
                    public void intercept(PickerView pickerView, LinearLayout.LayoutParams params)
                    {
                        // 修改中心装饰线
                        pickerView.setCenterDecoration(decoration);
                    }
                }).create();

                // 设置标题，这里调用getTopBar来设置标题
                CustomTopBar topBar = (CustomTopBar) mPicker.getTopBar();
                topBar.getTitleView().setText("城市");
                mPicker.setTopBar(topBar);
                mPicker.setData(provinceList);
            }
        });
    }

    //城市选择
    private void showCity()
    {
        if (mPicker != null)
        {
            mPicker.setSelectedWithValues(provinceId, cityId);
            mPicker.show();
            // 设置弹窗
            Dialog dialog = mPicker.getPickerDialog();
            dialog.getWindow().setWindowAnimations(R.style.DialogOutAndInStyle);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            Display display = getWindowManager().getDefaultDisplay();

            DisplayMetrics metric = new DisplayMetrics();
            ActivityBaseInfo.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
            int widthT = metric.widthPixels; // 屏幕宽度（像素）
            int heightT = metric.heightPixels; // 屏幕高度（像素）
            lp.width = (int) (widthT * 0.9);
            lp.height = (int) (heightT * 0.5);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            lp.alpha = 0.9f;
            dialog.getWindow().setAttributes(lp);
        }
    }

    List<PPIndustryListBean.DataBeanX.DataBean> industryLists = new ArrayList<>();
    private int indexLoopView1 = 0;
    private int indexLoopView2 = 0;

    private void getIndustryData()
    {
        ListHttpHelper.getIndustryListData(this, new SDRequestCallBack(PPIndustryListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PPIndustryListBean bean = (PPIndustryListBean) responseInfo.getResult();
                industryLists.clear();
                industryLists.addAll(bean.getData().getData());

                if (StringUtils.notEmpty(industryLists) && industryLists.size() > 0)
                {
                    initIndustryPopupWindow(1);
                }
            }
        });
    }

    private List<NewGroupListBean.DataBeanX.DataBean> newGroupListBeen = new ArrayList<>();

    private void getGroupDataNew()
    {
        ListHttpHelper.getGroupListDataNew(this, new SDRequestCallBack(NewGroupListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                NewGroupListBean bean = (NewGroupListBean) responseInfo.getResult();
                newGroupListBeen = bean.getData().getData();
                if (StringUtils.notEmpty(newGroupListBeen) && newGroupListBeen.size() > 0)
                {
                    initIndustryPopupWindow(2);
                }
            }
        });
    }

    private void initIndustryPopupWindow(final int flag)
    {
        backgroundAlpha(0.7f);

        final List<String> listStr = new ArrayList<>();
        final List<String> listStrChildren = new ArrayList<>();
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_loopview2, null);
        DisplayMetrics metric = new DisplayMetrics();
        ActivityBaseInfo.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int widthT = metric.widthPixels; // 屏幕宽度（像素）
        int heightT = metric.heightPixels; // 屏幕高度（像素）
        int h = (int) (heightT * 0.5);
        int w = (int) (widthT * 0.9);
        final PopupWindow popupWindow = new PopupWindow(contentView, w, ViewGroup
                .LayoutParams.WRAP_CONTENT, true);
//        ColorDrawable cd = new ColorDrawable(0xd0000000);
//        popupWindow.setBackgroundDrawable(cd);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                //popupwindow消失的时候恢复成原来的透明度
                backgroundAlpha(1f);
            }
        });
        LoopView loopView = (LoopView) contentView.findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) contentView.findViewById(R.id.loopView2);
        Button btnOk = (Button) contentView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (flag == 1)
                {
                    tvIndustryClassification.setText(industryLists.get(indexLoopView1).getChildren().get(indexLoopView2)
                            .getCodeNameZhCn());
                    comIndu = industryLists.get(indexLoopView1).getChildren().get(indexLoopView2).getCodeKey();
                } else
                {
                    tvIndustryGroup.setText(newGroupListBeen.get(indexLoopView1).getChildren().get(indexLoopView2).getDeptName());
                    indusGroup = newGroupListBeen.get(indexLoopView1).getChildren().get(indexLoopView2).getDeptId();
                }
                popupWindow.dismiss();
            }
        });
        Button btnCancel = (Button) contentView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                popupWindow.dismiss();
            }
        });
        if (flag == 1)
        {
            for (int i = 0; i < industryLists.size(); i++)
            {
                listStr.add(industryLists.get(i).getCodeNameZhCn());
            }
        } else
        {
            for (int i = 0; i < newGroupListBeen.size(); i++)
            {
                listStr.add(newGroupListBeen.get(i).getDeptName());
            }
        }
        loopView.setNotLoop();
        loopView.setListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(int index)
            {
                indexLoopView1 = index;
                indexLoopView2 = 0;
                listStrChildren.clear();
                if (flag == 1)
                {
                    for (int i = 0; i < industryLists.get(index).getChildren().size(); i++)
                    {
                        listStrChildren.add(industryLists.get(index).getChildren().get(i).getCodeNameZhCn());
                    }
                } else
                {
                    for (int i = 0; i < newGroupListBeen.get(index).getChildren().size(); i++)
                    {
                        listStrChildren.add(newGroupListBeen.get(index).getChildren().get(i).getDeptName());
                    }
                }
                loopView2.setItems(listStrChildren);
                loopView2.setCurrentPosition(0);
                loopView2.setTextSize(14);

            }
        });
        loopView.setItems(listStr);
        loopView.setInitPosition(0);
        loopView.setTextSize(15);

        if (flag == 1)
        {
            for (int i = 0; i < industryLists.get(0).getChildren().size(); i++)
            {
                listStrChildren.add(industryLists.get(0).getChildren().get(i).getCodeNameZhCn());
            }
        } else
        {
            for (int i = 0; i < newGroupListBeen.get(0).getChildren().size(); i++)
            {
                listStrChildren.add(newGroupListBeen.get(0).getChildren().get(i).getDeptName());
            }
        }
        loopView2.setNotLoop();
        loopView2.setListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(int index)
            {
                indexLoopView2 = index;
                if (flag == 1)
                {
                    comIndu = industryLists.get(indexLoopView1).getChildren().get(indexLoopView2).getCodeKey();
                } else
                {
                    indusGroup = newGroupListBeen.get(indexLoopView1).getChildren().get(indexLoopView2).getDeptId();
                }
            }
        });
        loopView2.setItems(listStrChildren);
        loopView2.setInitPosition(0);
        loopView2.setTextSize(15);

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    //行业分类
    private void ShowIndustryTypeDialog()
    {
        HttpHelperUtils.getInstance().getType(ActivityBaseInfo.this, true, Constants.TYPE_PROJECT_INDU, new HttpHelperUtils
                .InputListener()
        {
            @Override
            public void onData(List<BeanIceProject> icApprovedList)
            {
                if (icApprovedList.size() > 0)
                {
                    BaseDialogUtils.showDialogProject(ActivityBaseInfo.this, false, false, false, "行业分类", icApprovedList, new
                            DialogFragmentProject.InputListener()
                            {
                                @Override
                                public void onData(BeanIceProject content)
                                {
                                    comIndu = content.getKey();
                                    tvIndustryClassification.setText(content.getValue());
                                }
                            });
                } else
                {
                    MyToast.showToast(ActivityBaseInfo.this, "请重新获取行业分类");
                }
            }
        });
    }

    //行业小组
    private void ShowIndustryGroupTypeDialog()
    {
        HttpHelperUtils.getInstance().getGroupListType(ActivityBaseInfo.this, new HttpHelperUtils
                .InputListener()
        {
            @Override
            public void onData(List<BeanIceProject> icApprovedList)
            {
                if (icApprovedList.size() > 0)
                {
                    BaseDialogUtils.showDialogProject(ActivityBaseInfo.this, false, false, false, "行业小组", icApprovedList, new
                            DialogFragmentProject.InputListener()
                            {
                                @Override
                                public void onData(BeanIceProject content)
                                {
                                    indusGroup = content.getKey();
                                    tvIndustryGroup.setText(content.getValue());
                                }
                            });
                } else
                {
                    MyToast.showToast(ActivityBaseInfo.this, "请重新获取行业小组");
                }
            }
        });
    }

    //来源人
    private void ShowSourceDialog()
    {
        HttpHelperUtils.getInstance().getType(ActivityBaseInfo.this, true, Constants.TYPE_PROJECT_SOUR, new HttpHelperUtils
                .InputListener()
        {
            @Override
            public void onData(List<BeanIceProject> icApprovedList)
            {
                if (icApprovedList.size() > 0)
                {
                    BaseDialogUtils.showDialogProject(ActivityBaseInfo.this, false, false, false, "来源渠道", icApprovedList, new
                            DialogFragmentProject.InputListener()
                            {
                                @Override
                                public void onData(BeanIceProject content)
                                {
                                    projSour = content.getKey();
                                    tvSourceWay.setText(content.getValue());
                                }
                            });
                } else
                {
                    MyToast.showToast(ActivityBaseInfo.this, "请重新获取来源渠道");
                }
            }
        });
    }

    List<BeanIceProject> beanIceProjectList = new ArrayList<>();

    private void showDirector()
    {
        beanIceProjectList.clear();
        beanIceProjectList.add(new BeanIceProject(0, "Y", " 是"));
        beanIceProjectList.add(new BeanIceProject(1, "N", " 否"));
        BaseDialogUtils.showDialogProject(ActivityBaseInfo.this, false, false, false, "是否有董事席位", beanIceProjectList, new
                DialogFragmentProject.InputListener()
                {
                    @Override
                    public void onData(BeanIceProject content)
                    {
                        seatFlag = content.getKey();
                        tvDirectorHas.setText(content.getValue());
                    }
                });
    }

    private void showESG()
    {
        beanIceProjectList.clear();
        beanIceProjectList.add(new BeanIceProject(0, "Y", " 是"));
        beanIceProjectList.add(new BeanIceProject(1, "N", " 否"));
        BaseDialogUtils.showDialogProject(ActivityBaseInfo.this, false, false, false, "是否有符合ESG政策", beanIceProjectList, new
                DialogFragmentProject.InputListener()
                {
                    @Override
                    public void onData(BeanIceProject content)
                    {
                        esgFlag = content.getKey();
                        tvEsg.setText(content.getValue());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null)
        {
            final List<PublicUserListBean.DataBeanX.DataBean> userList = (List<PublicUserListBean.DataBeanX.DataBean>) data
                    .getSerializableExtra("userLists");// str即为回传的值
            if (userList != null && userList.size() > 0)
            {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < userList.size(); i++)
                {
                    builder.append(userList.get(i).getUserName() + "、");
                }

                if (requestCode == mRequestCode_partners)
                {
                    partnersLists = userList;
                    tvPartner.setText(builder.substring(0, builder.length() - 1));
                } else if (requestCode == mRequestCode_projManagers)
                {
                    projManagersLists = userList;
                    tvProjectPreside.setText(builder.substring(0, builder.length() - 1));
                } else if (requestCode == mRequestCode_projTeams)
                {
                    projTeamsLists = userList;
                    tvGroupNumber.setText(builder.substring(0, builder.length() - 1));
                } else if (requestCode == mRequestCode_contributer)
                {
                    contributerLists = userList;
                    tvContributer.setText(builder.substring(0, builder.length() - 1));
                } else if (requestCode == mRequestCode_projDDManagers)
                {
                    projDDManagersLists = userList;
                    tvInvestigatePreside.setText(builder.substring(0, builder.length() - 1));
                } else if (requestCode == mRequestCode_projLawyers)
                {
                    projLawyersLists = userList;
                    tvProjectLawyer.setText(builder.substring(0, builder.length() - 1));
                }
                else if (requestCode == mRequestCode_projDirectors)
                {
                    projDirectorList = userList;
                    tvDirectorInfo.setText(builder.substring(0, builder.length() - 1));
                }
                else if (requestCode == mRequestCode_projObservers)
                {
                    projObserversList = userList;
                    tvObserver.setText(builder.substring(0, builder.length() - 1));
                }

            } else
            {
                if (requestCode == mRequestCode_partners)
                {
                    tvPartner.setText("");
                    partnersLists.clear();
                } else if (requestCode == mRequestCode_projManagers)
                {
                    tvProjectPreside.setText("");
                    projManagersLists.clear();
                } else if (requestCode == mRequestCode_projTeams)
                {
                    tvGroupNumber.setText("");
                    projTeamsLists.clear();
                } else if (requestCode == mRequestCode_contributer)
                {
                    tvContributer.setText("");
                    contributerLists.clear();
                } else if (requestCode == mRequestCode_projDDManagers)
                {
                    tvInvestigatePreside.setText("");
                    projDDManagersLists.clear();
                } else if (requestCode == mRequestCode_projLawyers)
                {
                    tvProjectLawyer.setText("");
                    projLawyersLists.clear();
                }
                else if (requestCode == mRequestCode_projLawyers)
                {
                    tvDirectorInfo.setText("");
                    projDirectorList.clear();
                }
                else if (requestCode == mRequestCode_projObservers)
                {
                    tvObserver.setText("");
                    projObserversList.clear();
                }
            }
        }
    }

}
