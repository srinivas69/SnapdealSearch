package com.srinivasworks.snapdealsearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dell on 23/10/17.
 */

public class MainRecyclerviewAdapter extends RecyclerView.Adapter<MainRecyclerviewAdapter.ViewHolder> {

    private Context context;
    private List<DataModelClass> dataList;

    public MainRecyclerviewAdapter(Context context, List<DataModelClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_rl_view_item_layout, parent, false);
        return new MainRecyclerviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DataModelClass obj = dataList.get(position);

        String title = obj.getTitle();
        String url = obj.getUrl();

        holder.titleTv.setText(title);

        Picasso.with(context).load(url).into(holder.pdtIv);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTv;
        private ImageView pdtIv;
        public ViewHolder(View itemView) {
            super(itemView);

            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            pdtIv = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
