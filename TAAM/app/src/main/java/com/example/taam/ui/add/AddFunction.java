package com.example.taam.ui.add;

import static android.app.Activity.RESULT_OK;

import com.example.taam.database.CategorySpinner;
import com.example.taam.database.DataView;
import com.example.taam.database.Item;
import com.example.taam.database.DataModel;
import com.example.taam.database.PeriodSpinner;
import com.example.taam.ui.FragmentLoader;
import com.example.taam.ui.home.AdminHomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import android.content.Intent;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;



public class AddFunction extends Fragment implements DataView{
    private EditText editTextName, editTextLotNumber, editTextDescription;
    private Spinner spinnerCategory, spinnerPeriod;
    private ArrayList<HashMap<String, String>> mediaUrls;

    private Uri uri;
    ProgressBar progressBar;
    private ActivityResultLauncher<Intent> uploadMediaLauncher;
    private String mediaType;
    StorageReference storageReference = DataModel.storageReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_function, container, false);
        editTextLotNumber = view.findViewById(R.id.editTextLotNumber);
        editTextName = view.findViewById(R.id.editTextName);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerPeriod = view.findViewById(R.id.spinnerPeriod);
        CategorySpinner.getSpinner(requireContext(), spinnerCategory);
        PeriodSpinner.getSpinner(requireContext(), spinnerPeriod);

        Button buttonUploadMedia = view.findViewById(R.id.buttonUploadMedia);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);

        FloatingActionButton buttonAddCategory = view.findViewById(R.id.buttonAddCategory);
        FloatingActionButton buttonAddPeriod = view.findViewById(R.id.buttonAddPeriod);
        FloatingActionButton buttonRemoveCategory = view.findViewById(R.id.buttonRemoveCategory);
        FloatingActionButton buttonRemovePeriod = view.findViewById(R.id.buttonRemovePeriod);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        mediaUrls = new ArrayList<>();

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

        buttonUploadMedia.setOnClickListener(v -> askMediaType());
        buttonSubmit.setOnClickListener(v -> addItem());
        buttonAddCategory.setOnClickListener(v -> showAddDialog("Category"));
        buttonAddPeriod.setOnClickListener(v -> showAddDialog("Period"));
        buttonRemoveCategory.setOnClickListener(v -> showRemoveDialog("Category"));
        buttonRemovePeriod.setOnClickListener(v -> showRemoveDialog("Period"));

        return view;
    }

    private void showAddDialog(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add new " + type);

        final EditText input = new EditText(requireContext());
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String label = input.getText().toString().trim();
            if (!label.isEmpty()) {
                if(type.equals("Category")){
                    CategorySpinner.addCategory(getContext(), label, spinnerCategory);
                    Toast.makeText(getContext(), "New Category added successfully", Toast.LENGTH_SHORT).show();
                } else{
                    PeriodSpinner.addPeriod(getContext(), label, spinnerPeriod);
                    Toast.makeText(getContext(), "New Period added successfully", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Empty " + type + " cannot be added!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showRemoveDialog(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Are you sure you want to remove this " + type  + " and all its items?");

        builder.setPositiveButton("Remove", (dialog, which) -> {
            DataModel dm = new DataModel(this);
            String selected;
            if (type != null) {
                if (type.equals("Category")) {
                    selected = spinnerCategory.getSelectedItem().toString();
                    if (!CategorySpinner.isUserAddedCategory(selected)) {
                        Toast.makeText(getContext(), "Default category " + selected + " cannot be deleted", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DataModel.ref.child("newCategories").child(selected).removeValue();
                    CategorySpinner.removeUserAddedCategory(selected);

                } else {
                    selected = spinnerPeriod.getSelectedItem().toString();
                    if (!PeriodSpinner.isUserAddedPeriod(selected)) {
                        Toast.makeText(getContext(), "Default period " + selected + " cannot be deleted", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DataModel.ref.child("newPeriods").child(selected).removeValue();
                    PeriodSpinner.removeUserAddedPeriod(selected);
                }

                dm.getItemsByCategory(type.toLowerCase(), selected, requireContext());
                Toast.makeText(getContext(), selected + " removed successfully", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }


    private void askMediaType(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("please select media type to upload");
        builder.setItems(new CharSequence[]{"Image", "Video"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                if (choice == 0) {
                    launchUploadMediaIntent("image/*", "image");
                }
                else {
                    launchUploadMediaIntent("video/*", "video");
                }
            }
        });
        builder.show();
    }


    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = requireContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void launchUploadMediaIntent(String type, String mediaType) {
        Intent intent = new Intent();
        intent.setType(type);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.mediaType = mediaType;
        uploadMediaLauncher.launch(intent);
    }


    private void uploadMedia(Uri uri, String mediaType) {
        final StorageReference mediaReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        mediaReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                HashMap<String, String> map = new HashMap<>();
                if (mediaType.equals("image")) {
                    map.put("image", mediaReference.toString());
                    Toast.makeText(getContext(), "Photo uploaded successfully", Toast.LENGTH_SHORT).show();
                }
                else if (mediaType.equals("video")) {
                    map.put("video", mediaReference.toString());
                    Toast.makeText(getContext(), "Video uploaded successfully", Toast.LENGTH_SHORT).show();
                }
                mediaUrls.add(map);
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


    private void addItem() {
        //getting strings for each field (to enter into db)
        String name = editTextName.getText().toString().trim();
        String lot = editTextLotNumber.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().toLowerCase();
        String period = spinnerPeriod.getSelectedItem().toString().toLowerCase();

        if (name.isEmpty() || lot.isEmpty() || description.isEmpty() || category.isEmpty() || period.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Double.parseDouble(lot);
        }
        catch(NumberFormatException e) {
            Toast.makeText(getContext(), "Lot# must be an actual number!", Toast.LENGTH_SHORT).show();
            return;
        }

        Item newEntry = new Item(lot, name, category, period, description, mediaUrls);
        DataModel.addItem(newEntry, getContext(), new DataView.AddItemCallback() {
            @Override
            public void onComplete(boolean success) {
                if (success) {
                    Toast.makeText(getContext(), "Entry " + newEntry.getLot() + " added successfully!", Toast.LENGTH_SHORT).show();
                    clearFields();
                    FragmentLoader.loadFragment(getParentFragmentManager(), new AdminHomeFragment());
                }
            }
        });
    }

    private void clearFields(){
        editTextName.setText("");
        editTextLotNumber.setText("");
        editTextDescription.setText("");
        spinnerCategory.setSelection(0);
        spinnerPeriod.setSelection(0);
        mediaUrls.clear();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateView(Item item) {
        DataModel.removeItem(item);
    }

    @Override
    public void showError(String errorMessage) {
        Log.v("AddFunction", errorMessage);
    }

    @Override
    public void onComplete() {
        Log.v("Remove category/period", "Success");
    }
}