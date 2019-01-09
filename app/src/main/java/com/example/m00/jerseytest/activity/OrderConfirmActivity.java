package com.example.m00.jerseytest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m00.jerseytest.Orderform;
import com.example.m00.jerseytest.Orderinvoice;
import com.example.m00.jerseytest.R;
import com.example.m00.jerseytest.main.Util;
import com.example.m00.jerseytest.task.CommonTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;

public class OrderConfirmActivity extends AppCompatActivity {

    private final static String TAG = "OrderConfirmActivity";
    private RecyclerView rvOrderDetail;
    private TextView tvDeskNum,tvTotalAmount;
    private Button btnMenuModify,btnMenuSubmit;
    private List<Orderinvoice> orderList;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    private CommonTask orderAddTask;
    private int totalAmount,discountTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        findViews();
    }

    private void findViews() {

        // 取得上個頁面傳入的總金額、餐點明細List資料
        Bundle bundle = this.getIntent().getExtras();
        totalAmount = bundle.getInt("totalAmount");
        discountTotalAmount = totalAmount;
        orderList = (List<Orderinvoice>) bundle.getSerializable("orderList");

        tvDeskNum = findViewById(R.id.tvDeskNum);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnMenuModify = findViewById(R.id.btnMenuModify);
        btnMenuSubmit = findViewById(R.id.btnMenuSubmit);
        rvOrderDetail = findViewById(R.id.rvOrderDetail);

        rvOrderDetail.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvOrderDetail.setLayoutManager(layoutManager);

        // 設定分隔線樣式
        rvOrderDetail.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rvOrderDetail.setAdapter(new OrderAdapter(orderList));

        tvDeskNum.setText("桌位01");
        tvTotalAmount.setText("$"+Integer.toString(totalAmount));

        // 返回上一頁
        btnMenuModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderConfirmActivity.this.finish();
            }
        });

        // 訂單確認送出
        btnMenuSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Util.networkConnected(OrderConfirmActivity.this)) {
                    String url = Util.URL + "/order/addOrderform";

                    Orderform order = new Orderform();
                    order.setOrderType("0");
                    if(discountTotalAmount != totalAmount)
                        order.setOrderPrice(discountTotalAmount);
                    else
                        order.setOrderPrice(totalAmount);
                    order.setOrderStatus("0");
                    order.setOrderDate(new Date());
                    order.setDelivAddres("無");
                    order.setOrderinvoiceList(orderList);

                    // 宣告JasonObject物件，利用非同步任務連線到webservice
                    String ordStr = gson.toJson(order);
                    Log.e("ordStr", ordStr);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("orderform", ordStr);

                    String jsonOut = jsonObject.toString();
                    orderAddTask = new CommonTask(url, "POST",jsonOut);

                    // 訂單新增成功會轉換至OrderAddActivity頁面，失敗則show出FailCreateOrder訊息
                    try {

                        String result = orderAddTask.execute().get();
                        JSONObject successOrder = new JSONObject(result);

                        if ("1".equals(successOrder.getString("status"))) {

                            Toast.makeText(OrderConfirmActivity.this, successOrder.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OrderConfirmActivity.this, OrderAddActivity.class);
                            Bundle bundle = new Bundle();
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else {

                            Toast.makeText(OrderConfirmActivity.this, successOrder.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }

                }

            }
        });
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

        OrderAdapter(List<Orderinvoice> orderList) {
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView serialNum,meals_Type,meals_Name,meals_Price,meals_Count;

            ViewHolder(View view) {
                super(view);
                serialNum = view.findViewById(R.id.serialNum);
                meals_Type = view.findViewById(R.id.meals_Type);
                meals_Name = view.findViewById(R.id.meals_Name);
                meals_Price = view.findViewById(R.id.meals_Price);
                meals_Count = view.findViewById(R.id.meals_Count);
            }
        }

        @NonNull
        @Override
        public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_order, parent, false);
            return new OrderAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

            final Orderinvoice orderinvoice = orderList.get(position);

            holder.serialNum.setText(Integer.toString(position+1));
            switch (orderinvoice.getMenu().getMenuType()) {
                case "0" :
                    holder.meals_Type.setText("經典餐點");
                    break;
                case "1" :
                    holder.meals_Type.setText("客製化餐點");
                    break;
            }
            holder.meals_Name.setText(orderinvoice.getMenu().getMenuName());
            if(orderinvoice.getMenu().getMenuPrice() != null)
                holder.meals_Price.setText("$"+Integer.toString(orderinvoice.getMenu().getMenuPrice()));
            holder.meals_Count.setText(Integer.toString(orderinvoice.getQuantity()));
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (orderAddTask != null) {
            orderAddTask.cancel(true);
        }
    }
}
