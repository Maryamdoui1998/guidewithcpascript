package com.sausageman.firstguide;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sausageman.firstguide.Models.Apps;

import java.util.ArrayList;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppsHolder> {
    private static final String TAG = "AppsAdapter";
    ArrayList<Apps> mApps = new ArrayList<>();
    Context context;
    OnItemClickListener mListener;

    public AppsAdapter(ArrayList<Apps> mApps, Context context) {
        this.mApps = mApps;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public AppsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer, parent, false);
        return new AppsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppsHolder holder, int position) {
        Apps apps = mApps.get(position);
        holder.textView.setText(apps.getAppName());
        //holder.textView.startScroll();

        Glide.with(context)
                .load(apps.getAppIcon())
                .centerCrop()
                .placeholder(R.drawable.loading_image)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class AppsHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public AppsHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.app_icon);
            textView = itemView.findViewById(R.id.app_name);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSelected(true);
            textView.setSingleLine(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }{

    }
}
