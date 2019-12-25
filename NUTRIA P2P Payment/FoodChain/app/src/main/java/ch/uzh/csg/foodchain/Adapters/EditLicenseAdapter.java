package ch.uzh.csg.foodchain.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ch.uzh.csg.foodchain.Fragments.EditLicensesFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.NewProducerFragment;
import ch.uzh.csg.foodchain.Models.AllCertificateModel;
import ch.uzh.csg.foodchain.Models.HashMapModel;
import ch.uzh.csg.foodchain.R;

/**
 * The type Edit license adapter.
 */
public class EditLicenseAdapter extends RecyclerView.Adapter<EditLicenseAdapter.MyViewHolder> {
    private ArrayList<AllCertificateModel> editLicensesModelArrayList;
    private Context context;
    private List<Boolean> isCheckedItem = new ArrayList<>();

    private SparseBooleanArray itemStateArray= new SparseBooleanArray();


    /**
     * Instantiates a new Edit license adapter.
     *
     * @param context                    the context
     * @param editLicensesModelArrayList the edit licenses model array list
     */
    public EditLicenseAdapter(Context context, ArrayList<AllCertificateModel> editLicensesModelArrayList) {
        this.context = context;
        this.editLicensesModelArrayList = editLicensesModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_licenses_layout, parent, false);
        return new MyViewHolder(rootView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final AllCertificateModel model = editLicensesModelArrayList.get(position);
        holder.aSwitch.setOnCheckedChangeListener(null);
        holder.tvEditLicense.setText(model.getCertificateName());
        holder.bind(position);

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    String certificate_id = model.getCertificateId();
                    String certificate_name = model.getCertificateName();

                    NewProducerFragment.certificates.put(certificate_id,certificate_name);

                        Log.d("check","data inserted");
                        isCheckedItem.add(true);

                        holder.aSwitch.setChecked(true);
                        itemStateArray.put(position, true);
                        Log.d("zma ischecked", String.valueOf(isCheckedItem.size()));

                }

                else if(!isChecked) {
                    String certificate_id = model.getCertificateId();

                        holder.aSwitch.setChecked(false);
                        itemStateArray.put(position, false);
                        NewProducerFragment.certificates.remove(certificate_id);

                }

            }
        });

        holder.aSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holder.aSwitch.getParent().requestDisallowInterceptTouchEvent(true);
                }
                // always return false since we don't care about handling tapping, flinging, etc.
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return editLicensesModelArrayList.size();
    }

    /**
     * Load items.
     *
     * @param tournaments the tournaments
     */
    public void loadItems(ArrayList<AllCertificateModel> tournaments) {
        this.editLicensesModelArrayList = tournaments;
        notifyDataSetChanged();
    }

    /**
     * The type My view holder.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Iv profile.
         */
        ImageView ivProfile;
        /**
         * The Tv edit license.
         */
        TextView tvEditLicense;
        /**
         * The A switch.
         */
        SwitchCompat aSwitch;

        /**
         * Instantiates a new My view holder.
         *
         * @param itemView the item view
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.tvProfile);
            tvEditLicense = itemView.findViewById(R.id.tvEditLicense);
            aSwitch = itemView.findViewById(R.id.swich);
           // itemView.setOnClickListener(this);
        }

        /**
         * Bind.
         *
         * @param position the position
         */
        void bind(int position) {
            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {

                aSwitch.setChecked(false);}
            else {
                aSwitch.setChecked(true);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }
}
