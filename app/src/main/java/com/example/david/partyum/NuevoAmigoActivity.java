package com.example.david.partyum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NuevoAmigoActivity extends AppCompatActivity {

    EditText etBusquedaGente;
    ImageButton ibBuscarGente;
    ListView lvGente;
    ProgressBar pbBuscando;
    ArrayList<User> gente;
    ArrayList<User> genteOriginal;
    private static CustomAdapterGente adapterGente;

    String IP = "http://partyum.000webhostapp.com";
    String GET = IP + "/obtener_usuarios.php";

    ObtenerWebService hiloconexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_amigo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Gente");
        setSupportActionBar(toolbar);

        etBusquedaGente = (EditText)findViewById(R.id.etBusquedaGente);
        ibBuscarGente = (ImageButton)findViewById(R.id.ibBuscargente);
        lvGente = (ListView)findViewById(R.id.lvGente);
        pbBuscando = (ProgressBar)findViewById(R.id.pbBuscando);

        hiloconexion = new ObtenerWebService();
        hiloconexion.execute(GET,"1");

        gente= new ArrayList<>();
        genteOriginal = new ArrayList<>();

        lvGente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User user= gente.get(position);
                //Intent amigo
                Toast.makeText(view.getContext(), ""+user.getUsuario(), Toast.LENGTH_SHORT).show();

            }
        });

        ibBuscarGente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<User> genteFiltrada;
                genteFiltrada= new ArrayList<>();

                String filtro = etBusquedaGente.getText().toString().toLowerCase();

                if(filtro.equals("")){
                    gente = genteOriginal;
                    adapterGente= new CustomAdapterGente(gente, getApplicationContext());
                    lvGente.setAdapter(adapterGente);
                } else {
                    for(User usuario : genteOriginal){
                        if(usuario.getUsuario().toLowerCase().contains(filtro) || usuario.getNombre().toLowerCase().contains(filtro) || usuario.getApellidos().toLowerCase().contains(filtro)){
                            genteFiltrada.add(usuario);
                        }
                    }
                    gente = genteFiltrada;
                    adapterGente= new CustomAdapterGente(genteFiltrada, getApplicationContext());
                    lvGente.setAdapter(adapterGente);
                }
            }
        });

    }

    public class ObtenerWebService extends AsyncTask<String,Void,String> {

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
                                String tempUsername = usersJSON.getJSONObject(i).getString("username");
                                String tempName = usersJSON.getJSONObject(i).getString("name");
                                String tempSurname = usersJSON.getJSONObject(i).getString("surname");
                                String tempEmail = usersJSON.getJSONObject(i).getString("email");
                                int tempConectado = usersJSON.getJSONObject(i).getInt("conectado");
                                User u = new User(tempUsername,"", tempName, tempSurname, tempEmail, null, tempConectado, "", 0);
                                gente.add(u);
                                genteOriginal.add(u);
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
            pbBuscando.setVisibility(View.INVISIBLE);
            adapterGente= new CustomAdapterGente(gente, getApplicationContext());
            lvGente.setAdapter(adapterGente);
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
