package com.example.traffic_predictor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_Up extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextName, editTextConfirmPassword;
    Button Signupbutton;
    FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth=FirebaseAuth.getInstance();
        editTextName=findViewById(R.id.editTextText);
        editTextEmail=findViewById(R.id.editTextTextEmailAddress2);
        editTextPassword=findViewById(R.id.editTextTextPassword2);
        editTextConfirmPassword=findViewById(R.id.editTextTextPassword3);
        Signupbutton=findViewById(R.id.button3);

        Signupbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String name,email,password,cnfpassword;
                name=String.valueOf(editTextName.getText());
                email=String.valueOf(editTextEmail.getText());
                password=String.valueOf(editTextPassword.getText());
                cnfpassword=String.valueOf(editTextConfirmPassword.getText());

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(Sign_Up.this,"Enter Your Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Sign_Up.this,"Enter Your Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Sign_Up.this,"Enter Your Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(cnfpassword)){
                    Toast.makeText(Sign_Up.this,"Confirm Your Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Sign_Up.this, "Account created",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Sign_Up.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                Intent intent = new Intent(Sign_Up.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}