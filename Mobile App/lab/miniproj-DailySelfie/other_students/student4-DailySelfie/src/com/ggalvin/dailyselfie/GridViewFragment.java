package com.ggalvin.dailyselfie;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridViewFragment.ImageGridListener} interface
 * to handle interaction events.
 * Use the {@link GridViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridViewFragment extends Fragment {

    private ImageGridListener mListener;
    private ImageAdapter imageAdapter = null;
    private GridView photoGridView = null;
    private String mCurrentPhotoPath = null;

    static final int REQUEST_IMAGE_CAPTURE = 1;


    public static GridViewFragment newInstance() {
        return new GridViewFragment();
    }

    public GridViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View gridFragment = inflater.inflate(R.layout.fragment_grid_view, container, false);

        setupImageAdapter();

        photoGridView = (GridView)gridFragment.findViewById(R.id.photogridview);
        photoGridView.setAdapter(imageAdapter);
        photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelfieImage imageToDisplay = (SelfieImage) imageAdapter.getItem(position);

                if (imageToDisplay != null) {
                    if (imageToDisplay.getFileName() == null) {
                        // There is no filename ... display error
                        Snackbar.make(view, "There is no large image for selected picture.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        mListener.onImageSelected(imageToDisplay.getFileName());
                    }
                }
            }
        });

        FloatingActionButton cameraButton = (FloatingActionButton) gridFragment.findViewById(R.id.photo_button);
        cameraButton.setBackgroundTintList(null);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        //                       ...
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });

        // Inflate the layout for this fragment
        return gridFragment;
    }

    private void setupImageAdapter()
    {
        imageAdapter = new ImageAdapter(getActivity());

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.contains("SELFIE"))
                {
                    return true;
                }

                return false;
            }
        };
        // Load Old Images ...
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File imageFiles[] = storageDir.listFiles(filter);

        for (File imageFile: imageFiles)
        {
            imageAdapter.addImage(imageFile.getAbsolutePath());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode ==Activity.RESULT_OK) {
            if (data != null)
            {
                // We have a thumbnail ... add it to the grid view.
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageAdapter.addImage(imageBitmap);
            }
            else
            {
                if (mCurrentPhotoPath != null)
                {
                    // Add the full size photo using the photo path.
                    imageAdapter.addImage(mCurrentPhotoPath);
                }
            }
            //Force the grid to redraw.
//            photoGridView.invalidate();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "SELFIE_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (ImageGridListener) getActivity();
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

    public interface ImageGridListener {
        public void onImageSelected(String imageFileName);
    }

}
