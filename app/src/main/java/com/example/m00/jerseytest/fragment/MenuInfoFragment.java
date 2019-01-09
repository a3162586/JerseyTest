package com.example.m00.jerseytest.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m00.jerseytest.Menu;
import com.example.m00.jerseytest.Orderinvoice;
import com.example.m00.jerseytest.R;
import com.example.m00.jerseytest.activity.OrderAddActivity;
import com.example.m00.jerseytest.main.Util;
import com.example.m00.jerseytest.task.CommonTask;
import com.example.m00.jerseytest.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MenuInfoFragment extends Fragment {

    private RecyclerView rvMenuInfo;
    private final static String TAG = "MenuInfoFragment";
    private View view;
    private CommonTask getMenuTask;
    private ImageTask menuImageTask;
    private List<Menu> menuList;

    private OrderAddActivity oaa;
    private List<Orderinvoice> orderList = new ArrayList<>();

    public MenuInfoFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        oaa = (OrderAddActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        view = inflater.inflate(R.layout.fragment_menu_info, container, false);

        // check if the device connect to the network
        if (Util.networkConnected(getActivity())) {

            // 利用非同步任務連線到webservice
            getMenuTask = new CommonTask(Util.URL + "/order/listMenu","GET");

            try {

                // 將getMenuTask回傳的result重新轉型回List<Menu>物件
                String jsonIn = getMenuTask.execute().get();
                Type listType = new TypeToken<List<Menu>>() {
                }.getType();
                menuList = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (menuList == null || menuList.isEmpty()) {
                Util.showToast(getActivity(), R.string.msg_MenusNotFound);
            } else {
                showResult(menuList);
            }

        } else {
            Util.showToast(getActivity(), R.string.msg_NoNetwork);
        }

        return view;
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

        private List<Menu> menuList;
        private int imageSize;

        public MenuAdapter(List<Menu> menuList) {
            this.menuList = menuList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivMenu_Photo;
            private TextView tvMenu_Name,tvMenu_Price;
            private Button btnMenu_Add;

            public ViewHolder(View view) {
                super(view);
                ivMenu_Photo = view.findViewById(R.id.ivMenu_Photo);
                tvMenu_Name = view.findViewById(R.id.tvMenu_Name);
                tvMenu_Price = view.findViewById(R.id.tvMenu_Price);
                btnMenu_Add = view.findViewById(R.id.btnMenuAdd);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_menu, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Menu menu = menuList.get(position);
            holder.tvMenu_Name.setText(menu.getMenuName());
            holder.tvMenu_Price.setText("$"+menu.getMenuPrice());

            // menuImageTask傳入ViewHolder物件，處理完之後會直接將圖show在對應的view上
            String url = Util.URL + "/order/listMenuImage/" + imageSize +"/"+ menu.getMenuId();
            String reqMethod = "GET";
            menuImageTask = new ImageTask(url, reqMethod, holder.ivMenu_Photo);
            menuImageTask.execute();

            holder.btnMenu_Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 比對list中是否存在相同餐點，存在則將該餐點數量+1，不存在則加入此餐點至list
                    boolean check = false;
                    Iterator it = orderList.iterator();
                    while (it.hasNext()) {

                        Orderinvoice existOi = (Orderinvoice) it.next();
                        if(existOi.getMenuId().equals(menu.getMenuId())) {

                            existOi.setQuantity(existOi.getQuantity()+1);
                            check = true;
                        }
                    }

                    if(!check) {

                        Orderinvoice newOi = new Orderinvoice();
                        newOi.setInvoId("");
                        newOi.setOrderId("");
                        newOi.setMenuId(menu.getMenuId());
                        newOi.setInvoStatus("0");
                        newOi.setQuantity(1);
                        newOi.setMenu(menu);

                        orderList.add(newOi);
                    }

                    oaa.setOrderList(orderList);
                }
            });
        }

        @Override
        public int getItemCount() {
            return menuList.size();
        }
    }

    // 將餐點清單放入RecyclerView
    public void showResult(List<Menu> result) {

        rvMenuInfo = view.findViewById(R.id.rvMenuInfo);
        rvMenuInfo.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvMenuInfo.setLayoutManager(layoutManager);
        rvMenuInfo.setAdapter(new MenuAdapter(result));

    }

    @Override
    public void onPause() {
        if (getMenuTask != null) {
            getMenuTask.cancel(true);
            getMenuTask = null;
        }
        if (menuImageTask != null) {
            menuImageTask.cancel(true);
            menuImageTask = null;
        }
        super.onPause();
    }

}
