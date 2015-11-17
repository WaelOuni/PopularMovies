package wael.mobile.dev.popularmovies.model;

import com.google.gson.annotations.SerializedName;


/**
 * Created by wael on 13/11/15.
 */
public class MovieEntity {

    private boolean adult;
    private String backdrop_path;
    private String[] belongs_to_collection;                            // null
    private int budget;
    private Genre[] genres;
    private String homepage;
    private String imdb_id;
    private String original_language;
    private String original_title;
    private String overview;
    private float popularity;
    private String poster_path;
    private Company[] production_companies;
    private Country[] production_countries;
    private String release_date;
    private double revenue;
    private int runtime;
    private Language[] spoken_languages;
    private String status;
    private String tagline;
    private boolean video;
    private float vote_average;
    private int vote_count;
    @SerializedName("id")
    private int id;
    @SerializedName("titleMovie")
    private String title;

    public MovieEntity(boolean adult, String backdrop_path, String[] belongs_to_collection,
                       int budget, Genre[] genres, String homepage, int id, String imdb_id, String original_language,
                       String original_title, String overview, float popularity, String poster_path,
                       Company[] production_companies, Country[] production_countries, String release_date, double revenue
            , int runtime, Language[] spoken_languages, String status, String tagline, String title,
                       boolean video, float vote_average, int vote_count) {

        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.belongs_to_collection = belongs_to_collection;
        this.budget = budget;
        this.genres = genres;
        this.homepage = homepage;
        this.id = id;
        this.imdb_id = imdb_id;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.production_companies = production_companies;
        this.production_countries = production_countries;
        this.release_date = release_date;
        this.revenue = revenue;
        this.runtime = runtime;
        this.spoken_languages = spoken_languages;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.video = video;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    public MovieEntity(int id, String title) {
        this.title = title;
        this.id = id;
    }

    public MovieEntity(int id, String title, String release_date, Company[] production_companies, Genre[] genres, boolean adult, String backdrop_path, String original_title) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.production_companies = production_companies;
        this.genres = genres;
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.original_title = original_title;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String[] getBelongs_to_collection() {
        return belongs_to_collection;
    }

    public void setBelongs_to_collection(String[] belongs_to_collection) {
        this.belongs_to_collection = belongs_to_collection;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Company[] getProduction_companies() {
        return production_companies;
    }

    public void setProduction_companies(Company[] production_companies) {
        this.production_companies = production_companies;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public Language[] getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(Language[] spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
}
