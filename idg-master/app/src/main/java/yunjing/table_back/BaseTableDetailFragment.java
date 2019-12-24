package yunjing.table_back;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.entity.gztutils.ZTUtils;
import com.http.SDHttpHelper;
import com.injoy.idg.R;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yunjing.table.BaseInfoAdapter;
import yunjing.table.BaseNameAdapter;
import yunjing.table.LinkedHorizontalScrollView;
import yunjing.table.NoScrollHorizontalScrollView;



/**
 * Created by lzk_xiao on 2016/10/17.
 */
public abstract class BaseTableDetailFragment extends Fragment {
    public LinearLayout left_bar_ll;//表格左边最外层
    public LinearLayout left_bar_ll_item;//表格左边的item
    public TextView item_1;

    protected LinearLayout addLL, listLL;

    public View view_line_title;//顶部5dp白色view
    private String total;
    public SDHttpHelper mHttpHelper;
    public View all_bottom_bar_id_list,warehouse_line; //底部上下页分享按钮那些
    protected LinearLayout ll_bottom_page_left;
    protected TextView bottomLeftBtn; //上下页
    protected TextView bottomRightBtn;
    protected LinearLayout ll_bottom_share_right;//分享按钮


    private TextView tv_bottom_name, tv_bottom1, tv_bottom2, tv_bottom3, tv_bottom4, tv_bottom5, tv_bottom6, tv_bottom7, tv_bottom8, tv_bottom9, tv_bottom10, tv_bottom11;

    private TextView tv_second_line;//第二行保持不动的需求view

    public TextView tv_name;//左上角第一个Textview


    public TextView pay_button;//本次付款/本次收款

    int[] bottomIds = new int[]{R.id.tv_bottom_name, R.id.tv_bottom1, R.id.tv_bottom2, R.id.tv_bottom3, R.id.tv_bottom4, R.id.tv_bottom5, R.id.tv_bottom6, R.id.tv_bottom7, R.id.tv_bottom8, R.id.tv_bottom9, R.id.tv_bottom10, R.id.tv_bottom11};

    TextView[] bottomTextView = new TextView[]{tv_bottom_name, tv_bottom1, tv_bottom2, tv_bottom3, tv_bottom4, tv_bottom5, tv_bottom6, tv_bottom7, tv_bottom8, tv_bottom9, tv_bottom10, tv_bottom11};

    public int bottomNumber = 4;//底部Textview的数量

    private RelativeLayout rl_table;//root


    private NoScrollHorizontalScrollView sv_normalgoods_title;//不可滑动的顶部左侧的ScrollView
    public NoScrollHorizontalScrollView sv_bottom_title;//底部的合计
    private LinkedHorizontalScrollView sv_normalgoods_detail;//底部左侧的ScrollView

    public ListView lv_normalgoodname;//底部左侧的ListView
    public ListView lv_normalgood_info;//底部右侧的ListView

    boolean isLeftListEnabled = false;
    boolean isRightListEnabled = false;

    public boolean isTwo = false; //第二行
    public boolean isBottom = false; //底部



    public BaseNameAdapter mLvNormalNameAdapter;//左边的listview
    public BaseInfoAdapter mLvNormalInfoAdapter;//右边的infoListview


    public List<TextView> bottoms;//装底部Textview

    public LinearLayout tv_two_line;//第二行保持不动的需求view


    public LayoutInflater mInflater;
    public LinearLayout headLayout = null;
    public int[] arrHeadWidth = null;
    public ListView listView;
    public CustomeTableViewAdapter adapter = null;
    public ArrayList<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();
    public View view;//填充的view
    public View nameFootView;//底部view
    public View infoFootView;//底部view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = LayoutInflater.from(getActivity());
        view = inflater.inflate(R.layout.erp_fragment_open_order_new_should_assemby_detail, container, false);
        infoFootView = inflater.inflate(R.layout.erp_table_pay_foot_view, null);
        pay_button = (TextView) infoFootView.findViewById(R.id.pay_button);//本次付款
        nameFootView = inflater.inflate(R.layout.erp_table_pay_foot_view_false, null);

//        left_bar_ll = (LinearLayout) view.findViewById(R.id.left_bar_ll);


        /**
         * 设置footview高度
         */
//        nameFootView.setMinimumHeight(ZTUtils.dip2px(getActivity(), 41));
//        infoFootView.setMinimumHeight(ZTUtils.dip2px(getActivity(), 41));
//        nameFootView.getLayoutParams().height = ZTUtils.dip2px(getActivity(), 40);
//        infoFootView.getLayoutParams().height = ZTUtils.dip2px(getActivity(), 40);


        initView(view);

        this.testData();//数据

        init(view);

//        initAdapter();
//        if (mLvNormalInfoAdapter != null) {
//            bottomNumber = mLvNormalInfoAdapter.getTitleNumber();//初始化完适配器后拿到标题数量
//        }
        if (isBottom) {
            addTextView();//将textview添加到集合当中并显示对应的标题
            setBottomData(bottoms);//用于设置底部数据
        }

        initTwoAndBottom();


        return view;
    }

    /**
     * @param fist   第一个TextView
     * @param second 第二行跨所有列的TextView
     */
    public void setFistAndSecond(String fist, String second) {
        tv_name.setText(fist);
        if (isTwo) {
            tv_second_line.setText(second);
        }
    }

    private void initView(View view) {
        mHttpHelper = new SDHttpHelper(getActivity());
        view_line_title = view.findViewById(R.id.view_line_title);

        tv_name = (TextView) view.findViewById(R.id.tv_name);
       // tv_name.setTypeface(BaseApplication.createWRYHTypeFace());
        sv_normalgoods_title = (NoScrollHorizontalScrollView) view.findViewById(R.id.sv_title);//头部
        sv_bottom_title = (NoScrollHorizontalScrollView) view.findViewById(R.id.sv_bottom_total);//底部
        sv_normalgoods_detail = (LinkedHorizontalScrollView) view.findViewById(R.id.sv_good_detail);

        lv_normalgoodname = (ListView) view.findViewById(R.id.lv_goodname);//左边第一条
        lv_normalgood_info = (ListView) view.findViewById(R.id.lv_good_info);//右半部info

        all_bottom_bar_id_list = (View) view.findViewById(R.id.all_bottom_bar_id_list);
       // warehouse_line = (View) view.findViewById(R.id.warehouse_line); //最后一个listitem下面的线条

        rl_table = (RelativeLayout) view.findViewById(R.id.rl_table);

        /**
         * 列表显示
         */
//        listLL = (LinearLayout) view.findViewById(R.id.ll_bottom_list_left);
//        addLL = (LinearLayout) view.findViewById(R.id.ll_bottom_add_left);



        combination(lv_normalgoodname, lv_normalgood_info, sv_normalgoods_title, sv_normalgoods_detail, sv_bottom_title);


        headLayout = (LinearLayout) view.findViewById(R.id.linearlayout_head);
        listView = (ListView) view.findViewById(R.id.listview);

        tv_two_line = (LinearLayout) view.findViewById(R.id.tv_two_line);//第二行跨全列的需求
        tv_second_line = (TextView) view.findViewById(R.id.tv_second_line);//第二行跨全列的需求
        /**
         * 初始化底部
         */
        for (int j = 0; j < bottomIds.length; j++) {
            bottomTextView[j] = (TextView) view.findViewById(bottomIds[j]);
        }

        bottomView(view);
    }

    /**
     * 底部按钮初始化
     *
     * @param view
     */
    private void bottomView(View view) {
        ll_bottom_page_left = (LinearLayout) view.findViewById(R.id.ll_bottom_page_left);
//        ll_bottom_share_right = (LinearLayout) view.findViewById(R.id.ll_bottom_share_right);//分享按钮
//        bottomLeftBtn = (TextView) view.findViewById(R.id.bottom_left_btn);
//        bottomRightBtn = (TextView) view.findViewById(R.id.bottom_right_btn);

    }

    /**
     * 上页
     * @param listener
     */
    protected void setBottomLeftBtn(View.OnClickListener listener) {
        ll_bottom_page_left.setVisibility(View.VISIBLE);
        bottomLeftBtn.setVisibility(View.VISIBLE);
        bottomLeftBtn.setOnClickListener(listener);
    }

    /**
     * 修改左右按钮文字
     * @param left
     * @param right
     */
    protected void setLeftRightText(String left, String right){
        bottomLeftBtn.setText(left);
        bottomRightBtn.setText(right);
    }

    /**
     * 下页
     * @param listener
     */
    protected void setBottomRightBtn(View.OnClickListener listener) {
        bottomRightBtn.setVisibility(View.VISIBLE);
        bottomRightBtn.setOnClickListener(listener);
    }


    /**
     * 列表按钮显示
     * @param listener
     */
    public void setBottomLeftListVisible(View.OnClickListener listener) {
        addLL.setVisibility(View.GONE);
        listLL.setVisibility(View.VISIBLE);
        listLL.setOnClickListener(listener);
    }
    /**
     * 显示分享按钮可点
     */
    protected void showShareButton(final String url, final String shareId, final String title, final String text, final Activity context, final ZTUtils.OnSelectShareToListener mOnSelectShareToListener) {
        boolean isShare = (boolean) SPUtils.get(getActivity(), SPUtils.ISSHARE, true);
        if (isShare) {
            ll_bottom_share_right.setVisibility(View.VISIBLE);
        } else {
            ll_bottom_share_right.setVisibility(View.GONE);
        }
        ll_bottom_share_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(url)) {
                    ZTUtils.showShareDialog(url, shareId, title, text, context, mOnSelectShareToListener);
                }else {
                    MyToast.showToast(getActivity(), "暂无数据");
                    return;
                }
            }
        });
    }

//    public void setItemBackground(ViewHolder helper, int position){
//        if ((position + 1) % 2 == 0) {
//            helper.getView(R.id.item_layout).setBackgroundResource(R.color.staff_list_default_bg);
//        } else {
//            helper.getView(R.id.item_layout).setBackgroundResource(R.color.staff_list_undertint_bg);
//        }
//    }

    //返回页数
    protected int getIndex() {
        int index = 0;
        if (!TextUtils.isEmpty(total)) {
            if (Integer.parseInt(total) % 15 == 0) {
                index = Integer.parseInt(total) / 15;
            } else {
                index = (Integer.parseInt(total) / 15) + 1;
            }

        }

        return index;
    }


    public void addTextView() {


        bottoms = new ArrayList<>();
        /**
         * 底部
         */
        for (int j = 0; j < bottomNumber; j++) {
            bottoms.add(j, bottomTextView[j]);
            bottomTextView[j].setVisibility(View.VISIBLE);//显示对应的标题
        }
        /**
         * 动态设置位置
         */

        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) lv_normalgoodname.getLayoutParams();
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) sv_normalgoods_detail.getLayoutParams();
        layoutParams1.addRule(RelativeLayout.ABOVE, R.id.tv_bottom_name);
        layoutParams2.addRule(RelativeLayout.ABOVE, R.id.tv_bottom_name);
        layoutParams2.addRule(RelativeLayout.RIGHT_OF, R.id.lv_goodname);
        lv_normalgoodname.setLayoutParams(layoutParams1);
        sv_normalgoods_detail.setLayoutParams(layoutParams2);

    }


    public void initTwoAndBottom() {
        if (isTwo) {
            tv_two_line.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
//        if (mLvNormalNameAdapter != null && mLvNormalInfoAdapter != null) {
//            lv_normalgoodname.setAdapter(mLvNormalNameAdapter);
////            lv_normalgoodname.setDivider(new ColorDrawable(getResources().getColor(R.color.search_icon_de_bg)));
////            lv_normalgoodname.setDividerHeight(1);
//
//            lv_normalgood_info.setAdapter(mLvNormalInfoAdapter);
////            lv_normalgood_info.setDivider(new ColorDrawable(getResources().getColor(R.color.search_icon_de_bg)));
////            lv_normalgood_info.setDividerHeight(1);
//        }
    }


    private void combination(final ListView lvName, final ListView lvDetail, final NoScrollHorizontalScrollView title, final LinkedHorizontalScrollView content, final HorizontalScrollView bottom) {
        /**
         * 顶部title
         */
        title.setMyScrollChangeListener(new NoScrollHorizontalScrollView.NoScrollHorizontalScrollViewListener() {
            @Override
            public void onscroll(NoScrollHorizontalScrollView view, int x, int y, int oldl, int oldt) {
                content.scrollTo(x, y);//详情
            }
        });

        /**
         * 左右滑动同步
         */
        content.setMyScrollChangeListener(new LinkedHorizontalScrollView.LinkScrollChangeListener() {

            @Override
            public void onscroll(LinkedHorizontalScrollView view, int x, int y, int oldx, int oldy) {
                title.scrollTo(x, y);//头部

                if (isBottom) {//需要底部同步滑动
                    bottom.scrollTo(x, y);//底部
                }
            }
        });

        /**
         * 上下滑动同步
         */
        // 禁止快速滑动
        lvName.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        lvDetail.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        //左侧ListView滚动时，控制右侧ListView滚动
        lvName.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //这两个enable标志位是为了避免死循环
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isRightListEnabled = false;
                    isLeftListEnabled = true;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isRightListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View child = view.getChildAt(0);
                if (child != null && isLeftListEnabled) {
                    lvDetail.setSelectionFromTop(firstVisibleItem, child.getTop());
                }
            }
        });

        //右侧ListView滚动时，控制左侧ListView滚动
        lvDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isLeftListEnabled = false;
                    isRightListEnabled = true;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isLeftListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View c = view.getChildAt(0);
                if (c != null && isRightListEnabled) {
                    lvName.setSelectionFromTop(firstVisibleItem, c.getTop());
                }
            }
        });
    }


    private void testData() {
        HashMap headMap = new HashMap();
        this.testAddHead(headMap, "列1");
        this.testAddHead(headMap, "列2");
        this.testAddHead(headMap, "列3");
        this.testAddHead(headMap, "列4");
        this.testAddHead(headMap, "列5");
        this.testAddHead(headMap, "列6");
        this.testAddHead(headMap, "列7");
        this.testAddHead(headMap, "列8");
        this.testAddHead(headMap, "列9");
        this.testAddHead(headMap, "列10");
        this.testAddHead(headMap, "列11");
        this.testAddHead(headMap, "列12");
        this.testAddHead(headMap, "列13");
        this.testAddHead(headMap, "列14");
        this.testAddHead(headMap, "列15");
        this.testAddHead(headMap, "列16");
        this.testAddHead(headMap, "列17");
        this.testAddHead(headMap, "列18");

        this.addHead(headMap);

//        HashMap contentMap = new HashMap();
//        this.testAddContentRows(contentMap);
        /**
         * table
         */
        setTable();
        adapter = new CustomeTableViewAdapter(getActivity(), lists, listView
                , false, this.arrHeadWidth);
        adapter.notifyDataSetChanged();
    }

    protected abstract void setTable();

    private void testAddHead(HashMap headMap, String headName) {
        HeadItemCell itemCell = new HeadItemCell(headName, 100);
        headMap.put(headMap.size() + "", itemCell);
    }

    private void addHead(HashMap headMap) {
        arrHeadWidth = new int[headMap.size()];
        int width = 0;
        for (int i = 0; i < headMap.size(); i++) {
            HeadItemCell itemCell = (HeadItemCell) headMap.get(i + "");
            String name = itemCell.getCellValue();
            width = Dp2Px(getActivity(), itemCell.getWidth());
            setHeadName(name, width);
            arrHeadWidth[i] = width;
            if (i != headMap.size() - 1) {
                this.addVLine();
            }
        }
    }

    private void setHeadName(String name, int width) {
        TextView headView = (TextView) mInflater.inflate(R.layout.erp_atom_head_text_view, null);
        if (headView != null) {
            String viewName = "<b>" + name + "</b>";
            headView.setText(Html.fromHtml(name));
            headView.setWidth(width);
            headLayout.addView(headView);
        }
    }

    private int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void addVLine() {
        LinearLayout v_line = (LinearLayout) getVerticalLine();
        v_line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        headLayout.addView(v_line);
    }

    private View getVerticalLine() {
        return mInflater.inflate(R.layout.erp_atom_line_v_view, null);
    }

    /**
     * @param rowMap
     * @param colSpan   跨列数(1表示1列即没跨)
     * @param cellValue 数据
     * @param cellType  看CellTypeEnum
     * @param rowNum    所在行数(看跨最大行的map有几行)从零开始
     * @param colNum    所在列数(从零开始)
     * @param rowSpan   跨行数(1表示1列即没跨)
     */
    public void testAddRows(HashMap rowMap, int colSpan, String cellValue, CellTypeEnum cellType, int rowNum, int colNum, int rowSpan) {
        ItemCell itemCell = new ItemCell(cellValue, cellType, colSpan);
        itemCell.setPos(rowNum, colNum, rowSpan);
        rowMap.put(rowMap.size() + "", itemCell);
    }

    public abstract void init(View view);

    public abstract void initAdapter();

    public abstract void setBottomData(List<TextView> bottoms);//设置底部的数据
}
