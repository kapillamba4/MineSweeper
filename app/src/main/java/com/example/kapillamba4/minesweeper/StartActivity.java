package com.example.kapillamba4.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static android.R.attr.button;

public class StartActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private Button button;
    private RadioGroup radioGroup;

    public static final String HIGH_SCORE = "high_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        editText = (EditText) findViewById(R.id.username);
        radioGroup = (RadioGroup) findViewById(R.id.difficulty);
        button = (Button) findViewById(R.id.start);
        textView = (TextView) findViewById(R.id.high_score);

        // create MineSweeper sharedPreference if not found
        final SharedPreferences sharedPreferences = getSharedPreferences("MineSweeper", MODE_PRIVATE);

        // 0 is default if HIGH_SCORE not found
        int highScore = sharedPreferences.getInt(HIGH_SCORE, 0);
        textView.setText(String.valueOf(highScore));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", editText.getText().toString());
                RadioButton checkedButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                bundle.putString("difficulty", String.valueOf(checkedButton.getText()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
