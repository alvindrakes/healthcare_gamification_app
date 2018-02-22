package com.alvindrakes.loginpage;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import android.view.Window;

public class SettingPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //for navigation drawer
    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Settings");
        getSupportActionBar().show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting_page);

        myDrawer = (DrawerLayout) findViewById(R.id.myDrawer);
        myToggle = new ActionBarDrawerToggle(this, myDrawer, R.string.open, R.string.close);

        navigationView = (NavigationView) findViewById(R.id.my_navigation);
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(this);

        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (myToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.profile_page:
                Intent i = new Intent(SettingPage.this, ProfilePage.class);
                startActivity(i);
                Toast.makeText(this, "the profile is clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.setting_page:
                Intent h = new Intent(SettingPage.this, SettingPage.class);
                startActivity(h);
                Toast.makeText(this, "the setting is clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.home_page:
                Intent g = new Intent(SettingPage.this, MainActivity.class);
                startActivity(g);
                Toast.makeText(this, "the home is clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}