package io.hasura.sms_gateway;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;

import static java.security.AccessController.getContext;

public class HomeScreen extends AppCompatActivity {

    //private String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        final int PERMISSION_REQUEST_CODE = 1;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }



        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        TextView dev_id = (TextView) findViewById(R.id.log);
        dev_id.setText("Device ID: "+device_id);

        final HasuraClient client = Hasura.getClient();
        HasuraUser user = client.getUser();



        Timer timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask() {
            public void run() {

                try{

                    client.useDataService()
                            .setRequestBody(new SelectQuery(device_id))
                            .expectResponseTypeArrayOf(ResponseMessage.class)
                            .enqueue(new Callback<List<ResponseMessage>, HasuraException>() {
                                @Override
                                public void onSuccess(List<ResponseMessage> r) {
                                    int num_msg = r.size();
                                    for (int i = 0; i < num_msg; i++) {
                                        String msg_body = r.get(i).getMsg_body().toString().trim();
                                        String dest_num = r.get(i).getDest_num().toString().trim();
                                        sendSMS(dest_num, msg_body);
                                    }

                                    client.useDataService()
                                            .setRequestBody(new DeleteQuery(device_id))
                                            .expectResponseType(DeleteResponse.class)
                                            .enqueue(new Callback<DeleteResponse, HasuraException>() {
                                                @Override
                                                public void onSuccess(DeleteResponse deleteResponse) {
                                                    Toast.makeText(HomeScreen.this, "Sent "+ Integer.toString(deleteResponse.getAffected_rows()) + " message(s)", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(HasuraException e) {
                                                    Toast.makeText(HomeScreen.this, e.toString(), Toast.LENGTH_SHORT).show();

                                                }
                                            });


                                    //Toast.makeText(HomeScreen.this, "Success!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(HasuraException e) {
                                    Toast.makeText(HomeScreen.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });



                }
                catch (Exception e) {
                    Toast.makeText(HomeScreen.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, 0, 5000);
    }





    public void sendSMS(String dest_num, String msg_body) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(dest_num, null, msg_body, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent to "+dest_num, Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
