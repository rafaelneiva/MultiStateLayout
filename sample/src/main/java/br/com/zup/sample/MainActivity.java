package br.com.zup.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.zup.multistatelayout.MultiStateLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MultiStateLayout multiStateLayout = (MultiStateLayout) findViewById(R.id.multiStateLayout);

        Button btnContent = (Button) findViewById(R.id.btnContent);
        Button btnLoading = (Button) findViewById(R.id.btnLoading);
        Button btnError = (Button) findViewById(R.id.btnError);
        Button btnEmpty = (Button) findViewById(R.id.btnEmpty);

        btnContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiStateLayout.setViewState(MultiStateLayout.State.CONTENT);
            }
        });

        btnLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiStateLayout.setViewState(MultiStateLayout.State.LOADING);
            }
        });

        btnError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiStateLayout.setViewState(MultiStateLayout.State.ERROR);
            }
        });

        btnEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiStateLayout.setViewState(MultiStateLayout.State.EMPTY);
            }
        });
    }
}
