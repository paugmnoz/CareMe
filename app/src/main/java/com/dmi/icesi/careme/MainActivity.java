package com.dmi.icesi.careme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmi.icesi.careme.Fragments.ChatFragment;
import com.dmi.icesi.careme.Fragments.HomeFragment;
import com.dmi.icesi.careme.Fragments.ProfileFragment;
import com.dmi.icesi.careme.Model.MensajeEnviar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    //Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Fragment inicial
    private Fragment homeFragment;
    private HomeFragment.OnFragmentInteractionListener mListener;

    String projectname;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {

                case R.id.navigation_dashboard:
                    Fragment homeFragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, homeFragment).commit();
                    break;

                case R.id.navigation_home:
                    Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                    startActivity(i);
                   // Fragment chatFragment = new ChatFragment();
                    //getSupportFragmentManager().beginTransaction().replace(R.id.content, chatFragment).commit();
                    break;

                case R.id.navigation_notifications:
                    Fragment profileFragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, profileFragment).commit();
                    break;
            }

            return true;
        }

    };

    public MainActivity () {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f4f4f4")));

        //inicializa variables Firebase
        mAuth = FirebaseAuth.getInstance();



        //evaluar sesión
        evaluarUsuario();

        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, homeFragment).commit();

    }

    private void evaluarUsuario() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Project name
                    projectname = "Cactus";
                    getSupportActionBar().setTitle(projectname);

                } else {
                    // User is signed out
                    Intent i = new Intent(getApplicationContext(), BeginnerActivity.class);
                    startActivity(i);
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

    }

    public String getProjectname() {
        return projectname;
    }
}
