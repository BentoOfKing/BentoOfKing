package com.cce.nkfust.tw.bentoofking;

import android.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditPhotoActivity extends AppCompatActivity {
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
    private ImageView mainImageView;
    private ImageView[] otherImageView;
    private Button quickChooseButton,nextButton;
    public static final int REQUEST_EXTERNAL_STORAGE = 9;
    public static final int GALLERY_INTENT = 8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        store =(Store) intent.getSerializableExtra(passStoreInfo);
        meal = (ArrayList<Meal>) intent.getSerializableExtra(passmenuInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        toolbar.setTitle(getResources().getString(R.string.editPhoto));
        quickChooseButton = findViewById(R.id.quickChooseButton);
        nextButton = findViewById(R.id.nextButton);
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
        ImageViewClickHandler imageViewClickHandler = new ImageViewClickHandler();
        mainImageView.setOnClickListener(imageViewClickHandler);
        for(int i=0;i<8;i++){
            otherImageView[i].setOnClickListener(imageViewClickHandler);
        }
    }
    public class ImageViewClickHandler implements View.OnClickListener{
        ImageView imageView;
        @Override
        public void onClick(View view) {
            imageView = findViewById(view.getId());
            if(imageView.getDrawable().getCurrent().getConstantState()==getResources().getDrawable(R.drawable.ic_image_add).getConstantState()){
                getFilePermission();
            }else{
                new AlertDialog.Builder(context)
                        .setItems(getResources().getStringArray(R.array.editPhoto),new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

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

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

        }else if(requestCode == GALLERY_INTENT&& resultCode != RESULT_OK){

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

}
