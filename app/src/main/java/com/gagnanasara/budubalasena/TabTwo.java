package com.gagnanasara.budubalasena;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabTwo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabTwo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View mainFrame;

    LayoutInflater layoutiInflater;
    Button chooseImage;
    ImageView imageView;

    private Button sendButton;
    private Bitmap bitmap;
    private String fileName;
    private String messageDateTime;
    private Uri filePath;
    String currentPhotoPath;
    Uri photoURI;

    private String name;
    private String telephone;
    private String email;
    private String message;

    public static final String UPLOAD_URL = "http://www.graphicsstudio.com.au/bbs/upload/upload.php";
    public static final String UPLOAD_KEY = "image";

    //private OnFragmentInteractionListener mListener;

    MainActivity mainActivity;

    //private OnFragmentInteractionListener mListener;

    public TabTwo() {
        // Required empty public constructor
    }

    public TabTwo(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static TabTwo newInstance(String param1, String param2) {
        TabTwo fragment = new TabTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void openImageChooser(Context context){
        final CharSequence[] options = { "Camera", "Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select a Photo to Attach");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Camera")) {

                    //Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(takePicture, 0);

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            photoURI = FileProvider.getUriForFile(mainActivity,
                                    "com.gagnanasara.budubalasena.android.fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, 0);
                        }
                    }

                } else if (options[item].equals("Gallery")) {

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    //startActivityForResult(galleryIntent, 1);


                    if (galleryIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            photoURI = FileProvider.getUriForFile(mainActivity,
                                    "com.gagnanasara.budubalasena.android.fileprovider",
                                    photoFile);
                            galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(galleryIntent, 1);
                        }
                    }


/*
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.putExtra("return-data", true);
                    startActivityForResult(pickPhoto , 1);


                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
*/

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case 0:

                    if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

                        try {
                           // bitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), filePath);
                            //bitmap = data.getParcelableExtra("data");
                            bitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), photoURI);
                            setImage(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    /*
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(bitmap);
                        fileName = getFileName();
                    }
                    */

                    break;
                case 1:

                    if (data != null) {
                        Uri contentURI = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), contentURI);
                            //String path = saveImage(bitmap);
                            //Toast.makeText(mainActivity, "Image Saved!", Toast.LENGTH_SHORT).show();
                            setImage(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(mainActivity, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    /*
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = mainActivity.getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                                //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                                String picturePath = cursor.getString(columnIndex);
                                File imgFile = new  File(picturePath);

                                if(imgFile.exists()) {
                                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    imageView.setImageBitmap(myBitmap);
                                }

                                cursor.close();
                            }
                        }

                    }
                    */
                    break;
            }
        }
    }

    private void setImage(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
        ((Button)mainFrame.findViewById(R.id.imageButton)).setText(R.string.change_attached_photo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutiInflater = inflater;
        mainFrame = layoutiInflater.inflate(R.layout.fragment_tab_two, container, false);

        chooseImage = mainFrame.findViewById(R.id.imageButton);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser(getContext());
            }
        });

        sendButton = mainFrame.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendButtonClick();
            }
        });

        imageView = mainFrame.findViewById(R.id.imageView);

        return mainFrame;
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(mainActivity, "Sending Your Message", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                // clear the input fields
                ((EditText)mainFrame.findViewById(R.id.text_name)).setText("");
                ((EditText)mainFrame.findViewById(R.id.text_telephone)).setText("");
                ((EditText)mainFrame.findViewById(R.id.text_email)).setText("");
                ((EditText)mainFrame.findViewById(R.id.text_message)).setText("");

                 imageView.setImageBitmap(null);
                 bitmap = null;

                (mainFrame.findViewById(R.id.text_name)).requestFocus();

                Toast toast = Toast.makeText(mainActivity.getApplicationContext(),s,Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                Bitmap bitmap = params[0];

                HashMap<String,String> data = new HashMap<>();

                data.put("image_name", "");

                messageDateTime  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                if(bitmap != null) {
                    String uploadImage = getStringImage(bitmap);
                    data.put(UPLOAD_KEY, uploadImage);
                    data.put("image_name", fileName);
                }

                data.put("name", name);
                data.put("telephone", telephone);
                data.put("email", email);
                data.put("message", message);
                data.put("date_time", messageDateTime);

                String result = rh.postRequest(UPLOAD_URL,data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public void onSendButtonClick() {

        name = ((EditText)mainFrame.findViewById(R.id.text_name)).getText().toString();
        telephone = ((EditText)mainFrame.findViewById(R.id.text_telephone)).getText().toString();
        email = ((EditText)mainFrame.findViewById(R.id.text_email)).getText().toString();
        message = ((EditText)mainFrame.findViewById(R.id.text_message)).getText().toString();

        if(!name.equals("") && !message.equals("") && (!telephone.equals("") || !email.equals(""))) {
            uploadImage();
        } else {
            Toast toast = Toast.makeText(mainActivity,"Please enter your name, telephone or email and a message.",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        fileName = image.getName();

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
