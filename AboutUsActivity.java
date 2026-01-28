package com.jg.scientificcalculator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AboutUsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Toolbar
        toolbar = findViewById(R.id.toolbarAbout);
        setSupportActionBar(toolbar);

        // Drawer
        drawerLayout = findViewById(R.id.drawerLayoutAbout);
        navigationView = findViewById(R.id.navigationViewAbout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Hamburger color white
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_scientific) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_age) {
            startActivity(new Intent(this, AgeCalculatorActivity.class));
        } else if (id == R.id.nav_loan) {
            startActivity(new Intent(this, LoanCalculatorActivity.class));
        } else if (id == R.id.nav_about) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        finish(); // important → return properly
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
