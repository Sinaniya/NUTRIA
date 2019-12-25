package ch.uzh.csg.foodchain.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import ch.uzh.csg.foodchain.Fragments.ConsumerFragments.QRScaningFragment;
import ch.uzh.csg.foodchain.R;


/**
 * The type New product qr code fragment.
 */
public class NewProductQrCodeFragment extends Fragment {
    private Button btnReadQr, btnGeneateQR;
    /**
     * The Str bar code value.
     */
    String strBarCodeValue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_product_qr_code, container, false);

        btnGeneateQR = view.findViewById(R.id.btnReadQRcode);

        btnGeneateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new QRScaningFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack("abc").commit();
            }
        });

        return view;
    }


}
