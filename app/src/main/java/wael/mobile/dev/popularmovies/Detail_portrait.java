package wael.mobile.dev.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Detail_portrait extends AppCompatActivity {
    TextView textItemDetail;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_portrait);
        String id=getIntent().getStringExtra("id");
        textItemDetail=(TextView)findViewById(R.id.description_portrait);
        textItemDetail.setText(""+id);

    }
}
