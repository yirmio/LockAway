package com.yirmio.lockaway;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.yirmio.lockaway.UI.MainActivity;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddMenuItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddMenuItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMenuItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AddMenuItemFragment";
    private static final int IMAGE_PICKER_SELECT = 999;
    private static final int RESULT_LOAD_IMAGE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextTimeToMake;
    private EditText editTextPrive;
    private CheckBox checkBoxIsGlotenFree;
    private CheckBox checkBoxIsVeg;
    private CheckBox checkBoxIsAvalible;
    private ImageView imgViewPhoto;
    private Button btnSend;
    private Button btnChoosePhotos;

    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMenuItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMenuItemFragment newInstance(String param1, String param2) {
        AddMenuItemFragment fragment = new AddMenuItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddMenuItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewToReturn = inflater.inflate(R.layout.fragment_add_menu_item, container, false);

        initViewElements(viewToReturn);

        return viewToReturn;
    }

    private void initViewElements(View viewToReturn) {
        this.checkBoxIsAvalible = (CheckBox) viewToReturn.findViewById(R.id.insertItemChckBxAvalibleInMemu);
        this.checkBoxIsGlotenFree = (CheckBox) viewToReturn.findViewById(R.id.insertItemChckBxIsGlotenFree);
        this.checkBoxIsVeg = (CheckBox) viewToReturn.findViewById(R.id.insertItemChckBxIsVeg);
//Edit Text
        this.editTextDescription = (EditText) viewToReturn.findViewById(R.id.insertItemEditTextDescription);
        this.editTextName = (EditText) viewToReturn.findViewById(R.id.insertItemEditTextName);
        this.editTextTimeToMake = (EditText) viewToReturn.findViewById(R.id.insertItemEditTextTimeToMake);
        this.editTextPrive = (EditText) viewToReturn.findViewById(R.id.insertItemEditTextPrice);
//ImageView
        this.imgViewPhoto = (ImageView) viewToReturn.findViewById(R.id.insertItemImgViewPhoto);
//Buttons
        this.btnChoosePhotos = (Button) viewToReturn.findViewById(R.id.insertItemBtnAddImages);
        this.btnChoosePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGE_PICKER_SELECT);            }
        });
        this.btnSend = (Button) viewToReturn.findViewById(R.id.insertItemBtnSend);
        this.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ((editTextName.getText() == null || editTextName.getText().length() <= 0) ||
                            (editTextDescription.getText() == null || editTextDescription.getText().length() <= 0) ||
                            (editTextTimeToMake.getText() == null || editTextTimeToMake.getText().length() <= 0) ||
                            (editTextPrive.getText() == null || editTextPrive.getText().length() <= 0) ||
                            (imgViewPhoto.getDrawable() == null)
                            ) {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.check_item, Toast.LENGTH_SHORT).show();
                    } else {
                        final ParseObject newItem = new ParseObject("MenuObjects");
                        newItem.put("StoreID",getResources().getString(R.string.AfeyaStoreID));
                        newItem.put("Name",editTextName.getText().toString());
                        newItem.put("Price",Integer.parseInt(editTextPrive.getText().toString()));
                        newItem.put("Veg",checkBoxIsVeg.isChecked());
                        newItem.put("GlotenFree",checkBoxIsGlotenFree.isChecked());
                        newItem.put("TimeToMake",Integer.parseInt(editTextTimeToMake.getText().toString()));
                        newItem.put("IsAvaliable",checkBoxIsAvalible.isChecked());
                        newItem.put("Description", editTextDescription.getText().toString());
                        newItem.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Object Saved", Toast.LENGTH_SHORT).show();
                                    final ParseObject photoObject = new ParseObject("MenuPhotos");
                                    Bitmap bitmap = ((BitmapDrawable) imgViewPhoto.getDrawable()).getBitmap();
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    // get byte array here
                                    byte[] bytearray = stream.toByteArray();

                                    photoObject.put("MenuObjectID", newItem.getObjectId());
                                    if (bytearray != null) {
                                        final ParseFile file = new ParseFile(newItem.getObjectId() + ".png", bytearray);
                                        file.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    photoObject.put("PhotoFile", file);
                                                    Toast.makeText(getActivity().getApplicationContext(), "Photo uploaded", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity().getApplicationContext(), "Error uploading photo", Toast.LENGTH_SHORT).show();
                                                }
                                                photoObject.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e != null) {
                                                            Toast.makeText(getActivity().getApplicationContext(), "Error saving photo object", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });

                                    }
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Error saving object", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            insertImageToImageView(picturePath, this.imgViewPhoto);

            // String picturePath contains the path of selected Image
        }
    }

    private void insertImageToImageView(String picturePath,ImageView imgView) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, bmOptions);
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, bmOptions);

        int imgViewWidth = imgView.getWidth();
        int imgViewHeight = imgView.getHeight();

        imgView.setImageBitmap(bitmap);
        scaleImage(imgView,imgView.getWidth());

    }
    private void scaleImage(ImageView view, int boundBoxInDp)
    {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundBoxInDp) / width;
        float yScale = ((float) boundBoxInDp) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
//        params.width = width;
//        params.height = height;
//        view.setLayoutParams(params);
    }

    private int dpToPx(int dp)
    {
        float density = getActivity().getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
