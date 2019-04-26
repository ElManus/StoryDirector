package com.apps.gragas.storydirector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.apps.gragas.storydirector.R;

public class SceneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);


            /*
        Toolbar
         */
        Toolbar toolbar = findViewById(R.id.toolbarScenesActivity);
        //toolbar.setNavigationIcon(R.drawable.ic_save_icon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Новая сцена");
        //TODO если новая сцена, тогда тайтл = новая сцена, если старая сцена - берем имя из базы
    }
}
