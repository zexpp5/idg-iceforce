package yunjing.table;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTableMainFragment extends Fragment {
    int[] titleIds = new int[]{R.id.tv_name, R.id.tv_title1, R.id.tv_title2, R.id.tv_title3, R.id.tv_title4, R.id.tv_title5, R.id.tv_title6, R.id.tv_title7, R.id.tv_title8, R.id.tv_title9, R.id.tv_title10, R.id.tv_title11};
    int[] bottomIds = new int[]{R.id.tv_bottom_name, R.id.tv_bottom1, R.id.tv_bottom2, R.id.tv_bottom3, R.id.tv_bottom4, R.id.tv_bottom5, R.id.tv_bottom6, R.id.tv_bottom7, R.id.tv_bottom8, R.id.tv_bottom9, R.id.tv_bottom10, R.id.tv_bottom11};

    private TextView tv_name;
    private TextView tv_title1;
    private TextView tv_title2;
    private TextView tv_title3;
    private TextView tv_title4;
    private TextView tv_title5;
    private TextView tv_title6;
    private TextView tv_title7;
    private TextView tv_title8;
    private TextView tv_title9;
    private TextView tv_title10;
    private TextView tv_title11;

    private TextView tv_bottom_name;
    private TextView tv_bottom1;
    private TextView tv_bottom2;
    private TextView tv_bottom3;
    private TextView tv_bottom4;
    private TextView tv_bottom5;
    private TextView tv_bottom6;
    private TextView tv_bottom7;
    private TextView tv_bottom8;
    private TextView tv_bottom9;
    private TextView tv_bottom10;
    private TextView tv_bottom11;

    TextView[] titleTextView = new TextView[]{tv_name, tv_title1, tv_title2, tv_title3, tv_title4, tv_title5, tv_title6, tv_title7, tv_title8,tv_title9,tv_title10, tv_title11};
    TextView[] bottomTextView = new TextView[]{tv_bottom_name, tv_bottom1, tv_bottom2, tv_bottom3, tv_bottom4,tv_bottom5, tv_bottom6, tv_bottom7, tv_bottom8,tv_bottom9,tv_bottom10,tv_bottom11};



    public NoScrollHorizontalScrollView sv_normalgoods_title;//不可滑动的顶部左侧的ScrollView
    public NoScrollHorizontalScrollView sv_bottom_title;//底部的合计
    public LinkedHorizontalScrollView sv_normalgoods_detail;//底部左侧的ScrollView
    public ListView lv_normalgoodname;//底部左侧的ListView
    private ListView lv_normalgood_info;//底部右侧的ListView

    boolean isLeftListEnabled = false;
    boolean isRightListEnabled = false;

    public BaseNameAdapter mLvNormalNameAdapter;//左边的listview
    public BaseInfoAdapter mLvNormalInfoAdapter;//右边的infoListview


    public List<TextView> titles;
    public List<TextView> bottoms;

    private int titleNumber;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.erp_fragment_main, container, false);

        initView(view);

        init(view);

        initAdapter();

        titleNumber = mLvNormalInfoAdapter.getTitleNumber();//初始化完适配器后拿到标题数量

        addTextView();

        setTitleAndBottom(titles, bottoms);


        return view;
    }

    public void addTextView(){
        titles = new ArrayList<>();
        bottoms = new ArrayList<>();
        /**
         * 头部控件
         */
        for (int i = 0; i < titleNumber; i++){
            titles.add(i, titleTextView[i]);
            titleTextView[i].setVisibility(View.VISIBLE);//显示对应的标题
        }


//           titles.add(0, tv_title1);
//           titles.add(1, tv_title2);
//           titles.add(2, tv_title3);
//           titles.add(3, tv_title4);
//           titles.add(4, tv_title5);
//           titles.add(5, tv_title6);
//           titles.add(6, tv_title7);
//           titles.add(7, tv_title8);
//           titles.add(8, tv_title9);
//           titles.add(9, tv_title10);
//           titles.add(10, tv_title11);
        /**
         * 底部
         */
        for (int j = 0; j < titleNumber; j++){
            bottoms.add(j, bottomTextView[j]);
            bottomTextView[j].setVisibility(View.VISIBLE);//显示对应的标题
        }

//           bottoms.add(0, tv_bottom1);
//           bottoms.add(1, tv_bottom2);
//           bottoms.add(2, tv_bottom3);
//           bottoms.add(3, tv_bottom4);
//           bottoms.add(4, tv_bottom5);
//           bottoms.add(5, tv_bottom6);
//           bottoms.add(6, tv_bottom7);
//           bottoms.add(7, tv_bottom8);
//           bottoms.add(8, tv_bottom9);
//           bottoms.add(9, tv_bottom10);
//           bottoms.add(10,tv_bottom11);
    }


    @Override
    public void onStart() {
        super.onStart();
        if(mLvNormalNameAdapter != null && mLvNormalInfoAdapter != null){
            lv_normalgoodname.setAdapter(mLvNormalNameAdapter);
            lv_normalgoodname.setDivider(new ColorDrawable(getResources().getColor(R.color.search_icon_de_bg)));
            lv_normalgoodname.setDividerHeight(1);

            lv_normalgood_info.setAdapter(mLvNormalInfoAdapter);
            lv_normalgood_info.setDivider(new ColorDrawable(getResources().getColor(R.color.search_icon_de_bg)));
            lv_normalgood_info.setDividerHeight(1);
        }
    }

    private void initView(View view) {
        /**
         * 初始化头部
         */
        for (int i = 0; i < titleIds.length; i++) {
            titleTextView[i] = (TextView) view.findViewById(titleIds[i]);
        }
        /**
         * 初始化底部
         */
        for (int j = 0; j < titleIds.length; j++) {
            bottomTextView[j] = (TextView) view.findViewById(bottomIds[j]);
        }


//
//        tv_name = (TextView) view.findViewById(R.id.tv_name);
//        tv_title1 = (TextView) view.findViewById(R.id.tv_title1);
//        tv_title2 = (TextView) view.findViewById(R.id.tv_title2);
//        tv_title3 = (TextView) view.findViewById(R.id.tv_title3);
//        tv_title4 = (TextView) view.findViewById(R.id.tv_title4);
//        tv_title5 = (TextView) view.findViewById(R.id.tv_title5);
//        tv_title6 = (TextView) view.findViewById(R.id.tv_title6);
//        tv_title7 = (TextView) view.findViewById(R.id.tv_title7);
//        tv_title8 = (TextView) view.findViewById(R.id.tv_title8);
//        tv_title9 = (TextView) view.findViewById(R.id.tv_title9);
//        tv_title10 = (TextView) view.findViewById(R.id.tv_title10);
//        tv_title11 = (TextView) view.findViewById(R.id.tv_title11);
//
//        tv_bottom_name = (TextView) view.findViewById(R.id.tv_bottom_name);
//        tv_bottom1 = (TextView) view.findViewById(R.id.tv_bottom1);
//        tv_bottom2 = (TextView) view.findViewById(R.id.tv_bottom2);
//        tv_bottom3 = (TextView) view.findViewById(R.id.tv_bottom3);
//        tv_bottom4 = (TextView) view.findViewById(R.id.tv_bottom4);
//        tv_bottom5 = (TextView) view.findViewById(R.id.tv_bottom5);
//        tv_bottom6 = (TextView) view.findViewById(R.id.tv_bottom6);
//        tv_bottom7 = (TextView) view.findViewById(R.id.tv_bottom7);
//        tv_bottom8 = (TextView) view.findViewById(R.id.tv_bottom8);
//        tv_bottom9 = (TextView) view.findViewById(R.id.tv_bottom9);
//        tv_bottom10 = (TextView) view.findViewById(R.id.tv_bottom10);
//        tv_bottom11 = (TextView) view.findViewById(R.id.tv_bottom11);


        sv_normalgoods_title = (NoScrollHorizontalScrollView)view.findViewById(R.id.sv_title);
        sv_bottom_title = (NoScrollHorizontalScrollView)view.findViewById(R.id.sv_bottom_total);
        sv_normalgoods_detail = (LinkedHorizontalScrollView)view.findViewById(R.id.sv_good_detail);
        lv_normalgoodname = (ListView) view.findViewById(R.id.lv_goodname);
        lv_normalgood_info = (ListView) view.findViewById(R.id.lv_good_info);
        combination(lv_normalgoodname, lv_normalgood_info, sv_normalgoods_title, sv_normalgoods_detail, sv_bottom_title);

    }


    private void combination(final ListView lvName, final ListView lvDetail, final HorizontalScrollView title, LinkedHorizontalScrollView content, final HorizontalScrollView bottom) {
        /**
         * 左右滑动同步
         */
        content.setMyScrollChangeListener(new LinkedHorizontalScrollView.LinkScrollChangeListener() {

            @Override
            public void onscroll(LinkedHorizontalScrollView view, int x, int y, int oldx, int oldy) {
                title.scrollTo(x, y);//头部
                bottom.scrollTo(x, y);//底部
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

    public abstract void init(View view);
    public abstract void initAdapter();
    public abstract void setTitleAndBottom(List<TextView> titles, List<TextView> bottoms);//设置标题与底部的数据
}