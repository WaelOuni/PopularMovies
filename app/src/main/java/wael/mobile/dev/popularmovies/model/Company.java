package wael.mobile.dev.popularmovies.model;

/**
 * Created by wael on 13/11/15.
 */
public class Company {

    private int id;
    private String name;

    public Company(String name, int id) {
        this.name = name;
        this.id = id;
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
