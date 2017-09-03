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

public class forgot_password extends AppCompatActivity implements View.OnClickListener {

    Button reset;
    EditText input_email;
    TextView btnlogin;
    RelativeLayout activity_forgot;
    private FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        reset=(Button)findViewById(R.id.main_reset);
        input_email=(EditText)findViewById(R.id.reset_email);
        btnlogin=(TextView)findViewById(R.id.login_page);
        activity_forgot=(RelativeLayout)findViewById(R.id.activity_forgot_password);

        reset.setOnClickListener(this);
        btnlogin.setOnClickListener(this);

        //initialize firebase authentication
        auth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.login_page){
            startActivity(new Intent(forgot_password.this,login.class));
            finish();
        }
        else if(view.getId()==R.id.main_reset){
            resetuser(input_email.getText().toString());
        }
    }

    private void resetuser(final String email) {
        if(email.length()>0) {
            auth.sendPasswordResetEmail(email)
                   .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               Snackbar s = Snackbar.make(activity_forgot, "Reset link sent at : " + email, Snackbar.LENGTH_SHORT);
                               s.show();
                               input_email.setText(null);
                           }
                           else{
                               Snackbar s = Snackbar.make(activity_forgot, "Failed to send Password at : " + email, Snackbar.LENGTH_SHORT);
                               s.show();
                           }
                       }
                   });
        }
        else{
            Snackbar s2 = Snackbar.make(activity_forgot, "Enter email ID", Snackbar.LENGTH_SHORT);
            s2.show();
        }
    }
}
