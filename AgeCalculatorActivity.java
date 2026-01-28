package com.jg.scientificcalculator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class AgeCalculatorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    EditText etDob;
    TextView tvAgeResult;
    Button btnCalculate;
    CardView cardAgeResult;

    Calendar dobCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_calculator);

        // ===== TOOLBAR =====
        toolbar = findViewById(R.id.toolbarAge);
        setSupportActionBar(toolbar);

        // ===== DRAWER =====
        drawerLayout = findViewById(R.id.drawerLayoutAge);
        navigationView = findViewById(R.id.navigationViewAge);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 🔥 FORCE HAMBURGER ICON COLOR
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);

        navigationView.setNavigationItemSelectedListener(this);

        // ===== UI =====
        etDob = findViewById(R.id.etDob);
        tvAgeResult = findViewById(R.id.tvAgeResult);
        btnCalculate = findViewById(R.id.btnCalculateAge);
        cardAgeResult = findViewById(R.id.cardAgeResult);

        // Hide result card initially
        cardAgeResult.setVisibility(View.GONE);

        etDob.setOnClickListener(v -> showDatePicker());
        btnCalculate.setOnClickListener(v -> calculateAge());
    }

    private void showDatePicker() {
        Calendar today = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    dobCalendar.set(year, month, dayOfMonth);
                    etDob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private void calculateAge() {
        Calendar today = Calendar.getInstance();

        int years = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
        int months = today.get(Calendar.MONTH) - dobCalendar.get(Calendar.MONTH);
        int days = today.get(Calendar.DAY_OF_MONTH) - dobCalendar.get(Calendar.DAY_OF_MONTH);

        if (days < 0) {
            months--;
            today.add(Calendar.MONTH, -1);
            days += today.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        if (months < 0) {
            years--;
            months += 12;
        }

        // Show result card
        cardAgeResult.setVisibility(View.VISIBLE);

        tvAgeResult.setText(
                years + " Years, " +
                        months + " Months, " +
                        days + " Days"
        );
    }

    // ===== DRAWER MENU =====
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_scientific) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_loan) {
            startActivity(new Intent(this, LoanCalculatorActivity.class));
        }else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutUsActivity.class));
        }


        drawerLayout.closeDrawer(GravityCompat.START);
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
