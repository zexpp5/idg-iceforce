package yunjing.table_back;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entity.gztutils.ZTUtils;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.HashMap;


public class CustomeTableItem extends LinearLayout {
    private Context context = null;
    private boolean isNeed = false;//是否需要第二行动
    private ArrayList<View> viewList = new ArrayList();//行的表格列表
    public HashMap<String, View> viewMap = new HashMap();//key为行列组合
    private int[] headWidthArr = null;//表头的列宽设置
    private String rowType = "0";//行的样式id
    private int rowHeight = 0;
    private boolean isSimple = true;//是否简单的行模式（没有行合并)
    private TextView view1 = new TextView(getContext());
    private final int textSize = 15;


    public CustomeTableItem(Context context) {
        super(context);
    }

    public CustomeTableItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomeTableItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
     * rowType:行的样式，字符任意，相同样式的行不需要再创建了
     * itemCells:单元格信息
     * headWidthArr:每列宽度
     */
    public void buildItem(Context context, String rowType, ArrayList<ItemCell> itemCells
            , int[] headWidthArr, boolean isSpecial) {
        if (this.getTag() != null
                && this.getTag() instanceof String
                && this.getTag().equals("2")) {//设置成2为复杂的行合并
            this.isSimple = false;
        }
        this.setOrientation(LinearLayout.VERTICAL);//第一层布局垂直
        this.context = context;
        this.headWidthArr = headWidthArr.clone();
        this.rowType = rowType;

        rowHeight = Dp2Px(context, 40);

        if (isSimple) {
            this.addCell(itemCells);
        } else {
            this.addCellR(itemCells);
        }
    }

    private void addCell(ArrayList<ItemCell> itemCells) {
        this.removeAllViews();
        LinearLayout secondLayout = new LinearLayout(context);
        secondLayout.setOrientation(LinearLayout.HORIZONTAL);
        secondLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.addView(secondLayout);
        int cellIndex = 0;

        for (int i = 0; i < itemCells.size(); i++) {
            ItemCell itemCell = itemCells.get(i);
            int endIndex = cellIndex + itemCell.getCellSpan();//所占行数

            int width = getCellWidth(cellIndex, endIndex);//行宽度
            cellIndex = endIndex;
            if (itemCell.getCellType() == CellTypeEnum.STRING) {
                EditText view = (EditText) getInputView();
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                this.setEditView(view);
                secondLayout.addView(view);
                viewList.add(view);
            } else if (itemCell.getCellType() == CellTypeEnum.DIGIT) {
                EditText view = (EditText) getInputView();
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                this.setEditView(view);
                this.setOnKeyBorad(view);
                secondLayout.addView(view);
                viewList.add(view);
            } else if (itemCell.getCellType() == CellTypeEnum.LABEL) {
                TextView view = (TextView) getLabelView();
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                secondLayout.addView(view);
                viewList.add(view);
            }
            if (i != itemCells.size() - 1) {//插入竖线
                LinearLayout v_line = (LinearLayout) getVerticalLine();
                v_line.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                secondLayout.addView(v_line);
            }
        }
    }

    private void addCellR(ArrayList<ItemCell> itemCells) {
//        //获取屏幕宽度
//        int windowWidth = getWindowWidth();
//        int restWidth = windowWidth - 90;
        this.removeAllViews();


        RelativeLayout secondLayout = new RelativeLayout(context);
        //secondLayout.setOrientation(LinearLayout.HORIZONTAL);
        secondLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.addView(secondLayout);
        //int cellIndex = 0;
        for (int i = 0; i < itemCells.size(); i++) {
            ItemCell itemCell = itemCells.get(i);
            int endIndex = itemCell.getColNum() + itemCell.getCellSpan();//所占行数
            int width;//行宽度
            int height;
            int top;
            int left;
            int right = 0;

            height = getCellHeight(itemCell.getRowSpan());
            top = this.getCellTop(itemCell.getRowNum());
            width = getCellWidth(itemCell.getColNum(), endIndex);//行宽度
            left = this.getCellLeft(itemCell.getColNum());

            if (itemCell.getCellType() == CellTypeEnum.B_HEIGHT) {//乔哥较为复杂表头
                height = getCellHeight(itemCell.getRowSpan()) / 2;
                top = this.getCellTop(itemCell.getRowNum()) / 2;
            }   if (itemCell.getCellType() == CellTypeEnum.B_HEIGHT1) {//乔哥较为复杂表头
                height = getCellHeight(itemCell.getRowSpan()) / 2+ ZTUtils.px2dip(context, 3);
                top = this.getCellTop(itemCell.getRowNum()) / 2;
            } else if (itemCell.getCellType() == CellTypeEnum.JINGYING_FLOW_MONTH) {
                width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.5);
            } else if (itemCell.getCellType() == CellTypeEnum.ASSEMBY_WIDTH) {
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.5);
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.5);
                        break;
                    case 2:
                        width = getCellWidth(itemCell.getColNum(), endIndex) / 2;//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.5);
                        break;

                }
            } else if (itemCell.getCellType() == CellTypeEnum.INFUNDS_MONTH) {//应收明细特殊宽度
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.5);
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.5);
                        break;
                }
            } else if (itemCell.getCellType() == CellTypeEnum.BUSINESS_WIDTH) {//销售业务汇总特殊宽度(第一个字段保持原宽度100)
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex)* 1.4);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.4);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.4);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.2);
//                        right = (int) (this.getCellLeft(itemCell.getColNum()) * 1.5);
                        break;
                }
            } else if (itemCell.getCellType() == CellTypeEnum.SETTING_MONEY) {//库存初始化特殊宽度(最后一个字段保持原宽度100)
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1);
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.4);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.0);
                        break;
                    case 3:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.4);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.135);
                        break;
                }
            } else if (itemCell.getCellType() == CellTypeEnum.SETTING_GET_AND_PAY) {//库存初始化特殊宽度(最后一个字段保持原宽度100)
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.4);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.4);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.2);
                        break;

                }
            } else if (itemCell.getCellType() == CellTypeEnum.FUND_YEAR) {


                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.5);
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.5);
                        break;
                }
            } else if (itemCell.getCellType() == CellTypeEnum.FUND_FLOW_SUMMARY) {
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.5);
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.25);
                        break;
                    case 3:
                        width = getCellWidth(itemCell.getColNum(), endIndex);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.332);
                        break;
                }
            } else if (itemCell.getCellType() == CellTypeEnum.FUND_ACCOUNT_YUE) {


                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.25);
                        break;
                    case 3:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.335);
                        break;
                    case 4:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.375);
                        break;
                }
            } else if (itemCell.getCellType() == CellTypeEnum.CUS_FOR_ORDER) {//销售业务汇总特殊宽度(最后一个字段保持原宽度100)
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.4);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.0);
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.4);
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.4);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.2);
                        break;

                }
            } else if (itemCell.getCellType() == CellTypeEnum.SALE_MONEY_TOTAL) {//销售金额汇总按商品特殊宽度(最后三个字段150)
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) );//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) );
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) );
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 3:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 4:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.126);
                        break;
                    case 5:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.201);
                        break;

                }
            }else if (itemCell.getCellType() == CellTypeEnum.CAIGOU_MONEY_TOTAL) {//采购金额汇总按商品特殊宽度(最后三个字段150)
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) );//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) );
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) );
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex)* 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 3:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.168);
                        break;
                    case 4:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.252);
                        break;

                }
            } else if (itemCell.getCellType() == CellTypeEnum.ZY_3) {//商品出入库
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.1);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.1);
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.05);
                        break;
                    case 3:
                        width = getCellWidth(itemCell.getColNum(), endIndex);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.2);
                        break;
                }
            } else if (itemCell.getCellType() == CellTypeEnum.ZY_LAST) {//邹爷最后一列宽度
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                }
            } else if (itemCell.getCellType() == CellTypeEnum.ZY_DETAIL) {//当前库存资金明细
                switch (i) {
                    case 0:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex));//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 1:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()));
                        break;
                    case 2:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.25);
                        break;
                    case 3:
                        width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);//行宽度
                        left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.335);
                        break;
                }
            }


            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            rl.leftMargin = left;
            rl.topMargin = top;
            /**
             * 传入的标签
             */
            if (itemCell.getCellType() == CellTypeEnum.STRING) {
                EditText view = (EditText) getInputView();
                /**
                 * 边框
                 */
//                view.setBackgroundResource(R.drawable.erp_shape_right_bottom);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);

//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                this.setEditView(view);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.DIGIT) {
                EditText view = (EditText) getInputView();
                /**
                 * 边框
                 */
//                view.setBackgroundResource(R.drawable.erp_shape_right_bottom);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);

                this.setEditView(view);
                this.setOnKeyBorad(view);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.TOTAL) {//合计居中

                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setBackgroundResource(R.drawable.erp_shape_right_bottom);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.LABEL) {//////////////////////////////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);

                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.ZY_3) {///////////////////////商品出入库///////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);

                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.ZY_LAST) {/////////////////////////出入库数量月统计/////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);

                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.ZY_DETAIL) {/////////////////////////当前库存资金明细/////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);

                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.JINGYING_FLOW_MONTH) {
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);


            } else if (itemCell.getCellType() == CellTypeEnum.FUND_FLOW_SUMMARY) {//////////////////////////////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.FUND_ACCOUNT_YUE) {
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.FUND_YEAR) {
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            }else if (itemCell.getCellType() == CellTypeEnum.SETTING_MONEY) {//////////////////////////////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
//                view.setBackgroundResource(R.drawable.erp_first_setting_shape_right);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.SETTING_GET_AND_PAY) {//////////////////////////////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
//                view.setBackgroundResource(R.drawable.erp_first_setting_shape_right);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.CUS_FOR_ORDER) {//////////////////////////////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.SALE_WIDTH) {/////////////销售汇总特殊宽度/////////////////////////////////////
                width = (int) (getCellWidth(itemCell.getColNum(), endIndex) * 1.5);
                left = (int) (this.getCellLeft(itemCell.getColNum()) * 1.5);
                rl.leftMargin = left;

                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
//                view.getLayoutParams().width = 300;
                view.setTextSize(textSize);
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setHeight(height);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            }
            else if (itemCell.getCellType() == CellTypeEnum.SALE_MONEY_TOTAL) {/////////////销售金额汇总特殊宽度/////////////////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
//                view.getLayoutParams().width = 300;
                view.setTextSize(textSize);
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setHeight(height);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            }
            else if (itemCell.getCellType() == CellTypeEnum.CAIGOU_MONEY_TOTAL) {/////////////采购金额汇总特殊宽度/////////////////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
//                view.getLayoutParams().width = 300;
                view.setTextSize(textSize);
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setHeight(height);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            }
            else if (itemCell.getCellType() == CellTypeEnum.ASSEMBY_WIDTH) {/////////////应付明细特殊宽度/////////////////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
//                view.getLayoutParams().width = 300;
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.INFUNDS_MONTH) {/////////////应付明细特殊宽度/////////////////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
//                view.getLayoutParams().width = 300;
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.BUSINESS_WIDTH) {/////////////销售业务报表特殊宽度/////////////////////////////////////
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
//                view.getLayoutParams().width = 300;
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setBackgroundResource(R.drawable.erp_shape_right);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.GREY_COLOR) {//////////////////////////////////////////////////灰色背景
                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
//                view.setBackgroundResource(R.drawable.erp_shape_right_gray_bg);
                view.setText(itemCell.getCellValue());
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.B_HEIGHT) {//乔哥复杂表头

                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
                switch (i) {
                    case 0:
                    case 1:
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                        view.setBackgroundResource(R.drawable.erp_shape_right);
                        break;
                    case 2:
                    case 5:
//                        view.setBackgroundResource(R.drawable.erp_shape_right_bottom);
                        break;
                }
                view.setText(itemCell.getCellValue());
                //配合下面的表格(对齐线)
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setWidth(width - ZTUtils.px2dip(context, 1));
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.B_HEIGHT1) {//乔哥复杂表头

                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
                switch (i) {
                    case 0:
                    case 1:
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                        view.setBackgroundResource(R.drawable.erp_shape_right);
                        break;
                    case 2:
                    case 5:
//                        view.setBackgroundResource(R.drawable.erp_shape_right_bottom);
                        break;
                }


                view.setText(itemCell.getCellValue());
                //配合下面的表格(对齐线)
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setWidth(width + ZTUtils.px2dip(context, 2));
                view.setHeight(height+ ZTUtils.px2dip(context,3));
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.TESHU_WIDTH) {//

                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
                switch (i) {
                    case 0:
                    case 1:
                    case 3:
                    case 4:
                    case 6:
                    case 7:
                        view.setBackgroundResource(R.drawable.erp_shape_right);
                        break;
                    case 2:
                    case 5:
//                        view.setBackgroundResource(R.drawable.erp_shape_right_bottom);
                        break;
                }
//                view.setTypeface(BaseApplication.createWRYHTypeFace());
                view.setText(itemCell.getCellValue());
                //配合下面的表格(对齐线)
                view.setWidth(width - ZTUtils.px2dip(context, 1) + 25);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.RED_COLOR) {//红色字体

                TextView view = (TextView) getLabelView();
                /**
                 * 边框
                 */
                //view.setTypeface(BaseApplication.createWRYHTypeFace());
//                view.setBackgroundResource(R.drawable.erp_shape_right_bottom);
                view.setText(itemCell.getCellValue());
                view.setTextColor(Color.RED);
                view.setWidth(width);
                view.setHeight(height);
                view.setTextSize(textSize);
                secondLayout.addView(view, rl);
                viewMap.put(itemCell.getId() + "", view);
            } else if (itemCell.getCellType() == CellTypeEnum.ORDER_DOWN) {
                /*降序排序的情况下*/
                /*TextView view = (TextView) getLabelView();*/
                view1 = (TextView) getLabelView();
                view1.setGravity(Gravity.CENTER);
//                Drawable drawable = getResources().getDrawable(R.mipmap.erp_order_title_desc_bg);
//                drawable.setBounds(-20, 0, drawable.getMinimumWidth()/10, (int) (drawable.getMinimumHeight()));
//                view1.setCompoundDrawables(null, null, drawable, null);
//                view1.setTag(i + 1);
                /**
                 * 边框
                 */
//                view1.setTypeface(BaseApplication.createWRYHTypeFace());
                view1.setBackgroundResource(R.drawable.erp_shape_right);
                view1.setText("");
                view1.setText(itemCell.getCellValue());
                view1.setWidth(width);
                view1.setHeight(height);
                view1.setTextSize(textSize);
                secondLayout.addView(view1, rl);
                view1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*SDToast.showShort("00000000000000");*/
                        if (null != mOnTableItemViewClick) {
                            mOnTableItemViewClick.onClick(v);
                        }
                    }
                });
                viewMap.put(itemCell.getId() + "", view1);
            }else if (itemCell.getCellType() == CellTypeEnum.ORDER_DOWN1) {
                /*降序排序的情况下*/
                /*TextView view = (TextView) getLabelView();*/
                view1 = (TextView) getLabelView();
                view1.setGravity(Gravity.CENTER);
//                Drawable drawable = getResources().getDrawable(R.mipmap.erp_order_title_desc_bg);
//
//                drawable.setBounds(-20, 0, drawable.getMinimumWidth()/10, (int) (drawable.getMinimumHeight()));
//                view1.setCompoundDrawables(null, null, drawable, null);
//                view1.setTag(i + 1);
                /**
                 * 边框
                 */
//                view1.setTypeface(BaseApplication.createWRYHTypeFace());
                view1.setBackgroundResource(R.drawable.erp_shape_right);
                view1.setWidth(width);
                view1.setHeight(height);
                view1.setTextSize(textSize);
                secondLayout.addView(view1, rl);
                view1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*SDToast.showShort("00000000000000");*/
                        if (null != mOnTableItemViewClick) {
                            mOnTableItemViewClick.onClick(v);
                        }
                    }
                });
                view1.setText(itemCell.getCellValue());
                viewMap.put(itemCell.getId() + "", view1);
            }
            else if (itemCell.getCellType() == CellTypeEnum.ORDER_UP) {
                /*升序排序的情况下*/
                view1 = (TextView) getLabelView();
                view1.setGravity(Gravity.CENTER);
                view1.setTextSize(textSize);
//                Drawable drawable = getResources().getDrawable(R.mipmap.erp_order_title_asc_bg);
//                drawable.setBounds(-20, 0, drawable.getMinimumWidth()/10,  drawable.getMinimumHeight());
//                view1.setCompoundDrawables(null, null, drawable, null);
                view1.setTag(i + 1);
                /**
                 * 边框
                 */
//                view1.setTypeface(BaseApplication.createWRYHTypeFace());
                view1.setBackgroundResource(R.drawable.erp_shape_right);
                view1.setText(itemCell.getCellValue());

                view1.setWidth(width);
                view1.setHeight(height);

                secondLayout.addView(view1, rl);
                view1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*SDToast.showShort("000000222200000000");*/
                        if (null != mOnTableItemViewClick) {
                            mOnTableItemViewClick.onClick(v);
                        }
                    }
                });
                viewMap.put(itemCell.getId() + "", view1);
            }
//	    	if(i!=itemCells.size()-1){//插入竖线
//				LinearLayout v_line = (LinearLayout)getVerticalLine();
//				v_line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
//				secondLayout.addView(v_line);
//			}
        }
    }

    public void refreshData(ArrayList<ItemCell> itemCells) {
        if (this.isSimple) {
            this.refreshDataSimple(itemCells);
        } else {
            this.refreshDataR(itemCells);
        }
    }

    private void refreshDataSimple(ArrayList<ItemCell> itemCells) {
        for (int i = 0; i < itemCells.size(); i++) {
            ItemCell itemCell = itemCells.get(i);
            if (itemCell.getCellType() == CellTypeEnum.LABEL) {
                TextView view = (TextView) viewList.get(i);
                view.setText(itemCell.getCellValue());
            } else if (itemCell.getCellType() == CellTypeEnum.DIGIT) {
                EditText view = (EditText) viewList.get(i);
                view.setText(itemCell.getCellValue());
                this.setEditView(view);
                this.setOnKeyBorad(view);
            } else if (itemCell.getCellType() == CellTypeEnum.STRING) {
                EditText view = (EditText) viewList.get(i);
                view.setText(itemCell.getCellValue());
                this.setEditView(view);
            }
        }
    }

    private void refreshDataR(ArrayList<ItemCell> itemCells) {
        for (int i = 0; i < itemCells.size(); i++) {
            ItemCell itemCell = itemCells.get(i);
            if (itemCell.getCellType() == CellTypeEnum.LABEL) {
                TextView view = (TextView) viewMap.get(itemCell.getId() + "");
                view.setText(itemCell.getCellValue());
            } else if (itemCell.getCellType() == CellTypeEnum.DIGIT) {
                EditText view = (EditText) viewMap.get(itemCell.getId() + "");
                view.setText(itemCell.getCellValue());
                this.setEditView(view);
                this.setOnKeyBorad(view);
            } else if (itemCell.getCellType() == CellTypeEnum.STRING) {
                EditText view = (EditText) viewMap.get(itemCell.getId() + "");
                view.setText(itemCell.getCellValue());
                this.setEditView(view);
            }
        }
    }

    private View getVerticalLine() {
        return LayoutInflater.from(context).inflate(R.layout.erp_atom_line_v_view, null);
    }

    private int getCellWidth(int cellStart, int cellEnd) {
        int width = 0;
        for (int i = cellStart; i < cellEnd; i++) {
            width = this.headWidthArr[i] + width;
        }
        return width;
    }

    private int getCellLeft(int cellIndex) {//从0开始编号
        int left = 0;
        for (int i = 0; i < cellIndex; i++) {
            left = this.headWidthArr[i] + left;
        }
        return left;
    }

    private int getCellTop(int rowNum) {        //rowNum从0开始
        return rowNum * this.rowHeight;
    }

    private int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int getCellHeight(int rowSpan) {
        return rowSpan * this.rowHeight;
    }

    private View getLabelView() {
        return (View) LayoutInflater.from(context).inflate(R.layout.atom_text_view, null);
    }

    private View getInputView() {
        return (View) LayoutInflater.from(context).inflate(R.layout.atom_edttxt_view, null);
    }

    private void setEditView(EditText edtText1) {
        if (this.isNeed) {
            edtText1.setEnabled(false);
        } else {

        }
    }

    private void setOnKeyBorad(EditText edtText1) {
        //数字键盘
        if (!this.isNeed) {//非只读

        }
    }

    public String getRowType() {
        return rowType;
    }

    public int getWindowWidth() {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width;
    }


    private OnTableItemViewClick mOnTableItemViewClick;

    public OnTableItemViewClick getmOnTableItemViewClick() {
        return mOnTableItemViewClick;
    }

    public void setmOnTableItemViewClick(OnTableItemViewClick mOnTableItemViewClick) {
        this.mOnTableItemViewClick = mOnTableItemViewClick;
    }

    public interface OnTableItemViewClick {
        void onClick(View view);
    }


}
