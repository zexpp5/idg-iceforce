package com.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

import com.injoy.idg.R;



/**
 * @author jp
 */
public class DecimalInputWatcher implements TextWatcher {
    private int integerLimit;
    private int decimalLimit;
    private EditText editText;

    public DecimalInputWatcher(EditText editText, int integerLimit, int decimalLimit){
        this.editText = editText;
        this.integerLimit = integerLimit;
        this.decimalLimit = decimalLimit;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.toString().trim().length()==(integerLimit+1) && !s.toString().contains(".")){
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerLimit + 1)});
            if(!s.toString().substring(integerLimit, integerLimit + 1).equals(".")){
                editText.setText(s.subSequence(0, integerLimit));
                editText.setSelection(integerLimit);
                SDToast.showShort(StringUtil.getResourceString(editText.getContext(), R.string.at_more)
                        + integerLimit + StringUtil.getResourceString(editText.getContext(), R.string.integer_number));
            }
        }else if(s.toString().trim().length()==(integerLimit+1)&&s.toString().contains(".")){
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerLimit + decimalLimit + 1)});
        }
        if(!s.toString().contains(".")){
        	if(s.toString().trim().length()>(integerLimit)){
        		editText.setText(s.subSequence(0, integerLimit));
        		editText.setSelection(integerLimit);
        	}
        }
        
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                editText.setText(s);
                editText.setSelection(s.length());
                SDToast.showShort("最多输入2位小数");
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            editText.setText(s);
            editText.setSelection(2);
        }

        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                editText.setText(s.subSequence(1, s.toString().trim().length()));
                editText.setSelection(1);
                //return;
            }

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
