package com.example.david.partyum;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
 * {@link Tab4Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab4Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab4Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<User> amigos;
    ListView lvAmigos;
    private static CustomAdapterAmigo adapterAmigo;

    private OnFragmentInteractionListener mListener;

    public Tab4Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab4Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab4Fragment newInstance(String param1, String param2) {
        Tab4Fragment fragment = new Tab4Fragment();
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
        View view = inflater.inflate(R.layout.fragment_tab4, container, false);

        lvAmigos = (ListView) view.findViewById(R.id.lvAmigos);

        amigos= new ArrayList<>();

        amigos.add(new User("Amigo 1", "","Nombre 1","Apellidos 1","",null,1, "", 0));
        amigos.add(new User("Amigo 2", "","Nombre 2","Apellidos 2","",null,0, "", 0));
        amigos.add(new User("Amigo 3", "","Nombre 3","Apellidos 3","",null,0, "", 0));
        amigos.add(new User("Amigo 4", "","Nombre 4","Apellidos 4","",null,1, "", 0));
        amigos.add(new User("Amigo 5", "","Nombre 5","Apellidos 5","",null,1, "", 0));
        amigos.add(new User("Amigo 6", "","Nombre 6","Apellidos 6","",null,1, "", 0));
        amigos.add(new User("Amigo 7", "","Nombre 7","Apellidos 7","",null,0, "", 0));
        amigos.add(new User("Amigo 8", "","Nombre 8","Apellidos 8","",null,1, "", 0));
        amigos.add(new User("Amigo 9", "","Nombre 9","Apellidos 9","",null,0, "", 0));
        amigos.add(new User("Amigo 10", "","Nombre 10","Apellidos 10","",null,0, "", 0));
        amigos.add(new User("Amigo 11", "","Nombre 11","Apellidos 11","",null,1, "", 0));
        amigos.add(new User("Amigo 12", "","Nombre 12","Apellidos 12","",null,1, "", 0));
        amigos.add(new User("Amigo 13", "","Nombre 13","Apellidos 13","",null,0, "", 0));
        amigos.add(new User("Amigo 14", "","Nombre 14","Apellidos 14","",null,1, "", 0));


        adapterAmigo= new CustomAdapterAmigo(amigos, view.getContext());

        lvAmigos.setAdapter(adapterAmigo);
        lvAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User user= amigos.get(position);
                //Intent amigo
                Toast.makeText(view.getContext(), ""+user.getUsuario(), Toast.LENGTH_SHORT).show();

            }
        });

        FloatingActionButton fbNuevoAmigo = (FloatingActionButton) view.findViewById(R.id.fbNuevoAmigo);
        fbNuevoAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(), NuevoAmigoActivity.class);
                startActivity(i);
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
