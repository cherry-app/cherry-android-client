package in.getcherry.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tv=(TextView)findViewById(R.id.forgot);

        tv.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            }
        });
    }
}
