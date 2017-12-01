package com.dmi.icesi.careme.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.dmi.icesi.careme.LoginActivity;
import com.dmi.icesi.careme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tv_profile_projname;

    private TextView username, email, password;
    private EditText et_email, et_pass;
    private Button btnEmail, btnPass;
    private ViewSwitcher vs;
    private ViewSwitcher vsPass;
    private FirebaseUser usuarioActual;

    private OnFragmentInteractionListener mListener;
    private Button signOutBut;
    private FirebaseAuth mAuth;

    private TextView mensajeTextView;
    private EditText mensajeEditText;

    private StorageReference mStorageRef;

    private CircleImageView holder;
    private Bitmap pic;
    private Uri uri;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();

    private static final int SELECT_SINGLE_PICTURE = 101;
    public static final String IMAGE_TYPE = "image/*";

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        username = (TextView) view.findViewById(R.id.tv_name);
        email = (TextView) view.findViewById(R.id.tv_email);
        password = (TextView) view.findViewById(R.id.tv_password);

        tv_profile_projname = (TextView) view.findViewById(R.id.tv_profile_projname);
        tv_profile_projname.setText(LoginActivity.projectTitle);

        et_email = (EditText) view.findViewById(R.id.et_email);
        et_pass = (EditText) view.findViewById(R.id.et_pass);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        btnEmail = (Button) view.findViewById(R.id.btn_email);
        btnPass = (Button) view.findViewById(R.id.btn_pass);

        vs = (ViewSwitcher) view.findViewById(R.id.my_switcher);
        vsPass = (ViewSwitcher) view.findViewById(R.id.switcherPass);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vs.showNext();
            }
        });


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vs.showNext();
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vsPass.showNext();
            }
        });


        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vs.showNext();
                updateEmail(et_email.getText().toString());
            }
        });


        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vsPass.showNext();
                updatePassword(et_pass.getText().toString());
            }
        });



        usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        username.setText(usuarioActual.getDisplayName());
        email.setText(usuarioActual.getEmail());
        et_email.setText(usuarioActual.getEmail());
       // password.setText(usuarioActual.getPassword());

        mAuth = FirebaseAuth.getInstance();

        signOutBut = (Button) view.findViewById(R.id.btn_logout);

        signOutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });


        mStorageRef = FirebaseStorage.getInstance().getReference();

        ImageLoader.getInstance().init(getConfig());

        holder = (CircleImageView) view.findViewById(R.id.perfil);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot fotos : dataSnapshot.getChildren()) {
                    if(fotos.getKey().contains("Photo")) {
                        //Si es un usuario especifico se puede hacer con el correo o
                        //con el Uid con mAuth.getCurrentUser().getUid()
                        Uri downloaded = Uri.parse(fotos.getValue().toString());
                        Toast.makeText(getActivity().getApplicationContext(),"La foto es: " + downloaded.toString(),Toast.LENGTH_LONG).show();
                        ImageLoader.getInstance().displayImage(downloaded.toString(),holder);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Seleccione una imagen"), SELECT_SINGLE_PICTURE);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void updateEmail(String email){
        usuarioActual.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        } else{
                            Log.d(TAG, "Buuu NO address updated.");
                        }
                    }
                });
    }

    public void updatePassword(String newPassword){

        usuarioActual.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
    }


/*    public void TextViewClicked(View v) {
        ViewSwitcher switcher = (ViewSwitcher) getView().findViewById(R.id.my_switcher);
        switcher.showNext(); //or switcher.showPrevious();
        TextView emailTV = (TextView) switcher.findViewById(R.id.tv_email);
        //myTV.setText("value");
    }*/

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_SINGLE_PICTURE) {

                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    pic = bitmap;
                    holder.setImageBitmap(bitmap);

                    StorageReference storageReference = mStorageRef.child("Ossa");
                    byte[] profileBytes = getBytesFromBitmap(pic,100);

                    UploadTask uploadTask = null;
                    uploadTask = storageReference.putBytes(profileBytes);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri getDownloadUrl = taskSnapshot.getDownloadUrl();
                            ref.child("Ossa").setValue(getDownloadUrl.toString());
                            //Toast.makeText(getContext(), "Se subio foto de perfil a: " + getDownloadUrl.toString(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), "No se pudo cambiar la foto de perfil", Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    System.out.println("Error al cargar imagen");
                }
            }
        } else {
            Toast.makeText(getActivity(), "No se cargo ninguna imagen", Toast.LENGTH_LONG).show();
        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    public ImageLoaderConfiguration getConfig(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        return configuration;
    }

}
