package com.cxgz.activity.cx.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.cxgz.activity.cx.bean.Image;
import com.injoy.idg.R;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 多图选择
 * Created by Nereo on 2015/4/7.
 */
public class MultiImageSelectorActivity extends FragmentActivity implements MultiImageSelectorFragment.Callback
{

    /**
     * 最大图片选择次数，int类型，默认9
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，默认多选
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，默认显示
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";
    public static final String EXTRA_SMALL_RESULT = "extra_small_result";
    /**
     * 默认选择集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";
    /**
     * 原图
     */
    public static final String ORIGINAL = "original";
    public static final String ORIGINAL_SHOW = "originalshow";

    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<>();
    /**
     * small image
     */
    private ArrayList<String> smallResultList = new ArrayList<>();
    private Button mSubmitButton;
    private int mDefaultCount;
    /**
     * 原图
     */
    private boolean isOriginal;
    /**
     * 原图按钮
     */
    private boolean isOriginalShow;

    private List<Image> mSelectedImages = new ArrayList<>();

    public static final String IS_DETAIL_NOTIFY = "is_detail_select";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        isOriginal = intent.getBooleanExtra(ORIGINAL, false);
        isOriginalShow = intent.getBooleanExtra(ORIGINAL_SHOW, true);
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST))
        {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putBoolean(MultiImageSelectorFragment.IS_ORIGINAL, isOriginal);
        bundle.putBoolean(MultiImageSelectorFragment.IS_ORIGINAL_SHOW, isOriginalShow);
        bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();

        // 返回按钮
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent data = new Intent();
                data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                data.putStringArrayListExtra(EXTRA_SMALL_RESULT, smallResultList);
                data.putExtra(ORIGINAL, isOriginal);
                //toDatas();
                Bundle bundle=new Bundle();
                bundle.putSerializable(IS_DETAIL_NOTIFY,(Serializable)mSelectedImages);
                data.putExtras(bundle);
                //setResult(RESULT_CANCELED);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        // 完成按钮
        mSubmitButton = (Button) findViewById(R.id.commit);
        if (resultList == null || resultList.size() <= 0)
        {
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        } else
        {
            mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
            mSubmitButton.setEnabled(true);
        }
        mSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (resultList != null && resultList.size() > 0)
                {
                    // 返回已选择的图片数据
                    Intent data = new Intent();
                    data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                    data.putStringArrayListExtra(EXTRA_SMALL_RESULT, smallResultList);
                    data.putExtra(ORIGINAL, isOriginal);
                    Bundle bundle=new Bundle();
                    //toDatas();
                    bundle.putSerializable(IS_DETAIL_NOTIFY,(Serializable)mSelectedImages);
                    data.putExtras(bundle);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }

//    private void toDatas(){
//        if(BaseApplication.isMoney){
//            List<Image> appmSelectedImages = BaseApplication.mSelectedImages;
//            if(null!=appmSelectedImages&&appmSelectedImages.size()>0){
//                for(Image image:appmSelectedImages){
//                    if(null!=mSelectedImages&&mSelectedImages.size()>0){
//
//                        for ( int m=0;m<mSelectedImages.size();m++){
//
//                            if(!mSelectedImages.get(m).path.equals(image.path)){
//                                mSelectedImages.add(image);
//                            }
//                        }
//                    }else{
//                        mSelectedImages.add(image);
//                    }
//
//                }
//            }
//        }
//    }

    @Override
    public void onSingleImageSelected(String path)
    {
        Intent data = new Intent();
        resultList.add(path);
       // mSelectedImages.add(image);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        Bundle bundle=new Bundle();
        bundle.putSerializable(IS_DETAIL_NOTIFY,(Serializable)mSelectedImages);
        data.putExtras(bundle);
        data.putExtra(ORIGINAL, isOriginal);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path)
    {
//        if (!mSelectedImages.contains(image))
//        {
//            mSelectedImages.add(image);
//        }

        if (!resultList.contains(path))
        {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if (resultList.size() > 0)
        {
            mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
            if (!mSubmitButton.isEnabled())
            {
                mSubmitButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onImageUnselected(String path,Image image)
    {

//        if (mSelectedImages.contains(image))
//        {
//            mSelectedImages.remove(image);
//            //mSubmitButton.setText("完成(" + mSelectedImages.size() + "/" + mDefaultCount + ")");
//        } else
//        {
//            //mSubmitButton.setText("完成(" + mSelectedImages.size() + "/" + mDefaultCount + ")");
//        }

        if (resultList.contains(path))
        {
            resultList.remove(path);
            mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
        } else
        {
            mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
        }
        // 当为选择图片时候的状态
        if (resultList.size() == 0)
        {
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        }
    }

    @Override
    public void onCameraShot(File imageFile)
    {
        if (imageFile != null)
        {
            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onOriginal(boolean check)
    {
        isOriginal = check;
    }
}
