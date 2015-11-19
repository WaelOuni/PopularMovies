package wael.mobile.dev.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Detail_portrait extends AppCompatActivity {
    TextView textItemDetail;
    ImageView img;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_portrait);
        img = (ImageView) findViewById(R.id.img);
        textItemDetail=(TextView)findViewById(R.id.description_portrait);
        String str = getIntent().getStringExtra("description");
        String CurrentString = str;
        if (str.contains("&")) {
            String[] separated = CurrentString.split("&");
            separated[1] = separated[1].trim();
            Picasso.with(getApplicationContext()).load(MainActivity.IMAGES_URL + separated[1]).into(img);
            textItemDetail.setText("" + separated[0]);
        } else {
            textItemDetail.setText(str);
            //picasso constructor use the builder pattern
            Picasso.with(getApplicationContext()).load(R.drawable.profile).into(img);
        }
    }
}
