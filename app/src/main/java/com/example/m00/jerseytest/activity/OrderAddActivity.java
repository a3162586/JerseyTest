package com.example.m00.jerseytest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m00.jerseytest.Menu;
import com.example.m00.jerseytest.Orderinvoice;
import com.example.m00.jerseytest.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderAddActivity extends AppCompatActivity {
    private ListView menuDetail;
    private OrderinvoiceAdapter adapter;
    private List<Orderinvoice> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_add);

        Button btnMenuCancel = findViewById(R.id.btnMenuCancel);
        Button btnMenuOk = findViewById(R.id.btnMenuOk);
        menuDetail = findViewById(R.id.MenuDetail);

        // 加入上下格線，初始化orderList
        menuDetail.addHeaderView(new View(this));
        menuDetail.addFooterView(new View(this));
        initOrderList(new ArrayList<Orderinvoice>());

        // 返回上一頁
        btnMenuCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderAddActivity.this.finish();
            }
        });

        // bundle存總金額、餐點明細List﹐進入訂單確認頁面
        btnMenuOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 計算訂單總金額
                int totalAmount = CalTotalAmount();
                if(totalAmount == 0) {
                    Toast.makeText(OrderAddActivity.this, "未選擇餐點!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(OrderAddActivity.this,OrderConfirmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("totalAmount",totalAmount);
                bundle.putSerializable("orderList", (Serializable) orderList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private class OrderinvoiceAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<Orderinvoice> orderList;
        private OrderinvoiceAdapter(Context context, List<Orderinvoice> orderList) {
            this.orderList = orderList;
            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return orderList.size();
        }

        @Override
        public Object getItem(int position) {
            return orderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            ViewHolder holder;

            if (view == null) {
                holder = new ViewHolder();
                view = layoutInflater.inflate(R.layout.listview_menu, viewGroup, false);
                holder.Invo_No = view.findViewById(R.id.Invo_No);
                holder.Menu_Name = view.findViewById(R.id.Menu_Name);
                holder.Menu_Quantity = view.findViewById(R.id.Menu_quantity);
                holder.ivDelete = view.findViewById(R.id.ivDelete);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            final Orderinvoice orderinvoice = orderList.get(position);
            holder.Invo_No.setText(Integer.toString(position+1));
            holder.Menu_Name.setText(orderinvoice.getMenu().getMenuName());
            if(orderinvoice.getQuantity() != null)
                holder.Menu_Quantity.setText("X "+Integer.toString(orderinvoice.getQuantity()));
            holder.ivDelete.setImageResource(R.drawable.ic_delete_black_24dp);

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 餐點明細List減少餐點數量或是移除該項資料，通知Adapter刷新頁面
                    Orderinvoice oi = orderList.get(position);
                    if(oi.getQuantity() != 1)
                        oi.setQuantity(oi.getQuantity()-1);
                    else
                        orderList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(OrderAddActivity.this,orderinvoice.getMenu().getMenuName()+"已刪除",Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }

        private class ViewHolder {
            ImageView ivDelete;
            TextView Invo_No, Menu_Name, Menu_Quantity;
        }
    }

    // 初始化orderList
    public void initOrderList(List<Orderinvoice> orderList) {
        for(int i=1; i<=5; i++) {

            Orderinvoice oi = new Orderinvoice();
            oi.setInvoId("");
            oi.setOrderId("");
            oi.setMenuId("");
            oi.setInvoStatus("");
            oi.setQuantity(null);
            Menu menu = new Menu();
            menu.setMenuName("");
            oi.setMenu(menu);
            orderList.add(oi);
        }

        this.orderList = orderList;
        adapter = new OrderinvoiceAdapter(this,orderList);
        menuDetail.setAdapter(adapter);
    }


    // 刷新orderList
    public void setOrderList(List<Orderinvoice> orderList) {
        this.orderList = orderList;
        adapter = new OrderinvoiceAdapter(this,orderList);
        menuDetail.setAdapter(adapter);
    }

    // 計算訂單總金額
    private int CalTotalAmount() {
        int totalAmount = 0;
        try {
            for(Orderinvoice oi : orderList) {
                totalAmount += (oi.getQuantity() * oi.getMenu().getMenuPrice());
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
        return  totalAmount;
    }

}
