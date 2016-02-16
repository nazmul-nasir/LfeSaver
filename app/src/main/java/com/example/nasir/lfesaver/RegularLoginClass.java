package com.example.nasir.lfesaver;

/**
 * Created by Nasir on 11/19/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.util.HashMap;

/**
 * Created by Nasir on 11/14/2015.
 */
public class RegularLoginClass extends Activity implements View.OnClickListener, AsyncResponse {

    EditText etUsername, etPassword;
    Button btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regular_login_layout);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (Button)findViewById(R.id.btn_signup);
        Intent intent = getIntent();

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),LoginClass.class);
                startActivity(intent1);

            }
        });


    }

    // public void registration()
    //  {
    //   Intent intent1 = new Intent(getApplicationContext(),LoginClass.class);
    //   startActivity(intent1);

    //   }

    @Override
    public void onClick(View v) {

        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("mobile","android");
        postData.put("username", etUsername.getText().toString());
        postData.put("password", etPassword.getText().toString() );
        PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,postData);
        loginTask.execute("http://192.168.0.102:8080/client/login.php");

    }

    @Override
    public void processFinish(String result) {
          Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        if (result.equals("Login successfully"))
        {
            Intent intent2 = new Intent(getApplicationContext(),Welcome.class);
            intent2.putExtra("username",etUsername.getText().toString());
            startActivity(intent2);

        }
        else if (result.equals(""))
        {
            Toast.makeText(this, "Please check you network connection and try again!", Toast.LENGTH_LONG).show();
        }
        else if (result.equals("Login Failed"))
        {
            Toast.makeText(this, "Login Failed try again!", Toast.LENGTH_LONG).show();
        }
        else
        {

            Toast.makeText(this, "Something Wrong!", Toast.LENGTH_LONG).show();

        }


    }

    int backButtonCount=0;

    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        Toast.makeText(this, "Press the back button once again to close the application.", 0).show();
        this.backButtonCount++;
    }
}
