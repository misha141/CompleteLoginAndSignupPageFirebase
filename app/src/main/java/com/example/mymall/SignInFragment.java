package com.example.mymall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.auto.value.AutoAnnotation;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.mymall.RegisterActivity.onResetPasswordFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    public SignInFragment() {
        // Required empty public constructor
    }

    private TextView dontHaveAnAccount,forgotPassword;
    private FrameLayout parentFrameLayout;

    private EditText email, password;
    private ImageButton closeButton;
    private Button signInButton;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    private String EmailPatter="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in,container,false);
        dontHaveAnAccount= view.findViewById(R.id.tv_dont_have_an_account);
        parentFrameLayout = getActivity().findViewById(R.id.register_frame_layout);

        email= view.findViewById(R.id.signin_email);
        password= view.findViewById(R.id.signin_password);
        forgotPassword = view.findViewById(R.id.signin_forgot_password);


        closeButton = view.findViewById(R.id.singin_close_button);
        signInButton = view.findViewById(R.id.signin_button);
        progressBar = view.findViewById(R.id.signin_progress_bar);

        firebaseAuth= FirebaseAuth.getInstance();

        return view;
        
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
       dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setFragment(new SignUpFragment());
           }
       });

       forgotPassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onResetPasswordFragment = true;
               setFragment(new ResetPasswordFragment());

           }
       });

       closeButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               MainIntent();
           }
       });

       email.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               checkInputs();

           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
       password.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               checkInputs();
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

       signInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               checkEmailAndPassword();
           }
       });
    }
    
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.silde_out_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(password.getText())){

                    signInButton.setEnabled(true);
                    signInButton.setTextColor(Color.rgb(255,255,255));


            }else{
                signInButton.setEnabled(false);
                signInButton.setTextColor(Color.argb(50,255,255,255));

            }
        }else {
            signInButton.setEnabled(false);
            signInButton.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void checkEmailAndPassword(){

        if(email.getText().toString().matches(EmailPatter)){
            if(password.length()>=8){

                progressBar.setVisibility(View.VISIBLE);
                signInButton.setEnabled(false);
                signInButton.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    MainIntent();

                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signInButton.setEnabled(true);
                                    signInButton.setTextColor(Color.rgb(255,255,255));

                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else{
                Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();

        }

    }

    private void MainIntent(){
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();

    }
}
