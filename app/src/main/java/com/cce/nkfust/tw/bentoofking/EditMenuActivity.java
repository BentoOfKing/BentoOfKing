package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class EditMenuActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static String passmenuInfo = "MENU_INFO";
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView,mealListView;
    private DrawerLayout drawerLayout;
    private Store store;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Meal> meal;
    private ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private Context context;
    private Button nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        store =(Store) intent.getSerializableExtra(passStoreInfo);
        toolbar = findViewById(R.id.toolbar);
        mealListView = findViewById(R.id.mealListView);
        meal = new ArrayList<Meal>();
        getData();
        adapter = new ArrayAdapter<String>(this,R.layout.edit_menu_item,list);
        mealListView.setAdapter(adapter);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        toolbar.setTitle(getResources().getString(R.string.editMenu));
        OnItemClickHandler onItemClickHandler = new OnItemClickHandler();
        mealListView.setOnItemClickListener(onItemClickHandler);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        FloatingActionButtonHanbler floatingActionButtonHanbler = new FloatingActionButtonHanbler();
        floatingActionButton.setOnClickListener(floatingActionButtonHanbler);
        nextButton = findViewById(R.id.nextButton);
        NextButtonHandler nextButtonHandler = new NextButtonHandler();
        nextButton.setOnClickListener(nextButtonHandler);

    }

    public class NextButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(context,EditPhotoActivity.class);
            intent.putExtra(passUserInfo,userInfo);
            //intent.putExtra(passStoreInfo,store);
            //intent.putExtra(passmenuInfo,meal);
            if(meal.size()==0){
                Toast.makeText(context,getResources().getString(R.string.pleaseInputOneMeal), Toast.LENGTH_SHORT).show();
                return;
            }
            context.startActivity(intent);
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
    public class OnItemClickHandler implements AdapterView.OnItemClickListener{
        EditText mealNameEditText;
        EditText priceEditText;
        TextView sequenceTextView;
        Spinner sequenceSpinner;
        int mealIndex,nowIndex;
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
            sequenceSpinner = view.findViewById(R.id.sequenceSpinner);
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
                    meal.remove(mealIndex);
                    for(int i=0;i<meal.size();i++){
                        int meals=Integer.parseInt(meal.get(i).getSequence());
                        if(meals >= nowIndex){
                            meal.get(i).putSequence(Integer.toString(meals-1));
                        }
                    }
                    getData();
                    adapter.notifyDataSetChanged();
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
            sequenceSpinner = view.findViewById(R.id.sequenceSpinner);
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
                                Meal addMeal = new Meal();
                                addMeal.putName(mealNameEditText.getText().toString());
                                addMeal.putPrice(priceEditText.getText().toString());
                                addMeal.putSequence(Integer.toString((int) sequenceSpinner.getSelectedItemId()));
                                for(int i=0;i<meal.size();i++){
                                    int meals=Integer.parseInt(meal.get(i).getSequence());
                                    if(meals >= (int) sequenceSpinner.getSelectedItemId()){
                                        meal.get(i).putSequence(Integer.toString(meals+1));
                                    }
                                }
                                meal.add(addMeal);
                                getData();
                                adapter.notifyDataSetChanged();
                                alertDialog.dismiss();
                            }

                        }
                    });

        }
    }
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }
}
