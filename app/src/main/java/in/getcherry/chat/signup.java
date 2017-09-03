package in.getcherry.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity implements View.OnClickListener {

    Button btnsignup;
    EditText input_email,input_password,input_cpassword;
    TextView already;
    RelativeLayout activity_sign_up;
    private FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnsignup=(Button)findViewById(R.id.new_signup);
        input_email=(EditText)findViewById(R.id.signup_email);
        input_password=(EditText)findViewById(R.id.signup_password);
        input_cpassword=(EditText)findViewById(R.id.signup_cpassword);
        already=(TextView)findViewById(R.id.already_a_member);
        activity_sign_up=(RelativeLayout)findViewById(R.id.activity_signup);

        btnsignup.setOnClickListener(this);
        already.setOnClickListener(this);
        auth=FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.already_a_member){
            startActivity(new Intent(signup.this,login.class));
            finish();
        }
        else if(view.getId()==R.id.new_signup){
            signupuser(input_email.getText().toString(),input_password.getText().toString(),input_cpassword.getText().toString());
        }
    }

    private void signupuser(final String email,final String password,final String cpassword) {
        if (password.length() > 0 && email.length() > 0 && cpassword.length() > 0) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!email.matches(emailPattern)){
                                Snackbar s1 = Snackbar.make(activity_sign_up, "Enter Valid Email Id", Snackbar.LENGTH_SHORT);
                                s1.show();
                            }
                            else if(password.length() < 6){
                                Snackbar s2 = Snackbar.make(activity_sign_up, "Password length should be greater than 6", Snackbar.LENGTH_SHORT);
                                s2.show();
                            }
                            else if(!cpassword.equals(password)){
                                Snackbar s2 = Snackbar.make(activity_sign_up, "Both the passwords do not match", Snackbar.LENGTH_SHORT);
                                s2.show();
                            }
                            else if (!task.isSuccessful()) {
                                Snackbar s3 = Snackbar.make(activity_sign_up, "Unable to Register "+task.getException(), Snackbar.LENGTH_SHORT);
                                s3.show();
                            }
                            else {
                                Snackbar s4 = Snackbar.make(activity_sign_up, "Registered Succesfully :)", Snackbar.LENGTH_SHORT);
                                s4.show();
                                input_email.setText(null);
                                input_cpassword.setText(null);
                                input_password.setText(null);
                            }
                        }
                    });
        }
        else {
            Snackbar s5 = Snackbar.make(activity_sign_up, "Fill all fields", Snackbar.LENGTH_SHORT);
            s5.show();
        }
    }
}

