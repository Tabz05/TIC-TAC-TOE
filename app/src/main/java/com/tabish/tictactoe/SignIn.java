package com.tabish.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignIn extends AppCompatActivity {

    private EditText emailSignIn;
    private EditText passSignIn;
    private TextView forgotPassword;

    private FirebaseAuth frbAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private ImageView hideUnhidePass;

    private boolean passwordHidden=true;

    private void loginUser() {
        String email = emailSignIn.getText().toString();
        String password = passSignIn.getText().toString();
        frbAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent goToMainActivity = new Intent (getApplicationContext(),MainActivity.class);
                            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goToMainActivity);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(SignIn.this,"Sign In Unsuccessful, inavlid credentials",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void SignInClicked(View view)
    {
        loginUser();
    }

    public void forgotPasswordClick (View view)
    {
        Intent goToForgotPassword = new Intent(getApplicationContext(),PasswordReset.class);

        startActivity(goToForgotPassword);
    }

    public void hideOrUnhide(View view)
    {
        if(passwordHidden)
        {
            hideUnhidePass.setBackgroundResource(R.drawable.hide);
            passSignIn.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordHidden=false;
        }
        else
        {
            hideUnhidePass.setBackgroundResource(R.drawable.show);
            passSignIn.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordHidden=true;
        }

    }

    public void signUpClick(View view)
    {
        Intent goToSignUp = new Intent(getApplicationContext(),SignUp.class);

        startActivity(goToSignUp );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailSignIn=findViewById(R.id.emailSignIn);
        passSignIn=findViewById(R.id.passSignInText);
        forgotPassword=findViewById(R.id.ForgotPasswordText);

        hideUnhidePass=findViewById(R.id.hideUnhidePass);

        frbAuth = FirebaseAuth.getInstance();
        currentUser = frbAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }
}