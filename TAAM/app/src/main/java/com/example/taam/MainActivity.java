package com.example.taam;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taam.database.DataModel;
import com.example.taam.database.Item;
import com.example.taam.ui.home.UserHomeFragment;
import com.example.taam.ui.FragmentLoader;

public class MainActivity extends AppCompatActivity {

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

        if (savedInstanceState == null) {
            FragmentLoader.loadDefaultFragment(getSupportFragmentManager(), new UserHomeFragment());
        }

        Item item = new Item("321", "name", "a", "b", "c");
        DataModel.addItem(item);
        Item item2 = new Item("123", "name", "a", "b", "c");
        DataModel.addItem(item2);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack(); // reverses last fragment change
        } else {
            super.onBackPressed();
        }
    }
}