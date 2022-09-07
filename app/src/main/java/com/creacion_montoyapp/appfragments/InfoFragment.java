package com.creacion_montoyapp.appfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends Fragment {

    TextView tvInfo1,tvInfo2,tvInfo3,tvInfo4;
    View vista;
    //Instancia del MainActivity para obtener las variables de sus métodos
    MainActivity mainActivity;
    float anchoPantalla;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_info, container, false);

        tvInfo1 = vista.findViewById(R.id.tvInfo1);
        tvInfo2 = vista.findViewById(R.id.tvInfo2);
        tvInfo3 = vista.findViewById(R.id.tvInfo3);
        tvInfo4 = vista.findViewById(R.id.tvInfo4);





        return vista;
    }

    @Override
    public void onResume() {
        super.onResume();

        anchoLetra(anchoPantalla);
        float tamanoLetra1 = anchoPantalla * 0.04F;
        float tamanoLetra2 = anchoPantalla * 0.03F;
        tvInfo1.setTextSize(tamanoLetra1);
        tvInfo2.setTextSize(tamanoLetra2);
        tvInfo3.setTextSize(tamanoLetra1);
        tvInfo4.setTextSize(tamanoLetra1);
    }

    public void anchoLetra(float ancho){ anchoPantalla = ancho; }

}