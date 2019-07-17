package com.example.david.partyum;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegistroActivity extends AppCompatActivity {

    EditText etUsuario;
    EditText etContra;
    EditText etContra2;
    EditText etNombre;
    EditText etApellidos;
    EditText etEmail;
    ImageButton btnAtras;
    ImageButton btnGuardar;
    TextView tvErrorRegistro;
    ProgressBar pbGuardando;
    boolean usuarioRepetido;

    String IP = "http://partyum.000webhostapp.com";
    String GET = IP + "/obtener_usuarios.php";
    String INSERT = IP + "/insertar_usuario.php";

    ObtenerWebService hiloconexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etUsuario = (EditText)findViewById(R.id.etUsuario);
        etContra = (EditText)findViewById(R.id.etContra);
        etContra2 = (EditText)findViewById(R.id.etContra2);
        etNombre = (EditText)findViewById(R.id.etNombre);
        etApellidos = (EditText)findViewById(R.id.etApellidos);
        etEmail = (EditText)findViewById(R.id.etEmail);
        btnAtras = (ImageButton)findViewById(R.id.btnAtras);
        btnGuardar = (ImageButton)findViewById(R.id.btnGuardar);
        tvErrorRegistro = (TextView)findViewById(R.id.tvErrorRegistro);
        pbGuardando = (ProgressBar)findViewById(R.id.pbGuardando);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean correcto=true;

                if(etUsuario.getText().toString().equals("") || etUsuario.getText().toString().equals(null)){
                    etUsuario.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    correcto=false;
                }
                if(etContra.getText().toString().equals("") || etContra.getText().toString().equals(null)){
                    etContra.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    correcto=false;
                }
                if(etContra2.getText().toString().equals("") || etContra2.getText().toString().equals(null)){
                    etContra2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    correcto=false;
                }
                if(etNombre.getText().toString().equals("") || etNombre.getText().toString().equals(null)){
                    etNombre.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    correcto=false;
                }
                if(etApellidos.getText().toString().equals("") || etApellidos.getText().toString().equals(null)){
                    etApellidos.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    correcto=false;
                }
                if(etEmail.getText().toString().equals("") || etEmail.getText().toString().equals(null)){
                    etEmail.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    correcto=false;
                }
                if(!etContra.getText().toString().equals(etContra2.getText().toString())){
                    etContra.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    etContra2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    tvErrorRegistro.setText("Ambas contraseñas deben coincidir");
                    tvErrorRegistro.setVisibility(View.VISIBLE);
                    correcto=false;
                }
                if(correcto){
                    etUsuario.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    etContra.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    etContra2.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    etNombre.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    etApellidos.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    etEmail.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    etContra.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    etContra2.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    tvErrorRegistro.setVisibility(View.INVISIBLE);

                    hiloconexion = new ObtenerWebService();
                    hiloconexion.execute(GET,"1");

                }
            }
        });

    }

    public void noEncontrado(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbGuardando.setVisibility(View.VISIBLE);
                etUsuario.setVisibility(View.INVISIBLE);
                etContra.setVisibility(View.INVISIBLE);
                etContra2.setVisibility(View.INVISIBLE);
                etNombre.setVisibility(View.INVISIBLE);
                etApellidos.setVisibility(View.INVISIBLE);
                etEmail.setVisibility(View.INVISIBLE);
                btnAtras.setVisibility(View.INVISIBLE);
                btnGuardar.setVisibility(View.INVISIBLE);
                tvErrorRegistro.setVisibility(View.INVISIBLE);

                hiloconexion = new ObtenerWebService();
                hiloconexion.execute(INSERT,"3",etUsuario.getText().toString(),etContra.getText().toString(),
                        etNombre.getText().toString(),etApellidos.getText().toString(),etEmail.getText().toString());
            }
        });

    }

    public void siEncontrado(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvErrorRegistro.setText("El usuario ya existe");
                tvErrorRegistro.setVisibility(View.VISIBLE);
                etUsuario.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }
        });

    }


    public class ObtenerWebService extends AsyncTask<String,Void,String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
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

                        boolean encontrado = false;

                        if (resultJSON.equals("1")){
                            JSONArray usersJSON = respuestaJSON.getJSONArray("users");
                            for(int i=0;i<usersJSON.length();i++){
                                if(etUsuario.getText().toString().equals(usersJSON.getJSONObject(i).getString("username"))){
                                    devuelve = "Encontrado";
                                    encontrado = true;
                                }
                            }

                            if(encontrado == false){
                                noEncontrado();
                            } else if(encontrado == true){
                                siEncontrado();
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
            else if(params[1]=="3"){

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
                    jsonParam.put("password", params[3]);
                    jsonParam.put("name", params[4]);
                    jsonParam.put("surname", params[5]);
                    jsonParam.put("email", params[6]);
                    jsonParam.put("foto", "");
                    jsonParam.put("conectado", 0);
                    jsonParam.put("referido", "");
                    jsonParam.put("premium", 0);

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

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON.equals("1")) {      // hay un alumno que mostrar
                            devuelve = "Usuario insertado correctamente";

                        } else if (resultJSON == "2") {
                            devuelve = "El usuario no pudo insertarse";
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
            if(s.equals("Usuario insertado correctamente")){
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
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
