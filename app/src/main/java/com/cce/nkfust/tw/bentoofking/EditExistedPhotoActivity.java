package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class EditExistedPhotoActivity extends AppCompatActivity{
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static String passmenuInfo = "MENU_INFO";
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Store store;
    private ArrayList<Meal> meal;
    private Context context;
    private ImageView mainImageView,requestImageView;
    private ImageView[] otherImageView;
    private Button nextButton;
    private ImageViewClickHandler imageViewClickHandler;
    private MainThreadHandler mainThreadHandler;
    public static final int REQUEST_EXTERNAL_STORAGE = 9;
    public static final int GALLERY_INTENT = 8;
    public static final int REQUEST_PHOTO = 7;
    private OkHttpClient client;
    int index,photoCount;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    DownloadWebPicture[] downloadWebPicture;
    PhotoThreadHandler photoThreadHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_existed_photo);
        context = this;
        client = new OkHttpClient();
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        //store =(Store) intent.getSerializableExtra(passStoreInfo);
        //meal = (ArrayList<Meal>) intent.getSerializableExtra(passmenuInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        if(userInfo.getIdentity()==3) {
            userInfo.putStore((Store)intent.getSerializableExtra(passStoreInfo));
            if(userInfo.getStore().getState().equals("1")){
                toolbar.setTitle(getResources().getString(R.string.editPhoto));
            }else {
                toolbar.setTitle(getResources().getString(R.string.previewStorePhoto));
            };
        }else {
            toolbar.setTitle(getResources().getString(R.string.editPhoto));
        }
        nextButton = findViewById(R.id.completeButton);
        mainImageView = findViewById(R.id.mainImageView);
        otherImageView = new ImageView[8];
        otherImageView[0] = findViewById(R.id.otherImageView1);
        otherImageView[1] = findViewById(R.id.otherImageView2);
        otherImageView[2] = findViewById(R.id.otherImageView3);
        otherImageView[3] = findViewById(R.id.otherImageView4);
        otherImageView[4] = findViewById(R.id.otherImageView5);
        otherImageView[5] = findViewById(R.id.otherImageView6);
        otherImageView[6] = findViewById(R.id.otherImageView7);
        otherImageView[7] = findViewById(R.id.otherImageView8);
        imageViewClickHandler = new ImageViewClickHandler();
        mainImageView.setOnClickListener(imageViewClickHandler);
        otherImageView[0].setOnClickListener(imageViewClickHandler);
        CompeleteHandler compeleteHandler = new CompeleteHandler();
        mainThreadHandler = new MainThreadHandler(Looper.getMainLooper());
        nextButton.setOnClickListener(compeleteHandler);
        photoThreadHandler = new PhotoThreadHandler();
        progressDialog = ProgressDialog.show(context, "請稍等...", "照片載入中...", true);
        Thread t = new Thread(new LoadImage());
        t.start();
    }
    public class CompeleteHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if (mainImageView.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.ic_image_add).getConstantState()) {
                Toast.makeText(context,getResources().getString(R.string.pleaseInputPhoto), Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                progressDialog = ProgressDialog.show(context, "請稍等...", "照片更新中...", true);
            }
            photoCount = 0;
            for(int i=0;i<8;i++){
                if (otherImageView[i].getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.ic_image_add).getConstantState()) {
                    break;
                }
                photoCount++;
            }
            UpdateStore updateStore = new UpdateStore();
            Thread thread = new Thread(updateStore);
            thread.start();

        }
    }
    public class LoadImage implements Runnable{

        @Override
        public void run() {
            try {
                String[] photoString = userInfo.getStore().getPhoto().split(",");
                downloadWebPicture = new DownloadWebPicture[photoString.length];
                for (int i = 1; i < photoString.length; i++) {
                    if (i < 8) {
                        otherImageView[i].setOnClickListener(imageViewClickHandler);
                    }
                }
                if (photoString.length != 9) {
                    otherImageView[photoString.length - 1].setImageDrawable(getResources().getDrawable(R.drawable.ic_image_add));
                }
                for (int i = 0; i < photoString.length; i++) {
                    photoString[i] = new String("http://163.18.104.169/storeImage/" + photoString[i]);
                    downloadWebPicture[i] = new DownloadWebPicture();
                    downloadWebPicture[i] .getUrlPic(photoString[i], i);
                    photoThreadHandler.sendEmptyMessage(i);
                }
                progressDialog.dismiss();
            }catch (Exception e){
                progressDialog.dismiss();
                photoThreadHandler.sendEmptyMessage(FAIL);
            }
        }
    }

    public class PhotoThreadHandler extends Handler {
        public PhotoThreadHandler(){
            super();
        }
        public PhotoThreadHandler(Looper looper){
            super(looper);
        }
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mainImageView.setImageBitmap(downloadWebPicture[0].getPhoto());
                    break;
                case 1:
                    otherImageView[0].setImageBitmap(downloadWebPicture[1].getPhoto());
                    break;
                case 2:
                    otherImageView[1].setImageBitmap(downloadWebPicture[2].getPhoto());
                    break;
                case 3:
                    otherImageView[2].setImageBitmap(downloadWebPicture[3].getPhoto());
                    break;
                case 4:
                    otherImageView[3].setImageBitmap(downloadWebPicture[4].getPhoto());
                    break;
                case 5:
                    otherImageView[4].setImageBitmap(downloadWebPicture[5].getPhoto());
                    break;
                case 6:
                    otherImageView[5].setImageBitmap(downloadWebPicture[6].getPhoto());
                    break;
                case 7:
                    otherImageView[6].setImageBitmap(downloadWebPicture[7].getPhoto());
                    break;
                case 8:
                    otherImageView[7].setImageBitmap(downloadWebPicture[8].getPhoto());
                    break;
                case FAIL:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);

        }

    }
    public class DownloadWebPicture {
        private Bitmap bmp;
        private int i;
        public Bitmap getPhoto(){
            return bmp;
        }
        public synchronized void getUrlPic(String url,int i) {
            this.i=i;
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
    public class UpdateStore implements Runnable{

        @Override
        public void run() {
            Database database = new Database();
            try {
                postMainImage();
                String photoString = userInfo.getStore().getID() + ".jpg";
                for (int i = 0; i < photoCount; i++) {
                    photoString += "," + userInfo.getStore().getID() + "_" + Integer.toString(i) + ".jpg";
                    postOtherImage(i);
                }
                userInfo.getStore().putPhoto(photoString);

                if(database.UpdateStore(userInfo.getStore()).equals("Successful.")) {
                    mainThreadHandler.sendEmptyMessage(SUCCESS);
                }else{
                    mainThreadHandler.sendEmptyMessage(FAIL);
                }
            }catch (Exception e){
                mainThreadHandler.sendEmptyMessage(FAIL);
            }
        }
    }

    public void postMainImage(){
        String storeId = userInfo.getStore().getID();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Bitmap bm = ((BitmapDrawable)mainImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,30,byteArrayOutputStream);
        builder.addFormDataPart("img_main",storeId+".jpg", RequestBody.create(MediaType.parse("image/jpeg"),byteArrayOutputStream.toByteArray()));
        MultipartBody build = builder.build();

        okhttp3.Request bi = new okhttp3.Request.Builder()
                .url("http://163.18.104.169/storeImage/upload.php")
                .post(build)
                .build();

        client.newCall(bi).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "onFailure");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.i("TAG", "onResponse: " + response.body().string());
                //提交成功处理结果....
            }
        });
    }
    public void postOtherImage(int i){
        String storeId = userInfo.getStore().getID();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Bitmap bm = ((BitmapDrawable)otherImageView[i].getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,30,byteArrayOutputStream);
        builder.addFormDataPart("img_"+Integer.toString(i),storeId+"_"+Integer.toString(i)+".jpg", RequestBody.create(MediaType.parse("image/jpeg"),byteArrayOutputStream.toByteArray()));

        MultipartBody build = builder.build();

        okhttp3.Request bi = new okhttp3.Request.Builder()
                .url("http://163.18.104.169/storeImage/upload.php")
                .post(build)
                .build();

        client.newCall(bi).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "onFailure");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.i("TAG", "onResponse: " + response.body().string());
                //提交成功处理结果....
            }
        });
    }
    public class ImageViewClickHandler implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            requestImageView = findViewById(view.getId());
            index = -1;
            for(int i=0;i<8;i++){
                if(requestImageView == otherImageView[i]) index = i;
            }
            if(requestImageView.getDrawable().getCurrent().getConstantState()==getResources().getDrawable(R.drawable.ic_image_add).getConstantState()){
                getFilePermission();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_PHOTO);

            }else{
                new AlertDialog.Builder(context)
                        .setItems(getResources().getStringArray(R.array.editPhoto),new DialogInterface.OnClickListener() {
                            boolean findEmpty;
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(intent, REQUEST_PHOTO);
                                        break;
                                    case 1:
                                        if(index == -1 || index == 7) {
                                            requestImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_image_add));
                                        }else{
                                            findEmpty = false;
                                            for(int i=index ; i < 7 ; i++) {
                                                if (otherImageView[i+1].getDrawable().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.ic_image_add).getConstantState()) {
                                                    otherImageView[i].setImageBitmap(((BitmapDrawable)otherImageView[i+1].getDrawable()).getBitmap());
                                                    if(i==6) otherImageView[i+1].setImageDrawable(getResources().getDrawable(R.drawable.ic_image_add));
                                                }else{
                                                    otherImageView[i].setImageDrawable(getResources().getDrawable(R.drawable.ic_image_add));
                                                    if(i!=7){
                                                        otherImageView[i+1].setImageDrawable(new ColorDrawable(0x00000000));
                                                        otherImageView[i+1].setOnClickListener(null);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                }

                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    public void getFilePermission(){
        int permission = ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE);
            return ;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_PHOTO && resultCode == RESULT_OK){
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                requestImageView.setImageBitmap(bitmap);
                if(index != -1 && index != 7){
                    otherImageView[index+1].setImageDrawable(getResources().getDrawable(R.drawable.ic_image_add));
                    otherImageView[index+1].setOnClickListener(imageViewClickHandler);
                }
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }else if(requestCode == REQUEST_PHOTO&& resultCode != RESULT_OK){

        }

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, getResources().getString(R.string.errorPermission), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class MainThreadHandler extends Handler {
        public MainThreadHandler(){
            super();
        }
        public MainThreadHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case SUCCESS:
                    Toast.makeText(context,getResources().getString(R.string.imageUpdateSuc), Toast.LENGTH_SHORT).show();
                    if(userInfo.getIdentity()==3){
                        finish();
                    }else {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setClass(context, MainActivity.class);
                        intent.putExtra(passUserInfo, userInfo);
                        startActivity(intent);
                    }
                    progressDialog.dismiss();
                    break;
                case FAIL:
                    Toast.makeText(context,getResources().getString(R.string.imageUpdateFail), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    }

}
