package io.hasura.sms_gateway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.hasura.sdk.responseListener.AuthResponseListener;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        final EditText username = (EditText) findViewById(R.id.login_username);
        final EditText password = (EditText) findViewById(R.id.login_password);
        Button login_button = (Button) findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final HasuraClient client = Hasura.getClient();
                final HasuraUser user = client.getUser();

                String entered_username = username.getText().toString().trim();
                String entered_password = password.getText().toString().trim();

                user.setUsername(entered_username);
                user.setPassword(entered_password);

                user.login(new AuthResponseListener() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(LoginScreen.this, "Logged in", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(LoginScreen.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
