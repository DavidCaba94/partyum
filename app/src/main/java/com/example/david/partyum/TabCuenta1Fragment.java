package com.example.david.partyum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabCuenta1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabCuenta1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabCuenta1Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    User user = new User("","","","","","",0, "", 0);

    ImageView ivImagenUsuario;
    EditText etUsuario, etContrasena, etContrasenaRep, etNombre, etApellidos, etEmail;
    ImageButton btnGuardar;
    ProgressBar pbCuenta1;

    int REQUEST_GET_SINGLE_FILE = 0;

    String IP = "http://partyum.000webhostapp.com";
    String GET = IP + "/obtener_usuarios.php";
    String UPDATE = IP + "/actualizar_usuario.php";

    ObtenerWebService hiloconexion;

    private OnFragmentInteractionListener mListener;

    public TabCuenta1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabCuenta1Fragment newInstance(String param1, String param2) {
        TabCuenta1Fragment fragment = new TabCuenta1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_cuenta1, container, false);

        ivImagenUsuario = (ImageView) view.findViewById(R.id.ivImagenUsuarioCuenta);
        etUsuario = (EditText) view.findViewById(R.id.etUsuarioCuenta);
        etContrasena = (EditText) view.findViewById(R.id.etContrasenaCuenta);
        etContrasenaRep = (EditText) view.findViewById(R.id.etContrasenaRepCuenta);
        etNombre = (EditText) view.findViewById(R.id.etNombreCuenta);
        etApellidos = (EditText) view.findViewById(R.id.etApellidosCuenta);
        etEmail = (EditText) view.findViewById(R.id.etEmailCuenta); ;
        btnGuardar = (ImageButton) view.findViewById(R.id.btnGuardarCuenta);
        pbCuenta1 = (ProgressBar) view.findViewById(R.id.pbCuenta1);

        SharedPreferences pref = getActivity().getSharedPreferences("SharedLogin", 0);
        etUsuario.setText(pref.getString("username", null));

        hiloconexion = new ObtenerWebService();
        hiloconexion.execute(GET,"1");

        ivImagenUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_GET_SINGLE_FILE);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etContrasena.getText().toString().equals(etContrasenaRep.getText().toString()) && !etContrasena.getText().toString().equals("") && !etContrasenaRep.getText().toString().equals("")){
                    if(!etNombre.getText().toString().equals("") && !etApellidos.getText().toString().equals("") && !etEmail.getText().toString().equals("")){

                        User u = new User(etUsuario.getText().toString(), etContrasena.getText().toString(),
                                etNombre.getText().toString(), etApellidos.getText().toString(),
                                etEmail.getText().toString(), user.getFoto(), user.getConectado(),
                                user.getReferido(), user.getPremium());

                        hiloconexion = new ObtenerWebService();
                        hiloconexion.execute(UPDATE,"4", u.getUsuario(), u.getPassword(), u.getNombre(), u.getApellidos(), u.getEmail(), "");
                        pbCuenta1.setVisibility(View.VISIBLE);
                        btnGuardar.setVisibility(View.INVISIBLE);

                    } else {
                        Toast.makeText(getActivity(), "Todos los datos deben estar rellenos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Ambas contraseñas deben ser iguales", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode == getActivity().RESULT_OK)
            switch (requestCode){
                case 0:
                    Uri selectedImage = data.getData();
                    ivImagenUsuario.setImageURI(selectedImage);
                    InputStream imageStream = null;
                    try {
                        imageStream = this.getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    encodeTobase64(yourSelectedImage);
                    break;
            }
    }

    public void encodeTobase64(Bitmap image) {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        user.setFoto(imageEncoded);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                                if(etUsuario.getText().toString().equals(usersJSON.getJSONObject(i).getString("username"))){
                                    devuelve = "Encontrado";
                                    user.setUsuario(usersJSON.getJSONObject(i).getString("username"));
                                    user.setPassword(usersJSON.getJSONObject(i).getString("password"));
                                    user.setNombre(usersJSON.getJSONObject(i).getString("name"));
                                    user.setApellidos(usersJSON.getJSONObject(i).getString("surname"));
                                    user.setEmail(usersJSON.getJSONObject(i).getString("email"));
                                    user.setFoto(usersJSON.getJSONObject(i).getString("foto"));
                                    user.setConectado(usersJSON.getJSONObject(i).getInt("conectado"));
                                    user.setReferido(usersJSON.getJSONObject(i).getString("referido"));
                                    user.setPremium(usersJSON.getJSONObject(i).getInt("premium"));
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


            } else if(params[1]=="4"){

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
                    //Creo el Objeto JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username",params[2]);
                    jsonParam.put("password", params[3]);
                    jsonParam.put("name", params[4]);
                    jsonParam.put("surname", params[5]);
                    jsonParam.put("email", params[6]);
                    jsonParam.put("foto", params[7]);
                    // Envio los parámetros post.
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
                            devuelve = "Usuario actualizado correctamente";

                        } else if (resultJSON.equals("2")) {
                            devuelve = "El usuario no pudo actualizarse";
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
                pbCuenta1.setVisibility(View.INVISIBLE);
                etUsuario.setText(user.getUsuario());
                etContrasena.setText(user.getPassword());
                etContrasenaRep.setText(user.getPassword());
                etNombre.setText(user.getNombre());
                etApellidos.setText(user.getApellidos());
                etEmail.setText(user.getEmail());
                if(!user.getFoto().equals("null")){
                    //base64
                    //ivImagenUsuario.setImageBitmap(user.getFoto());
                    /*

                    //decode base64 string to image
                    imageBytes = Base64.decode(user.getFoto(), Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    image.setImageBitmap(decodedImage);

                     */
                }
                etUsuario.setVisibility(View.VISIBLE);
                etContrasena.setVisibility(View.VISIBLE);
                etContrasenaRep.setVisibility(View.VISIBLE);
                etNombre.setVisibility(View.VISIBLE);
                etApellidos.setVisibility(View.VISIBLE);
                etEmail.setVisibility(View.VISIBLE);
                ivImagenUsuario.setVisibility(View.VISIBLE);
                btnGuardar.setVisibility(View.VISIBLE);
            } else if(s.equals("Usuario actualizado correctamente")){
                pbCuenta1.setVisibility(View.INVISIBLE);
                btnGuardar.setVisibility(View.VISIBLE);
                actualizarUsuarioText("Datos actualizados");
            } else if(s.equals("El usuario no pudo actualizarse")){
                pbCuenta1.setVisibility(View.INVISIBLE);
                btnGuardar.setVisibility(View.VISIBLE);
                actualizarUsuarioText("Se ha producido un fallo actualizando los datos");
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

    public void actualizarUsuarioText(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
