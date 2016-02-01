package wael.mobile.dev.popularmovies;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wael.mobile.dev.popularmovies.adapter.ListMoviesCursorAdapter;
import wael.mobile.dev.popularmovies.data.GenresSingleton;
import wael.mobile.dev.popularmovies.database.PopularMoviesProvider;
import wael.mobile.dev.popularmovies.database.tables.PopularMoviesTable;
import wael.mobile.dev.popularmovies.wrapper.ListIMoviesWrapper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class MovieFragment extends Fragment implements AdapterView.OnItemClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    final String selection = null;
    final String[] selecArguments = null;
    ListMoviesCursorAdapter todoAdapter;
    GenresSingleton genreData = GenresSingleton.getInstance();
    private String mParam1;
    private OnFragmentInteractionListener mListener;
    private ContentResolver cr;
    private Cursor cursor;
    private List<ListIMoviesWrapper> movieList;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;
    private ListView lvItems;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieFragment() {
    }

    public static MovieFragment newInstance(String param1) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_movies, container, false);
        lvItems = (ListView) view.findViewById(R.id.list_movies);
        // Setup cursor adapter using cursor from last step
        todoAdapter = new ListMoviesCursorAdapter(getActivity(), cursor);
        lvItems.setAdapter(todoAdapter);
        lvItems.setOnItemClickListener(this);
        //refresh data in the movieFragment
        refreshUi();
        //  movieList.clear();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            loadData();

            String item = movieList.get(position).getmIdMovie() + "&" + movieList.get(position).getmOriginalTitle() + "&" + movieList.get(position).getmBackdropPath() + "&" + movieList.get(position).getmOverView() +
                    "&" + movieList.get(position).getmOriginalLang() + "&" + movieList.get(position).getmVoteCount() + "&" + movieList.get(position).getmGenresIds() +
                    "&" + movieList.get(position).getmVoteAverage();

            mListener.onFragmentInteraction(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*deleteOldMoviesItems(movieList.size());
        movieList.clear();*/
    }

    private void deleteOldMoviesItems(int number) {
        // You should probably sort the subselect on something
        // suitable indicating its age. The COLUMN_ID should do.
        String where = PopularMoviesTable._ID + " IN (SELECT " + PopularMoviesTable._ID + " FROM " +
                PopularMoviesTable.TABLE_RECORDS + " ORDER BY " + PopularMoviesTable.ID + " LIMIT ?)";
        getActivity().getContentResolver()
                .delete(PopularMoviesProvider.RECORDS_CONTENT_URI, where, new String[]{String.valueOf(number)});
    }

    private void loadData() {
        cr = getActivity().getContentResolver();
        cursor = cr.query(PopularMoviesProvider.RECORDS_CONTENT_URI, PopularMoviesTable.PROJECTION_ALL, selection, selecArguments, null);
        movieList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ListIMoviesWrapper movie = new ListIMoviesWrapper();
                movie.setmIdMovie(cursor.getInt(cursor.getColumnIndex(PopularMoviesTable.ID)));
                movie.setmOriginalTitle(cursor.getString(cursor.getColumnIndex(PopularMoviesTable.TITLE)));
                movie.setmOverView(cursor.getString(cursor.getColumnIndex(PopularMoviesTable.OVERVIEW)));
                movie.setmBackdropPath(cursor.getString(cursor.getColumnIndex(PopularMoviesTable.BACKDROPPATH)));
                movie.setmOriginalLang(cursor.getString(cursor.getColumnIndex(PopularMoviesTable.ORIGINALLANGAGE)));
                movie.setmVoteCount(cursor.getInt(cursor.getColumnIndex(PopularMoviesTable.VOTECOUNT)));
                movie.setmGenresIds(cursor.getString(cursor.getColumnIndex(PopularMoviesTable.GENREIDS)));
                movie.setmVoteAverage(cursor.getFloat(cursor.getColumnIndex(PopularMoviesTable.VOTEAVERAGE)));
                movieList.add(movie);
                if (!cursor.isClosed()) {
                    cursor.moveToNext();
                }
            }
        }
    }

    private void refreshUi(){

        ((MainActivity) getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh(ArrayList<ListIMoviesWrapper> array, long id, String originaltitle, String overview, String backdroppath,
                                  String originallangage, int votecount, String genresids, float voteaverage) {
                // Refresh Your Fragment
   /*             movieList.clear();
                movieList.addAll(array);
                cursor = todoAdapter.swapCursor(cr.query(PopularMoviesProvider.RECORDS_CONTENT_URI, PopularMoviesTable.PROJECTION_ALL, selection, selecArguments, null));
                ListIMoviesWrapper movie = new ListIMoviesWrapper();
                movie.setmIdMovie(id);
                movie.setmOriginalTitle(originaltitle);
                movie.setmOverView(overview);
                movie.setmBackdropPath(backdroppath);
                movie.setmOriginalLang(originallangage);
                movie.setmVoteCount(votecount);
                movie.setmGenresIds(genresids);
                movie.setmVoteAverage(voteaverage);*/
                //     movieList.add(movie);
                Toast.makeText(getActivity(), "New movie added", Toast.LENGTH_LONG).show();
            }
        });

        ((MainActivity) getActivity()).setFragmentRefreshMultipleListener(new MainActivity.FragmentRefreshMultipleListener() {

            @Override
            public void onRefresh(ArrayList<ListIMoviesWrapper> array) {
                movieList.clear();
                //movieList.addAll(array);
                loadData();
                todoAdapter.changeCursor(cursor);
      /*          todoAdapter.notifyDataSetChanged();
                new ListMoviesCursorAdapter(getActivity(), cursor);
                todoAdapter.changeCursor(todoAdapter);*/
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String description);
    }

}
