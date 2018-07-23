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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private ArrayList<MealClass> mealClass;
    private ArrayList<MenuItem> menuItem;
    private EditMealAdapter editMealAdapter;
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
        mealClass = new ArrayList<MealClass>();
        menuItem = new ArrayList<MenuItem>();
        getData();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        editMealAdapter = new EditMealAdapter(inflater,menuItem);
        mealListView.setAdapter(editMealAdapter);
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
        nextButton = findViewById(R.id.completeButton);
        NextButtonHandler nextButtonHandler = new NextButtonHandler();
        nextButton.setOnClickListener(nextButtonHandler);

    }

    public class NextButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(mealClass.size()==0){
                Toast.makeText(context,getResources().getString(R.string.pleaseInputOneMeal), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent();
            intent.setClass(context,EditPhotoActivity.class);
            intent.putExtra(passUserInfo,userInfo);
            intent.putExtra(passStoreInfo,store);
            intent.putExtra(passmenuInfo,mealClass);
            context.startActivity(intent);
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

    public class OnItemClickHandler implements AdapterView.OnItemClickListener{
        TextView mealNameTextView;
        TextView priceTextView;
        TextView sequenceTextView;
        EditText mealNameEditText;
        EditText priceEditText;
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
                priceEditText.setText(mealClass.get(classIndex).getMeal().get(mealIndex).getPrice());
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
                        mealClass.remove(classIndex);
                        for(int i=0;i<mealClass.size();i++){
                            int loopSequence = Integer.parseInt(mealClass.get(i).getSequence());
                            if(loopSequence > nowSequence){
                                mealClass.get(i).putSequence(Integer.toString(loopSequence-1));
                            }
                        }
                    }else{
                        int nowSequence = Integer.parseInt(mealClass.get(classIndex).getMeal().get(mealIndex).getSequence());
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
                                }
                                getData();
                                editMealAdapter.notifyDataSetChanged();
                                alertDialog.dismiss();
                            }

                        }
                    });
        }
    }

    public class FloatingActionButtonHanbler implements View.OnClickListener{
        TextView mealNameTextView;
        TextView priceTextView;
        TextView sequenceTextView;
        EditText mealNameEditText;
        EditText priceEditText;
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
                    }else{
                        mealNameTextView.setText("餐點名稱");
                        priceTextView.setVisibility(View.VISIBLE);
                        sequenceTextView.setVisibility(View.VISIBLE);
                        priceEditText.setVisibility(View.VISIBLE);
                        classSpinner.setVisibility(View.VISIBLE);
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
                                        thismeal.putName(mealNameEditText.getText().toString());
                                        thismeal.putPrice(priceEditText.getText().toString());
                                        thismeal.putMealClass(Integer.toString((int)classSpinner.getSelectedItemId()));
                                        thismeal.putSequence(Integer.toString(mealClass.get(i).getMeal().size()));
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }
}

