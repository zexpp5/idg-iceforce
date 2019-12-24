/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cxgz.activity.cxim;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.cxgz.activity.cx.utils.baiduMap.Utils;
import com.cxgz.activity.cxim.utils.BaiduUtils;
import com.injoy.idg.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cxgz.activity.cxim.base.BaseActivity;



public class BaiduMapActivity extends BaseActivity
{
    private final static String TAG = "map";
    static MapView mMapView = null;
    FrameLayout mMapViewContainer = null;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    Button sendButton = null;

    EditText indexText = null;
    int index = 0;
    // LocationData locData = null;
    static BDLocation lastLocation = null;
    public static BaiduMapActivity instance = null;
    ProgressDialog progressDialog;
    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LinearLayout ll_back;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor bitmap;

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class BaiduSDKReceiver extends BroadcastReceiver
    {
        public void onReceive(Context context, Intent intent)
        {
            String s = intent.getAction();
            String st1 = getResources().getString(R.string.Network_error);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR))
            {
//                String st2 = getResources().getString(R.string.please_check);
//                Toast.makeText(instance, st2, Toast.LENGTH_SHORT).show();
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR))
            {
                Toast.makeText(instance, st1, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private BaiduSDKReceiver mBaiduReceiver;

    @Override
    protected void init()
    {
        instance = this;
        mMapView = (MapView) findViewById(R.id.bmapView);
        sendButton = (Button) findViewById(R.id.btn_location_send);
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.5f);
        mBaiduMap.setMapStatus(msu);
        initMapView();
        if (latitude == 0)
        {
            mMapView = new MapView(this, new BaiduMapOptions());
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    mCurrentMode, true, null));
            showMapWithLocationClient();
        } else
        {
            double longtitude = intent.getDoubleExtra("longitude", 0);
            String address = intent.getStringExtra("address");

            BaiduUtils.Gps gps = BaiduUtils.bd09_To_Gcj02(longtitude, latitude);
            LatLng p = new LatLng(gps.lat, gps.lon);
            mMapView = new MapView(this,
                    new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                            .target(p).build()));
            showMap(gps.lat, gps.lon, address);
        }
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mBaiduReceiver = new BaiduSDKReceiver();
        registerReceiver(mBaiduReceiver, iFilter);
    }

    @Override
    protected int getContentLayout()
    {
        SDKInitializer.initialize(getApplicationContext());
        return R.layout.activity_mylocal_baidumap;
    }

    private void showMap(double latitude, double longtitude, String address)
    {
        //构建Marker图标
        sendButton.setVisibility(View.GONE);
        LatLng llA = new LatLng(latitude, longtitude);
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(llA);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();
        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_baidu_map);
        OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(bitmap)
                .zIndex(4).draggable(true);
        mBaiduMap.addOverlay(ooA);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 18.5f);
        mBaiduMap.animateMapStatus(u);
    }

    private void showMapWithLocationClient()
    {
        String str1 = getResources().getString(R.string.Making_sure_your_location);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(str1);

        progressDialog.setOnCancelListener(new OnCancelListener()
        {

            public void onCancel(DialogInterface arg0)
            {
                if (progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
                Log.d("map", "cancel retrieve location");
                finish();
            }
        });

        progressDialog.show();

        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
//         option.setCoorType("bd09ll"); //设置坐标类型
        // Johnson change to use gcj02 coordination. chinese national standard
        // so need to conver to bd09 everytime when draw on baidu map
        option.setCoorType(Utils.CoorType_GCJ02);
        option.setScanSpan(30000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
    }

    @Override
    protected void onPause()
    {
        mMapView.onPause();
        if (mLocClient != null)
        {
            mLocClient.stop();
        }
        super.onPause();
        lastLocation = null;
    }

    @Override
    protected void onResume()
    {
        mMapView.onResume();
        if (mLocClient != null)
        {
            mLocClient.start();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        if (mLocClient != null)
            mLocClient.stop();
        if (mMapView != null)
            mMapView.onDestroy();
        if (bitmap != null)
            bitmap.recycle();
        unregisterReceiver(mBaiduReceiver);
        super.onDestroy();
    }

    private void initMapView()
    {
        mMapView.setLongClickable(true);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    /**
     * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            if (location == null)
            {
                return;
            }
            sendButton.setEnabled(true);
            if (progressDialog != null)
            {
                progressDialog.dismiss();
            }

            if (lastLocation != null)
            {
                if (lastLocation.getLatitude() == location.getLatitude() && lastLocation.getLongitude() == location.getLongitude())
                {
//                    mMapView.refreshDrawableState(); //need this refresh?
                    return;
                }
            }
            lastLocation = location;
            mBaiduMap.clear();
            LatLng llA = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            CoordinateConverter converter = new CoordinateConverter();
            converter.coord(llA);
            converter.from(CoordinateConverter.CoordType.COMMON);
            LatLng convertLatLng = converter.convert();
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_baidu_map);
            OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(bitmap)
                    .zIndex(4).draggable(true);
            mBaiduMap.addOverlay(ooA);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 18.5f);
            mBaiduMap.animateMapStatus(u);
        }

        public void onReceivePoi(BDLocation poiLocation)
        {
            if (poiLocation == null)
            {
                return;
            }
        }
    }

    public void back(View v)
    {
        finish();
    }


    public static final String RESULT_LATITUDE = "latitude";
    public static final String RESULT_LONGITUDE = "longitude";
    public static final String RESULT_ADDRESS = "address";

    public void sendLocation(View view)
    {
        final Intent intent = this.getIntent();
//		final File file = new File(Config.CACHE_IMG_DIR + File.separator + "baidu_snapshot_"+System.currentTimeMillis()+".jpg");
//		if(!file.exists()){
//			file.mkdirs();
//		}
        BaiduUtils.Gps gps = BaiduUtils.gcj02_To_Bd09(lastLocation.getLongitude(), lastLocation.getLatitude());
        intent.putExtra("latitude", gps.lat);
        intent.putExtra("longitude", gps.lon);
        intent.putExtra("address", lastLocation.getAddrStr());
//		intent.putExtra("snapshot",file.getAbsolutePath());
//		mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
//			@Override
//			public void onSnapshotReady(Bitmap bitmap) {
//				FileOutputStream out;
//				try {
//					out = new FileOutputStream(file);
//					if (bitmap.compress(
//							Bitmap.CompressFormat.JPEG, 100, out)) {
//						out.flush();
//						out.close();
//					}
//					Toast.makeText(BaiduMapActivity.this,
//							"屏幕截图成功，图片存在: " + file.toString(),
//							Toast.LENGTH_SHORT).show();
//					setResult(RESULT_OK, intent);
//					finish();
//					overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		Toast.makeText(BaiduMapActivity.this, "正在截取屏幕图片...",
//				Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

}
