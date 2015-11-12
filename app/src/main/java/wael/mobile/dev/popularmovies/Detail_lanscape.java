package wael.mobile.dev.popularmovies;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class Detail_lanscape extends Fragment {

    TextView txt;

    public Detail_lanscape() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_detail_lanscape, container, false);
        // Inflate the layout for this fragment
        txt=(TextView)view.findViewById(R.id.descrption_landscape);

        return view;
    }


    public void changeData(String str){

        txt.setText(str);
    }


}
