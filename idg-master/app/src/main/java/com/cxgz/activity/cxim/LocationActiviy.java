package com.cxgz.activity.cxim;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.injoy.idg.R;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.chaoxiang.base.utils.StringUtils;

/**
 * @author selson
 * @time 2016/5/13
 * 绘制百度地图的Acitity
 */
public class LocationActiviy extends Activity
{
    private String latitude, longitude;
    private String addressString;
    private String userId;
    private double mlat, mlon;

    MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private OverlayOptions option;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bitmap = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_baidu_map);

    //标识点击事件！
    private Marker mMarkerA;

    //显示信息图层
    private InfoWindow mInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.location_activity);
        Bundle bl = new Bundle();
        // 获取Intent中的Bundle数据
        bl = this.getIntent().getExtras();
        // 经度
        longitude = bl.getString("longitude");
        // 纬度
        latitude = bl.getString("latitude");
        //地址
        addressString = bl.getString("address");
        //userID
//        userId = bl.getString("userId");
        // 经度
        mlon = Double.parseDouble(longitude);
        // 纬度
        mlat = Double.parseDouble(latitude);

        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(19.0f);
        mBaiduMap.setMapStatus(msu);
        tolocation(mlat, mlon, addressString);
        initViewListener();
    }

    private void initViewListener()
    {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi)
            {
                return false;
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(final Marker marker)
            {
                //使用自己定义的
                View view = LocationActiviy.this.getLayoutInflater().inflate(R.layout.baidu_chat_location_info, null);
                boolean isShowText = true;
                TextView baidu_address = (TextView) view.findViewById(R.id.baidu_address);

                if (StringUtils.isEmpty(addressString))
                {
                    baidu_address.setText("地址为空！");
                    isShowText = false;

                } else
                {
                    baidu_address.setText(addressString);
                }

                BitmapDescriptor bitmapView = BitmapDescriptorFactory.fromView(view);

                InfoWindow.OnInfoWindowClickListener listener = null;

//                if (isShowText)
                if (marker == mMarkerA)
                {
                    listener = new InfoWindow.OnInfoWindowClickListener()
                    {
                        public void onInfoWindowClick()
                        {
                            mBaiduMap.hideInfoWindow();
                        }
                    };
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(bitmapView, ll, -100, listener);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }

                return true;
            }
        });
    }

    /**
     * 获取用户的头像
     */
    private void getUserIcon(String userId)
    {

    }

    // 根据接收到的参数进行描点
    public void tolocation(double mlat, double mlon, String addressString)
    {
        // 定义Maker坐标点
        LatLng point = new LatLng(mlat, mlon);
        // 构建MarkerOption，用于在地图上添加Marker

        MarkerOptions ooA = new MarkerOptions().position(point).icon(bitmap)
                .zIndex(9).draggable(true);
        // 掉下动画
        //ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

        // 定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(19).build();

        // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        // 改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    @Override
    protected void onDestroy()
    {
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        if (mMapView != null)
        {
            mMapView.onDestroy();
            mMapView = null;
        }
        super.onDestroy();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        if (mMapView != null)
            mMapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if (mMapView != null)
            mMapView.onPause();
    }

    // 点击了返回
    public void back(View view)
    {
        this.finish();
    }

}
