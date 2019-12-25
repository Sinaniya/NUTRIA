package ch.uzh.csg.foodchain.Fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ch.uzh.csg.foodchain.Activities.PrintImageActivity;
import ch.uzh.csg.foodchain.Adapters.ViewPagerAdapter;
import ch.uzh.csg.foodchain.Fragments.ConsumerFragments.QRScaningFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.GenerateQRCodeFragment;
import ch.uzh.csg.foodchain.Models.MapDataModel;
import ch.uzh.csg.foodchain.Models.ScannedQRModel.PreviousProductTag;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.AlertUtils;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;

import static ch.uzh.csg.foodchain.Adapters.ProducersAdapter.Qrcode;
import static com.brother.ptouch.sdk.Printer.TAG;

/**
 * The type All map fragments.
 */
public class AllMapFragments extends Fragment {

    /**
     * The Tab layout.
     */
    TabLayout tabLayout;
    /**
     * The Progress bar.
     */
    ProgressBar progressBar;
    /**
     * The View pager.
     */
    ViewPager viewPager;
    /**
     * The View pager adapter.
     */
    ViewPagerAdapter viewPagerAdapter;
    /**
     * The Linear go to qr.
     */
    LinearLayout linearGoToQr;
    /**
     * The Go to qr text view.
     */
//Button print;
    TextView goToQRTextView;
    /**
     * The Product tag hash.
     */
    String productTagHash;
    /**
     * The Forward image.
     */
    ImageView forwardImage;
    /**
     * The Object info text view.
     */
    TextView objectInfoTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("ActivityNow: ", "onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);


        Bundle args = getArguments();
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        linearGoToQr = view.findViewById(R.id.goToQR);
        goToQRTextView = view.findViewById(R.id.goToQrText);
        forwardImage = view.findViewById(R.id.forward);
        objectInfoTextView=view.findViewById(R.id.tvObjectInfo);
        if (Extras.userType.equals("consumer")) {

            goToQRTextView.setText("");
            forwardImage.setImageResource(0);
            // print.setVisibility(view.GONE);
            objectInfoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    GeneralUtils.connectFragmentWithBackStack(getActivity(), new QRScaningFragment());
                }
            });

        } else {

            linearGoToQr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    GeneralUtils.connectFragmentWithBackStack(getActivity(), new GenerateQRCodeFragment());
                }
            });
        }
        objectInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GeneralUtils.connectFragmentWithBackStack(getActivity(), new ActorListFragment());
            }
        });

        assert args != null;
        viewPagerAdapter = new ViewPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());

        ProducerMapFragment producerMapFragment = new ProducerMapFragment();
        producerMapFragment.setArguments(args);
        viewPagerAdapter.addFragments(producerMapFragment, "Producer");

        TransporterFragment transporterFragment = new TransporterFragment();
        transporterFragment.setArguments(args);
        viewPagerAdapter.addFragments(transporterFragment, "Transporters");

        IntermediariesFragment intermediariesFragment = new IntermediariesFragment();
        intermediariesFragment.setArguments(args);
        viewPagerAdapter.addFragments(intermediariesFragment, "Intermediaries");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(view.GONE);

        Log.d("ALLMAPFRAGEND:", "APICALLSCANNING");

        return view;
    }

}