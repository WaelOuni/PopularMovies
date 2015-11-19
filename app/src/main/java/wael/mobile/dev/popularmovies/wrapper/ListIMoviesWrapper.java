package wael.mobile.dev.popularmovies.wrapper;


import java.io.Serializable;

public class ListIMoviesWrapper implements Serializable {


    private long mId;
    private String mTitle;
    private String mDescription;

    public ListIMoviesWrapper() {

    }

    public ListIMoviesWrapper(String title, String description) {
        mTitle = title;
        mDescription = description;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String desc) {
        this.mDescription = desc;
    }

}
