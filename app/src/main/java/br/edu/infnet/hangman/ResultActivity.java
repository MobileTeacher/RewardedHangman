package br.edu.infnet.hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String resultado = getIntent().getStringExtra("result");
        TextView textView = findViewById(R.id.result_text);
        textView.setText(resultado);
    }
}
