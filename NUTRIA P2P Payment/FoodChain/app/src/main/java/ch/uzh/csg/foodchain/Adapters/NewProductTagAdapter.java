package ch.uzh.csg.foodchain.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.uzh.csg.foodchain.Models.AllCertificateModel;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.Configuration;

/**
 * The type New product tag adapter.
 */
public class NewProductTagAdapter extends RecyclerView.Adapter<NewProductTagAdapter.MyViewHolder> {
    private ArrayList<AllCertificateModel> editLicensesModelArrayList;
    private Context context;

    /**
     * Instantiates a new New product tag adapter.
     *
     * @param context                    the context
     * @param editLicensesModelArrayList the edit licenses model array list
     */
    public NewProductTagAdapter(Context context, ArrayList<AllCertificateModel> editLicensesModelArrayList) {
        this.context = context;
        this.editLicensesModelArrayList = editLicensesModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_product_tag_layout, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final AllCertificateModel editLicensesModel = editLicensesModelArrayList.get(position);
        holder.tvNewProductAction.setText(editLicensesModel.getCertificateName());
        holder.aSwitch.setChecked(false);
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context, holder.tvNewProductAction.getText().toString(), Toast.LENGTH_SHORT).show();
                String[] array = new String[]{holder.tvNewProductAction.getText().toString()};
                holder.editor.putInt("array",array.length).commit();
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
         * The Tv new product action.
         */
        TextView tvNewProductAction;
        /**
         * The A switch.
         */
        SwitchCompat aSwitch;
        /**
         * The Shared preferences.
         */
        SharedPreferences sharedPreferences;
        /**
         * The Editor.
         */
        SharedPreferences.Editor editor;

        /**
         * Instantiates a new My view holder.
         *
         * @param itemView the item view
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            tvNewProductAction = itemView.findViewById(R.id.tvNewProductAction);
            aSwitch = itemView.findViewById(R.id.swich);
            sharedPreferences = context.getSharedPreferences(Configuration.MY_PREF,Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }
}

