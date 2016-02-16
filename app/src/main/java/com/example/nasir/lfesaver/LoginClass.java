package com.example.nasir.lfesaver;

/**
 * Created by Nasir on 11/19/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



import java.util.HashMap;

/**
 * Created by Nasir on 11/11/2015.
 */
public class LoginClass extends Activity implements View.OnClickListener, AsyncResponse {

    EditText name,address,mobile_no,username,password;
    String blood_group;
    Button button;
    double latitude ;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        Intent intent2 = getIntent();

        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        mobile_no = (EditText) findViewById(R.id.mobile_no);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        button=(Button) findViewById(R.id.button);



        Spinner dropdown = (Spinner)findViewById(R.id.blood_group_spn);
        String[] items = new String[]{"A+", "B+", "AB+","0+","A-", "B-", "AB-","0-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        blood_group=dropdown.getSelectedItem().toString();



        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Spinner d=(Spinner)findViewById(R.id.blood_group_spn);
        blood_group=d.getSelectedItem().toString();

        getLocation();
        Toast.makeText(
                getApplicationContext(),
                "Your Location is -\nLat: " + Double.toString(latitude) + "\nLong: "
                        + longitude, Toast.LENGTH_LONG).show();


        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("mobile","android");
        postData.put("username", username.getText().toString());
        postData.put("name", name.getText().toString());
        postData.put("address", address.getText().toString());
        postData.put("blood_group", blood_group);
        postData.put("mobile_no", mobile_no.getText().toString());
        postData.put("password", password.getText().toString() );
        postData.put("longitude", Double.toString(longitude) );
        postData.put("latitude",Double.toString(latitude) );

        PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,postData);
        loginTask.execute("http://192.168.0.102:8080/client/registration.php");


    }

    @Override
    public void processFinish(String result) {


        if(result.equals("1"))
        {
            for (int i=0; i < 2; i++)
                Toast.makeText(this,"Registration Completed. Please login!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(),RegularLoginClass.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,result, Toast.LENGTH_LONG).show();
            Toast.makeText(this,"Registration Failed. Try again. Use unique username", Toast.LENGTH_LONG).show();

        }

    }

    public void getLocation(){
        GPSTracker gps;
        gps = new GPSTracker(LoginClass.this);

        if(gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            /*Toast.makeText(
                    getApplicationContext(),
                    "Your Location is -\nLat: " + latitude + "\nLong: "
                            + longitude, Toast.LENGTH_LONG).show();*/
        } else {
            gps.showSettingsAlert();
        }

    }
}
