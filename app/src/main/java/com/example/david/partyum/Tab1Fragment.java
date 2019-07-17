package com.example.david.partyum;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<Evento> eventos;
    ListView lvEventos;
    private static CustomAdapterEvento adapter;

    private OnFragmentInteractionListener mListener;

    public Tab1Fragment() {
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
    public static Tab1Fragment newInstance(String param1, String param2) {
        Tab1Fragment fragment = new Tab1Fragment();
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
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        lvEventos = (ListView) view.findViewById(R.id.lvEventos);

        eventos= new ArrayList<>();

        eventos.add(new Evento("Beer Pong", "", "Evento","September 23, 2008", "23:00", "Bender"));
        eventos.add(new Evento("Concierto", "", "Evento","February 9, 2009", "23:00", "Bender"));
        eventos.add(new Evento("Música en directo", "", "Evento","April 27, 2009", "23:00", "Bender"));
        eventos.add(new Evento("Barra Libre","","Evento","September 15, 2009", "23:00", "Bender"));
        eventos.add(new Evento("Monólogo", "", "Evento","October 26, 2009", "23:00", "Bender"));
        eventos.add(new Evento("Evento Brugal", "", "Evento","May 20, 2010", "23:00", "Bender"));
        eventos.add(new Evento("Noche Barceló", "", "Evento","December 6, 2010", "23:00", "Bender"));
        eventos.add(new Evento("Fiesta fluor","","Evento","February 22, 2011", "23:00", "Bender"));
        eventos.add(new Evento("Fiesta de los 90", "", "Evento","October 18, 2011", "23:00", "Bender"));
        eventos.add(new Evento("Graduación de economía", "", "Evento","July 9, 2012", "23:00", "Bender"));
        eventos.add(new Evento("Fiesta Jagermeister", "", "Evento","October 31, 2013", "23:00", "Bender"));
        eventos.add(new Evento("Cotillón de nochevieja","","Evento","November 12, 2014", "23:00", "Bender"));
        eventos.add(new Evento("Fiesta de Halloween", "", "Evento","October 5, 2015", "23:00", "Bender"));

        adapter= new CustomAdapterEvento(eventos, view.getContext());

        lvEventos.setAdapter(adapter);
        lvEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Evento evento= eventos.get(position);
                //Intent evento
                Toast.makeText(view.getContext(), "Elemento "+position, Toast.LENGTH_SHORT).show();

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
}
