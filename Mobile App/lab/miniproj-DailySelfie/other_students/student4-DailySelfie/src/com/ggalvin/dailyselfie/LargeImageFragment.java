package com.ggalvin.dailyselfie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * Activities that contain this fragment must implement the
 * {@link LargeImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LargeImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LargeImageFragment extends Fragment {
    private static final String SELFIE_PATH = "filePath";
    private String selfieImagePath;

    private Button deleteButton;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imageFilePath - The path to the file to be displayed.
     * @return A new instance of fragment LargeImageFragment.
     */
    public static LargeImageFragment newInstance(String imageFilePath) {
        LargeImageFragment fragment = new LargeImageFragment();
        Bundle args = new Bundle();
        args.putString(SELFIE_PATH, imageFilePath);
        fragment.setArguments(args);
        return fragment;
    }

    public LargeImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selfieImagePath = getArguments().getString(SELFIE_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_large_image, container, false);

        // Get the image View
        ImageView selfieView = (ImageView)view.findViewById(R.id.selfie_image);
        loadImageIntoView(selfieView);

        //Find the delete button
        deleteButton = (Button)view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.deleteButtonPressed(selfieImagePath);
                }
            }

        });
        return view;
    }

    private void loadImageIntoView(ImageView selfieView)
    {
        // Read image from filePath.
/*        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selfieImagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int imageWidth = selfieView.getWidth();
        int imageHeight = selfieView.getHeight();

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/imageWidth, photoH/imageHeight);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
*/
        // Get the image
        Bitmap selfieImage = BitmapFactory.decodeFile(selfieImagePath);
 //       Bitmap selfieImage = BitmapFactory.decodeFile(selfieImagePath, bmOptions);

        selfieView.setImageBitmap(selfieImage);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void deleteButtonPressed(String filename);
    }

}
