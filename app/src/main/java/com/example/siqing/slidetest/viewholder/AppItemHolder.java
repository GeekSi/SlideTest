package com.example.siqing.slidetest.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.siqing.slidetest.R;

public class AppItemHolder extends RecyclerView.ViewHolder {
    public ImageView appIcon;
    public TextView appName;

    public AppItemHolder(View itemView) {
        super(itemView);
        appIcon = itemView.findViewById(R.id.app_icon_img);
        appName = itemView.findViewById(R.id.app_name_text);
    }
}
