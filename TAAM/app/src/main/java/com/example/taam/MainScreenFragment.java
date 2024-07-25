package com.example.taam;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainScreenFragment extends Fragment implements DataView{

    private Button buttonAdmin, buttonView1, buttonSearch1, buttonView2, buttonSearch2, buttonBack, buttonAdd, buttonRemove, buttonReport;
    private Group beforeLogIn, afterLogIn;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private DataModel dm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_screen_fragment, container, false);

        // setting up recycler view
        dm = new DataModel(this);
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);

        buttonAdmin = view.findViewById(R.id.buttonAdmin);
        buttonView1 = view.findViewById(R.id.buttonView1);
        buttonSearch1 = view.findViewById(R.id.buttonSearch1);
        buttonView2 = view.findViewById(R.id.buttonView2);
        buttonSearch2 = view.findViewById(R.id.buttonSearch2);
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonRemove = view.findViewById(R.id.buttonRemove);
        buttonReport = view.findViewById(R.id.buttonReport);
        beforeLogIn = view.findViewById(R.id.beforeLogIn);
        afterLogIn = view.findViewById(R.id.afterLogIn);
        recyclerView = view.findViewById(R.id.recyclerView);

        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentLoader.loadFragment(getParentFragmentManager(), new ReportFragment());
            }
        });

        //buttonAdmin.setOnClickListener(v -> loadFragment(new AdminFragment());
        buttonAdmin.setOnClickListener(v -> logIn());

        buttonView1.setOnClickListener(v -> viewItem());
        buttonView2.setOnClickListener(v -> viewItem());
        //buttonSearch1.setOnClickListener(v -> loadFragment(new SearchFragment());
        //buttonSearch2.setOnClickListener(v -> loadFragment(new SearchFragment());
        buttonBack.setOnClickListener(v -> viewBeforeLogIn());
        //buttonAdd.setOnClickListener(v -> loadFragment(new AddFragment());
        //buttonRemove.setOnClickListener(v -> removeItem(itemList));
//        buttonRemove.setOnClickListener(v -> FragmentLoader.loadFragment(
//                getParentFragmentManager(), new RemoveFragment(itemList)));
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemovePopUp popUp = new RemovePopUp(dm, getContext(), itemAdapter);
                popUp.removeItem();
            }
        });
        //buttonReport.setOnClickListener(v -> loadFragment(new ReportFragment());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);

        dm.displayAllItems();

        Item item = new Item("321", "name", "a", "b", "c");
        dm.addItem(item);
        Item item2 = new Item("123", "name", "a", "b", "c");
        dm.addItem(item2);

        viewBeforeLogIn();

        return view;
    }

    @Override
    public void updateView(Item item) {
        itemList.add(item);
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String errorMessage) {
        Log.v("Main Screen", errorMessage);
    }

    private void logIn(){
        // do something in adminFragment to ensure correct username and password is given
        // ...
        viewAfterLogIn();
    }

    private void viewBeforeLogIn(){
        beforeLogIn.setVisibility(View.VISIBLE);
        afterLogIn.setVisibility(View.GONE);
    }

    private void viewAfterLogIn(){
        beforeLogIn.setVisibility(View.GONE);
        afterLogIn.setVisibility(View.VISIBLE);
    }


    private void viewItem(){
        int count = 0;
        Item selected = null;
        for(Item item : itemList){
            if(item.isSelected()){
                count += 1;
                selected = item;
            }
            if(count > 1){
                Toast.makeText(getContext(), "More than 1 item is selected.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(count == 1){
            FragmentLoader.loadFragment(getParentFragmentManager(), new ViewFragment(selected.getLot()));
        }
        else{
            Toast.makeText(getContext(), "Please first select an item to view.", Toast.LENGTH_SHORT).show();
        }
    }
}
