package wael.mobile.dev.popularmovies.wrapper;


import java.io.Serializable;

public class ListIMoviesWrapper implements Serializable {


    private long mId;
    private long mIdMovie;
    private String mOriginalTitle;
    private String mOriginalLang;
    private String mOverView;
    private int mVoteCount;
    private float mVoteAverage;
    private String mGenresIds;
    private String mBackdropPath;


    public ListIMoviesWrapper() {

    }

    public ListIMoviesWrapper(long mId, long mIdMovie, String mOriginalTitle, String mOriginalLang, String mOverView, int mVoteCount, String mGenresIds, float mVoteAverage, String mBackdropPath) {
        this.mId = mId;
        this.mIdMovie = mIdMovie;
        this.mOriginalTitle = mOriginalTitle;
        this.mOriginalLang = mOriginalLang;
        this.mOverView = mOverView;
        this.mVoteCount = mVoteCount;
        this.mGenresIds = mGenresIds;
        this.mVoteAverage = mVoteAverage;
        this.mBackdropPath = mBackdropPath;
    }

    public long getmIdMovie() {
        return mIdMovie;
    }

    public void setmIdMovie(long mIdMovie) {
        this.mIdMovie = mIdMovie;
    }

    public String getmBackdropPath() {
        return mBackdropPath;
    }

    public void setmBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public float getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(float mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getmGenresIds() {
        return mGenresIds;
    }

    public void setmGenresIds(String mGenresIds) {
        this.mGenresIds = mGenresIds;
    }

    public int getmVoteCount() {
        return mVoteCount;
    }

    public void setmVoteCount(int mVoteCount) {
        this.mVoteCount = mVoteCount;
    }

    public String getmOverView() {
        return mOverView;
    }

    public void setmOverView(String mOverView) {
        this.mOverView = mOverView;
    }

    public String getmOriginalLang() {
        return mOriginalLang;
    }

    public void setmOriginalLang(String mOriginalLang) {
        this.mOriginalLang = mOriginalLang;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

}
