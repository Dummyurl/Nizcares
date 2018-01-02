package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.DealsItem;
import com.indglobal.nizcare.model.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/2/18.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(NewsItem newsItem);
    }

    private ArrayList<NewsItem> newsItemArrayList;
    private final NewsAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivNews,ivlogo,ivSave,ivShare;
        private TextView tvDescrptn,tvLink;

        MyViewHolder(View itemView) {
            super(itemView);

            ivNews = (ImageView) itemView.findViewById(R.id.ivNews);
            ivlogo = (ImageView)itemView.findViewById(R.id.ivlogo);
            ivSave = (ImageView)itemView.findViewById(R.id.ivSave);
            ivShare = (ImageView)itemView.findViewById(R.id.ivShare);
            tvDescrptn = (TextView) itemView.findViewById(R.id.tvDescrptn);
            tvLink = (TextView)itemView.findViewById(R.id.tvLink);

        }

        void bind(final NewsItem newsItem, final NewsAdapter.OnItemClickListener listener) {

            Picasso.with(context).load(newsItem.getMedia()).into(ivNews);
            Picasso.with(context).load(newsItem.getLogo()).into(ivlogo);

            tvDescrptn.setText(newsItem.getDescription());
            tvLink.setText(newsItem.getLink());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(newsItem);
                }
            });

        }
    }

    public NewsAdapter(Context context,ArrayList<NewsItem> newsItemArrayList, NewsAdapter.OnItemClickListener listener) {
        this.newsItemArrayList = newsItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);

        return new NewsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.MyViewHolder holder, final int position) {
        holder.bind(newsItemArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return newsItemArrayList.size();
    }


}