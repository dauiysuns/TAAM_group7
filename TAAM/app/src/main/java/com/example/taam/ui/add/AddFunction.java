package com.example.taam.ui.add;

import static android.app.Activity.RESULT_OK;

import com.example.taam.database.Item;
import com.example.taam.database.DataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.taam.R;
import android.content.ContentResolver;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import android.net.Uri;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import android.content.Intent;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFunction#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFunction extends Fragment {
    private EditText editTextName, editTextLotNumber, editTextDescription;
    private Spinner spinnerCategory, spinnerPeriod;
    private ArrayList<String> picUrls;
    private ArrayList<String> vidUrls;

    private Uri uri;
    ProgressBar progressBar;
    private ActivityResultLauncher<Intent> uploadMediaLauncher;
    private String mediaType;

    //ngl i don't know really know what these are... i just be using them ?? maybe
    private final DatabaseReference itemsRef = DataModel.ref; //the static field
    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://taam-cfc94-default-rtdb.firebaseio.com/");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //onCreateView is what happens when you initialize the fragment/activity! So when the user goes to the "AddFunction" screen, we need to setup all these "views" (the components of the screen, like text boxes and buttons) and fill them in with the necessary content (dropdown menu options, what actions to take if a user clicks something). Basically, the instruction manual for how the fragment will work.

        //assigns an xml view to this fragment
        View view = inflater.inflate(R.layout.fragment_add_function, container, false);

        //setting up the components in the xml file
        //text boxes
        editTextLotNumber = view.findViewById(R.id.editTextLotNumber);
        editTextName = view.findViewById(R.id.editTextName);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        //spinners
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerPeriod = view.findViewById(R.id.spinnerPeriod);
        //buttons
        Button buttonUploadMedia = view.findViewById(R.id.buttonUploadMedia);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //filling the spinners with options (from arrays):
        //category spinner
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(requireContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter1);
        //period spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(requireContext(),
                R.array.periods_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(adapter2);

        //setting up media upload launcher
        uploadMediaLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK) {
                    Intent data = o.getData();
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                        uploadMedia(uri, mediaType);
                    }
                }
            }
        });

        //setting up buttons with functionalities (method calls)
        buttonUploadMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askMediaType();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        return view;
    }

    //functionality for uploading media

    //helper function 1: dialog popup to ask if uploading photo or video
    private void askMediaType(){
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("please select media type to upload");
    builder.setItems(new CharSequence[]{"Image", "Video"}, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int choice) {
            if (choice == 0) { //chose to upload image
                launchUploadMediaIntent("image/*", "image");
            }
            else { //chose to upload video
                launchUploadMediaIntent("video/*", "video");
            }
        }
    });
    builder.show();
    }

    //helper function 2: to get file extension of an uploaded media using the Uri
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = requireContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    //change name
    //helper function 3: sets up intent and launches the uploader
    private void launchUploadMediaIntent(String type, String mediaType) {
        Intent intent = new Intent();
        intent.setType(type);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        uploadMediaLauncher.launch(intent);
    }

    //the method that actually uploads the media using the 3 helpers
    private void uploadMedia(Uri uri, String mediaType) {
        final StorageReference mediaReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        mediaReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mediaReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        if (mediaType.equals("image")) {
                            picUrls.add(downloadUrl);
                            Toast.makeText(getContext(), "Photo uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                        else if (mediaType.equals("video")) {
                            vidUrls.add(downloadUrl);
                            Toast.makeText(getContext(), "Video uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "upload failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }




    //functionality for submitting/adding an entry to database
    private void addItem() {
        //getting strings for each field (to enter into db)
        String name = editTextName.getText().toString().trim();
        String lot = editTextLotNumber.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().toLowerCase();
        String period = spinnerPeriod.getSelectedItem().toString().toLowerCase();

        //1st check: all text/spinner fields have been filled/selected
        if (name.isEmpty() || lot.isEmpty() || description.isEmpty() || category.isEmpty() || period.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        //2nd check: lot is an actual number
        try {
            Double.parseDouble(lot);
        }
        catch(NumberFormatException e) {
            Toast.makeText(getContext(), "Lot# must be an actual number!", Toast.LENGTH_SHORT).show();
            return;
        }

        //create the Item using current fields to add to db (+ add any uploaded media)
        Item newEntry = new Item(lot, name, category, period, description, picUrls, vidUrls);

        //using DataModel functions, add item to database
        DataModel.addItem(newEntry);

        Toast.makeText(getContext(), "Entry " + lot + " added successfully!", Toast.LENGTH_SHORT).show();

        //clear all fields
        editTextName.setText("");
        editTextLotNumber.setText("");
        editTextDescription.setText("");
        spinnerCategory.setSelection(0);
        spinnerPeriod.setSelection(0);
        picUrls.clear();
        vidUrls.clear();
        progressBar.setVisibility(View.INVISIBLE);
    }
}