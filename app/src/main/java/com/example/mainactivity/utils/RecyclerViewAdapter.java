package com.example.mainactivity.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mainactivity.Model;
import com.example.mainactivity.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING =0;
    private static final int VIEW_TYPE_NORMAL=1;
    private boolean isLoaderVisible = false;

    private List<Model> modelList;
    private List<Model.MData> mDataList;
    private OnRecycleClickListener onRecycleClickListener;

    public RecyclerViewAdapter(List<Model.MData> mDataList, OnRecycleClickListener onRecycleClickListener) {
        this.mDataList = mDataList;
        this.onRecycleClickListener = onRecycleClickListener;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType)
        {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyle,parent,false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.progress,parent,false));
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (isLoaderVisible)
        {
            return position == mDataList.size() -1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        }else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        holder.onBind(position);
//        holder.itemView.setOnClickListener(v -> {
//            onRecycleClickListener.onNewsClick(profileList.get(position));
//        });

    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public void addItems(ArrayList<Model.MData> data )
    {
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    public void addLoading()
    {
        Model.MData modelMdata = new Model.MData();
        isLoaderVisible = true;
        mDataList.add(modelMdata);
        notifyItemInserted(mDataList.size()-1);
    }

    public void removeLoading()
    {
        isLoaderVisible = false;
        int position = mDataList.size()-1;
        Model.MData mData = getItem(position);
        if (mDataList !=null)
        {
            mDataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear()
    {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public Model.MData getItem(int position)
    {
        return mDataList.get(position);
    }

    public class ProgressHolder extends BaseViewHolder
    {

        public ProgressHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener
    {


        @BindView(R.id.NewsImage)
        ImageView newsImage;
        @BindView(R.id.source)
        TextView newsSource;
        @BindView(R.id.category)
        TextView newsCategory;
        @BindView(R.id.publishedAT)
        TextView newsPublishedAt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }


        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            Model.MData mData = mDataList.get(position);
            Glide.with(itemView).load(mData.getAvtar()).fitCenter()
                    .into(newsImage);

            newsSource.setText(mData.getId());
            newsCategory.setText(mData.getFirst_name());
            newsPublishedAt.setText(mData.getEmail());


        }

        @Override
        public void onClick(View v) {
                onRecycleClickListener.onNewsClick(getAdapterPosition());
        }
    }

 public interface OnRecycleClickListener{
        void onNewsClick(int position);
    }
}
