package ch.uzh.csg.foodchain.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.uzh.csg.foodchain.R;


/**
 * The type Bar code creation data fragment.
 */
public class BarCodeCreationDataFragment extends Fragment {
    /**
     * The Btn licences.
     */
    Button btnLicences, /**
     * The Btn processes.
     */
    btnProcesses;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bar_code_creation_data, container, false);

        btnLicences = view.findViewById(R.id.goToLicences);
        btnProcesses = view.findViewById(R.id.gotoProcesses);

        btnLicences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditLicensesFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack("abc").commit();
            }
        });

        btnProcesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProcessActionFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack("abc").commit();
            }
        });
        return view;
    }
}
