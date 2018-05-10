package com.cce.nkfust.tw.bentoofking;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeMapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static final int requestCodeFineLoaction = 1, requestCodeCoarseLocation = 2;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private GoogleMap mMap;
    private Context context;
    private LocationManager mLocation;
    private Boolean returnBool;
    private String Latitude,Longitude,Search;
    private ProgressDialog progressDialog;
    private Database database;
    private Store[] stores;
    private ArrayList<Store> allStore;
    private Store clickStore;
    private MainThreadHandler mainThreadHandler;
    private static final int SUCCESS = 66,SEARCH_END = 78;
    private Button researchButton;
    private Handler timerHandler;
    private ImageView storeIcon;
    private DownloadWebPicture downloadWebPicture;
    private boolean isSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Search = "%";
        isSearch = false;
        context = this;
        allStore = new ArrayList<Store>();
        researchButton = findViewById(R.id.researchButton);
        researchButton.setOnClickListener(new ResearchHandler());
        mainThreadHandler = new MainThreadHandler();
        mLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        setSupportActionBar(toolbar);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.loactionItem:
                        requestUserLocation();
                        break;
                }
                return false;
            }
        });
        timerHandler=new Handler();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(12.0f);
        mMap.setInfoWindowAdapter(new CustomizeInfoWindowAdapter(getLayoutInflater()));
        // Add a marker in Sydney and move the camera
        database = new Database();
        mMap.setOnInfoWindowClickListener(new InfoWindowClickListener());
        requestUserLocationPermission();
    }

    public class InfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener{
        @Override
        public void onInfoWindowClick(Marker marker) {
            Intent intent = new Intent();
            intent.setClass(context,CheckStoreInfo.class);
            userInfo.putStore(clickStore);
            intent.putExtra(passUserInfo,userInfo);
            intent.putExtra("storeInfo",userInfo);
            context.startActivity(intent);
        }
    }

    class CustomizeInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        LayoutInflater inflater=null;
        CustomizeInfoWindowAdapter(LayoutInflater inflater) {
            this.inflater=inflater;
        }
        // 未點選前
        public View getInfoWindow(Marker marker) {
            return(null);
        }
        // 點選後
        public View getInfoContents(Marker marker) {
            //自訂 InfoWindow layout
            clickStore = null;
            clickStore = findMarkerStore(marker);
            View popWindow = inflater.inflate(R.layout.storelistview_item, null);
            if(clickStore != null) {
                storeIcon = popWindow.findViewById(R.id.storeIcon);
                ImageView storeStatus = popWindow.findViewById(R.id.storeStatus);
                TextView storeInfoLayout3 = popWindow.findViewById(R.id.storeInfoLayout3);
                TextView storeScore = popWindow.findViewById(R.id.storeScore);
                TextView storePrice = popWindow.findViewById(R.id.storePrice);
                TextView storeDistance = popWindow.findViewById(R.id.storeDistance);
                storeInfoLayout3.setText(marker.getTitle());
                storeScore.setText("評價："+clickStore.getRank());
                storePrice.setText("平均價位："+clickStore.getPrice());
                storeDistance.setText("");
                if(setDoBusiness(clickStore.getBusinessHours())){
                    storeStatus.setImageDrawable(getResources().getDrawable(R.drawable.store_open));
                }else{
                    storeStatus.setImageDrawable(getResources().getDrawable(R.drawable.store_close));
                }
                Thread t = new Thread(new LoadImage());
                t.start();
                try{
                    t.join();
                    storeIcon.setImageBitmap(downloadWebPicture.getPhoto());
                }catch (Exception e){

                }
            }
            return(popWindow);
        }
    }
    private Boolean setDoBusiness(String storeTime){
        Time time = new Time();
        time.setToNow();
        int timeNow = time.hour*60 + time.minute;
        int amStart=0,amEnd=0,pmStart=0,pmEnd=0;
        if(storeTime.length()>=8) {
            amStart = Integer.valueOf(storeTime.substring(0, 2)) * 60 + Integer.valueOf(storeTime.substring(2, 4));
            amEnd = Integer.valueOf(storeTime.substring(4, 6)) * 60 + Integer.valueOf(storeTime.substring(6, 8));
        }
        if(storeTime.length()>=16) {
            pmStart = Integer.valueOf(storeTime.substring(8, 10)) * 60 + Integer.valueOf(storeTime.substring(10, 12));
            pmEnd = Integer.valueOf(storeTime.substring(12, 14)) * 60 + Integer.valueOf(storeTime.substring(14, 16));
        }
        if((timeNow>=amStart&&timeNow<amEnd)||(timeNow>=pmStart&&timeNow<pmEnd))
            return true;
        else
            return false;
    }
    public Store findMarkerStore(Marker marker){
        for(int i=0;i<allStore.size();i++){
            if(Double.toString(marker.getPosition().latitude).equals(allStore.get(i).getLatitude()) && Double.toString(marker.getPosition().longitude).equals(allStore.get(i).getLongitude())){
                return allStore.get(i);
            }
        }
        return null;
    }
    public class LoadImage implements Runnable{
        @Override
        public void run() {
            try {
                String[] photoString = clickStore.getPhoto().split(",");
                downloadWebPicture = new DownloadWebPicture();
                downloadWebPicture.getUrlPic("http://163.18.104.169/storeImage/"+photoString[0]);
            }catch (Exception e){
            }
        }
    }
    public class DownloadWebPicture {
        private Bitmap bmp;
        private int i;
        public Bitmap getPhoto(){
            return bmp;
        }
        public synchronized void getUrlPic(String url) {
            bmp = null;

            try {
                URL imgUrl = new URL(url);
                HttpURLConnection httpURLConnection
                        = (HttpURLConnection) imgUrl.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                int length = (int) httpURLConnection.getContentLength();
                int tmpLength = 512;
                int readLen = 0,desPos = 0;
                byte[] img = new byte[length];
                byte[] tmp = new byte[tmpLength];
                if (length != -1) {
                    while ((readLen = inputStream.read(tmp)) > 0) {
                        System.arraycopy(tmp, 0, img, desPos, readLen);
                        desPos += readLen;
                    }
                    bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
                    if(desPos != length){
                        throw new IOException("Only read" + desPos +"bytes");
                    }
                }
                httpURLConnection.disconnect();
            }
            catch (IOException e) {
                Log.e("IOException", e.toString());
            }
        }
    }
    public boolean requestUserLocationPermission() {
        int permissionFineLoaction = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionFineLoaction != PackageManager.PERMISSION_GRANTED || permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCodeFineLoaction);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, requestCodeCoarseLocation);
            return false;
        } else {
            requestUserLocation();
            return true;
        }
    }

    public void requestUserLocation() {
        //判斷當前是否已經獲得了定位權限
        progressDialog = ProgressDialog.show(context, "請稍等...", "正在取得定位資訊...", true);
        int permissionFineLoaction = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionFineLoaction != PackageManager.PERMISSION_GRANTED || permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            boolean isGPSEnabled = mLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled) {
                setupGPS();
                isGPSEnabled = mLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
            boolean isNetworkEnabled = mLocation.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            int LOCATION_UPDATE_MIN_DISTANCE = 50;
            int LOCATION_UPDATE_MIN_TIME = 1000;
            if (isNetworkEnabled || isGPSEnabled) {
                if(isGPSEnabled)
                    mLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000, 50, mLocationListener);
                if(isNetworkEnabled)
                    mLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0, 100, mLocationListener);
            }
            if (!isNetworkEnabled && !isGPSEnabled) {
                progressDialog.dismiss();
                Toast toast = Toast.makeText(context, getResources().getString(R.string.canNotGetLocation), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
    private Boolean setupGPS() {
        new AlertDialog.Builder(context).setTitle("GPS服務").setMessage("您尚未開啟定位服務，要前往設定頁面啟動定位服務嗎？")
                .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                returnBool = true;
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(context, "未開啟定位服務，定位將會不準確", Toast.LENGTH_SHORT).show();
                returnBool = false;
            }
        }).show();
        return  returnBool;
    }
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            progressDialog.dismiss();
            if (location != null) {
                LatLng nowLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nowLocation, 15));
                Latitude = Double.toString(location.getLatitude());
                Longitude = Double.toString(location.getLongitude());
                timerHandler.postDelayed(runnable, 3000);
                Thread t =new Thread(new GetStore());
                t.start();
                if (userInfo.getIdentity() == 1) {
                    userInfo.getMember().putLatitude(Latitude);
                    userInfo.getMember().putLongitude(Longitude);
                    UpdateUserLocation updateUserLocation = new UpdateUserLocation();
                    Thread thread = new Thread(updateUserLocation);
                    thread.start();
                }
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };
    public class GetStore implements Runnable{

        @Override
        public void run() {
            stores = database.GetStoreByMap(Longitude,Latitude,Search);
            if(stores.length > 0 ){
                mainThreadHandler.sendEmptyMessage(SUCCESS);
            }else{
                mainThreadHandler.sendEmptyMessage(SEARCH_END);
            }
        }
    }
    public class MainThreadHandler extends Handler {
        public MainThreadHandler() {
            super();
        }

        public MainThreadHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    for(int i=0;i<stores.length;i++){
                        LatLng storeLocation = new LatLng(Double.parseDouble(stores[i].getLatitude()),Double.parseDouble(stores[i].getLongitude()));
                        mMap.addMarker(new MarkerOptions().position(storeLocation).title(stores[i].getStoreName()));
                        allStore.add(stores[i]);

                    }
                    Thread t = new Thread(new GetStore());
                    t.start();
                break;
                case SEARCH_END:
                    if(allStore.size() == 0){
                        Toast toast = Toast.makeText(context,getResources().getString(R.string.cannotSearchStore), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    if(isSearch){
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for(int i=0;i<allStore.size();i++){
                            LatLng storeLocation = new LatLng(Double.parseDouble(allStore.get(i).getLatitude()),Double.parseDouble(allStore.get(i).getLongitude()));
                            builder.include(storeLocation);
                        }
                        LatLngBounds latLngBounds = builder.build();
                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, width, height, padding);
                        mMap.animateCamera(cu);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class UpdateUserLocation implements Runnable {
        @Override
        public void run() {
            database.UpdateMember(userInfo.getMember());
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {//根据请求码判断是哪一次申请的权限
            case requestCodeFineLoaction:
                if (grantResults.length > 0) {//grantResults 数组中存放的是授权结果
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//同意授权
                        Toast toast = Toast.makeText(context, getResources().getString(R.string.canNotGetLocation), Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    } else {
                        requestUserLocation();
                    }
                }
                break;
            case requestCodeCoarseLocation:
                if (grantResults.length > 0) {//grantResults 数组中存放的是授权结果
                    if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {//同意授权
                        Toast toast = Toast.makeText(context, getResources().getString(R.string.canNotGetLocation), Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    } else {
                        requestUserLocation();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class ResearchHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            allStore.clear();
            mMap.clear();
            researchButton.setVisibility(View.INVISIBLE);
            database.refreshStoreIndex();
            Latitude = Double.toString(mMap.getCameraPosition().target.latitude);
            Longitude = Double.toString(mMap.getCameraPosition().target.longitude);
            Thread t = new Thread(new GetStore());
            t.start();
        }
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            float[] res = new float[1];
            Location.distanceBetween(Double.parseDouble(Latitude),Double.parseDouble(Longitude),mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude,res);
            if(res[0] >= 5000 && !isSearch){
                researchButton.setVisibility(View.VISIBLE);
            }
            timerHandler.postDelayed(this, 3000);
        }
    };
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.map_toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchItem);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnCloseListener(new SearchView.OnCloseListener(){
            @Override
            public boolean onClose() {
                isSearch = false;
                Search = "%";
                allStore.clear();
                mMap.clear();
                database.refreshStoreIndex();
                Latitude = Double.toString(mMap.getCameraPosition().target.latitude);
                Longitude = Double.toString(mMap.getCameraPosition().target.longitude);
                Thread t = new Thread(new GetStore());
                t.start();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //提交按钮的点击事件
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                isSearch = true;
                Search = "%"+query+"%";
                allStore.clear();
                mMap.clear();
                database.refreshStoreIndex();
                Latitude = Double.toString(mMap.getCameraPosition().target.latitude);
                Longitude = Double.toString(mMap.getCameraPosition().target.longitude);
                Thread t = new Thread(new GetStore());
                t.start();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //当输入框内容改变的时候回调
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
