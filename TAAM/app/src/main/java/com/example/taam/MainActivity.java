package com.example.taam;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.taam.ui.login.LoginFragmentView;

public class MainActivity extends AppCompatActivity
implements DataView
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DataModel model = new DataModel(this);
        model.displayItem("123");
        Log.v("main activity", "all items");
        model.displayAllItems();

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            // Add default fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, new LoginFragmentView()); // Default fragment
            transaction.commit();
        }
    }

    @Override
    public void updateView(Item item) {
        Log.v("main activity", "name: " + item.name);
        Log.v("main activity", "category: " + item.category);
    }

    public void showError(String error) {
        Log.v("main activity", error);
    }
}