package com.example.lu_xi.hw8_xinghe_lu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class TaskThreeActivity extends AppCompatActivity
        implements RecyclerViewFragment.onListItemClickListener
{
    Fragment mContent;
    Toolbar mToolBar;
    ActionBar mActionBar;
    Toolbar mToolBar2;
    ImageView unhideButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_three);

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

        mToolBar2 = (Toolbar)findViewById(R.id.toolbar2);
        mToolBar2.inflateMenu(R.menu.toolbar2);
        setUpToolbarItemSelected(this);


        mActionBar.setDisplayHomeAsUpEnabled(false);

        unhideButton = (ImageView)findViewById(R.id.unhide);
        unhideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolBar2.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.task2_toolbar_menu, menu);
        return true;
    }



    public void onListItemClickListener(int movieID, String movieTitle){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, MovieFragment.newInstance(movieID, movieTitle))
                .addToBackStack(null).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    public void setUpToolbarItemSelected(final Context context){
        mToolBar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id){
                    case R.id.home:
                        intent = new Intent(context, TaskOneActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.about:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, AboutFragment.newInstance())
                                .addToBackStack(null).commit();
                        break;
                }
                return false;
            }
        });
        mToolBar2.setNavigationIcon(R.drawable.hide);
        mToolBar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolBar2.setVisibility(View.GONE);
            }
        });
    }
}
