package com.example.david.partyum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText etUser;
    EditText etPassword;
    CheckBox cbRecordar;
    Button btnEntrar;
    Button btnRegistro;
    TextView tvErrorLogin;
    ProgressBar pbLogin;

    String IP = "http://partyum.000webhostapp.com";
    String GET = IP + "/obtener_usuarios.php";

    ObtenerWebService hiloconexion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = (EditText)findViewById(R.id.etUser);
        etPassword = (EditText)findViewById(R.id.etPassword);
        cbRecordar = (CheckBox)findViewById(R.id.cbRecordar);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);
        btnRegistro = (Button)findViewById(R.id.btnRegistro);
        tvErrorLogin = (TextView)findViewById(R.id.tvErrorLogin);
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedLogin", 0);
        if(!pref.getString("username","").equals("") && !pref.getString("password", "").equals("")){
            etUser.setText(pref.getString("username", null));
            etPassword.setText(pref.getString("password",null));
            login();
        }

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login(){
        tvErrorLogin.setVisibility(View.INVISIBLE);
        boolean correcto=true;
        if(etUser.getText().toString().equals("") || etUser.getText().toString().equals(null)){
            etUser.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            correcto=false;
        }
        if(etPassword.getText().toString().equals("") || etPassword.getText().toString().equals(null)){
            etPassword.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            correcto=false;
        }

        if(correcto){
            tvErrorLogin.setVisibility(View.INVISIBLE);
            etUser.setVisibility(View.INVISIBLE);
            etPassword.setVisibility(View.INVISIBLE);
            cbRecordar.setVisibility(View.INVISIBLE);
            btnEntrar.setVisibility(View.INVISIBLE);
            btnRegistro.setVisibility(View.INVISIBLE);
            pbLogin.setVisibility(View.VISIBLE);

            etUser.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            etPassword.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

            hiloconexion = new ObtenerWebService();
            hiloconexion.execute(GET,"1");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ObtenerWebService extends AsyncTask<String,Void,String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;
            String devuelve ="";



            if(params[1]=="1"){
                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        JSONObject respuestaJSON = new JSONObject(result.toString());

                        String resultJSON = respuestaJSON.getString("estado");

                        if (resultJSON.equals("1")){
                            JSONArray usersJSON = respuestaJSON.getJSONArray("users");
                            for(int i=0;i<usersJSON.length();i++){
                                if(etUser.getText().toString().equals(usersJSON.getJSONObject(i).getString("username"))
                                        && etPassword.getText().toString().equals(usersJSON.getJSONObject(i).getString("password"))){
                                        devuelve = "Encontrado";
                                }
                            }

                        }
                        else if (resultJSON=="2"){
                            devuelve = "No hay usuarios";
                        }

                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return devuelve;


            }
            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("Encontrado")){
                if(cbRecordar.isEnabled()){
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedLogin", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", etUser.getText().toString());
                    editor.putString("password", etPassword.getText().toString());
                    editor.commit();
                }
                tvErrorLogin.setVisibility(View.INVISIBLE);
                pbLogin.setVisibility(View.INVISIBLE);
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                finish();
            } else {
                tvErrorLogin.setVisibility(View.VISIBLE);
                etUser.setVisibility(View.VISIBLE);
                etPassword.setVisibility(View.VISIBLE);
                cbRecordar.setVisibility(View.VISIBLE);
                btnEntrar.setVisibility(View.VISIBLE);
                btnRegistro.setVisibility(View.VISIBLE);
                pbLogin.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
