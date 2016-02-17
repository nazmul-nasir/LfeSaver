package com.example.nasir.lfesaver;

/**
 * Created by Nasir on 11/19/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nasir on 11/14/2015.
 */
public class Welcome extends Activity implements View.OnClickListener, AsyncResponse, AdapterView.OnItemClickListener {

    ArrayList<HashMap<String, String>> arrList;
    String blood_group,uname;
    Button btn_search;
    TextView textResult;
    TextView username1;
    double latitude ;
    double longitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("username");
            this.username1 = (TextView) findViewById(R.id.username1);
            this.username1.setText(value.toString());
        }



        Spinner dropdown = (Spinner)findViewById(R.id.blood_group_spn2);
        String[] items = new String[]{"A+", "B+", "AB+","0+","A-", "B-", "AB-","0-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        btn_search = (Button) findViewById(R.id.btn_search);

        btn_search.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        Spinner d=(Spinner)findViewById(R.id.blood_group_spn2);
        blood_group=d.getSelectedItem().toString();

        getLocation();

        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("mobile", "android");
        postData.put("blood_group",blood_group);
        postData.put("longitude",Double.toString(longitude));
        postData.put("latitude",Double.toString(latitude));

        PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,postData);
        loginTask.execute("http://192.168.0.102:8080/client/search.php");

    }

    @Override
    public void processFinish(String result) {
        this.textResult = (TextView) findViewById(R.id.textResult);
        ListView listView = (ListView) findViewById(R.id.listView);
        this.arrList = new ArrayList();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                HashMap<String, String> map1 = new HashMap();
                map1.put("username", json.getString("username"));
                map1.put("name", json.getString("name"));
                map1.put("address", json.getString("address"));
                arrList.add(map1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!arrList.isEmpty()) {
            /*listView.setAdapter(new SimpleAdapter(this, arrList, R.layout.list_item, new String[]{"username", "name", "address"}, new int[]{R.id.username, R.id.name, R.id.address}));
            //listView.setOnItemClickListener(new C01681());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Toast.makeText(this,"position!" + position, Toast.LENGTH_LONG).show();

                 //   Intent intent = new Intent(getApplicationContext(), Profile.class);
                 //   intent.putExtra("UserName", (String)((HashMap)arrList.get(position)).get("username"));
                 //   startActivity(intent);
                }
            });

*/
            ListAdapter adapter = new SimpleAdapter( this, arrList,
                    R.layout.list_item, new String[] { "username", "name", "address" },
                    new int[] { R.id.username, R.id.name, R.id.address });

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);

           /* listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position,
                                        long id) {
                    Object o = listview.getItemAtPosition(position);
                    Toast.makeText(this,"position!" + position, Toast.LENGTH_LONG).show();

                }
            });*/

        }


    }

    @Override
    public void onItemClick(AdapterView adapterview, View view, int position, long id) {
        //Toast.makeText(this,"position!" + position, Toast.LENGTH_LONG).show();

        uname = arrList.get(position).get("username");
       // Toast.makeText(this,uname, Toast.LENGTH_LONG).show();




          Intent intent = new Intent(getApplicationContext(), Profile.class);
           intent.putExtra("UserName", uname);
           startActivity(intent);



    }

    int backButtonCount=0;

    public void onBackPressed() {
        if (backButtonCount >= 1) {
            System.exit(0);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        Toast.makeText(this, "Press the back button once again to close the application.", 0).show();
        this.backButtonCount++;
    }

    public void getLocation(){
        GPSTracker gps;
        gps = new GPSTracker(Welcome.this);

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
