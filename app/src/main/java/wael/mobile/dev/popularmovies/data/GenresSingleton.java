package wael.mobile.dev.popularmovies.data;

import java.util.ArrayList;

import wael.mobile.dev.popularmovies.model.Genre;

/**
 * Created by user on 04/01/2016.
 */
public class GenresSingleton {
    private static GenresSingleton mInstance;
    private ArrayList<Genre> mList;

    private GenresSingleton() {
        mList = new ArrayList<Genre>();
    }

    public static synchronized GenresSingleton getInstance() {
        if (mInstance == null)
            mInstance = new GenresSingleton();
        return mInstance;
    }

    public void add(int position, Genre result) {
        mList.add(position, result);
    }

    public void getList(ArrayList<Genre> list) {
        if (list != null) {
            list.clear();
            for (Genre wrapper : mList) {
                list.add(wrapper);
            }
        }
    }

    public void remove(int position) {
        mList.remove(position);
    }

    public Genre getItem(int position) {
        return mList.get(position);
    }

    public void clearList() {
        mList.clear();
    }

    public void editWithoutGettingPosition(int position, Genre in) {
        mList.set(position, in);

    }

    public int getSizeList() {
        return mList.size();
    }
}