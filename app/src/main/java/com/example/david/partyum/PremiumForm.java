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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PremiumForm extends AppCompatActivity {

    TextView tvTextoFormPremium;
    ProgressBar pbFormPremium;
    EditText etEmailPremium, etUsuarioPremium;
    Button btnFormPremium;

    String IP = "http://partyum.000webhostapp.com";
    String GET = IP + "/obtener_solicitud_premium.php";
    String INSERT = IP + "/insertar_solicitud_premium.php";

    ObtenerWebService hiloconexion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Premium");
        setSupportActionBar(toolbar);

        tvTextoFormPremium = (TextView) findViewById(R.id.tvTextoFormPremium);
        pbFormPremium = (ProgressBar) findViewById(R.id.pbFormPremium);
        etEmailPremium = (EditText) findViewById(R.id.etEmailPremium);
        etUsuarioPremium = (EditText) findViewById(R.id.etUsuarioPremium);
        btnFormPremium = (Button) findViewById(R.id.btnFormPremium);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedLogin", 0);
        etUsuarioPremium.setText(pref.getString("username", null));

        hiloconexion = new ObtenerWebService();
        hiloconexion.execute(GET,"1");

        btnFormPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etEmailPremium.getText().toString().equals("") && !etEmailPremium.getText().toString().equals(null)){
                    pbFormPremium.setVisibility(View.VISIBLE);
                    tvTextoFormPremium.setVisibility(View.INVISIBLE);
                    etUsuarioPremium.setVisibility(View.INVISIBLE);
                    etEmailPremium.setVisibility(View.INVISIBLE);
                    btnFormPremium.setVisibility(View.INVISIBLE);
                    hiloconexion = new ObtenerWebService();
                    hiloconexion.execute(INSERT,"3", etUsuarioPremium.getText().toString(), etEmailPremium.getText().toString());
                } else {
                    etEmailPremium.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                }
            }
        });
    }

    public class ObtenerWebService extends AsyncTask<String,Void,String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;
            String devuelve ="No Encontrado";



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
                                if(etUsuarioPremium.getText().toString().equals(usersJSON.getJSONObject(i).getString("username"))){
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


            } else if(params[1]=="3"){

                try {
                    HttpURLConnection urlConn;

                    DataOutputStream printout;
                    DataInputStream input;
                    url = new URL(cadena);
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestProperty("Accept", "application/json");
                    urlConn.connect();

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username", params[2]);
                    jsonParam.put("email", params[3]);

                    OutputStream os = urlConn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    int respuesta = urlConn.getResponseCode();


                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                        while ((line=br.readLine()) != null) {
                            result.append(line);
                        }

                        JSONObject respuestaJSON = new JSONObject(result.toString());

                        String resultJSON = respuestaJSON.getString("estado");

                        if (resultJSON.equals("1")) {
                            devuelve = "Solicitud insertada correctamente";

                        } else if (resultJSON == "2") {
                            devuelve = "La solicitud no pudo insertarse";
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
                pbFormPremium.setVisibility(View.INVISIBLE);
                tvTextoFormPremium.setText("Tu solicitud ya ha sido enviada. Por favor, espera nuestra respuesta en el email que nos indicaste");
                tvTextoFormPremium.setVisibility(View.VISIBLE);
            } else if(s.equals("No Encontrado")){
                pbFormPremium.setVisibility(View.INVISIBLE);
                tvTextoFormPremium.setVisibility(View.VISIBLE);
                etUsuarioPremium.setVisibility(View.VISIBLE);
                etEmailPremium.setVisibility(View.VISIBLE);
                btnFormPremium.setVisibility(View.VISIBLE);
            } else if(s.equals("Solicitud insertada correctamente")){
                pbFormPremium.setVisibility(View.INVISIBLE);
                tvTextoFormPremium.setText("Tu solicitud ya ha sido enviada. Por favor, espera nuestra respuesta en el email que nos indicaste");
                tvTextoFormPremium.setVisibility(View.VISIBLE);
            } else if(s.equals("La solicitud no pudo insertarse")){
                pbFormPremium.setVisibility(View.INVISIBLE);
                tvTextoFormPremium.setText("Tu solicitud no ha podido procesarse. Por favor, prueba de nuevo más tarde");
                tvTextoFormPremium.setVisibility(View.VISIBLE);
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
