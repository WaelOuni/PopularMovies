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
        final ListView lvItems = (ListView) view.findViewById(R.id.list_movies);
        // Setup cursor adapter using cursor from last step
        todoAdapter = new ListMoviesCursorAdapter(getActivity(), cursor);
        lvItems.setAdapter(todoAdapter);
        lvItems.setOnItemClickListener(this);
        //refresh data in the movieFragment
        refreshUi();

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
            mListener.onFragmentInteraction(movieList.get(position).getDescription());
        }
    }

    private void loadData() {
        cr = getActivity().getContentResolver();
        cursor = cr.query(PopularMoviesProvider.RECORDS_CONTENT_URI, PopularMoviesTable.PROJECTION_ALL, selection, selecArguments, null);
        movieList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ListIMoviesWrapper movie = new ListIMoviesWrapper();
                movie.setTitle(cursor.getString(cursor.getColumnIndex(PopularMoviesTable.LABEL)));
                movie.setDescription(cursor.getString(cursor.getColumnIndex(PopularMoviesTable.DESCRIPTION)));
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
            public void onRefresh(ArrayList<ListIMoviesWrapper> array, String title, String description) {
                // Refresh Your Fragment
                movieList.clear();
                movieList.addAll(array);
                cursor = todoAdapter.swapCursor(cr.query(PopularMoviesProvider.RECORDS_CONTENT_URI, PopularMoviesTable.PROJECTION_ALL, selection, selecArguments, null));
                ListIMoviesWrapper movie = new ListIMoviesWrapper();
                movie.setTitle(title);
                movie.setDescription(description);
                movieList.add(movie);
                Toast.makeText(getActivity(), "New movie added", Toast.LENGTH_LONG).show();
            }
        });

        ((MainActivity) getActivity()).setFragmentRefreshMultipleListener(new MainActivity.FragmentRefreshMultipleListener() {

            @Override
            public void onRefresh(ArrayList<ListIMoviesWrapper> array) {
                cursor = todoAdapter.swapCursor(cr.query(PopularMoviesProvider.RECORDS_CONTENT_URI, PopularMoviesTable.PROJECTION_ALL, selection, selecArguments, null));
                movieList.addAll(array);
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String description);
    }

}
