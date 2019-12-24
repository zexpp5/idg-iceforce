package com.cxgz.activity.cx.view;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.adapter.FolderAdapter;
import com.cxgz.activity.cx.adapter.ImageGridAdapter;
import com.cxgz.activity.cx.bean.Folder;
import com.cxgz.activity.cx.bean.Image;
import com.cxgz.activity.cx.utils.FileUtils;
import com.cxgz.activity.cx.utils.TimeUtils;
import com.cxgz.activity.cxim.utils.DensityUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 图片选择Fragment
 * Created by Nereo on 2015/4/7.
 */
public class MultiImageSelectorFragment extends Fragment
{

    private static final String TAG = "MultiImageSelector";

    /**
     * 最大图片选择次数，int类型
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，int类型
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，boolean类型
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 默认选择的数据集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    /**
     * 是否是原图
     */
    public static final String IS_ORIGINAL = "is_original";
    /**
     * 是否显示原图按钮
     */
    public static final String IS_ORIGINAL_SHOW = "is_original_show";
    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;
    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;


    /**
     * 详情需要选中的
     */



    // 结果数据
    private ArrayList<String> resultList = new ArrayList<>();
    // 文件夹数据
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    // 图片Grid
    private GridView mGridView;
    private Callback mCallback;

    private ImageGridAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;

    private ListPopupWindow mFolderPopupWindow;

    // 时间线
    private TextView mTimeLineText;
    // 类别
    private TextView mCategoryText;
    // 预览按钮
    private Button mPreviewBtn;
    // 底部View
    private View mPopupAnchorView;

    private int mDesireImageCount;

    private boolean hasFolderGened = false;

    private File mTmpFile;

    private CheckBox cbOriginal;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mCallback = (Callback) activity;
        } catch (ClassCastException e)
        {
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_multi_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // 选择图片数量
        mDesireImageCount = getArguments().getInt(EXTRA_SELECT_COUNT);

        // 图片选择模式
        final int mode = getArguments().getInt(EXTRA_SELECT_MODE);

        // 默认选择
        if (mode == MODE_MULTI)
        {
            ArrayList<String> tmp = getArguments().getStringArrayList(EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0)
            {
                resultList = tmp;
            }
        }

        // 是否显示照相机
        final boolean showCamera = getArguments().getBoolean(EXTRA_SHOW_CAMERA, true);
        boolean isOriginal = getArguments().getBoolean(IS_ORIGINAL, false);
        boolean isOriginalShow = getArguments().getBoolean(IS_ORIGINAL_SHOW, true);
        mImageAdapter = new ImageGridAdapter(getActivity(), showCamera);
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(mode == MODE_MULTI);

        // 如果显示了照相机，则创建临时文件
        if (showCamera)
        {
            mTmpFile = FileUtils.createTmpFile(getActivity());
        }

        mPopupAnchorView = view.findViewById(R.id.footer);

        mTimeLineText = (TextView) view.findViewById(R.id.timeline_area);
        // 初始化，先隐藏当前timeline
        mTimeLineText.setVisibility(View.GONE);

        mCategoryText = (TextView) view.findViewById(R.id.category_btn);
        // 初始化，加载所有图片
        mCategoryText.setText(R.string.folder_all);
        mCategoryText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mFolderPopupWindow.isShowing())
                {
                    mFolderPopupWindow.dismiss();
                } else
                {
                    mFolderPopupWindow.show();
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });

        mPreviewBtn = (Button) view.findViewById(R.id.preview);
        cbOriginal = (CheckBox) view.findViewById(R.id.cb_original);
        if (isOriginalShow)
        {
            cbOriginal.setVisibility(View.VISIBLE);
        } else
        {
            cbOriginal.setVisibility(View.GONE);
        }
        if (isOriginal)
        {
            cbOriginal.setChecked(true);
        } else
        {
            cbOriginal.setChecked(false);
        }
        // 初始化，按钮状态初始化
        if (resultList == null || resultList.size() <= 0)
        {
            mPreviewBtn.setText(R.string.preview);
            mPreviewBtn.setEnabled(false);
        }
        mPreviewBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TODO 预览
            }
        });
        cbOriginal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (mCallback != null)
                {
                    mCallback.onOriginal(isChecked);
                }
            }
        });
        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state)
            {

                final Picasso picasso = Picasso.with(getActivity());
                if (state == SCROLL_STATE_IDLE || state == SCROLL_STATE_TOUCH_SCROLL)
                {
                    picasso.resumeTag(getActivity());
                } else
                {
                    picasso.pauseTag(getActivity());
                }

                if (state == SCROLL_STATE_IDLE)
                {
                    // 停止滑动，日期指示器消失
                    mTimeLineText.setVisibility(View.GONE);
                } else if (state == SCROLL_STATE_FLING)
                {
                    mTimeLineText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                if (mTimeLineText.getVisibility() == View.VISIBLE)
                {
                    int index = firstVisibleItem + 1 == view.getAdapter().getCount() ? view.getAdapter().getCount() - 1 : firstVisibleItem + 1;
                    Image image = (Image) view.getAdapter().getItem(index);
                    if (image != null)
                    {
                        mTimeLineText.setText(TimeUtils.formatPhotoDate(image.path));
                    }
                }
            }
        });
        mGridView.setAdapter(mImageAdapter);
//        if(BaseApplication.isMoney){
//            mImageAdapter.setmSelectedImages(BaseApplication.mSelectedImages);
//            mImageAdapter.setMnewmSelectedImages(BaseApplication.mSelectedImages);
//            for (int n=0;n<BaseApplication.mSelectedImages.size();n++){
//                resultList.add(BaseApplication.mSelectedImages.get(n).path);
//            }
//            mImageAdapter.notifyDataSetChanged();
//        }
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout()
            {

//                final int width = mGridView.getWidth();
                final int width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dip2px(getActivity(), 10);
                final int height = mGridView.getHeight();

                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int numCount = width / desireSize;
              //  final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                final int columnSpace = 0;
                int columnWidth = (width - columnSpace * (numCount - 1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);

                if (mFolderPopupWindow == null)
                {
                    createPopupFolderList(width, height);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else
                {
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (mImageAdapter.isShowCamera())
                {
                    // 如果显示照相机，则第一个Grid显示为照相机，处理特殊逻辑
                    if (i == 0)
                    {
                        showCameraAction();
                    } else
                    {
                        // 正常操作
                        Image image = (Image) adapterView.getAdapter().getItem(i);
                        selectImageFromGrid(image, mode);
                    }
                } else
                {
                    // 正常操作
                    Image image = (Image) adapterView.getAdapter().getItem(i);
                    selectImageFromGrid(image, mode);
                }
            }
        });

        mFolderAdapter = new FolderAdapter(getActivity());
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList(int width, int height)
    {
        mFolderPopupWindow = new ListPopupWindow(getActivity());
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setHeight(height * 5 / 8);
        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (i == 0)
                {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                    mCategoryText.setText(R.string.folder_all);
                    mImageAdapter.setShowCamera(true);
                } else
                {
                    Folder folder = (Folder) adapterView.getAdapter().getItem(i);
                    if (null != folder)
                    {
                        Bundle args = new Bundle();
                        args.putString("path", folder.path);
                        getActivity().getSupportLoaderManager().restartLoader(LOADER_CATEGORY, args, mLoaderCallback);
                        mCategoryText.setText(folder.name);
                    }
                    mImageAdapter.setShowCamera(false);
                }
                mFolderAdapter.setSelectIndex(i);
                mFolderPopupWindow.dismiss();

                // 滑动到最初始位置
                mGridView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        // 首次加载所有图片
        //new LoadImageTask().execute();
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_CAMERA)
            {
                if (mTmpFile != null)
                {
                    if (mCallback != null)
                    {
                        mCallback.onCameraShot(mTmpFile);
                    }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        Log.d(TAG, "on change");

        final int orientation = newConfig.orientation;

        if (mFolderPopupWindow != null)
        {
            if (mFolderPopupWindow.isShowing())
            {
                mFolderPopupWindow.dismiss();
            }
        }

        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout()
            {

                final int height = mGridView.getHeight();

                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                Log.d(TAG, "Desire Size = " + desireSize);
                final int numCount = mGridView.getWidth() / desireSize;
                Log.d(TAG, "Grid Size = " + mGridView.getWidth());
                Log.d(TAG, "num count = " + numCount);
                final int columnSpace = 0;
                //final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (mGridView.getWidth() - columnSpace * (numCount - 1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);

                if (mFolderPopupWindow != null)
                {
                    mFolderPopupWindow.setHeight(height * 5 / 8);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else
                {
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        super.onConfigurationChanged(newConfig);

    }

    /**
     * 选择相机
     */
    private void showCameraAction()
    {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null)
        {
            // 设置系统相机拍照后的输出路径
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else
        {
            Toast.makeText(getActivity(), R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 选择图片操作
     *
     * @param image
     */
    private void selectImageFromGrid( Image image, int mode)
    {
        if (image != null)
        {
            // 多选模式
            if (mode == MODE_MULTI)
            {
                if (resultList.contains(image.path))
                {
                    resultList.remove(image.path);
                    if (resultList.size() != 0)
                    {
                        mPreviewBtn.setEnabled(true);
                        mPreviewBtn.setText(getResources().getString(R.string.preview) + "(" + resultList.size() + ")");
                    } else
                    {
                        mPreviewBtn.setEnabled(false);
                        mPreviewBtn.setText(R.string.preview);
                    }
                    if (mCallback != null)
                    {
                        final Image imgs=image;
//                        if (BaseApplication.isMoney){
//                            /*当时资金条目的附件*/
//                            if (BaseApplication.mSelectedImages!=null&&BaseApplication.mSelectedImages.size()>0){
//                                List<Image> mSelectedImagesfund = BaseApplication.mSelectedImages;
//                                for (int m=0;m<mSelectedImagesfund.size();m++){
//                                    if (imgs.path.equals(mSelectedImagesfund.get(m).path)){
//                                        mSelectedImagesfund.remove(m);
//                                    }
//                                }
//                                BaseApplication.mSelectedImages=mSelectedImagesfund;
//                            }
//                        }
                        mCallback.onImageUnselected(image.path,imgs);
                    }
                } else
                {
                    // 判断选择数量问题
                    if (mDesireImageCount == resultList.size())
                    {
                        Toast.makeText(getActivity(), R.string.msg_amount_limit, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    resultList.add(image.path);
                    mPreviewBtn.setEnabled(true);
                    mPreviewBtn.setText(getResources().getString(R.string.preview) + "(" + resultList.size() + ")");
                    if (mCallback != null)
                    {
                        final Image imgs=image;
                        mCallback.onImageSelected(image.path);
                    }
                }
                mImageAdapter.select(image);
            } else if (mode == MODE_SINGLE)
            {
                // 单选模式
                if (mCallback != null)
                {
                    final Image imgs=image;

                    mCallback.onSingleImageSelected(image.path);
                }
            }
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>()
    {
        private ProgressDialog progressDialog;
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args)
        {
            if (id == LOADER_ALL)
            {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY)
            {
                SDLogUtil.debug("argsPath==" + args.getString("path"));
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }


        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor data)
        {
            SDLogUtil.debug("=====" + Thread.currentThread().getName());
            if (data != null)
            {
                final List<Image> images = new ArrayList<>();
                int count = data.getCount();
                SDLogUtil.debug("count=====" + count);
                if (count > 0)
                {
                    data.moveToFirst();
                    AsyncTask task = new AsyncTask()
                    {
                        @Override
                        protected Object doInBackground(Object[] params)
                        {
                            do
                            {
                                String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                                String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                                long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
//                        if( !hasFolderGened ) {
                                // 获取文件夹名称
                                File imageFile = new File(path);
                                if (imageFile.exists() && imageFile.isFile())
                                {
                                    Image image = new Image(path, name, dateTime);
                                    images.add(image);
                                    File folderFile = imageFile.getParentFile();
                                    Folder folder = new Folder();
                                    folder.name = folderFile.getName();
                                    folder.path = folderFile.getAbsolutePath();
                                    folder.cover = image;
                                    if (!mResultFolder.contains(folder))
                                    {
                                        List<Image> imageList = new ArrayList<>();
                                        imageList.add(image);
                                        folder.images = imageList;
                                        mResultFolder.add(folder);
                                    } else
                                    {
                                        // 更新
                                        Folder f = mResultFolder.get(mResultFolder.indexOf(folder));
                                        f.images.add(image);
                                    }
                                }
//                        }
                            } while (data.moveToNext());
                            return null;
                        }

                        @Override
                        protected void onPreExecute()
                        {
                            if (progressDialog == null)
                            {
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("加载图片中...");
                                progressDialog.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
                            }
                            progressDialog.show();
                        }

                        @Override
                        protected void onPostExecute(Object o)
                        {
                            mImageAdapter.setData(images);

                            // 设定默认选择
                            if (resultList != null && resultList.size() > 0)
                            {
                                mImageAdapter.setDefaultSelected(resultList);
                            }

                            mFolderAdapter.setData(mResultFolder);
//                    hasFolderGened = true;
                            progressDialog.dismiss();
                        }
                    };
                    task.execute();
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader)
        {

        }
    };

    /**
     * 回调接口
     */
    public interface Callback
    {
         void onSingleImageSelected(String path);

         void onImageSelected(String path);

         void onImageUnselected(String path,Image image);

         void onCameraShot(File imageFile);

         void onOriginal(boolean check);
    }
}
