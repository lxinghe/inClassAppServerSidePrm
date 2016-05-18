package com.example.lu_xi.hw8_xinghe_lu;

/**
 * Created by lu_xi on 2/19/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private static List<Map<String, ?>> mDataset;
    private Context mContext;
    private static OnItemClickListener mItemClickListener;
    private static LruCache<String, Bitmap> mImgMemoryCache;

    public MyRecyclerViewAdapter(Context myContext, List<Map<String, ?>> myDataset, LruCache<String, Bitmap> cache){
        mContext = myContext;
        mDataset = myDataset;
        mImgMemoryCache = cache;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

         ImageView vIcon;
         TextView vTitle;
         TextView vDescription;
         CheckBox vCheckBox;
         ImageView vOverflowMenu;
        //String url = "http://ia.media-imdb.com/images/M/MV5BMjExNzM0NDM0N15BMl5BanBnXkFtZTcwMzkxOTUwNw@@._V1_SY317_CR0,0,214,317_AL_.jpg";

        public ViewHolder(View v){
            super(v);
            vIcon = (ImageView)v.findViewById(R.id.movieIcon);
            vTitle = (TextView)v.findViewById(R.id.movieTitle);
            vDescription = (TextView)v.findViewById(R.id.movieDescription);
            vCheckBox = (CheckBox)v.findViewById(R.id.checkBox);
            vOverflowMenu = (ImageView)v.findViewById(R.id.pop_up_menu);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        Map<String, ?> movie = mDataset.get(getPosition());
                        final Bitmap bitmap = mImgMemoryCache.get((String) movie.get("url"));
                        if(bitmap!=null)
                            mItemClickListener.onItemClick(v, getPosition(), (String) movie.get("id"));
                        else{
                            new MyDownloadImageAsynTask(vIcon).execute((String)movie.get("url"));
                        }

                        Log.d("MovieID in adapter", (String) movie.get("id"));
                    }
                }
            });

            vOverflowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        //Map<String, ?> movie = mDataset.get(getPosition());
                        mItemClickListener.onOverFlowMenuClick(v, getPosition());
                    }
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (mItemClickListener != null) {
                        Map<String, ?> movie = mDataset.get(getPosition());
                        mItemClickListener.onItemLongClick(v, getPosition());
                    }
                    return true;
                }
            });
        }

        public void bindMovieData(Map<String,?> movie){

            //vIcon.setImageResource((Integer) movie.get("image"));
            vDescription.setText((String) movie.get("description"));
            vTitle.setText((String) movie.get("name"));
            final Bitmap bitmap = mImgMemoryCache.get((String)movie.get("url"));
            if(bitmap!=null)
                vIcon.setImageBitmap(bitmap);
            else{
                 new MyDownloadImageAsynTask(vIcon).execute((String)movie.get("url"));
            }
        }

    }


    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String,?> movie = mDataset.get(position);
        holder.bindMovieData(movie);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position, String movieID);
        void onItemLongClick(View view, int position);
        void onOverFlowMenuClick(View v, int position);
    }

    private static class MyDownloadImageAsynTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewWeakReference;
        public MyDownloadImageAsynTask(ImageView imageView){
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = null;
            for(String url : urls){
                bitmap=MyUtility.downloadImageusingHTTPGetRequest(url);
                if(bitmap!=null)
                    mImgMemoryCache.put(url,bitmap);
            }
            return bitmap;
        }

        protected void onProgressUpdate(Void... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Bitmap bitmap) {
            if(imageViewWeakReference != null && bitmap != null){
                final ImageView imageView = imageViewWeakReference.get();
                if(imageView!=null)
                    imageView.setImageBitmap(bitmap);
            }
        }
    }

}
