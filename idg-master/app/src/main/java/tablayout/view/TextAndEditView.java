package tablayout.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.injoy.idg.R;
import com.superdata.marketing.view.percent.PercentLinearLayout;
import com.utils.DecimalInputWatcher;

/**
 * @author 黎平
 * @date 15-11-26 上午9:42
 */
public class TextAndEditView extends PercentLinearLayout {
    private View parent;
    private ImageView customerAdd;
    private TextView tvName;//昵称
    private TextView tvMoney;//元
    private TextView tvRmb;//（币种）
    private EditText etInputContent;//输入内容
    private TextView tvContent;//只显示不输入
    private View rightMargin;//特殊处理右边外边距
    private boolean enabled = true;
    public static final int INPUT_PRICE = 1;
    public static final int INPUT_NUMBER = 2;
    public static final int INPUT_PASSWORD = 3;
    public static final int INPUT_SCALE = 4;//比例
    public static final int INPUT_EMAIL = 5;
    public static final int INPUT_PHONE = 6;
    public static final int INPUT_WEB_TEXT = 7;
    public static final int LENGTH = 20;//长度
    public static final int MIN_MARK = 0;
    public static final int MAX_MARK = 100;
    /**
     * 小数长度
     */
    private static final int DECIMAL_DIGITS = 2;
    private static final int MAX_LENGHT = 7;

    public TextAndEditView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.textAndEdit);
        String name = ta.getString(R.styleable.textAndEdit_name);
        int length = ta.getInt(R.styleable.textAndEdit_max_Length, LENGTH);
        boolean eb = ta.getBoolean(R.styleable.textAndEdit_enabled, true);
        String hintText = ta.getString(R.styleable.textAndEdit_hint_text);
        boolean addCustomer = ta.getBoolean(R.styleable.textAndEdit_add_customer, false);

        int inputType = ta.getInt(R.styleable.textAndEdit_inputType, 0);
        ta.recycle();

        parent = LayoutInflater.from(context).inflate(R.layout.custom_text_and_edit_layout, null);
        rightMargin = parent.findViewById(R.id.view_edit_marginright);
        customerAdd = (ImageView) parent.findViewById(R.id.iv_customer_add);
        tvName = (TextView) parent.findViewById(R.id.tv_nickname);
        etInputContent = (EditText) parent.findViewById(R.id.et_input_content);
        tvContent = (TextView) parent.findViewById(R.id.tv_content);
        tvMoney = (TextView) parent.findViewById(R.id.tv_tad_money);
        tvRmb = (TextView) parent.findViewById(R.id.tv_tad_rmb);
        if (name != null && !name.equals("")) {
            tvName.setText(name + "：");
        }
        if(hintText!=null){
            etInputContent.setHint(Html.fromHtml("<small>" +
                    hintText + "</small>"));
        }

        //判断是否需要添加客户
        if (addCustomer) {
            etInputContent.setVisibility(View.GONE);//隐藏输入框
            tvContent.setVisibility(View.VISIBLE);//显示文本
            customerAdd.setVisibility(View.VISIBLE);//显示添加客户按钮
        }
        etInputContent.setEnabled(eb);
        etInputContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        setInputListener(inputType);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(parent, params);

    }
    public void addTextChangedListener(TextWatcher textWatcher){
        etInputContent.addTextChangedListener(textWatcher);
    }
    public void setLenthFilter(int length) {
        etInputContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }

    public void setInputListener(int inputType) {
        switch (inputType) {
            case INPUT_PRICE://价格
                rightMargin.setVisibility(View.GONE);
                tvMoney.setVisibility(View.VISIBLE);
                tvRmb.setVisibility(View.VISIBLE);
                etInputContent.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                etInputContent.addTextChangedListener(new DecimalInputWatcher(etInputContent, MAX_LENGHT, DECIMAL_DIGITS));
                break;
            case INPUT_NUMBER://数字
                etInputContent.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case INPUT_PASSWORD://密码
                etInputContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case INPUT_SCALE://比列
                rightMargin.setVisibility(View.GONE);
                tvMoney.setVisibility(View.VISIBLE);
                tvMoney.setText("%");
                etInputContent.setInputType(InputType.TYPE_CLASS_NUMBER);
                etInputContent.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s != null && !s.equals("")) {
                            if (MIN_MARK != -1 && MAX_MARK != -1) {
                                int markVal = 0;
                                try {
                                    markVal = Integer.parseInt(s.toString());
                                } catch (NumberFormatException e) {
                                    markVal = 0;
                                }
                                if (markVal > MAX_MARK) {
                                    etInputContent.setText(String.valueOf(MAX_MARK));
                                }
                                return;
                            }
                        }
                    }
                });
                break;
            case INPUT_EMAIL://邮箱
                etInputContent.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
               // etInputContent.setKeyListener(DigitsKeyListener.getInstance("abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.@/:0123456789"));
                break;
            case INPUT_PHONE://手机
                etInputContent.setInputType(InputType.TYPE_CLASS_PHONE);
                etInputContent.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
                break;
            case INPUT_WEB_TEXT:
               etInputContent.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
               // etInputContent.setKeyListener(DigitsKeyListener.getInstance("abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ./@:0123456789"));
                break;
        }
    }

    @Override
    public void clearFocus() {
        etInputContent.clearFocus();
    }

    /**
     * get input content
     */
    public String getContent() {
        if (tvContent.getVisibility() == View.VISIBLE) {
            return tvContent.getText().toString();
        }
        return etInputContent.getText().toString().trim();
    }

    /**
     * set defalut value
     */
    public void setValue(String value) {
        if (tvContent.getVisibility() == View.VISIBLE) {
            tvContent.setText(value);
        } else {
            etInputContent.setText(value);
        }
    }



    public void setSpannable(Spannable spannable) {
        etInputContent.setText(spannable);
    }

    /**
     * 设置左边的提示
     */
    public void setHint(String value) {
        tvName.setText(value + "：");
    }

    @Override
    public void setEnabled(boolean enabled) {
        etInputContent.setFocusable(enabled);
        etInputContent.setEnabled(enabled);
        this.enabled = enabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!enabled) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 设置左边提示文字
     *
     * @param text
     */
    public void setTips(String text) {
        if (tvName != null) {
            tvName.setText(text);
        }
    }

    /**
     * 隐藏右边添加客户的按钮
     */
    public void hidenAddCustomerBtn() {
        customerAdd.setVisibility(View.GONE);
    }

    /**
     * 显示右边添加客户的按钮
     */
    public void showAddCustomerBtn() {
        customerAdd.setVisibility(View.VISIBLE);
    }

    /**
     * 添加客户回调事件
     */
    public void setAddCustomerLinstener(OnClickListener l) {
        customerAdd.setOnClickListener(l);
    }

    public EditText getEtInputContent() {
        return etInputContent;
    }

    public void setEtInputContent(EditText etInputContent) {
        this.etInputContent = etInputContent;
    }
}
