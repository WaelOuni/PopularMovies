package wael.mobile.dev.popularmovies.model;

/**
 * Created by user on 04/01/2016.
 */
public class Genres {
    private Genre[] genres;

    public Genres(Genre[] genres) {
        this.genres = genres;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }
}
