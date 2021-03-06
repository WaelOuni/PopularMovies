package wael.mobile.dev.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import wael.mobile.dev.popularmovies.R;

/**
 * Created by wael on 08/11/15.
 */
public class ListMoviesCursorAdapter extends CursorAdapter {
    public ListMoviesCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_title_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvBody = (TextView) view.findViewById(R.id.description);
        String body = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        tvBody.setText(body);
    }
}