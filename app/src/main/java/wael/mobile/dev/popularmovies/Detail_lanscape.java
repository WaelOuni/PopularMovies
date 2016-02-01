package wael.mobile.dev.popularmovies;


import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import wael.mobile.dev.popularmovies.database.PopularMoviesProvider;
import wael.mobile.dev.popularmovies.database.tables.GenresMoviesTable;
import wael.mobile.dev.popularmovies.utils.CircleTransform;
import wael.mobile.dev.popularmovies.wrapper.ListGenresWrapper;


/**
 * A simple {@link Fragment} subclass.
 */
public class Detail_lanscape extends Fragment {
    private static String url_movie = "https://www.themoviedb.org/movie/";
    TextView txt, txt2, txt3, txt4;
    String id, title, overview, back, lang, vcount, vaverag, genresids;
    RatingBar ratingBar;
    ImageView img;
    private ContentResolver cr;
    private Cursor cursor;
    private List<ListGenresWrapper> genreList;
    public Detail_lanscape() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadData();

        View view=inflater.inflate(R.layout.fragment_detail_lanscape, container, false);
        // Inflate the layout for this fragment
        txt=(TextView)view.findViewById(R.id.descrption_landscape);
        txt2 = (TextView) view.findViewById(R.id.titleMV);
        txt3 = (TextView) view.findViewById(R.id.voteCount);
        txt4 = (TextView) view.findViewById(R.id.genreMV);

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        disableViews();
        ratingBar.setNumStars(10);

        img = (ImageView) view.findViewById(R.id.img);

        return view;
    }

    private void disableViews() {
        txt.setVisibility(View.INVISIBLE);

        txt2.setVisibility(View.INVISIBLE);

        txt3.setVisibility(View.INVISIBLE);

        txt4.setVisibility(View.INVISIBLE);

        ratingBar.setVisibility(View.INVISIBLE);

    }

    private void loadData() {
        cr = getActivity().getContentResolver();
        cursor = cr.query(PopularMoviesProvider.RECORDS_CONTENT_URI2, GenresMoviesTable.PROJECTION_ALL, null, null, null);
        genreList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ListGenresWrapper genre = new ListGenresWrapper(0, 0, "");
                genre.setmIdGenre(cursor.getInt(cursor.getColumnIndex(GenresMoviesTable.IDGENRE)));
                genre.setmNameGenre(cursor.getString(cursor.getColumnIndex(GenresMoviesTable.NAMEGENRE)));
                genreList.add(genre);
                if (!cursor.isClosed()) {
                    cursor.moveToNext();
                }
            }
        }
    }

    public void changeData(String str){
        enableViews();
        String CurrentString = str;
        if (str.contains("&")) {

            String[] tab = str.split("&");
            id = tab[0].trim();
            title = tab[1].trim();
            overview = tab[3].trim();
            lang = tab[4].trim();
            vcount = tab[5].trim();
            genresids = tab[6].trim();
            vaverag = tab[7].trim();
            back = tab[2].trim();

            Log.i("id", id);
            String[] strs = genresids.split(",");
            String[] genres = new String[strs.length];
            str = "";
            for (int i = 0; i < strs.length; i++) {
                genres[i] = strs[i];

                for (ListGenresWrapper g : genreList
                        ) {
                    if (TextUtils.equals(String.valueOf(g.getmIdGenre()), strs[i]))
                        str += g.getmNameGenre() + ", ";
                }
            }

//            Picasso.with(getActivity()).load(MainActivity.IMAGES_URL + back).transform(new CircleTransform()).into(img);
            ratingBar.setRating(Float.valueOf(vaverag));
            Picasso.with(getActivity()).load(MainActivity.IMAGES_URL + back).into(img);
            txt.setText("" + overview);
            txt2.setText(title);
            txt3.setText(vcount + " votes");
            txt4.setText(str + "(" + lang + ")");
        } else {
            txt.setText(str);
            //picasso constructor use the builder pattern   R.drawable.profile
            Picasso.with(getActivity()).load(R.drawable.profile).transform(new CircleTransform()).into(img);
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "press long to consult the movie details.", Toast.LENGTH_SHORT).show();

            }
        });
        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Uri uri = Uri.parse(url_movie + id);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return false;
            }
        });


    }

    private void enableViews() {
        txt.setVisibility(View.VISIBLE);

        txt2.setVisibility(View.VISIBLE);

        txt3.setVisibility(View.VISIBLE);

        txt4.setVisibility(View.VISIBLE);

        ratingBar.setVisibility(View.VISIBLE);

    }


}
