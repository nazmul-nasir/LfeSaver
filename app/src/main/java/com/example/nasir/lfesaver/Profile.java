package com.example.nasir.lfesaver;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends Activity implements AsyncResponse, OnClickListener {
    TextView address;
    ArrayList<HashMap<String, String>> arrList;
    TextView blood_group;
    Button button;
    TextView mobile_no;
    TextView name;
    String phone;
    TextView username1;
    String value;

    double latitude ;
    double longitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        value = getIntent().getExtras().getString("UserName");
        username1 = (TextView) findViewById(R.id.username);
        name = (TextView) findViewById(R.id.name);
        mobile_no = (TextView) findViewById(R.id.mbl_no);
        address = (TextView) findViewById(R.id.address);
        blood_group = (TextView) findViewById(R.id.bld_grp);
        button = (Button) findViewById(R.id.button);

        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("mobile", "android");
        postData.put("username", value);
       PostResponseAsyncTask profTask = new PostResponseAsyncTask(this,postData);
        profTask.execute("http://192.168.0.102:8080/client/profile.php");

        button.setOnClickListener(this);
    }
    public void onClick(View v) {


        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                +"+880"+ phone)));
    }

    public void processFinish(String result) {
        int i;
        /*for (i = 0; i < 2; i++) {
            this.arrList = new ArrayList();
        }*/
        try {
            JSONArray jArray = new JSONArray(result);
            for (i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                username1.setText("Username : " + json.getString("username"));
                name.setText("Name : " + json.getString("name"));
                address.setText("Address : " + json.getString("address"));
                mobile_no.setText("Mobile No : " + json.getString("mobile_no"));
                phone = json.getString("mobile_no");
                blood_group.setText("Blood Group : " + json.getString("blood_group"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
