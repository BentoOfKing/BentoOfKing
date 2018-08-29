package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passOrderID = "ORDER_ID";
    private UserInfo userInfo;
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private Toolbar toolbar;
    private ListView drawerListView,orderListView;
    private DrawerLayout drawerLayout;
    private Database database;
    private ArrayList<MemberOrder> orders;
    private ArrayList<String> store,orderCost;
    private MainThreadHandler mainThreadHandler;
    private Context context;
    private OrderListAdapter orderListAdapter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.findOrders));
        orderListView = findViewById(R.id.orderListView);
        orders = new ArrayList<MemberOrder>();
        store = new ArrayList<String>();
        orderCost = new ArrayList<String>();
        database = new Database();
        mainThreadHandler = new MainThreadHandler();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        orderListAdapter = new OrderListAdapter(inflater,orders,orderCost,store);
        orderListView.setAdapter(orderListAdapter);
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(context,CheckOrderActivity.class);
                intent.putExtra(passUserInfo,userInfo);
                if(orders.get((int)l).getState().equals("1")){
                    intent.putExtra(passOrderID,orders.get((int) l).getID()+"*");
                }else{
                    intent.putExtra(passOrderID,orders.get((int) l).getID());
                }
                startActivity(intent);
            }
        });
    }

    public class GetOrder implements Runnable{
        @Override
        public void run() {
            MemberOrder[] order = database.GetOrder(userInfo.getMember().getEmail());
            if(order != null){
                for(int i=0;i<order.length;i++){
                    String storeCost = database.GetOrderStoreCost(order[i].getID());
                    if(storeCost == null){
                        mainThreadHandler.sendEmptyMessage(FAIL);
                        return;
                    }
                    String[] token = storeCost.split(",");
                    store.add(token[0]);
                    orderCost.add(token[1]);
                    orders.add(order[i]);

                }
                mainThreadHandler.sendEmptyMessage(SUCCESS);
            }else{
                mainThreadHandler.sendEmptyMessage(FAIL);
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
                        orderListAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                        break;
                    case FAIL:
                        progressDialog.dismiss();
                        Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
    }
    @Override
    protected void onResume() {
        super.onResume();
        orders.clear();
        store.clear();
        orderCost.clear();
        database = null;
        database = new Database();
        Thread t = new Thread(new GetOrder());
        t.start();
        progressDialog = ProgressDialog.show(OrderListActivity.this, "請稍等...", "資料載入中...", true);
    }
}
class OrderListAdapter extends BaseAdapter {
    private ArrayList<MemberOrder> order;
    private ArrayList<String> orderCost;
    private ArrayList<String> store;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;


    public static class ViewHolder {
        RelativeLayout orderRelativeLayout;
        TextView storeTextView;
        TextView dateTextView;
        TextView costTextView;
    }

    public OrderListAdapter(LayoutInflater inflater, ArrayList<MemberOrder> order,ArrayList<String> orderCost,ArrayList<String> store) {
        this.order = order;
        this.orderCost = orderCost;
        this.store = store;
        this.inflater = inflater;
    }


    @Override
    public int getCount() {
        return order.size();
    }

    @Override
    public MemberOrder getItem(int i) {
        return order.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            viewHolder = new ViewHolder();
            view = this.inflater.inflate(R.layout.order_item, null);
            viewHolder.storeTextView = view.findViewById(R.id.storeTextView);
            viewHolder.dateTextView = view.findViewById(R.id.dateTextView);
            viewHolder.costTextView = view.findViewById(R.id.stateTextView);
            viewHolder.orderRelativeLayout = view.findViewById(R.id.orderRelativeLayout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.storeTextView.setText(store.get(position));
        String time = getItem(position).getTime();
        String year = time.substring(0,4);
        String month = time.substring(4,6);
        String day = time.substring(6,8);
        String hour = time.substring(8,10);
        String min = time.substring(10,12);
        viewHolder.dateTextView.setText(year+"年"+month+"月"+day+" "+hour+":"+min);
        switch (order.get(position).getState()){
            case "0":
                viewHolder.costTextView.setText("花費"+orderCost.get(position)+"元，待確認");
                break;
            case "1":
                viewHolder.costTextView.setText("花費"+orderCost.get(position)+"元，需確認");
                break;
            case "2":
                viewHolder.costTextView.setText("花費"+orderCost.get(position)+"元，已確認");
                break;
            case "3":
                viewHolder.costTextView.setText("花費"+orderCost.get(position)+"元，送餐中");
                break;
            case "4":
                viewHolder.costTextView.setText("花費"+orderCost.get(position)+"元，已完成");
                break;

        }

        return view;
    }
}