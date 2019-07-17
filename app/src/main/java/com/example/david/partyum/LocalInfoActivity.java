package com.example.david.partyum;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class LocalInfoActivity extends AppCompatActivity {

    String IP = "http://partyum.000webhostapp.com";
    String GET = IP + "/obtener_locales.php";

    ObtenerWebService hiloconexion;

    int id_local;
    String nombre_local;
    Local l;

    ProgressBar pbCargarLocalInfo;

    ImageView ivImagenLocal;
    TextView tvDescripcionLocal;
    TextView tvDireccionLocalInfo;
    TextView tvTelefonoLocal;
    TextView tvCorreoLocal;
    TextView tvCiudadLocal;
    TextView tvWebLocal;

    ImageView ivDireccionLocalIcono;
    ImageView ivTelefonoLocalIcono;
    ImageView ivCorreoLocalIcono;
    ImageView ivCiudadLocalIcono;
    ImageView ivWebLocalIcono;

    ImageButton ibMapaLocal;
    ImageButton ibChatLocal;
    ImageButton ibFavoritoLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle extras = getIntent().getExtras();
        id_local = extras.getInt("id_local");
        nombre_local = extras.getString("nombre_local");
        toolbar.setTitle(nombre_local);
        setSupportActionBar(toolbar);

        pbCargarLocalInfo = (ProgressBar) findViewById(R.id.pbCargarLocalInfo);

        ivImagenLocal = (ImageView)findViewById(R.id.ivImagenLocal);
        tvDescripcionLocal = (TextView)findViewById(R.id.tvDescripcionLocal);
        tvDireccionLocalInfo = (TextView)findViewById(R.id.tvDireccionLocalInfo);
        tvTelefonoLocal = (TextView)findViewById(R.id.tvTelefonoLocal);
        tvCorreoLocal = (TextView)findViewById(R.id.tvCorreoLocal);
        tvCiudadLocal = (TextView)findViewById(R.id.tvCiudadLocal);
        tvWebLocal = (TextView)findViewById(R.id.tvWebLocal);

        ivDireccionLocalIcono = (ImageView)findViewById(R.id.ivDireccionLocalIcono);
        ivTelefonoLocalIcono = (ImageView)findViewById(R.id.ivTelefonoLocalIcono);
        ivCorreoLocalIcono = (ImageView)findViewById(R.id.ivCorreoLocalIcono);
        ivCiudadLocalIcono = (ImageView)findViewById(R.id.ivCiudadLocalIcono);
        ivWebLocalIcono = (ImageView)findViewById(R.id.ivWebLocalIcono);

        ibMapaLocal = (ImageButton)findViewById(R.id.ibMapaLocal);
        ibChatLocal = (ImageButton)findViewById(R.id.ibChatLocal);
        ibFavoritoLocal = (ImageButton)findViewById(R.id.ibFavoritoLocal);

        ivImagenLocal.setVisibility(View.INVISIBLE);
        tvDescripcionLocal.setVisibility(View.INVISIBLE);
        tvDireccionLocalInfo.setVisibility(View.INVISIBLE);
        tvTelefonoLocal.setVisibility(View.INVISIBLE);
        tvCorreoLocal.setVisibility(View.INVISIBLE);
        tvCiudadLocal.setVisibility(View.INVISIBLE);
        tvWebLocal.setVisibility(View.INVISIBLE);

        ivDireccionLocalIcono.setVisibility(View.INVISIBLE);
        ivTelefonoLocalIcono.setVisibility(View.INVISIBLE);
        ivCorreoLocalIcono.setVisibility(View.INVISIBLE);
        ivCiudadLocalIcono.setVisibility(View.INVISIBLE);
        ivWebLocalIcono.setVisibility(View.INVISIBLE);

        ibMapaLocal.setVisibility(View.INVISIBLE);
        ibChatLocal.setVisibility(View.INVISIBLE);
        ibFavoritoLocal.setVisibility(View.INVISIBLE);

        hiloconexion = new ObtenerWebService();
        hiloconexion.execute(GET,"1");

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
                            JSONArray usersJSON = respuestaJSON.getJSONArray("locales");
                            for(int i=0;i<usersJSON.length();i++){
                                if(usersJSON.getJSONObject(i).getInt("id")==id_local){
                                    int tempId = usersJSON.getJSONObject(i).getInt("id");
                                    String tempNombre = usersJSON.getJSONObject(i).getString("nombre");
                                    String tempDescripcion = usersJSON.getJSONObject(i).getString("descripcion");
                                    String tempDireccion = usersJSON.getJSONObject(i).getString("direccion");
                                    String tempFoto = usersJSON.getJSONObject(i).getString("foto");
                                    String tempResponsable = usersJSON.getJSONObject(i).getString("responsable");
                                    String tempTelefono = usersJSON.getJSONObject(i).getString("telefono");
                                    String tempCorreo = usersJSON.getJSONObject(i).getString("correo");
                                    String tempCiudad = usersJSON.getJSONObject(i).getString("ciudad");
                                    String tempWeb = usersJSON.getJSONObject(i).getString("web");
                                    l = new Local(tempId, tempNombre, tempDescripcion, tempDireccion, tempFoto, tempResponsable, tempTelefono, tempCorreo, tempCiudad, tempWeb);
                                }
                            }

                        }
                        else if (resultJSON=="2"){
                            devuelve = "No existe el local";
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
            if(!l.getFoto().equals("") && !l.getFoto().equals(null)){
                //ivImagenLocal.setImageResource();
            }
            if(!l.getDescripcion().equals("") && !l.getDescripcion().equals(null)){
                tvDescripcionLocal.setText(l.getDescripcion());
            }
            if(!l.getDireccion().equals("") && !l.getDireccion().equals(null)){
                tvDireccionLocalInfo.setText(l.getDireccion());
            }
            if(!l.getTelefono().equals("") && !l.getTelefono().equals(null)){
                tvTelefonoLocal.setText(l.getTelefono());
            }
            if(!l.getCorreo().equals("") && !l.getCorreo().equals(null)){
                tvCorreoLocal.setText(l.getCorreo());
            }
            if(!l.getCiudad().equals("") && !l.getCiudad().equals(null)){
                tvCiudadLocal.setText(l.getCiudad());
            }
            if(!l.getWeb().equals("") && !l.getWeb().equals(null)){
                tvWebLocal.setText(l.getWeb());
            }
            pbCargarLocalInfo.setVisibility(View.INVISIBLE);

            ivImagenLocal.setVisibility(View.VISIBLE);
            tvDescripcionLocal.setVisibility(View.VISIBLE);
            tvDireccionLocalInfo.setVisibility(View.VISIBLE);
            tvTelefonoLocal.setVisibility(View.VISIBLE);
            tvCorreoLocal.setVisibility(View.VISIBLE);
            tvCiudadLocal.setVisibility(View.VISIBLE);
            tvWebLocal.setVisibility(View.VISIBLE);

            ivDireccionLocalIcono.setVisibility(View.VISIBLE);
            ivTelefonoLocalIcono.setVisibility(View.VISIBLE);
            ivCorreoLocalIcono.setVisibility(View.VISIBLE);
            ivCiudadLocalIcono.setVisibility(View.VISIBLE);
            ivWebLocalIcono.setVisibility(View.VISIBLE);

            ibMapaLocal.setVisibility(View.VISIBLE);
            ibChatLocal.setVisibility(View.VISIBLE);
            ibFavoritoLocal.setVisibility(View.VISIBLE);
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
