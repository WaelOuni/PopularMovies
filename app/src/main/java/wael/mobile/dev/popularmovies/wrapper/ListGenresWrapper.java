package wael.mobile.dev.popularmovies.wrapper;

import java.io.Serializable;

/**
 * Created by user on 04/01/2016.
 */
public class ListGenresWrapper implements Serializable {


    private long mId;
    private int mIdGenre;
    private String mNameGenre;

    public ListGenresWrapper() {

    }

    public ListGenresWrapper(long mId, int mIdGenre, String mNameGenre) {
        this.mId = mId;
        this.mIdGenre = mIdGenre;
        this.mNameGenre = mNameGenre;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public int getmIdGenre() {
        return mIdGenre;
    }

    public void setmIdGenre(int mIdGenre) {
        this.mIdGenre = mIdGenre;
    }

    public String getmNameGenre() {
        return mNameGenre;
    }

    public void setmNameGenre(String mNameGenre) {
        this.mNameGenre = mNameGenre;
    }
}
