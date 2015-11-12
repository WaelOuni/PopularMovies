package wael.mobile.dev.popularmovies.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import wael.mobile.dev.popularmovies.R;
import wael.mobile.dev.popularmovies.wrapper.ListIMoviesWrapper;


public class AddDialog extends DialogFragment {


    private static OnAddListener mListener;

    private EditText mLabel;
    private EditText mDescription;


    public static AddDialog newInstance(OnAddListener listener) {

        AddDialog dialog = new AddDialog();
        mListener = listener;
        return dialog;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_dialog_layout, null);

        initViews(view);

        Dialog dialog = builder
                .setTitle("Add new movie in your database").setView(view)
                .setPositiveButton(
                        getResources().getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mListener.onOkClicked(createListItem());
                            }
                        })
                .setNegativeButton(
                        getResources().getString(android.R.string.cancel),
                        null).create();

        return dialog;
    }

    private void initViews(View view) {
        mLabel = (EditText) view.findViewById(R.id.title_movie);
        mDescription = (EditText) view.findViewById(R.id.description_movie);
    }

    private ListIMoviesWrapper createListItem() {


        ListIMoviesWrapper listIMoviesWrapper = new ListIMoviesWrapper();
        listIMoviesWrapper.setTitle(mLabel.getText().toString());
        listIMoviesWrapper.setDescription(mDescription.getText().toString());

        return listIMoviesWrapper;
    }

    public interface OnAddListener {

        public void onOkClicked(ListIMoviesWrapper listIMoviesWrapper);


    }

}
