package newProject.company.project_manager.investment_project.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

/**
 * Created by zsz on 2019/7/26.
 */

public class SeekButtonGroupView extends LinearLayout{

    private TextView tvReduce;
    private TextView tvAdd;
    private EditText etNumber;


    public SeekButtonGroupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.linearlayout_seek_button_group,null);
        initView(myView);
        addView(myView);

    }

    private void initView(View view){
        etNumber = (EditText) view.findViewById(R.id.et_number);
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.notEmpty(s)){
                    int num = Integer.parseInt(s.toString());
                    if (num > 5){
                        etNumber.setText("5");
                    }else if (num < 1){
                        etNumber.setText("1");
                    }
                }
            }
        });
        tvReduce = (TextView) view.findViewById(R.id.tv_reduce);
        tvReduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(etNumber.getText().toString());
                if (num - 1 >= 1){
                    etNumber.setText((num-1)+"");
                }
            }
        });

        tvAdd = (TextView) view.findViewById(R.id.tv_add);
        tvAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(etNumber.getText().toString());
                if (num + 1 <= 5){
                    etNumber.setText((num+1)+"");
                }
            }
        });
    }

    public String getNum(){
        return etNumber.getText().toString();
    }


}
