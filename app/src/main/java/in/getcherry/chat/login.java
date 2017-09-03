package in.getcherry.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity implements View.OnClickListener {

    Button btnlogin,btnsignup;
    EditText input_email,input_password;
    TextView btnforgot;
    RelativeLayout activity_main;
    private FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlogin=(Button)findViewById(R.id.main_login);
        btnsignup=(Button)findViewById(R.id.new_signup);
        input_email=(EditText)findViewById(R.id.login_email);
        input_password=(EditText)findViewById(R.id.login_password);
        btnforgot=(TextView)findViewById(R.id.login_forgot);
        activity_main=(RelativeLayout)findViewById(R.id.activity_login);

        btnsignup.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        btnforgot.setOnClickListener(this);

        //initialize firebase authentication
        auth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
    if(view.getId()==R.id.login_forgot){
        startActivity(new Intent(login.this,forgot_password.class));
        finish();
    }
    else if(view.getId()==R.id.new_signup){
        startActivity(new Intent(login.this,signup.class));
        finish();
    }
    else if(view.getId()==R.id.main_login){
            loginuser(input_email.getText().toString(),input_password.getText().toString());
        }

    }

    private void loginuser(final String email,final String password) {
        if(password.length()>0 && email.length()>0) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                if(!email.matches(emailPattern)){
                                    Snackbar s = Snackbar.make(activity_main, "Enter Valid Email Id", Snackbar.LENGTH_SHORT);
                                    s.show();
                                }
                                else {
                                    Snackbar s1 = Snackbar.make(activity_main, "Email Id and Password do not match", Snackbar.LENGTH_SHORT);
                                    s1.show();
                                }
                            }
                            else {
                                startActivity(new Intent(login.this, Chat_page.class));
                            }
                        }
                    });
        }
        else{
            Snackbar s2 = Snackbar.make(activity_main, "Fill all fields", Snackbar.LENGTH_SHORT);
            s2.show();
        }
    }
}