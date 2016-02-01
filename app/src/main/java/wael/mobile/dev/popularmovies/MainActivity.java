package wael.mobile.dev.popularmovies;

import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
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
import wael.mobile.dev.popularmovies.database.tables.GenresMoviesTable;
import wael.mobile.dev.popularmovies.database.tables.PopularMoviesTable;
import wael.mobile.dev.popularmovies.model.Genre;
import wael.mobile.dev.popularmovies.model.Genres;
import wael.mobile.dev.popularmovies.model.MovieItem;
import wael.mobile.dev.popularmovies.model.TopRated;
import wael.mobile.dev.popularmovies.ui.AddDialog;
import wael.mobile.dev.popularmovies.wrapper.ListGenresWrapper;
import wael.mobile.dev.popularmovies.wrapper.ListIMoviesWrapper;

public class MainActivity extends AppCompatActivity implements MovieFragment.OnFragmentInteractionListener, AddDialog.OnAddListener, android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LIST_CONTENT_KEY = "list_content_key";
    private static final int RECORD_TABLE_ID = 1;
    private final static String SERVER_URL2 = "http://api.themoviedb.org/3/genre/movie/list?api_key=3defbee8a7ff35deff02366f0f76a940";
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
        String category = "";
        Intent recupCateg;
        recupCateg = getIntent();
        category = recupCateg.getStringExtra("category");
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        switch (category) {
            case "now":
                SERVER_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=3defbee8a7ff35deff02366f0f76a940";
                setTitle("Now play movies");
                //  toolbar.setTitleTextColor(Color.BLUE);
                break;
            case "popular":
                SERVER_URL = "https://api.themoviedb.org/3/movie/popular?api_key=3defbee8a7ff35deff02366f0f76a940";
                setTitle("Popular movies");
                bar.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));

                //toolbar.setTitleTextColor(Color.YELLOW);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.YELLOW);
                }
                break;
            case "top":
                SERVER_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=3defbee8a7ff35deff02366f0f76a940";
                setTitle("Top rated movies");
                //toolbar.setTitleTextColor(Color.RED);
                bar.setBackgroundDrawable(new ColorDrawable(Color.RED));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.RED);
                }
                break;
            case "upcoming":
                SERVER_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=3defbee8a7ff35deff02366f0f76a940";
                setTitle("Upcomming movies");
                setTitleColor(Color.GREEN);
                //toolbar.setTitleTextColor(Color.GREEN);
                bar.setBackgroundDrawable(new ColorDrawable(Color.GREEN));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.GREEN);
                }
                break;
        }

        IMAGES_URL = getResources().getString(R.string.image_url);
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
                    if (isOnline()) {
                        new MovieParser().execute();
                        Log.i("Size :", "" + mObjectList.size());
                    }
                    else
                        Log.i("echec", "Echec internet Connexion !");
                    m++;
                    //Log.i("timer", "" + m);
                }
            };
            long whenToStart = 1 * 1000L; // 20 seconds
            long howOften = 3600 * 1000L; // 1 heure
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
       /* mObjectList.add(listIMoviesWrapper);
        ContentValues contentValues = new ContentValues();
        contentValues.put(PopularMoviesTable.ID, listIMoviesWrapper.getmIdMovie());
        contentValues.put(PopularMoviesTable.TITLE, listIMoviesWrapper.getmOriginalTitle());
        contentValues.put(PopularMoviesTable.OVERVIEW, listIMoviesWrapper.getmOverView());
        contentValues.put(PopularMoviesTable.BACKDROPPATH, listIMoviesWrapper.getmBackdropPath());
        contentValues.put(PopularMoviesTable.ORIGINALLANGAGE, listIMoviesWrapper.getmOriginalLang());
        contentValues.put(PopularMoviesTable.VOTECOUNT, listIMoviesWrapper.getmVoteCount());
        contentValues.put(PopularMoviesTable.VOTEAVERAGE, listIMoviesWrapper.getmVoteAverage());
        Uri uri = getContentResolver().insert(PopularMoviesProvider.RECORDS_CONTENT_URI, contentValues);
        listIMoviesWrapper.setmId(Long.valueOf(uri.getLastPathSegment()));
        if (getFragmentRefreshListener() != null) {
            getFragmentRefreshListener().onRefresh(mObjectList, listIMoviesWrapper.getmIdMovie(), listIMoviesWrapper.getmOriginalTitle(), listIMoviesWrapper.getmOverView(),
                    listIMoviesWrapper.getmBackdropPath(), listIMoviesWrapper.getmOriginalLang(), listIMoviesWrapper.getmVoteCount(), listIMoviesWrapper.getmGenresIds() ,
                    listIMoviesWrapper.getmVoteAverage());
        }*/
        Toast.makeText(getApplicationContext(), "Not yet ...", Toast.LENGTH_SHORT).show();
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
                    ListIMoviesWrapper movie = new ListIMoviesWrapper(0, 0, "", "", "", 0, "", 0f, "");
                    movie.setmIdMovie(data.getInt(data.getColumnIndex(PopularMoviesTable.ID)));
                    movie.setmOriginalTitle(data.getString(data.getColumnIndex(PopularMoviesTable.TITLE)));
                    movie.setmOverView(data.getString(data.getColumnIndex(PopularMoviesTable.OVERVIEW)));
                    movie.setmBackdropPath(data.getString(data.getColumnIndex(PopularMoviesTable.BACKDROPPATH)));
                    movie.setmOriginalLang(data.getString(data.getColumnIndex(PopularMoviesTable.ORIGINALLANGAGE)));
                    movie.setmVoteCount(data.getInt(data.getColumnIndex(PopularMoviesTable.VOTECOUNT)));
                    movie.setmGenresIds(data.getString(data.getColumnIndex(PopularMoviesTable.GENREIDS)));
                    movie.setmVoteAverage(data.getFloat(data.getColumnIndex(PopularMoviesTable.VOTEAVERAGE)));
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

    public boolean verifyExistMovie(long id) {

        boolean trouve = false;
        for (ListIMoviesWrapper movie : mObjectList
                ) {
            if (movie.getmIdMovie() == id) {
                trouve = true;
                break;
            }
        }
        return trouve;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cursor cursor;
        cr = getApplicationContext().getContentResolver();
        cursor = cr.query(PopularMoviesProvider.RECORDS_CONTENT_URI, PopularMoviesTable.PROJECTION_ALL, null, null, null);
        deleteOldMoviesItems(cursor.getCount());
        mObjectList.clear();
        finish();
    }

    public void loadFromWebService(MovieItem[] movieEntities) {
        Cursor cursor;
        cr = getApplicationContext().getContentResolver();
        cursor = cr.query(PopularMoviesProvider.RECORDS_CONTENT_URI, PopularMoviesTable.PROJECTION_ALL, null, null, null);
        deleteOldMoviesItems(cursor.getCount());
        mObjectList.clear();
        for (MovieItem Mv : movieEntities
                ) {
            String genresString = "";
            for (int i : Mv.getGenre_ids()
                    ) {
                genresString += i + ",";
            }

            ListIMoviesWrapper listIMoviesWrapper = new ListIMoviesWrapper();
            listIMoviesWrapper.setmId(Mv.getId());
            listIMoviesWrapper.setmIdMovie(Mv.getId());
            listIMoviesWrapper.setmOriginalTitle(Mv.getOriginal_title());
            listIMoviesWrapper.setmOverView(Mv.getOverview());
            listIMoviesWrapper.setmBackdropPath(Mv.getBackdrop_path());
            listIMoviesWrapper.setmOriginalLang(Mv.getOriginal_language());
            listIMoviesWrapper.setmVoteCount(Mv.getVote_count());
            listIMoviesWrapper.setmGenresIds(genresString);
            listIMoviesWrapper.setmVoteAverage(Mv.getVote_average());
            if (!verifyExistMovie(listIMoviesWrapper.getmIdMovie())) {
                mObjectList.add(listIMoviesWrapper);
                ContentValues contentValues = new ContentValues();
                contentValues.put(PopularMoviesTable.ID, listIMoviesWrapper.getmIdMovie());
                contentValues.put(PopularMoviesTable.TITLE, listIMoviesWrapper.getmOriginalTitle());
                contentValues.put(PopularMoviesTable.OVERVIEW, listIMoviesWrapper.getmOverView());
                contentValues.put(PopularMoviesTable.BACKDROPPATH, listIMoviesWrapper.getmBackdropPath());
                contentValues.put(PopularMoviesTable.ORIGINALLANGAGE, listIMoviesWrapper.getmOriginalLang());
                contentValues.put(PopularMoviesTable.VOTECOUNT, listIMoviesWrapper.getmVoteCount());
                contentValues.put(PopularMoviesTable.GENREIDS, listIMoviesWrapper.getmGenresIds());
                contentValues.put(PopularMoviesTable.VOTEAVERAGE, listIMoviesWrapper.getmVoteAverage());
                Log.i("Title", "" + listIMoviesWrapper.getmOriginalTitle());
                Uri uri = getContentResolver().insert(PopularMoviesProvider.RECORDS_CONTENT_URI, contentValues);
                listIMoviesWrapper.setmId(Long.valueOf(uri.getLastPathSegment()));
            }
        }
        if (getFragmentRefreshMultipleListener() != null) {
            getFragmentRefreshMultipleListener().onRefresh(mObjectList);
        }
    }

    public void loadGenresFromWebService(Genre[] genres) {

        for (Genre gr : genres
                ) {
            ListGenresWrapper listGenresWrapper = new ListGenresWrapper(0, 0, "");
            listGenresWrapper.setmIdGenre(gr.getId());
            listGenresWrapper.setmNameGenre(gr.getName());
            ContentValues contentValues = new ContentValues();
            contentValues.put(GenresMoviesTable.IDGENRE, listGenresWrapper.getmIdGenre());
            contentValues.put(GenresMoviesTable.NAMEGENRE, listGenresWrapper.getmNameGenre());
            Uri uri = getContentResolver().insert(PopularMoviesProvider.RECORDS_CONTENT_URI2, contentValues);
            listGenresWrapper.setmId(Long.valueOf(uri.getLastPathSegment()));
            Cursor cursor;
            cr = getApplicationContext().getContentResolver();
            cursor = cr.query(PopularMoviesProvider.RECORDS_CONTENT_URI2, GenresMoviesTable.PROJECTION_ALL, null, null, null);
            if (cursor.getCount() > 20) {
                deleteOldItems(20);
            }
        }
    }

    private void deleteOldItems(int number) {
        // You should probably sort the subselect on something
        // suitable indicating its age. The COLUMN_ID should do.
        String where = GenresMoviesTable.IDGENRE + " IN (SELECT " + GenresMoviesTable.IDGENRE + " FROM " +
                GenresMoviesTable.TABLE_RECORDS + " ORDER BY " + GenresMoviesTable.IDGENRE + " LIMIT ?)";
        getContentResolver()
                .delete(PopularMoviesProvider.RECORDS_CONTENT_URI2, where, new String[]{String.valueOf(number)});
    }

    private void deleteOldMoviesItems(int number) {
        // You should probably sort the subselect on something
        // suitable indicating its age. The COLUMN_ID should do.
        String where = PopularMoviesTable._ID + " IN (SELECT " + PopularMoviesTable._ID + " FROM " +
                PopularMoviesTable.TABLE_RECORDS + " ORDER BY " + PopularMoviesTable.ID + " LIMIT ?)";
        getContentResolver()
                .delete(PopularMoviesProvider.RECORDS_CONTENT_URI, where, new String[]{String.valueOf(number)});
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public interface FragmentRefreshListener {
        void onRefresh(ArrayList<ListIMoviesWrapper> array, long idmovie, String originaltitle, String overview, String backdroppath,
                       String originallangage, int votecount, String genresids, float voteaverage);
    }

    public interface FragmentRefreshMultipleListener {
        void onRefresh(ArrayList<ListIMoviesWrapper> array);
    }

    private class MovieParser extends AsyncTask<Void, Void, String[]> {
        private static final String TAG = "MovieParser";

        @Override
        protected String[] doInBackground(Void... params) {
            String data = getJSON(SERVER_URL, 50000);
            String data2 = getJSON(SERVER_URL2, 50000);
            String[] datas = new String[2];
            datas[0] = data;
            datas[1] = data2;
            return datas;
        }

        @Override
        protected void onPostExecute(String[] datas) {
            super.onPostExecute(datas);
            //   mObjectList.clear();
            TopRated topRated;
            MovieItem[] movies;
            topRated = new Gson().fromJson(datas[0], TopRated.class);
            movies = topRated.getResults();
            Genres genres;
            genres = new Gson().fromJson(datas[1], Genres.class);
            loadFromWebService(movies);
            loadGenresFromWebService(genres.getGenres());
            }
    }
}
