package com.cce.nkfust.tw.bentoofking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditExistedMenuActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView,mealListView;
    private DrawerLayout drawerLayout;
    private Context context;
    private FloatingActionButton floatingActionButton;
    private ArrayList<MealClass> mealClass;
    private ArrayList<MenuItem> menuItem;
    private ArrayList<String> deleteClass,deleteMeal;
    private Button completeButton;
    MainThreadHandler mainThreadHandler;
    private EditMealAdapter editMealAdapter;
    String result;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_existed_menu);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        if(userInfo.getIdentity()==3) {
            userInfo.putStore((Store)intent.getSerializableExtra(passStoreInfo));
            if(userInfo.getStore().getState().equals("1")){
                toolbar.setTitle(getResources().getString(R.string.editMenu));
            }else{
                toolbar.setTitle(getResources().getString(R.string.previewStoreMenu));
            }

        }else{
            toolbar.setTitle(getResources().getString(R.string.editMenu));
        }
        mealListView = findViewById(R.id.mealListView);
        progressDialog = ProgressDialog.show(context, "請稍等...", "資料載入中...", true);
        Thread thread = new Thread(new GetMeal());
        thread.start();
        deleteClass = new ArrayList<String>();
        deleteMeal = new ArrayList<String>();
        mainThreadHandler = new MainThreadHandler();
        mealClass = new ArrayList<MealClass>();
        menuItem = new ArrayList<MenuItem>();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        editMealAdapter = new EditMealAdapter(inflater,menuItem);
        mealListView.setAdapter(editMealAdapter);
        mealListView.setOnItemClickListener(new OnItemClickHandler());
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new FloatingActionButtonHanbler());
        completeButton = findViewById(R.id.completeButton);
        completeButton.setOnClickListener(new ButtonHandler());

    }
    public class OnItemClickHandler implements AdapterView.OnItemClickListener{
        TextView mealNameTextView;
        TextView priceTextView;
        TextView sequenceTextView;
        TextView descriptionTextView;
        EditText mealNameEditText;
        EditText priceEditText;
        EditText descriptionEditText;
        Button upButton;
        Button downButton;
        int classIndex,mealIndex,sequence,nowSequence;
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            mealIndex=-1;
            sequence = 0;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.alertdialog_edit_meal,null);
            mealNameTextView = view.findViewById(R.id.mealNameTextView);
            priceTextView = view.findViewById(R.id.priceTextView);
            sequenceTextView = view.findViewById(R.id.sequenceTextView);
            mealNameEditText = view.findViewById(R.id.mealNameEditText);
            priceEditText = view.findViewById(R.id.priceEditText);
            descriptionTextView = view.findViewById(R.id.descriptionTextView);
            descriptionEditText = view.findViewById(R.id.descriptionEditText);
            upButton = view.findViewById(R.id.upButton);
            downButton = view.findViewById(R.id.downButton);
            if(menuItem.get((int)id).Price.equals("-1")){
                for(int i=0;i<mealClass.size();i++){
                    if(menuItem.get((int)id).Name.equals(mealClass.get(i).getName())){
                        classIndex = i;
                        break;
                    }
                }
                mealNameTextView.setText("類別名稱");
                mealNameEditText.setText(mealClass.get(classIndex).getName());
                priceTextView.setVisibility(View.GONE);
                priceEditText.setVisibility(View.GONE);
                descriptionTextView.setVisibility(View.GONE);
                descriptionEditText.setVisibility(View.GONE);
                nowSequence = Integer.parseInt(mealClass.get(classIndex).getSequence());
                upButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(nowSequence == 0){
                            return;
                        }else{
                            for(int i=0;i<mealClass.size();i++){
                                if(mealClass.get(i).getSequence().equals(Integer.toString(nowSequence-1))){
                                    mealClass.get(i).putSequence(Integer.toString(nowSequence));
                                    mealClass.get(classIndex).putSequence(Integer.toString(nowSequence-1));
                                    sequence--;
                                    nowSequence--;
                                    if(sequence>0){
                                        sequenceTextView.setText("位置：往下 "+sequence+" 個");
                                    }else if(sequence==0){
                                        sequenceTextView.setText("位置：");
                                    }else{
                                        sequenceTextView.setText("位置：往上 "+-sequence+" 個");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                });
                downButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(nowSequence == mealClass.size()){
                            return;
                        }else{
                            for(int i=0;i<mealClass.size();i++){
                                if(mealClass.get(i).getSequence().equals(Integer.toString(nowSequence+1))){
                                    mealClass.get(i).putSequence(Integer.toString(nowSequence));
                                    mealClass.get(classIndex).putSequence(Integer.toString(nowSequence+1));
                                    sequence++;
                                    nowSequence++;
                                    if(sequence>0){
                                        sequenceTextView.setText("位置：往下 "+sequence+" 個");
                                    }else if(sequence==0){
                                        sequenceTextView.setText("位置：");
                                    }else{
                                        sequenceTextView.setText("位置：往上 "+-sequence+" 個");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                });
            }else{
                for(int i=(int)id;i>=0;i--){
                    if(menuItem.get(i).getPrice().equals("-1")){
                        for(int j=0;j<mealClass.size();j++){
                            if(mealClass.get(j).getName().equals(menuItem.get(i).getName())){
                                classIndex = j;
                                break;
                            }
                        }
                        break;
                    }
                }
                for(int i=0;i<mealClass.get(classIndex).getMeal().size();i++){
                    if(menuItem.get((int)id).getName().equals(mealClass.get(classIndex).getMeal().get(i).getName())){
                        mealIndex = i;
                        break;
                    }
                }
                mealNameTextView.setText("餐點名稱");
                mealNameEditText.setText(mealClass.get(classIndex).getMeal().get(mealIndex).getName());
                priceTextView.setVisibility(View.VISIBLE);
                priceEditText.setVisibility(View.VISIBLE);
                descriptionTextView.setVisibility(View.VISIBLE);
                descriptionEditText.setVisibility(View.VISIBLE);
                priceEditText.setText(mealClass.get(classIndex).getMeal().get(mealIndex).getPrice());
                descriptionEditText.setText(mealClass.get(classIndex).getMeal().get(mealIndex).getDescription());
                nowSequence = Integer.parseInt(mealClass.get(classIndex).getMeal().get(mealIndex).getSequence());
                upButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(nowSequence == 0){
                            return;
                        }else{
                            for(int i=0;i<mealClass.get(classIndex).getMeal().size();i++){
                                if(mealClass.get(classIndex).getMeal().get(i).getSequence().equals(Integer.toString(nowSequence-1))){
                                    mealClass.get(classIndex).getMeal().get(i).putSequence(Integer.toString(nowSequence));
                                    mealClass.get(classIndex).getMeal().get(mealIndex).putSequence(Integer.toString(nowSequence-1));
                                    sequence--;
                                    nowSequence--;
                                    if(sequence>0){
                                        sequenceTextView.setText("位置：往下 "+sequence+" 個");
                                    }else if(sequence==0){
                                        sequenceTextView.setText("位置：");
                                    }else{
                                        sequenceTextView.setText("位置：往上 "+-sequence+" 個");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                });
                downButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(nowSequence == mealClass.get(classIndex).getMeal().size()){
                            return;
                        }else{
                            for(int i=0;i<mealClass.get(classIndex).getMeal().size();i++){
                                if(mealClass.get(classIndex).getMeal().get(i).getSequence().equals(Integer.toString(nowSequence+1))){
                                    mealClass.get(classIndex).getMeal().get(i).putSequence(Integer.toString(nowSequence));
                                    mealClass.get(classIndex).getMeal().get(mealIndex).putSequence(Integer.toString(nowSequence+1));
                                    nowSequence++;
                                    sequence++;
                                    if(sequence>0){
                                        sequenceTextView.setText("位置：往下 "+sequence+" 個");
                                    }else if(sequence==0){
                                        sequenceTextView.setText("位置：");
                                    }else{
                                        sequenceTextView.setText("位置：往上 "+-sequence+" 個");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                });
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getResources().getString(R.string.editMeal));
            builder.setView(view);
            builder.setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton(getResources().getString(R.string.delete),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(mealIndex == -1){
                        int nowSequence = Integer.parseInt(mealClass.get(classIndex).getSequence());
                        String deleteID = mealClass.get(classIndex).getID();
                        if(deleteID != "") {
                            deleteClass.add(deleteID);
                        }
                        mealClass.remove(classIndex);
                        for(int i=0;i<mealClass.size();i++){
                            int loopSequence = Integer.parseInt(mealClass.get(i).getSequence());
                            if(loopSequence > nowSequence){
                                mealClass.get(i).putSequence(Integer.toString(loopSequence-1));
                            }
                        }
                    }else{
                        int nowSequence = Integer.parseInt(mealClass.get(classIndex).getMeal().get(mealIndex).getSequence());
                        String deleteID = mealClass.get(classIndex).getMeal().get(mealIndex).getID();
                        if(deleteID != "") {
                            deleteMeal.add(deleteID);
                        }
                        mealClass.get(classIndex).getMeal().remove(mealIndex);
                        for(int i=0;i<mealClass.get(classIndex).getMeal().size();i++){
                            int loopSequence = Integer.parseInt(mealClass.get(classIndex).getMeal().get(i).getSequence());
                            if(loopSequence > nowSequence){
                                mealClass.get(classIndex).getMeal().get(i).putSequence(Integer.toString(loopSequence-1));
                            }
                        }

                    }
                    getData();
                    editMealAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(getResources().getString(R.string.check),null);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mealNameEditText.getText().toString().equals("")) {
                                Toast.makeText(context,getResources().getString(R.string.pleaseInputMealName), Toast.LENGTH_SHORT).show();
                                return;
                            }else if(priceEditText.getText().toString().equals("") && mealIndex != -1) {
                                Toast.makeText(context,getResources().getString(R.string.pleaseInputMealPrice), Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                if(mealIndex == -1){
                                    mealClass.get(classIndex).putName(mealNameEditText.getText().toString());
                                }else{
                                    mealClass.get(classIndex).getMeal().get(mealIndex).putName(mealNameEditText.getText().toString());
                                    mealClass.get(classIndex).getMeal().get(mealIndex).putPrice(priceEditText.getText().toString());
                                    mealClass.get(classIndex).getMeal().get(mealIndex).putDescription(descriptionEditText.getText().toString());
                                }
                                getData();
                                editMealAdapter.notifyDataSetChanged();
                                alertDialog.dismiss();
                            }

                        }
                    });
        }
    }
    public class ButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(mealClass.size()==0){
                Toast.makeText(context,getResources().getString(R.string.pleaseInputOneMeal), Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
            Thread t1 = new Thread(new UpdateMeal());
            t1.start();
            try {
                    t1.join();
                    if(result.equals("Successful.")) {
                        if(userInfo.getIdentity()==3){
                            finish();
                        }else {
                            Intent intent = new Intent();
                            intent.setClass(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra(passUserInfo, userInfo);
                            startActivity(intent);
                        }
                    progressDialog.dismiss();
                    Toast.makeText(context,getResources().getString(R.string.editSuc), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                        progressDialog.dismiss();
                        Toast.makeText(context,getResources().getString(R.string.editFail), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
        }
    }
    class GetMeal implements Runnable{
        @Override
        public void run() {
            try {
                Database database = new Database();
                mealClass = database.getMeal(userInfo.getStore().getID());
                mainThreadHandler.sendEmptyMessage(SUCCESS);
            }catch (Exception e){
                mainThreadHandler.sendEmptyMessage(FAIL);
            }
        }
    }
    class UpdateMeal implements Runnable{
        @Override
        public void run() {
            Database database = new Database();
            database.deleteMeal(deleteClass,deleteMeal);
            result = database.updateMeal(mealClass);
        }
    }
    public class FloatingActionButtonHanbler implements View.OnClickListener{
        TextView mealNameTextView;
        TextView priceTextView;
        TextView sequenceTextView;
        TextView descriptionTextView;
        EditText mealNameEditText;
        EditText priceEditText;
        EditText descriptionEditText;
        Spinner classSpinner;
        Spinner addTypeSpinner;
        @Override
        public void onClick(View view) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.alertdialog_add_meal,null);
            mealNameTextView = view.findViewById(R.id.mealNameTextView);
            priceTextView = view.findViewById(R.id.priceTextView);
            sequenceTextView = view.findViewById(R.id.sequenceTextView);
            mealNameEditText = view.findViewById(R.id.mealNameEditText);
            priceEditText = view.findViewById(R.id.priceEditText);
            classSpinner = view.findViewById(R.id.classSpinner);
            addTypeSpinner = view.findViewById(R.id.addTypeSpinner);
            descriptionTextView = view.findViewById(R.id.descriptionTextView);
            descriptionEditText = view.findViewById(R.id.descriptionEditText);
            classSpinner.setSelection(0);
            ArrayAdapter<CharSequence> mealAddType = new ArrayAdapter<CharSequence>(context,android.R.layout.simple_spinner_dropdown_item);
            mealAddType.add("新增類別");
            mealAddType.add("新增餐點");
            addTypeSpinner.setAdapter(mealAddType);
            if(mealClass.size() == 0){
                addTypeSpinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
            }
            addTypeSpinner.setSelection(0);
            addTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int ii, long l) {
                    if((int)l == 0){
                        mealNameTextView.setText("類別名稱");
                        priceTextView.setVisibility(View.GONE);
                        sequenceTextView.setVisibility(View.GONE);
                        priceEditText.setVisibility(View.GONE);
                        classSpinner.setVisibility(View.GONE);
                        descriptionTextView.setVisibility(View.GONE);
                        descriptionEditText.setVisibility(View.GONE);
                    }else{
                        mealNameTextView.setText("餐點名稱");
                        priceTextView.setVisibility(View.VISIBLE);
                        sequenceTextView.setVisibility(View.VISIBLE);
                        priceEditText.setVisibility(View.VISIBLE);
                        classSpinner.setVisibility(View.VISIBLE);
                        descriptionTextView.setVisibility(View.VISIBLE);
                        descriptionEditText.setVisibility(View.VISIBLE);
                        ArrayAdapter<CharSequence> mealClassName = new ArrayAdapter<CharSequence>(context,android.R.layout.simple_spinner_dropdown_item);
                        for(int i=0;i<mealClass.size();i++){
                            for(int j=0;j<mealClass.size();j++){
                                if(Integer.toString(i).equals(mealClass.get(j).getSequence()))
                                    mealClassName.add(mealClass.get(j).getName());
                            }
                        }
                        classSpinner.setAdapter(mealClassName);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getResources().getString(R.string.addMeal));
            builder.setView(view);
            builder.setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(getResources().getString(R.string.check),null);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mealNameEditText.getText().toString().equals("")) {
                                Toast.makeText(context, getResources().getString(R.string.pleaseInputMealName), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if((int)addTypeSpinner.getSelectedItemId() == 0){
                                MealClass thismealClass = new MealClass();
                                thismealClass.putID("");
                                thismealClass.putStore(userInfo.getStore().getID());
                                thismealClass.putName(mealNameEditText.getText().toString());
                                thismealClass.putSequence(Integer.toString(mealClass.size()));
                                mealClass.add(thismealClass);
                            }else{
                                if(priceEditText.getText().toString().equals("")) {
                                    Toast.makeText(context, getResources().getString(R.string.pleaseInputMealPrice), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                for(int i=0;i<mealClass.size();i++){
                                    if(Integer.toString((int)classSpinner.getSelectedItemId()).equals(mealClass.get(i).getSequence())){
                                        Meal thismeal = new Meal();
                                        thismeal.putID("");
                                        thismeal.putName(mealNameEditText.getText().toString());
                                        thismeal.putPrice(priceEditText.getText().toString());
                                        thismeal.putMealClass(Integer.toString((int)classSpinner.getSelectedItemId()));
                                        thismeal.putSequence(Integer.toString(mealClass.get(i).getMeal().size()));
                                        thismeal.putDescription(descriptionEditText.getText().toString());
                                        mealClass.get(i).getMeal().add(thismeal);
                                    }
                                }
                            }
                            getData();
                            editMealAdapter.notifyDataSetChanged();
                            alertDialog.dismiss();

                        }
                    });

        }
    }
    private void getData() {
        menuItem.clear();
        for(int i=0;i<mealClass.size();i++){
            for(int j=0;j<mealClass.size();j++){
                if(Integer.toString(i).equals(mealClass.get(j).getSequence())){
                    menuItem.add(new MenuItem(mealClass.get(j).getName(),"-1"));
                    for(int ii=0;ii<mealClass.get(j).getMeal().size();ii++){
                        for(int jj=0;jj<mealClass.get(j).getMeal().size();jj++){
                            if(Integer.toString(ii).equals(mealClass.get(j).getMeal().get(jj).getSequence())){
                                menuItem.add(new MenuItem(mealClass.get(j).getMeal().get(jj).getName(),mealClass.get(j).getMeal().get(jj).getPrice()));
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
    public class MainThreadHandler extends Handler {
        public MainThreadHandler(){
            super();
        }
        public MainThreadHandler(Looper looper){
            super(looper);
        }
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    getData();
                    editMealAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    break;
                case FAIL:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);

        }

    }
}
