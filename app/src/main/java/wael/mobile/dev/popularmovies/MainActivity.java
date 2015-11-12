package wael.mobile.dev.popularmovies;

import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import wael.mobile.dev.popularmovies.database.PopularMoviesProvider;
import wael.mobile.dev.popularmovies.database.tables.PopularMoviesTable;
import wael.mobile.dev.popularmovies.ui.AddDialog;
import wael.mobile.dev.popularmovies.wrapper.ListIMoviesWrapper;

public class MainActivity extends AppCompatActivity implements MovieFragment.OnFragmentInteractionListener, AddDialog.OnAddListener, android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LIST_CONTENT_KEY = "list_content_key";
    private static final int RECORD_TABLE_ID = 1;
    FragmentManager manager;
    Detail_lanscape fragment;
    ContentResolver cr;
    private ArrayList<ListIMoviesWrapper> mObjectList;
    private android.app.LoaderManager mLoaderManager;

    private FragmentRefreshListener fragmentRefreshListener;
    private FragmentRefreshMultipleListener fragmentRefreshMultipleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cr = getContentResolver();

        mLoaderManager = getLoaderManager();

        if (savedInstanceState == null) {
            mLoaderManager.initLoader(RECORD_TABLE_ID, null, this);
            mObjectList = new ArrayList<ListIMoviesWrapper>();
        } else {
            mObjectList = (ArrayList<ListIMoviesWrapper>) savedInstanceState.getSerializable(LIST_CONTENT_KEY);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.create_database) {
            ArrayList<ListIMoviesWrapper> fakeList = createFakeDatabase();
            if (getFragmentRefreshMultipleListener() != null) {
                getFragmentRefreshMultipleListener().onRefresh(fakeList);
            }
            return true;
        }
        if (id == R.id.action_add) {
            AddDialog addDialog = AddDialog.newInstance(this);
            addDialog.show(getSupportFragmentManager(), "");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Method for create fake movies's database
    ArrayList<ListIMoviesWrapper> createFakeDatabase() {
        ArrayList<ListIMoviesWrapper> fakeList = new ArrayList<ListIMoviesWrapper>();
        for (int i = 0; i < 5; i++) {
            ContentValues movieItem = new ContentValues();
            ListIMoviesWrapper movie = createMovieItem("Movie fake " + i, "Wael ouni movie for testing android application " + getResources().getString(R.string.app_name));
            movieItem.put(PopularMoviesTable.LABEL, movie.getTitle());
            movieItem.put(PopularMoviesTable.DESCRIPTION, movie.getDescription());
            fakeList.add(movie);
            cr.insert(PopularMoviesProvider.RECORDS_CONTENT_URI, movieItem);
        }
        return fakeList;
    }


    private ListIMoviesWrapper createMovieItem(String title, String description) {

        ListIMoviesWrapper listIMoviesWrapper = new ListIMoviesWrapper();
        listIMoviesWrapper.setTitle(title);
        listIMoviesWrapper.setDescription(description);

        return listIMoviesWrapper;
    }

    @Override
    public void onFragmentInteraction(String description) {
        manager = getFragmentManager();
        fragment = (Detail_lanscape) manager.findFragmentById(R.id.fragment3);
        if (fragment != null && fragment.isVisible()) {

            fragment.changeData(description);
        } else {
            Intent intent = new Intent(getApplicationContext(), Detail_portrait.class);
            intent.putExtra("id", description);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(LIST_CONTENT_KEY, mObjectList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onOkClicked(ListIMoviesWrapper listIMoviesWrapper) {

        mObjectList.add(listIMoviesWrapper);
        ContentValues contentValues = new ContentValues();
        contentValues.put(PopularMoviesTable.LABEL, listIMoviesWrapper.getTitle());
        contentValues.put(PopularMoviesTable.DESCRIPTION, listIMoviesWrapper.getDescription());

        Uri uri = getContentResolver().insert(PopularMoviesProvider.RECORDS_CONTENT_URI, contentValues);
        listIMoviesWrapper.setId(Long.valueOf(uri.getLastPathSegment()));
        if (getFragmentRefreshListener() != null) {
            getFragmentRefreshListener().onRefresh(listIMoviesWrapper.getTitle(), listIMoviesWrapper.getDescription());
        }
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this,
                PopularMoviesProvider.RECORDS_CONTENT_URI, PopularMoviesTable.PROJECTION_ALL, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == RECORD_TABLE_ID) {
            if (data.moveToFirst()) {
                while (!data.isAfterLast()) {
                    ListIMoviesWrapper movie = new ListIMoviesWrapper(0, "", "", "");
                    movie.setTitle(data.getString(data.getColumnIndex(PopularMoviesTable.LABEL)));

                    movie.setDescription(data.getString(data.getColumnIndex(PopularMoviesTable.DESCRIPTION)));
                    mObjectList.add(movie);
                    if (!data.isClosed()) {
                        data.moveToNext();
                    }
                }
                //    mAdapter.notifyDataSetChanged();
            }
            data.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;

    }

    public FragmentRefreshMultipleListener getFragmentRefreshMultipleListener() {
        return fragmentRefreshMultipleListener;
    }

    public void setFragmentRefreshMultipleListener(FragmentRefreshMultipleListener fragmentRefreshMultipleListener) {
        this.fragmentRefreshMultipleListener = fragmentRefreshMultipleListener;
    }

    public interface FragmentRefreshListener {
        void onRefresh(String title, String description);
    }

    public interface FragmentRefreshMultipleListener {
        void onRefresh(ArrayList<ListIMoviesWrapper> array);
    }

}
