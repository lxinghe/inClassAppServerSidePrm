package com.example.lu_xi.hw8_xinghe_lu;

/**
 * Created by lu_xi on 3/23/2016.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDataJson {
    public static final String PHP_SERVER = "http://xinghe.com/";
    List<Map<String,?>> moviesList = new ArrayList<Map<String,?>>();;
    private String jsonString ="{\"movies\":";

    public MovieDataJson(boolean inMovieFragment){
        if(inMovieFragment)
            moviesList.add(createMovie("", "", "", "", "", "", 0.0, "", "", ""));
    };

    public List<Map<String, ?>> getMoviesList() {
        return moviesList;
    }

    public int getSize(){
        return moviesList.size();
    }

    public HashMap getItem(int i){
        if (i >=0 && i < moviesList.size()){
            return (HashMap) moviesList.get(i);
        } else return null;
    }

    public void downloadMovieDataJson(String moviesurl){
        jsonString = jsonString + MyUtility.downloadJSONusingHTTPGetRequest(moviesurl)+"}";
        this.parseData();

    }

    public void removeItem(int movieID){
        moviesList.remove(movieID);
    }

    public void addItem(int position, HashMap item){
        moviesList.add(position, createMovie((String) item.get("id") +"new"+ Integer.toString(countMovie(position)), (String) item.get("name"), (String) item.get("image"), (String) item.get("description"), (String) item.get("year"), (String) item.get("length"), (Double) item.get("rating"), (String) item.get("director"), (String) item.get("stars"), (String) item.get("url")));
    }

    public int countMovie(int position){
        HashMap movie = this.getItem(position-1);
        int count = 0;
        for(int i=0;i<moviesList.size();i++){
            //Log.d("i=",Integer.toString(i));
            //Log.d("position=",Integer.toString(position-1));
            //Log.d("size=",Integer.toString(this.getSize()));
            if(this.getItem(i).get("name").equals(movie.get("name")))
                count++;
        }
        Log.d("count=",Integer.toString(count));
        return count;
    }

    public void parseData(){
        String id, name, description, length, year, director, stars, url, image;
        double rating;

        try {

            JSONObject jsonObj = new JSONObject(jsonString);
            Log.d("Movie jsonObj", jsonObj.toString());
            JSONArray jsonArray = jsonObj.getJSONArray("movies");
            Log.d("Movie jsonArr", jsonArray.toString());

            for(int i=0;i<jsonArray.length();i++) {
                JSONObject movie = jsonArray.getJSONObject(i);
                id = (String)movie.get("id");
                name = (String)movie.get("name");
                Log.d("Movie id", id);
                description = (String)movie.get("description");
                length = (String)movie.get("length");
                director = (String)movie.get("director");
                stars = (String)movie.get("stars");
                url = (String)movie.get("url");
                year = (String) movie.get("year");
                image = (String)movie.get("image");
                rating = Double.parseDouble((String) movie.get("rating"));
                moviesList.add(createMovie(id, name, image, description, year, length, rating, director, stars, url));
            }
            //Log.d("Movie ", movie.toString());
        } catch (Throwable e) {
            Log.e("Json error", "unexpected exception", e);
        }
    }

    public String getJsonString()
    {
        return jsonString;
    }

    public void setJsonString(String str){
        jsonString = jsonString + str;
    }

    private HashMap createMovie(String id, String name, String image, String description, String year,
                                String length, double rating, String director, String stars, String url) {
        HashMap movie = new HashMap();
        movie.put("id",id);
        movie.put("image",image);
        movie.put("name", name);
        movie.put("description", description);
        movie.put("year", year);
        movie.put("length",length);
        movie.put("rating",rating);
        movie.put("director",director);
        movie.put("stars",stars);
        movie.put("url",url);
        movie.put("selection",false);
        return movie;
    }
}
