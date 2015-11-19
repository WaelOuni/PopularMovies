package wael.mobile.dev.popularmovies;

import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import wael.mobile.dev.popularmovies.database.PopularMoviesProvider;
import wael.mobile.dev.popularmovies.database.tables.PopularMoviesTable;
import wael.mobile.dev.popularmovies.model.MovieItem;
import wael.mobile.dev.popularmovies.model.TopRated;
import wael.mobile.dev.popularmovies.ui.AddDialog;
import wael.mobile.dev.popularmovies.wrapper.ListIMoviesWrapper;

public class MainActivity extends AppCompatActivity implements MovieFragment.OnFragmentInteractionListener, AddDialog.OnAddListener, android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LIST_CONTENT_KEY = "list_content_key";
    private static final int RECORD_TABLE_ID = 1;
    public static String IMAGES_URL;
    public static String SERVER_URL;
    FragmentManager manager;
    Detail_lanscape fragment;
    ContentResolver cr;
    MovieParser parser;
    private ArrayList<ListIMoviesWrapper> mObjectList;
    private android.app.LoaderManager mLoaderManager;
    private FragmentRefreshListener fragmentRefreshListener;
    private FragmentRefreshMultipleListener fragmentRefreshMultipleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IMAGES_URL = getResources().getString(R.string.image_url);
        SERVER_URL = getResources().getString(R.string.server_url);
        cr = getContentResolver();
        mLoaderManager = getLoaderManager();
        if (savedInstanceState == null) {
            mLoaderManager.initLoader(RECORD_TABLE_ID, null, this);
            mObjectList = new ArrayList<ListIMoviesWrapper>();
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                int m = 0;

                @Override
                public void run() {

                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }
                    if (isOnline())
                        new MovieParser().execute();
                    else
                        Toast.makeText(getApplicationContext(), "Echec internet Connexion !", Toast.LENGTH_LONG).show();
                    m++;
                    Log.i("timer", "" + m);
                }
            };
            long whenToStart = 1 * 1000L; // 20 seconds
            long howOften = 25 * 1000L; // 20 seconds
            timer.scheduleAtFixedRate(task, whenToStart, howOften);

        } else {
            mObjectList = (ArrayList<ListIMoviesWrapper>) savedInstanceState.getSerializable(LIST_CONTENT_KEY);
            parser = (MovieParser) savedInstanceState.getSerializable("Ajout");
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
            item.setEnabled(false);
            return true;
        }
        if (id == R.id.action_add) {
            AddDialog addDialog = AddDialog.newInstance(this);
            addDialog.show(getSupportFragmentManager(), "");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String description) {
        manager = getFragmentManager();
        fragment = (Detail_lanscape) manager.findFragmentById(R.id.fragment3);
        if (fragment != null && fragment.isVisible()) {
            fragment.changeData(description);
        } else {
            Intent intent = new Intent(getApplicationContext(), Detail_portrait.class);
            intent.putExtra("description", description);
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
            getFragmentRefreshListener().onRefresh(mObjectList, listIMoviesWrapper.getTitle(), listIMoviesWrapper.getDescription());
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
                    ListIMoviesWrapper movie = new ListIMoviesWrapper("", "");
                    movie.setTitle(data.getString(data.getColumnIndex(PopularMoviesTable.LABEL)));

                    movie.setDescription(data.getString(data.getColumnIndex(PopularMoviesTable.DESCRIPTION)));
                    mObjectList.add(movie);
                    if (!data.isClosed()) {
                        data.moveToNext();
                    }
                }
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

    public String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public boolean verifyExistMovie(String title) {

        boolean trouve = false;
        for (ListIMoviesWrapper movie : mObjectList
                ) {
            if (TextUtils.equals(movie.getTitle(), title)) {
                trouve = true;
                break;
            }
        }
        return trouve;
    }

    public void loadFromWebService(MovieItem[] movieEntities) {

        for (MovieItem Mv : movieEntities
                ) {
            ListIMoviesWrapper listIMoviesWrapper = new ListIMoviesWrapper();
            listIMoviesWrapper.setTitle(Mv.getOriginal_title());
            listIMoviesWrapper.setDescription(Mv.getOverview() + "&" + Mv.getBackdrop_path());
            if (!verifyExistMovie(listIMoviesWrapper.getTitle())) {
                mObjectList.add(listIMoviesWrapper);
                ContentValues contentValues = new ContentValues();
                contentValues.put(PopularMoviesTable.LABEL, listIMoviesWrapper.getTitle());
                contentValues.put(PopularMoviesTable.DESCRIPTION, listIMoviesWrapper.getDescription());
                Uri uri = getContentResolver().insert(PopularMoviesProvider.RECORDS_CONTENT_URI, contentValues);
                listIMoviesWrapper.setId(Long.valueOf(uri.getLastPathSegment()));
            }
        }
        if (getFragmentRefreshMultipleListener() != null) {
            getFragmentRefreshMultipleListener().onRefresh(mObjectList);
        }


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public interface FragmentRefreshListener {
        void onRefresh(ArrayList<ListIMoviesWrapper> array, String title, String description);
    }

    public interface FragmentRefreshMultipleListener {
        void onRefresh(ArrayList<ListIMoviesWrapper> array);
    }

    private class MovieParser extends AsyncTask<Void, Void, MovieItem[]> {
        private static final String TAG = "MovieParser";

        @Override
        protected MovieItem[] doInBackground(Void... params) {
            String data = getJSON(SERVER_URL, 50000);
            TopRated topRated;
            MovieItem[] movies;
            topRated = new Gson().fromJson(data, TopRated.class);
            movies = topRated.getResults();
            return movies;
        }

        @Override
        protected void onPostExecute(MovieItem[] movieEntities) {
            super.onPostExecute(movieEntities);
            loadFromWebService(movieEntities);
            }
    }
}
