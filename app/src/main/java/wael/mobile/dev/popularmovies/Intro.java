package wael.mobile.dev.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Intro extends AppCompatActivity {
    ImageButton moviesBtn, famousBtn;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        moviesBtn = (ImageButton) findViewById(R.id.list_movies);

        moviesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "List Movies", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Movies.class));
            }
        });

        famousBtn = (ImageButton) findViewById(R.id.celebreties);
        famousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "List Actors and Actress", Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        // Quitter Dashboard activity
        i++;
        if (i == 1) {
            Toast.makeText(Intro.this, "Appuier une autre fois sur Retour pour Quitter",
                    Toast.LENGTH_SHORT).show();
        } else if (i > 1) {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        i = 0;
    }

}
