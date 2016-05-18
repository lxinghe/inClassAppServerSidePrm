package com.example.lu_xi.hw8_xinghe_lu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Intent;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;


public class MovieFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //HashMap<String,?> movie;
    private String movieID;
    TextView movieNameTV,movieYearIV,movieDescriptionIV,movieStarsIV,movieDirectorIV,movieLengthIV,
            movieRatingIV;
    RatingBar movieRatingBar;
    ShareActionProvider mShareActionProvider;
    MovieDataJson movieDataJson = new MovieDataJson(true);
    String movieDescrip;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public MovieFragment() {
        // Required empty public constructor
    }

    public void setPage(View view){//used to set movie page

        HashMap movie = movieDataJson.getItem(0);

        String movieName = (String)movie.get("name");
        String movieStars = (String)movie.get("stars");
        String movieYear = (String)movie.get("year");
        String movieDescription = (String)movie.get("description");
        movieDescrip = movieDescription;
        String movieDirector = (String)movie.get("director");
        String movieLength = (String)movie.get("length");
        double movieRating = (double)movie.get("rating");


        movieNameTV = (TextView)view.findViewById(R.id.movieName);
        movieStarsIV = (TextView)view.findViewById(R.id.stars);
        movieYearIV = (TextView)view.findViewById(R.id.year);
        movieDescriptionIV = (TextView)view.findViewById((R.id.description));
        movieDirectorIV = (TextView)view.findViewById(R.id.director);
        movieLengthIV = (TextView)view.findViewById(R.id.length);
        movieRatingIV = (TextView)view.findViewById(R.id.rating);
        movieRatingBar = (RatingBar)view.findViewById(R.id.ratingBar);

        movieNameTV.setText(movieName);
        movieStarsIV.setText(movieStars);
        movieDescriptionIV.setText(movieDescription);
        movieYearIV.setText("("+movieYear+")");
        movieDirectorIV.setText(movieDirector);
        movieLengthIV.setText(movieLength);
        movieRatingIV.setText(String.valueOf(movieRating)+"/10");
        movieRatingBar.setRating((float)movieRating/2);
    }

    public static MovieFragment newInstance(int position,String movieID) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //args.putInt("position", position);
        args.putString("movieID", movieID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.layout_movie_fragment, container, false);
        //index=getArguments().getInt("position");
        movieID = getArguments().getString("movieID");
        final String url = MovieDataJson.PHP_SERVER+"movies/id/"+movieID;
        Log.d("MovieURL", url);
        new MyDownloadJsonAsynTask(v).execute(url);
        Log.d("MovieJsonString", movieDataJson.getJsonString());
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        if(menu.findItem(R.id.action_share)==null)
            inflater.inflate(R.menu.fragment_menu_2, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, movieID + ": " + movieDescrip);
        mShareActionProvider.setShareIntent(intentShare);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private class MyDownloadJsonAsynTask extends AsyncTask<String, Void, MovieDataJson> {
        //private final WeakReference<MyRecyclerViewAdapter> adapterWeakReference;
        View v;

        public MyDownloadJsonAsynTask(View view){
            //adapterWeakReference = new WeakReference<MyRecyclerViewAdapter>(adapter);
            v = view;
        }

        @Override
        protected MovieDataJson doInBackground(String... urls) {
            MovieDataJson threadMovieData = new MovieDataJson(true);
            for(String url : urls){
                threadMovieData.downloadMovieDataJson(url);
            }

            if(threadMovieData.getSize()>1)
                threadMovieData.moviesList.remove(0);

            return threadMovieData;
        }

        protected void onProgressUpdate(Void... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(MovieDataJson threadMovieData) {

            movieDataJson.moviesList.clear();
            for (int i=0;i<threadMovieData.getSize();i++){
                movieDataJson.moviesList.add(threadMovieData.moviesList.get(i));
            }
            movieDataJson.setJsonString(threadMovieData.getJsonString());

            //Log.d("ThreadMovieJsonString", (String) movieDataJson.getItem(0).get("director"));
            setPage(v);
            new MyDownloadImageAsynTask((ImageView)v.findViewById(R.id.movieImage)).execute((String) movieDataJson.getItem(0).get("url"));
            //Log.d("ThreadMovieJsonString", threadMovieData.getJsonString());
        }
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
