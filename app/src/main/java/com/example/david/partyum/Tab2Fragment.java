package com.example.david.partyum;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab2Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText etBusquedaLocal;
    ImageButton ibBuscarLocal;
    ArrayList<Local> locales;
    ArrayList<Local> localesOriginal;
    ListView lvLocales;
    private static CustomAdapterLocales adapterLocales;
    ProgressBar pbCargandoLocales;

    String IP = "http://partyum.000webhostapp.com";
    String GET = IP + "/obtener_locales.php";

    ObtenerWebService hiloconexion;

    private OnFragmentInteractionListener mListener;

    public Tab2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab2Fragment newInstance(String param1, String param2) {
        Tab2Fragment fragment = new Tab2Fragment();
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
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        etBusquedaLocal = (EditText) view.findViewById(R.id.etBusquedaLocal);
        ibBuscarLocal = (ImageButton) view.findViewById(R.id.ibBuscarLocal);
        lvLocales = (ListView) view.findViewById(R.id.lvLocales);
        pbCargandoLocales = (ProgressBar) view.findViewById(R.id.pbCargandoLocales);

        locales = new ArrayList<>();
        localesOriginal = new ArrayList<>();

        hiloconexion = new ObtenerWebService();
        hiloconexion.execute(GET,"1");

        lvLocales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Local local= locales.get(position);
                Intent i = new Intent(getContext(), LocalInfoActivity.class);
                i.putExtra("id_local", local.getId());
                i.putExtra("nombre_local", local.getNombre());
                startActivity(i);

            }
        });

        FloatingActionButton fbLocalesFavoritos = (FloatingActionButton) view.findViewById(R.id.fbLocalesFavoritos);
        fbLocalesFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(), LocalesFavoritosActivity.class);
                startActivity(i);
            }
        });

        ibBuscarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Local> localesFiltrados;
                localesFiltrados= new ArrayList<>();

                String filtro = etBusquedaLocal.getText().toString().toLowerCase();

                if(filtro.equals("")){
                    locales = localesOriginal;
                    adapterLocales= new CustomAdapterLocales(locales, view.getContext());
                    lvLocales.setAdapter(adapterLocales);
                } else {
                    for(Local local : localesOriginal){
                        if(local.getNombre().toLowerCase().contains(filtro)){
                            localesFiltrados.add(local);
                        }
                    }
                    locales = localesFiltrados;
                    adapterLocales= new CustomAdapterLocales(locales, view.getContext());
                    lvLocales.setAdapter(adapterLocales);
                }
            }
        });

        return view;
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
                                Local l = new Local(tempId, tempNombre, tempDescripcion, tempDireccion, tempFoto, tempResponsable, tempTelefono, tempCorreo, tempCiudad, tempWeb);
                                locales.add(l);
                                localesOriginal.add(l);
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
            pbCargandoLocales.setVisibility(View.INVISIBLE);
            adapterLocales= new CustomAdapterLocales(locales, getContext());
            lvLocales.setAdapter(adapterLocales);
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
}
