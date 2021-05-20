package com.example.mainactivity.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mainactivity.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING =0;
    private static final int VIEW_TYPE_NORMAL=1;
    private boolean isLoaderVisible = false;

    private List<News> newsList;
    private OnRecycleClickListener onRecycleClickListener;

    public MovieRecyclerViewAdapter(List<News> movieList, OnRecycleClickListener onRecycleClickListener) {
        this.newsList = newsList;
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
            return position == newsList.size() -1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        }else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        holder.onBind(position);
        holder.itemView.setOnClickListener(v -> {
            onRecycleClickListener.onNewsClick(newsList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    public void addItems(List<News> movies)
    {
        newsList.addAll(movies);
        notifyDataSetChanged();
    }

    public void addLoading()
    {
        isLoaderVisible = true;
        newsList.add(new News());
        notifyItemInserted(newsList.size()-1);
    }

    public void removeLoading()
    {
        isLoaderVisible = false;
        int position = newsList.size()-1;
        News movie = getItem(position);
        if (movie!=null)
        {
            newsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear()
    {
        newsList.clear();
        notifyDataSetChanged();
    }

    public News getItem(int position)
    {
        return newsList.get(position);
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

    public class ViewHolder extends BaseViewHolder
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

            News news = newsList.get(position);
            Glide.with(itemView).load(news.getMediumCoverImage()).fitCenter()
                    .into(newsImage);

            newsSource.setText(news.getTitleLong());

        }
    }

    public interface OnRecycleClickListener{
        void onNewsClick(News news);
    }
}