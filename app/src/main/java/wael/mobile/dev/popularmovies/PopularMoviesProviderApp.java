package wael.mobile.dev.popularmovies;

import android.app.Application;
import android.os.StrictMode;

public class PopularMoviesProviderApp extends Application {

    public void onCreate() {
        super.onCreate();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll().penaltyLog().build());

    }

}
