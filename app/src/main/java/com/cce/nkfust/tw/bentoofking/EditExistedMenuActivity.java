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
    private ArrayList<Meal> meal;
    private Button completeButton;
    MainThreadHandler mainThreadHandler;
    ArrayAdapter<String> adapter;
    private ArrayList<String> list;
    private Meal deleteMeal,addMeal;
    int mealIndex,nowIndex;
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
        mainThreadHandler = new MainThreadHandler();
        meal = new ArrayList<Meal>();
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(context,R.layout.edit_menu_item,list);
        mealListView.setAdapter(adapter);
        mealListView.setOnItemClickListener(new OnItemClickHandler());
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new FloatingActionButtonHanbler());
        completeButton = findViewById(R.id.completeButton);
        completeButton.setOnClickListener(new ButtonHandler());

    }
    public class OnItemClickHandler implements AdapterView.OnItemClickListener{
        EditText mealNameEditText;
        EditText priceEditText;
        TextView sequenceTextView;
        Spinner sequenceSpinner;
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            for(int i=0;i<meal.size();i++){
                if(meal.get(i).getSequence().equals(Integer.toString((int) id))){
                    mealIndex = i;
                    nowIndex = (int) id;
                    break;
                }
            }
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.alertdialog_add_meal,null);
            mealNameEditText = view.findViewById(R.id.mealNameEditText);
            mealNameEditText.setText(meal.get(mealIndex).getName());
            priceEditText = view.findViewById(R.id.priceEditText);
            priceEditText.setText(meal.get(mealIndex).getPrice());
            //sequenceSpinner = view.findViewById(R.id.sequenceSpinner);
            sequenceTextView = view.findViewById(R.id.sequenceTextView);
            sequenceTextView.setText(getResources().getString(R.string.moveTo));
            ArrayAdapter<CharSequence> mealName = new ArrayAdapter<CharSequence>(context,android.R.layout.simple_spinner_dropdown_item);
            for(int i=0;i<meal.size();i++){
                for(int j=0;j<meal.size();j++){
                    if(Integer.toString(i).equals(meal.get(j).getSequence()))
                        mealName.add(meal.get(j).getName()+" "+getResources().getString(R.string.someonePosition));
                }
            }
            sequenceSpinner.setAdapter(mealName);
            sequenceSpinner.setSelection(nowIndex);
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
                    progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                    deleteMeal = meal.get(mealIndex);
                    Thread t = new Thread(new DeleteMeal());
                    t.start();
                    try{
                        t.join();
                        if(result.equals("Successful.")) {
                            meal.remove(mealIndex);
                            for (int i = 0; i < meal.size(); i++) {
                                int meals = Integer.parseInt(meal.get(i).getSequence());
                                if (meals >= nowIndex) {
                                    meal.get(i).putSequence(Integer.toString(meals - 1));
                                }
                            }
                            Thread t1 = new Thread(new UpdateMeal());
                            t1.start();
                            t1.join();
                            getData();
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                            Toast.makeText(context,getResources().getString(R.string.deleteSuc), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(context,getResources().getString(R.string.deleteFail), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){

                    }

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
                            }else if(priceEditText.getText().toString().equals("")) {
                                Toast.makeText(context,getResources().getString(R.string.pleaseInputMealPrice), Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                int selectIndex = (int) sequenceSpinner.getSelectedItemId();
                                if(selectIndex > nowIndex){
                                    for(int i=0;i<meal.size();i++){
                                        int meals=Integer.parseInt(meal.get(i).getSequence());
                                        if(meals <= selectIndex && meals > nowIndex){
                                            meal.get(i).putSequence(Integer.toString(meals-1));
                                        }
                                    }
                                }else if(selectIndex < nowIndex){
                                    for(int i=0;i<meal.size();i++){
                                        int meals=Integer.parseInt(meal.get(i).getSequence());
                                        if(meals >= selectIndex && meals < nowIndex){
                                            meal.get(i).putSequence(Integer.toString(meals+1));
                                        }
                                    }
                                }
                                meal.get(mealIndex).putName(mealNameEditText.getText().toString());
                                meal.get(mealIndex).putPrice(priceEditText.getText().toString());
                                meal.get(mealIndex).putSequence(Integer.toString(selectIndex));
                                getData();
                                adapter.notifyDataSetChanged();
                                alertDialog.dismiss();
                            }

                        }
                    });
        }
    }
    public class ButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(meal.size()==0){
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
    class AddMeal implements Runnable {
        @Override
        public void run() {
            Database database = new Database();
            ArrayList<Meal> m = new ArrayList<Meal>();
            m.add(addMeal);
            //result = database.addMeal(m);
        }
    }
    class DeleteMeal implements Runnable {
        @Override
        public void run() {
            Database database = new Database();
            result = database.deleteMeal(deleteMeal.getID());
        }
    }
    class GetMeal implements Runnable{
        @Override
        public void run() {
            try {
                Database database = new Database();
                meal = database.getMeal(userInfo.getStore().getID());
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
            result = database.updateMeal(meal);
        }
    }
    public class FloatingActionButtonHanbler implements View.OnClickListener{
        EditText mealNameEditText;
        EditText priceEditText;
        Spinner sequenceSpinner;
        @Override
        public void onClick(View view) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.alertdialog_add_meal,null);
            mealNameEditText = view.findViewById(R.id.mealNameEditText);
            priceEditText = view.findViewById(R.id.priceEditText);
            //sequenceSpinner = view.findViewById(R.id.sequenceSpinner);
            sequenceSpinner.setSelection(0);
            ArrayAdapter<CharSequence> mealName = new ArrayAdapter<CharSequence>(context,android.R.layout.simple_spinner_dropdown_item);
            for(int i=0;i<meal.size();i++){
                for(int j=0;j<meal.size();j++){
                    if(Integer.toString(i).equals(meal.get(j).getSequence()))
                        mealName.add(meal.get(j).getName()+" "+getResources().getString(R.string.someonePosition));
                }
            }
            if(meal.size() ==0 ){
                mealName.add(getResources().getString(R.string.first));
            }
            sequenceSpinner.setAdapter(mealName);
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
                                Toast.makeText(context,getResources().getString(R.string.pleaseInputMealName), Toast.LENGTH_SHORT).show();
                                return;
                            }else if(priceEditText.getText().toString().equals("")) {
                                Toast.makeText(context,getResources().getString(R.string.pleaseInputMealPrice), Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                                addMeal = new Meal();
                                //addMeal.putStore(userInfo.getStore().getID());
                                addMeal.putName(mealNameEditText.getText().toString());
                                addMeal.putPrice(priceEditText.getText().toString());
                                addMeal.putSequence(Integer.toString((int) sequenceSpinner.getSelectedItemId()));
                                Thread t = new Thread(new AddMeal());
                                t.start();
                                try {
                                    t.join();
                                    try {
                                        int ID = Integer.parseInt(result);
                                        for (int i = 0; i < meal.size(); i++) {
                                            int meals = Integer.parseInt(meal.get(i).getSequence());
                                            if (meals >= (int) sequenceSpinner.getSelectedItemId()) {
                                                meal.get(i).putSequence(Integer.toString(meals + 1));
                                            }
                                        }
                                        addMeal.putID(result);
                                        meal.add(addMeal);
                                        Thread t1 = new Thread(new UpdateMeal());
                                        t1.start();
                                        t1.join();
                                        getData();
                                        adapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                        Toast.makeText(context,getResources().getString(R.string.addSuc), Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    }catch (Exception e){
                                        progressDialog.dismiss();
                                        Toast.makeText(context,getResources().getString(R.string.addFail), Toast.LENGTH_SHORT).show();

                                    }
                                }catch (Exception e){

                                }
                            }

                        }
                    });

        }
    }
    private void getData() {
        list.clear();
        for(int i=0;i<meal.size();i++){
            for(int j=0;j<meal.size();j++){
                if(Integer.toString(i).equals(meal.get(j).getSequence())){
                    list.add(meal.get(j).getName()+"，"+getResources().getString(R.string.price)+" "+meal.get(j).getPrice()+" 元");
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
                    adapter.notifyDataSetChanged();
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
