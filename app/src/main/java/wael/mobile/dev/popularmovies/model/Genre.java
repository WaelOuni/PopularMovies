package wael.mobile.dev.popularmovies.model;

/**
 * Created by wael on 13/11/15.
 */
public class Genre {

    private int id;
    private String name;


    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
