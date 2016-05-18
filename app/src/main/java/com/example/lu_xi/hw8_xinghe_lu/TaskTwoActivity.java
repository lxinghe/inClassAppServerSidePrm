package com.example.lu_xi.hw8_xinghe_lu;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;


public class TaskTwoActivity extends AppCompatActivity
        implements RecyclerViewFragment.onListItemClickListener
{
    Fragment mContent;
    Toolbar mToolBar;
    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_two);

        if(savedInstanceState!=null){
            mContent=getSupportFragmentManager().getFragment(savedInstanceState,"mContent");
        }
        else {
            mContent=RecyclerViewFragment.newInstance();
        }


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mContent)
                .addToBackStack(null).commit();

        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.task2_toolbar_menu,menu);
        return true;
    }



    public void onListItemClickListener(int id, String movieID){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, MovieFragment.newInstance(id, movieID))
                .addToBackStack(null).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

}
