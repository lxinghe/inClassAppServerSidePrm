package com.example.lu_xi.hw8_xinghe_lu;

//import android.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class TaskOneActivity extends AppCompatActivity
                                implements NavigationView.OnNavigationItemSelectedListener

{
    Toolbar mToolBar;
    NavigationView navigationView;
    ActionBar mActionBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    Boolean onHomePage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tast_one);

        if (findViewById(R.id.container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            FrontPageFragment firstFragment = new FrontPageFragment();

            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, firstFragment).commit();
        }

        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mActionBar=getSupportActionBar();

        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        //mActionBar.setLogo();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,mToolBar, R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }
        };

        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        Intent intent;

       switch (id){
           case  R.id.item0:
               if(!onHomePage){
                   getSupportFragmentManager().beginTransaction()
                           .replace(R.id.container, FrontPageFragment.newInstance())
                           .addToBackStack(null).commit();
               }
               onHomePage=true;
               break;
            case R.id.item1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, AboutMeFragment.newInstance())
                        .addToBackStack(null).commit();
                onHomePage=false;
                break;
            case  R.id.item2:
                intent = new Intent(this, TaskTwoActivity.class);
                startActivity(intent);
                break;
            case R.id.item3:
                intent = new Intent(this, TaskThreeActivity.class);
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
