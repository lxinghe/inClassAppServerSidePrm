package com.example.lu_xi.hw8_xinghe_lu;

import org.json.JSONObject;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.support.v4.view.MenuItemCompat;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class RecyclerViewFragment extends Fragment
                                    //implements View.OnClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LruCache<String, Bitmap> mImgMemoryCache;
    private static String jsonStr = "haha";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutMannager;
    MyRecyclerViewAdapter mRecyclerViewAdpater;
    MovieDataJson movieDataJson = new MovieDataJson(false);


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   private onListItemClickListener mListener;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RecyclerViewFragment newInstance() {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);

        if(mImgMemoryCache==null){
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;

            mImgMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String url = MovieDataJson.PHP_SERVER+"movies/";
        Log.d("MovieURL", url);
        new MyDownloadJsonAsynTask(mRecyclerViewAdpater).execute(url);
        Log.d("MovieJsonString", movieDataJson.getJsonString());

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);

        mRecyclerView.setHasFixedSize(true);

        mLayoutMannager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutMannager);

        mRecyclerViewAdpater = new MyRecyclerViewAdapter(getActivity(), movieDataJson.getMoviesList(),mImgMemoryCache);

        mRecyclerView.setAdapter(mRecyclerViewAdpater);

        //defaultAnimation();

        mRecyclerViewAdpater.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, String movieID) {
                Map<String, ?> movie = movieDataJson.moviesList.get(0);
                for (int i = 0; i < movieDataJson.moviesList.size(); i++) {
                    if (movieDataJson.moviesList.get(i).get("id").equals(movieID)) {
                        movie = movieDataJson.moviesList.get(i);
                    }
                }
                Bitmap bitmap = mImgMemoryCache.get((String) movie.get("url"));
                mListener.onListItemClickListener(position, movieID);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                getActivity().startActionMode(new ActionBarCallBack(position));
            }

            @Override
            public void onOverFlowMenuClick(View view, final int position) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_delete:
                                deleteItem(position);
                                return true;

                            case R.id.popup_duplicate:
                                addItem(position);
                                return true;

                            default:
                                return false;
                        }
                    }
                });

                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.pop_up_menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){//search view
        if(menu.findItem(R.id.action_search)==null)
            inflater.inflate(R.menu.fragment_menu_1, menu);
        SearchView search = (SearchView)menu.findItem(R.id.action_search).getActionView();
        if(search!=null){
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    final String url = MovieDataJson.PHP_SERVER + "movies/rating/" + query;
                    Log.d("MovieURL", url);
                    new MyDownloadJsonAsynTask(mRecyclerViewAdpater).execute(url);
                    Log.d("MovieJsonString", movieDataJson.getJsonString());

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    return true;
                }

            });
        }



        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do whatever you need

                return true; // KEEP IT TO TRUE OR IT DOESN'T OPEN !!
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do whatever you need
                final String url = MovieDataJson.PHP_SERVER+"movies/";
                //Log.d("MovieURL", url);
               new MyDownloadJsonAsynTask(mRecyclerViewAdpater).execute(url);
                Log.d("backarrow", "hey it is triggered!!");
                return true; // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onListItemClickListener) {
            mListener = (onListItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onListItemClickListener");
        }
    }

    public interface onListItemClickListener {

        void onListItemClickListener(int position, String movieTitle);
    }

    public void addItem(int moviePosition){
        movieDataJson.addItem(moviePosition + 1, movieDataJson.getItem(moviePosition));
        mRecyclerViewAdpater.notifyItemInserted(moviePosition + 1);
        final HashMap movie = movieDataJson.getItem(moviePosition+1);
        String description = (String)movie.get("description");
        description=description.replace("'","''");
        String name = (String)movie.get("name");
        name=name.replace("'","''");
        final String id = "{\"id\":\""+movie.get("id")+"\",\"name\":\""+name+"\",\"description\":\""+description+"\",\"stars\":\""+movie.get("stars")+"\",\"length\":\""+movie.get("length")+"\",\"image\":\""+movie.get("image")+"\",\"year\":\""+movie.get("year")+"\",\"rating\":\""+String.valueOf(movie.get("rating"))+"\",\"director\":\""+movie.get("director")+"\",\"url\":\""+movie.get("url")+"\"}";
        Log.d("Add movie jsonString", id);
        final String url = "http://xinghe.com/post/add";

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{

                    final JSONObject jsonObject = new JSONObject(id);
                    MyUtility.sendHttPostRequest(url, jsonObject);
                } catch (Throwable e) {
                    Log.e("Json error", "unexpected exception", e);
                }

            }
        };
        new Thread(runnable).start();
    }

    /*public void addOrDeletedataOnDB(String mUrl){
        final String url = "http://xinghe.com/post/delete";

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{

                    final JSONObject jsonObject = new JSONObject(id);
                    MyUtility.sendHttPostRequest(url, jsonObject);
                } catch (Throwable e) {
                    Log.e("Json error", "unexpected exception", e);
                }

            }
        };
        new Thread(runnable).start();
    }*/


    public void deleteItem(int movieID){
        //final HashMap movie = movieDataJson.getItem(movieID);
        final String id = "{\"id\":"+"\""+(String)movieDataJson.getItem(movieID).get("id")+"\"}";
        Log.d("Delete item", id);
        movieDataJson.removeItem(movieID);
        mRecyclerViewAdpater.notifyItemRemoved(movieID);
        final String url = "http://xinghe.com/post/delete";

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{

                    final JSONObject jsonObject = new JSONObject(id);
                    MyUtility.sendHttPostRequest(url, jsonObject);
                } catch (Throwable e) {
                    Log.e("Json error", "unexpected exception", e);
                }

            }
        };
        new Thread(runnable).start();
    }

    private void defaultAnimation(){
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(800);
        animator.setRemoveDuration(800);
        //animator.animateAdd();

        mRecyclerView.setItemAnimator(animator);
    }

    class ActionBarCallBack implements ActionMode.Callback{
        int position;

        public ActionBarCallBack(int position){this.position=position;}

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item){
            int id=item.getItemId();
            switch (id){
                case R.id.action_delete:
                    deleteItem(position);
                    mode.finish();
                    break;
                case R.id.action_duplicate:
                    addItem(position);
                    mode.finish();
                    break;
            }

            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu){
            mode.getMenuInflater().inflate(R.menu.fragment_menu_3, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode){}

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu){return false;}
    }

    private class MyDownloadJsonAsynTask extends AsyncTask<String, Void, MovieDataJson> {
        private final WeakReference<MyRecyclerViewAdapter> adapterWeakReference;
        public MyDownloadJsonAsynTask(MyRecyclerViewAdapter adapter){
            adapterWeakReference = new WeakReference<MyRecyclerViewAdapter>(adapter);
        }

        @Override
        protected MovieDataJson doInBackground(String... urls) {
            MovieDataJson threadMovieData = new MovieDataJson(false);
            for(String url : urls){
                threadMovieData.downloadMovieDataJson(url);
            }

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
            if(adapterWeakReference!=null){
                final MyRecyclerViewAdapter adapter =adapterWeakReference.get();
                if (adapter!=null)
                    adapter.notifyDataSetChanged();
            }
            //Log.d("ThreadMovieJsonString", threadMovieData.getJsonString());
        }
    }

}
