package com.example.david.partyum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabCuenta2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabCuenta2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabCuenta2Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tvTextoPremium;
    Button btnPremium;
    ProgressBar pbPremium;

    String username;

    String IP = "http://partyum.000webhostapp.com";
    String GET = IP + "/obtener_usuarios.php";

    ObtenerWebService hiloconexion;

    private OnFragmentInteractionListener mListener;

    public TabCuenta2Fragment() {
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
    public static TabCuenta2Fragment newInstance(String param1, String param2) {
        TabCuenta2Fragment fragment = new TabCuenta2Fragment();
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
        View view = inflater.inflate(R.layout.fragment_tab_cuenta2, container, false);
        tvTextoPremium = (TextView) view.findViewById(R.id.tvTextoPremium);
        btnPremium = (Button) view.findViewById(R.id.btnPremium);
        pbPremium = (ProgressBar) view.findViewById(R.id.pbPremium);

        SharedPreferences pref = getActivity().getSharedPreferences("SharedLogin", 0);
        username = pref.getString("username", null);

        hiloconexion = new ObtenerWebService();
        hiloconexion.execute(GET,"1");

        btnPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PremiumForm.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return view;
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
                                if(username.equals(usersJSON.getJSONObject(i).getString("username"))){
                                    if(usersJSON.getJSONObject(i).getInt("premium")==1){
                                        devuelve = "Premium";
                                    }
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
            if(s.equals("Premium")){
                pbPremium.setVisibility(View.INVISIBLE);
                tvTextoPremium.setVisibility(View.VISIBLE);
                tvTextoPremium.setText("Premium");
            } else {
                pbPremium.setVisibility(View.INVISIBLE);
                tvTextoPremium.setVisibility(View.VISIBLE);
                btnPremium.setVisibility(View.VISIBLE);
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
