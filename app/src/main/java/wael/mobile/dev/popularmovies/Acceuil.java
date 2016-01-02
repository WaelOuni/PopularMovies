package wael.mobile.dev.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class Acceuil extends AppCompatActivity implements View.OnClickListener {

    ImageButton latest, nowplay, popular, toprated, upcoming;

    Intent inten, chooser, intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = new Intent(getApplicationContext(), MainActivity.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                inten = new Intent(Intent.ACTION_SEND);
                inten.setData(Uri.parse("mailto:"));
                //you   can
                String[] TO = {"waelounie@gmail.com"};
                inten.putExtra(Intent.EXTRA_EMAIL, TO);
                inten.putExtra(Intent.EXTRA_SUBJECT, "Salut, c'est message à part de mon application");
                inten.putExtra(Intent.EXTRA_TEXT, "opaa, Testez app PopularMovies ");
                inten.setType("message/rfc822");
                chooser = Intent.createChooser(inten, "Send mail");
                startActivity(chooser);

            }
        });


        latest = (ImageButton) findViewById(R.id.imageLatest);
        nowplay = (ImageButton) findViewById(R.id.imageNowplaying);
        popular = (ImageButton) findViewById(R.id.imagePopular);
        toprated = (ImageButton) findViewById(R.id.imageToprated);
        upcoming = (ImageButton) findViewById(R.id.imageUpcomming);

        latest.setOnClickListener(this);


        nowplay.setOnClickListener(this);

        popular.setOnClickListener(this);


        toprated.setOnClickListener(this);


        upcoming.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageLatest:
                intent.putExtra("category", "latest");
                startActivity(intent);
                break;
            case R.id.imageNowplaying:

                intent.putExtra("category", "now");
                startActivity(intent);
                break;
            case R.id.imagePopular:

                intent.putExtra("category", "popular");
                startActivity(intent);
                break;
            case R.id.imageToprated:

                intent.putExtra("category", "top");
                startActivity(intent);
                break;
            case R.id.imageUpcomming:

                intent.putExtra("category", "upcoming");
                startActivity(intent);
                break;
            default:

                break;
        }
    }
}
