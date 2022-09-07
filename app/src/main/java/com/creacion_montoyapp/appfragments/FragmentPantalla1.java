package com.creacion_montoyapp.appfragments;


import static androidx.fragment.app.FragmentManager.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class FragmentPantalla1 extends Fragment {

    ImageButton btnPlay_Pause;
    TextView tvEscucha,tvEmisora;
    View vista;
    //Instancia del MainActivity para obtener las variables de sus métodos
    MainActivity mainActivity;
    private boolean play;
    float anchoPantalla;

    private InterstitialAd mInterstitialAd;
    AdRequest adRequest = new AdRequest.Builder().build();

    public FragmentPantalla1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        vista = inflater.inflate(R.layout.fragment_pantalla1, container, false);
        btnPlay_Pause = vista.findViewById(R.id.btnPlay_Pause);
        tvEscucha = vista.findViewById(R.id.tvEscucha);
        tvEmisora =vista.findViewById(R.id.tvNombreEmisora);

        //Banner publicidad **********************************************
        FrameLayout frameLayout = vista.findViewById(R.id.bannerContainer);
        AdView mAdView = new AdView(requireActivity());
        mAdView.setAdUnitId(getString(R.string.banner_admob_id));
        frameLayout.addView(mAdView);

        MobileAds.initialize(requireActivity(), initializationStatus -> {
        });

        //mAdView = vista.findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        mAdView.setAdSize(adSize);
        mAdView.loadAd(adRequest);
        //****************************************************************
        cargandoAnuncio();



        btnPlay_Pause.setOnClickListener(v -> {

            if (!play) {

                btnPlay_Pause.setImageResource(R.drawable.pause);
                tvEscucha.setText(R.string.tvEscucha2);

            } else {
                btnPlay_Pause.setImageResource(R.drawable.play);
                tvEscucha.setText(R.string.tvEscucha);

            }
            play = !play;
            mainActivity  = (MainActivity) getActivity();
            assert mainActivity != null;
            mainActivity.playPulsado(play);

            //Publicidad Intersticial *******************************************
            if (mInterstitialAd != null) {
                mInterstitialAd.show(requireActivity());
            } else {
                Toast.makeText(getActivity(), "No está cargado el anuncio", Toast.LENGTH_SHORT).show();;
            }
            //*******************************************************************

        });


        // Inflate the layout for this fragment
        return vista;
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(requireActivity(), adWidth);
    }

    public void cargandoAnuncio(){
        InterstitialAd.load(requireActivity(),"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        //Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        //Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();

        anchoLetra(anchoPantalla);
        float tamanoLetra1 = anchoPantalla * 0.04F;
        float tamanoLetra2 = anchoPantalla * 0.06F;
        tvEscucha.setTextSize(tamanoLetra1);
        tvEmisora.setTextSize(tamanoLetra2);

        reproductorSonando(play);
        if (play){
            btnPlay_Pause.setImageResource(R.drawable.pause);
            tvEscucha.setText(R.string.tvEscucha2);
        } else {
            btnPlay_Pause.setImageResource(R.drawable.play);
            tvEscucha.setText(R.string.tvEscucha);
        }

    }

    public void reproductorSonando(boolean botonPulsado){
        play = botonPulsado;
    }

    public void errorReproductor(boolean botonPulsado){
        play = botonPulsado;
        if (!play){
            btnPlay_Pause.setImageResource(R.drawable.play);
            tvEscucha.setText(R.string.tvEscucha);
        } else {
            btnPlay_Pause.setImageResource(R.drawable.pause);
            tvEscucha.setText(R.string.tvEscucha2);
        }
    }

    public void anchoLetra(float ancho){
        anchoPantalla = ancho;
    }


}