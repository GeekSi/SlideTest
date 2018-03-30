package com.example.siqing.slidetest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.siqing.slidetest.R;
import com.example.siqing.slidetest.modle.AppModel;
import com.example.siqing.slidetest.viewholder.AppItemHolder;

import java.util.ArrayList;
import java.util.List;

public class AppItemAdapter extends RecyclerView.Adapter<AppItemHolder> {
    private Context context;
    private List<AppModel> appModelList = new ArrayList<>();

    public AppItemAdapter(Context context, List<AppModel> appModelList) {
        this.context = context;
        this.appModelList = appModelList;
    }

    @Override
    public AppItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_layout, parent, false);
        return new AppItemHolder(view);
    }

    @Override
    public void onBindViewHolder(AppItemHolder holder, final int position) {
        final AppModel appModel = appModelList.get(position);
        holder.appIcon.setImageResource(appModel.appIconRes);
        holder.appName.setText(appModel.appName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击了 : " + appModel.appName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appModelList.size();
    }
}