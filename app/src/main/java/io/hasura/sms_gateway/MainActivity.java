package io.hasura.sms_gateway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.ProjectConfig;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.SignUpResponseListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hasura.setProjectConfig(new ProjectConfig.Builder()
                .setProjectName("eyepiece95")
                //.enableOverHttp()
                .build())
                .enableLogs()
                .initialise(this);

        final EditText username = (EditText) findViewById(R.id.signup_username);
        final EditText password = (EditText) findViewById(R.id.signup_password);
        //final EditText m_num = (EditText) findViewById(R.id.mobile_number);
        Button signup = (Button) findViewById(R.id.signup_button);
        TextView textView = (TextView) findViewById(R.id.link_to_signin);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HasuraClient client = Hasura.getClient();
                HasuraUser user = client.getUser();

                user.setUsername(username.getText().toString().trim());
                user.setPassword(password.getText().toString().trim());
                //user.setMobile(m_num.getText().toString().trim());

                user.signUp(new SignUpResponseListener() {
                    @Override
                    public void onSuccessAwaitingVerification(HasuraUser hasuraUser) {
                        /*Intent i = new Intent(getApplicationContext(), OTP.class);
                        startActivity(i);*/
                    }

                    @Override
                    public void onSuccess(HasuraUser hasuraUser) {
                        Toast.makeText(MainActivity.this, "Sign up complete", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), io.hasura.sms_gateway.LoginScreen.class);
                        startActivity(i);

                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), io.hasura.sms_gateway.LoginScreen.class);
                startActivity(i);
            }
        });



    }
}



