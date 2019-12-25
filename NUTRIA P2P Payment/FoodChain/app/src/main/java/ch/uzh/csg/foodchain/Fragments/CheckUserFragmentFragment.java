package ch.uzh.csg.foodchain.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.uzh.csg.foodchain.Fragments.ConsumerFragments.QRScaningFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.NewProducerFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.ProducerLoginFragment;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;


/**
 * The type Check user fragment fragment.
 */
public class CheckUserFragmentFragment extends Fragment {
    /**
     * The Btn producer.
     */
    Button btnProducer, /**
     * The Btn consumer.
     */
    btnConsumer;
    /**
     * The Tag.
     */
    String TAG="CheckUserFragmentFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_user, container, false);
        btnProducer = view.findViewById(R.id.btnProducer);
        btnConsumer = view.findViewById(R.id.btnConsumer);

        btnProducer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragmentWithBackStack(getActivity(), new ProducerLoginFragment());
            }
        });

        btnConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragment(getActivity(), new QRScaningFragment());
            }
        });
        return view;
    }

}
