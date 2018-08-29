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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class EditPhotoActivity extends AppCompatActivity{
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
    private ArrayList<MealClass> mealClass;
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
    private ProgressDialog progressDialog = null;
    private String[] photoText = {"","","","","","","","",""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        context = this;
        client = new OkHttpClient();
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        store =(Store) intent.getSerializableExtra(passStoreInfo);
        mealClass = (ArrayList<MealClass>) intent.getSerializableExtra(passmenuInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        toolbar.setTitle(getResources().getString(R.string.editPhoto));
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
        NextHandler nextHandler = new NextHandler();
        mainThreadHandler = new MainThreadHandler(Looper.getMainLooper());
        nextButton.setOnClickListener(nextHandler);
    }
    public class NextHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if (mainImageView.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.ic_image_add).getConstantState()) {
                Toast.makeText(context,getResources().getString(R.string.pleaseInputPhoto), Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                progressDialog = ProgressDialog.show(EditPhotoActivity.this, "請稍等...", "店家新增中...", true);
            }
            photoCount = 0;
            for(int i=0;i<8;i++){
                if (otherImageView[i].getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.ic_image_add).getConstantState()) {
                    break;
                }
                photoCount++;
            }
            AddStore addStore = new AddStore();
            Thread thread = new Thread(addStore);
            thread.start();





        }
    }
    public class AddStore implements Runnable{

        @Override
        public void run() {
            Database database = new Database();
            try {
                store.putRank("3");
                store.putState("0");
                store.putPoint("0");
                if(userInfo.getIdentity() == 1){
                    store.putNote("由 "+userInfo.getMember().getEmail()+" 新增");
                }else {
                    store.putNote("");
                }
                int price = 0,mealCount = 0;
                for(int i=0;i<mealClass.size();i++){
                    for(int j=0;j<mealClass.get(i).getMeal().size();j++) {
                        price += Integer.parseInt(mealClass.get(i).getMeal().get(j).getPrice());
                        mealCount++;
                    }
                }
                price /= mealCount;
                store.putPrice(Integer.toString(price));
                store.putID(database.addStore(store));
                String photoString = store.getID() + ".jpg";
                String photoTextStr = photoText[0];
                postMainImage();
                for (int i = 0; i < photoCount; i++) {
                    photoString += "," + store.getID() + "_" + Integer.toString(i) + ".jpg";
                    photoTextStr += ","+photoText[i+1];
                    postOtherImage(i);
                }
                store.putPhoto(photoString);
                store.putPhotoText(photoTextStr);
                database.UpdateStore(store);
                for(int i=0;i<mealClass.size();i++){
                    mealClass.get(i).putStore(store.getID());
                }
                database.addMeal(mealClass);
                mainThreadHandler.sendEmptyMessage(SUCCESS);

            }catch (Exception e){
                mainThreadHandler.sendEmptyMessage(FAIL);

            }
        }
    }
    public void postMainImage(){
        String storeId = store.getID();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Bitmap bm = ((BitmapDrawable)mainImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
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
        String storeId = store.getID();
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
                                            case 2:
                                                LayoutInflater inflater = LayoutInflater.from(EditPhotoActivity.this);
                                                View view = inflater.inflate(R.layout.alertdialog_photo_text, null);
                                                AlertDialog.Builder builder = new AlertDialog.Builder(EditPhotoActivity.this);
                                                builder.setTitle("輸入圖片備註");
                                                builder.setView(view);
                                                final EditText photoTextEditText = view.findViewById(R.id.photoTextEditText);
                                                index++;
                                                photoTextEditText.setText(photoText[index]);
                                                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        photoText[index] = photoTextEditText.getText().toString();
                                                    }
                                                });
                                                AlertDialog alertDialog = builder.create();
                                                alertDialog.show();
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
                    Toast.makeText(context,getResources().getString(R.string.addStoreSuccessful), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setClass(context,MainActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    startActivity(intent);
                    progressDialog.dismiss();
                    break;
                case FAIL:
                    Toast.makeText(context,getResources().getString(R.string.addStoreFail), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    }

}
