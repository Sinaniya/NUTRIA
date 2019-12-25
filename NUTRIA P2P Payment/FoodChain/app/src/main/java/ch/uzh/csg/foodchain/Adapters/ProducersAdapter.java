package ch.uzh.csg.foodchain.Adapters;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ch.uzh.csg.foodchain.Fragments.ActorListFragment;
import ch.uzh.csg.foodchain.Models.MapDataModel;
import ch.uzh.csg.foodchain.Models.ProductDataModel;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.Extras;

import static java.lang.Integer.valueOf;

/**
 * The type Producers adapter.
 */
public class ProducersAdapter extends RecyclerView.Adapter<ProducersAdapter.MyViewHolder> {
    private ArrayList<ProductDataModel> editLicensesModelArrayList;
    private Context context;
    /**
     * The constant Qrcode.
     */
    public static String Qrcode;

    /**
     * Instantiates a new Producers adapter.
     *
     * @param context                    the context
     * @param editLicensesModelArrayList the edit licenses model array list
     */
    public ProducersAdapter(Context context, ArrayList<ProductDataModel> editLicensesModelArrayList) {
        this.context = context;
        this.editLicensesModelArrayList = editLicensesModelArrayList;

    }

    private void sortingArrayList() {

        Collections.sort(editLicensesModelArrayList, new Comparator<ProductDataModel>() {
            public int compare(ProductDataModel d1, ProductDataModel d2) {
                return valueOf(d1.getDate().compareTo(d2.getDate()));
            }
        });

    }

    @NonNull
    @Override
    public ProducersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // sorting Array list w.r.t date
        sortingArrayList();
        // reverse Array list
        Collections.reverse(editLicensesModelArrayList);

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producer_layout, parent, false);
        return new ProducersAdapter.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ProductDataModel model = editLicensesModelArrayList.get(position);

        holder.tvProducerName.setText(" \tDate: "+model.getDate().replace("T", "\t\t\tTime: "));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Qrcode= model.getProductTagHash();

                Extras.scan_type = "producer";
                Fragment fragment = new ActorListFragment();
                ((AppCompatActivity)context).getFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,fragment).addToBackStack("producersadapter").commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return editLicensesModelArrayList.size();
    }

    /**
     * The type My view holder.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Tv producer name.
         */
        TextView tvProducerName;

        /**
         * Instantiates a new My view holder.
         *
         * @param itemView the item view
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            tvProducerName = itemView.findViewById(R.id.tvProducers);
        }
    }

}
