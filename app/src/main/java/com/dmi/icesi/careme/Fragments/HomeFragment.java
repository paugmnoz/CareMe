package com.dmi.icesi.careme.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmi.icesi.careme.Adapters.MoistureChangeAdapter;
import com.dmi.icesi.careme.LoginActivity;
import com.dmi.icesi.careme.Model.MoistureChange;
import com.dmi.icesi.careme.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Graphic View
    private LinearLayout graphLayout;
    private LineChart graph;
    ArrayList<Entry> yValues;
    private OnFragmentInteractionListener mListener;
    private int currentMoisture;

    //Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Moisture changes historial of cardviews
    private MoistureChangeAdapter mc_adapter;
    RecyclerView recyclerView;
    ArrayList<MoistureChange> moistureChanges;

    private TextView humactual;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private void bringMoistureChanges() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ref.child(LoginActivity.projectTitle).child("data").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshotDos) {
                        moistureChanges.clear();

                for (DataSnapshot data : dataSnapshotDos.getChildren()) {
                    MoistureChange modelo = data.getValue(MoistureChange.class);
                    currentMoisture = modelo.getMoisturePercentage();
                    String tempMoisture = Integer.toString(currentMoisture);
                    moistureChanges.add(modelo);
                    recyclerView.setAdapter(mc_adapter);
                    humactual.setText(tempMoisture + "%");
                    yValues.add(new Entry(moistureChanges.get(moistureChanges.size()-1).getHour()
                            ,moistureChanges.get(moistureChanges.size()-1).getMoisturePercentage()));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //inicializa variables Firebase
        mAuth = FirebaseAuth.getInstance();




        //evaluar sesión
        evaluarUsuario();


        humactual = (TextView) view.findViewById(R.id.hum);
        //-------------------------------- LINE CHART GRAPH ------------------------------//

        // Inflate the layout for this fragment
        graphLayout = (LinearLayout) view.findViewById(R.id.rl_graph);
        //Layout for graph
        graph = new LineChart(getContext());
        graphLayout.addView(graph,650,550);

        //CUSTOM

        graph.setTouchEnabled(true);
        graph.setDragEnabled(true);
        graph.setScaleEnabled(true);

        graph.setDrawGridBackground(false);

        //DATA
        //add graph to layout
        yValues = new ArrayList<>();

        yValues.add(new Entry(0,60f));
        yValues.add(new Entry(1,50f));
        yValues.add(new Entry(2,70f));



        LineDataSet set1 = new LineDataSet(yValues,"Data set 1");

        set1.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data =  new LineData(dataSets);

        graph.setData(data);

        //--------------------- MOISTURE CHANGE HISTORIAL ------------------//

        moistureChanges = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerId);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));

        //listaprueba();

        mc_adapter = new MoistureChangeAdapter(moistureChanges);
        recyclerView.setAdapter(mc_adapter);

        return view;
    }

    private void evaluarUsuario() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Project name
                    bringMoistureChanges();

                } else {

                }
                // ...
            }
        };
    }

    private void listaprueba() {
        moistureChanges.add(new MoistureChange(85,13,01));
        moistureChanges.add(new MoistureChange(85,13,03));
        moistureChanges.add(new MoistureChange(85,13,04));
        moistureChanges.add(new MoistureChange(85,13,06));
        moistureChanges.add(new MoistureChange(85,13,07));
    }

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
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
