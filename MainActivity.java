package com.jg.scientificcalculator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQ_HISTORY = 100;

    TextView display, tvEquation, tvModeIndicator;
    ImageButton btnHistory;

    HorizontalScrollView scrollDisplay, scrollEquation;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    StringBuilder expression = new StringBuilder();
    boolean isDegreeMode = true;

    HistoryDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ===== Toolbar & Drawer =====
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);

        navigationView.setNavigationItemSelectedListener(this);

        // ===== UI =====
        display = findViewById(R.id.display);
        tvEquation = findViewById(R.id.tvEquation);
        tvModeIndicator = findViewById(R.id.tvModeIndicator);
        btnHistory = findViewById(R.id.btnHistory);

        scrollDisplay = findViewById(R.id.scrollDisplay);
        scrollEquation = findViewById(R.id.scrollEquation);

        dbHelper = new HistoryDBHelper(this);

        btnHistory.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(this, HistoryActivity.class),
                        REQ_HISTORY
                )
        );

        // ===== CLEAR =====
        findViewById(R.id.btnClear).setOnClickListener(v -> {
            expression.setLength(0);
            display.setText("0");
            tvEquation.setText("");
            autoScrollInput();
        });

        // ===== DELETE =====
        findViewById(R.id.btnDel).setOnClickListener(v -> {
            if (expression.length() > 0) {
                expression.deleteCharAt(expression.length() - 1);
                display.setText(expression.length() == 0 ? "0" : expression.toString());
                autoScrollInput();
            }
        });

        // ===== NUMBERS =====
        int[] nums = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        for (int id : nums) {
            findViewById(id).setOnClickListener(v -> {
                expression.append(((Button) v).getText().toString());
                display.setText(expression.toString());
                autoScrollInput();
            });
        }

        findViewById(R.id.btnDot).setOnClickListener(v -> {
            expression.append(".");
            display.setText(expression.toString());
            autoScrollInput();
        });

        // ===== OPERATORS =====
        findViewById(R.id.btnPlus).setOnClickListener(v -> appendOperator("+"));
        findViewById(R.id.btnMinus).setOnClickListener(v -> appendOperator("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> appendOperator("×"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> appendOperator("÷"));
        findViewById(R.id.btnPower).setOnClickListener(v -> appendOperator("^"));

        // ===== EQUALS =====
        findViewById(R.id.btnEquals).setOnClickListener(v -> evaluateExpression());

        // ===== SCIENTIFIC =====
        findViewById(R.id.btnSin).setOnClickListener(v -> applyScientific("sin"));
        findViewById(R.id.btnCos).setOnClickListener(v -> applyScientific("cos"));
        findViewById(R.id.btnTan).setOnClickListener(v -> applyScientific("tan"));
        findViewById(R.id.btnLog).setOnClickListener(v -> applyScientific("log"));
        findViewById(R.id.btnLn).setOnClickListener(v -> applyScientific("ln"));
        findViewById(R.id.btnSqrt).setOnClickListener(v -> applyScientific("sqrt"));
        findViewById(R.id.btnSq).setOnClickListener(v -> applyScientific("sq"));
        findViewById(R.id.btnCube).setOnClickListener(v -> applyScientific("cube"));

        // ===== DEG / RAD =====
        findViewById(R.id.btnMode).setOnClickListener(v -> {
            isDegreeMode = !isDegreeMode;
            tvModeIndicator.setText(isDegreeMode ? "DEG" : "RAD");
            ((Button) v).setText(isDegreeMode ? "RAD" : "DEG");
        });
    }

    private void appendOperator(String op) {
        if (expression.length() == 0) return;
        char last = expression.charAt(expression.length() - 1);
        if ("+-×÷^".indexOf(last) != -1) return;
        expression.append(op);
        display.setText(expression.toString());
        autoScrollInput();
    }

    private void trimTrailingOperators() {
        while (expression.length() > 0) {
            char last = expression.charAt(expression.length() - 1);
            if ("+-×÷^".indexOf(last) != -1) {
                expression.deleteCharAt(expression.length() - 1);
            } else break;
        }
    }

    private void applyScientific(String type) {
        try {
            int i = expression.length() - 1;
            while (i >= 0 && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                i--;
            }

            String numberStr = expression.substring(i + 1);
            double v = Double.parseDouble(numberStr);
            double r = 0;

            switch (type) {
                case "sin": r = isDegreeMode ? Math.sin(Math.toRadians(v)) : Math.sin(v); break;
                case "cos": r = isDegreeMode ? Math.cos(Math.toRadians(v)) : Math.cos(v); break;
                case "tan": r = isDegreeMode ? Math.tan(Math.toRadians(v)) : Math.tan(v); break;
                case "log": r = Math.log10(v); break;
                case "ln": r = Math.log(v); break;
                case "sqrt": r = Math.sqrt(v); break;
                case "sq": r = v * v; break;
                case "cube": r = v * v * v; break;
            }

            String before = expression.substring(0, i + 1);
            String equationText = before + type + "(" + numberStr + ")";
            tvEquation.setText(equationText);

            expression.replace(i + 1, expression.length(), format(r));
            display.setText(expression.toString());

            // ❌ NO SCROLL for results
            dbHelper.insertHistory(equationText, format(r));

        } catch (Exception e) {
            display.setText("Error");
            expression.setLength(0);
        }
    }

    private void evaluateExpression() {
        try {
            trimTrailingOperators();

            if (expression.length() == 0) {
                display.setText("0");
                return;
            }

            String expr = expression.toString()
                    .replace("×", "*")
                    .replace("÷", "/");

            double result = simpleEvaluate(expr);

            dbHelper.insertHistory(expression.toString(), format(result));

            display.setText(format(result));
            tvEquation.setText(expression + " =");

            scrollDisplay.scrollTo(0, 0);

            expression.setLength(0);
            expression.append(format(result));

            // ❌ NO SCROLL for answers
        } catch (Exception e) {
            display.setText("Error");
            expression.setLength(0);
        }
    }

    private double simpleEvaluate(String expr) {
        String[] tokens = expr.split("(?=[-+*/^])|(?<=[-+*/^])");
        double result = Double.parseDouble(tokens[0]);

        for (int i = 1; i < tokens.length; i += 2) {
            double n = Double.parseDouble(tokens[i + 1]);
            switch (tokens[i]) {
                case "+": result += n; break;
                case "-": result -= n; break;
                case "*": result *= n; break;
                case "/": result /= n; break;
                case "^": result = Math.pow(result, n); break;
            }
        }
        return result;
    }

    private String format(double d) {
        return d == (long) d ? String.valueOf((long) d) : String.valueOf(d);
    }

    // ✅ AUTO SCROLL ONLY FOR INPUT
    private void autoScrollInput() {
        scrollDisplay.post(() ->
                scrollDisplay.fullScroll(HorizontalScrollView.FOCUS_RIGHT));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_HISTORY && resultCode == RESULT_OK && data != null) {
            expression.setLength(0);
            expression.append(data.getStringExtra("result"));
            display.setText(expression.toString());
            tvEquation.setText(data.getStringExtra("expression"));
            autoScrollInput();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_age) {
            startActivity(new Intent(this, AgeCalculatorActivity.class));
        } else if (id == R.id.nav_loan) {
            startActivity(new Intent(this, LoanCalculatorActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutUsActivity.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }
}
