package com.jg.scientificcalculator;

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

import java.text.DecimalFormat;

public class LoanCalculatorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    EditText etPrincipal, etRate, etYears;
    TextView tvLoanResult;
    Button btnCalculate;
    CardView cardLoanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calculator);

        // ===== TOOLBAR =====
        toolbar = findViewById(R.id.toolbarLoan);
        setSupportActionBar(toolbar);

        // ===== DRAWER =====
        drawerLayout = findViewById(R.id.drawerLayoutLoan);
        navigationView = findViewById(R.id.navigationViewLoan);

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
        etPrincipal = findViewById(R.id.etPrincipal);
        etRate = findViewById(R.id.etRate);
        etYears = findViewById(R.id.etYears);
        tvLoanResult = findViewById(R.id.tvLoanResult);
        btnCalculate = findViewById(R.id.btnCalculateLoan);
        cardLoanResult = findViewById(R.id.cardLoanResult);

        btnCalculate.setOnClickListener(v -> calculateLoan());
    }

    private void calculateLoan() {

        if (etPrincipal.getText().toString().isEmpty()
                || etRate.getText().toString().isEmpty()
                || etYears.getText().toString().isEmpty()) {

            cardLoanResult.setVisibility(View.VISIBLE);
            tvLoanResult.setText("⚠ Please enter all values");
            return;
        }

        double p = Double.parseDouble(etPrincipal.getText().toString());
        double r = Double.parseDouble(etRate.getText().toString());
        int y = Integer.parseInt(etYears.getText().toString());

        double interest = (p * r * y) / 100;
        double total = p + interest;

        // ✅ FORMAT TO AVOID SCIENTIFIC NOTATION
        DecimalFormat df = new DecimalFormat("#,##0.00");

        cardLoanResult.setVisibility(View.VISIBLE);

        tvLoanResult.setText(
                "Principal Amount\n₹ " + df.format(p) +
                        "\n\nTotal Interest\n₹ " + df.format(interest) +
                        "\n\nTotal Payable Amount\n₹ " + df.format(total)
        );
    }

    // ===== DRAWER MENU =====
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_scientific) {
            startActivity(new Intent(this, MainActivity.class));
        }
        else if (id == R.id.nav_age) {
            startActivity(new Intent(this, AgeCalculatorActivity.class));
        } else if (id == R.id.nav_about) {
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
