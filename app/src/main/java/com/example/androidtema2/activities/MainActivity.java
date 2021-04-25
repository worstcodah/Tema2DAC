package com.example.androidtema2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.androidtema2.R;
import com.example.androidtema2.fragments.Fragment1;
import com.example.androidtema2.fragments.Fragment2;
import com.example.androidtema2.fragments.Fragment3;
import com.example.androidtema2.interfaces.ActivityFragmentCommunication;
import com.example.androidtema2.models.Element;

public class MainActivity extends AppCompatActivity implements ActivityFragmentCommunication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openFragment1();
    }

    @Override
    public void openFragment1() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = Fragment1.class.getName();
        Fragment1 fragment1 = new Fragment1();
        FragmentTransaction addTransaction = transaction.add(
                R.id.frame_layout, fragment1, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openFragment2(Element user) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = Fragment2.class.getName();
        Fragment2 fragment2 = new Fragment2(user);
        FragmentTransaction addTransaction = transaction.replace(
                R.id.frame_layout, fragment2, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openFragment3(Element album) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = Fragment3.class.getName();
        Fragment3 fragment3 = new Fragment3(album);
        FragmentTransaction addTransaction = transaction.replace(
                R.id.frame_layout, fragment3, tag
        );
        addTransaction.commit();
    }
}