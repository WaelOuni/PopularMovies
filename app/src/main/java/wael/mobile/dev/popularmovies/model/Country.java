package wael.mobile.dev.popularmovies.model;

/**
 * Created by wael on 16/11/15.
 */
public class Country {
    private String iso_3166_1;
    private String name;

    public Country(String iso_3166_1, String name, int id) {
        this.iso_3166_1 = iso_3166_1;
        this.name = name;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
