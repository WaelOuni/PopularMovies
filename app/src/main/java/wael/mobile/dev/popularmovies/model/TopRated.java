package wael.mobile.dev.popularmovies.model;

/**
 * Created by wael on 17/11/15.
 */
public class TopRated {

    private String page;
    private MovieEntity[] results;
    private int total_pages;
    private int total_results;

    public TopRated(String page, MovieEntity[] results, int total_pages, int total_results) {
        this.page = page;
        this.results = results;
        this.total_pages = total_pages;
        this.total_results = total_results;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public MovieEntity[] getResults() {
        return results;
    }

    public void setResults(MovieEntity[] results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
