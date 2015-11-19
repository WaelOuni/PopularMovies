package wael.mobile.dev.popularmovies;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class Detail_lanscape extends Fragment {
    TextView txt;
    ImageView img;

    public Detail_lanscape() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_detail_lanscape, container, false);
        // Inflate the layout for this fragment
        txt=(TextView)view.findViewById(R.id.descrption_landscape);

        img = (ImageView) view.findViewById(R.id.img);
        return view;
    }


    public void changeData(String str){
        String CurrentString = str;
        if (str.contains("&")) {
            String[] separated = CurrentString.split("&");
            separated[1] = separated[1].trim();
            Picasso.with(getActivity()).load(MainActivity.IMAGES_URL + separated[1]).into(img);
            txt.setText(separated[0]);
        } else {
            txt.setText(str);
            //picasso constructor use the builder pattern
            Picasso.with(getActivity()).load(R.drawable.profile).into(img);
        }
    }


}
