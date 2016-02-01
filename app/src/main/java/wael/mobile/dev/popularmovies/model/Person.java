package wael.mobile.dev.popularmovies.model;

/**
 * Created by user on 03/01/2016.
 */
public class Person {
    private boolean adult;
    private String[] also_known_as;
    private String biography;
    private String birthday;
    private String deathday;
    private String homepage;
    private int id;
    private String imdb_id;
    private String name;
    private String place_of_birth;
    private float popularity;

    private String profile_path;


    public Person(boolean adult, String[] also_known_as, String biography, String birthday, String deathday, String homepage, int id, String imdb_id, String name, String place_of_birth, float popularity, String profile_path) {
        this.adult = adult;
        this.also_known_as = also_known_as;
        this.biography = biography;
        this.birthday = birthday;
        this.deathday = deathday;
        this.homepage = homepage;
        this.id = id;
        this.imdb_id = imdb_id;
        this.name = name;
        this.place_of_birth = place_of_birth;
        this.popularity = popularity;
        this.profile_path = profile_path;
    }

    public Person(String profile_path, String name, String place_of_birth, int id, String homepage, String deathday, String birthday, String biography, String[] also_known_as) {
        this.profile_path = profile_path;
        this.name = name;
        this.place_of_birth = place_of_birth;
        this.id = id;
        this.homepage = homepage;
        this.deathday = deathday;
        this.birthday = birthday;
        this.biography = biography;
        this.also_known_as = also_known_as;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String[] getAlso_known_as() {
        return also_known_as;
    }

    public void setAlso_known_as(String[] also_known_as) {
        this.also_known_as = also_known_as;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
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

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public void setPlace_of_birth(String place_of_birth) {
        this.place_of_birth = place_of_birth;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
