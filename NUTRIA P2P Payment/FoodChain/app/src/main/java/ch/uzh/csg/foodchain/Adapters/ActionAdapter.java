package ch.uzh.csg.foodchain.Adapters;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.csg.foodchain.Fragments.ConsumerFragments.QRScaningFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.GenerateQRCodeFragment;
import ch.uzh.csg.foodchain.Models.ProcessActionDataModel;
import ch.uzh.csg.foodchain.R;

/**
 * The type Action adapter.
 */
public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.MyViewHolder> {

    //private DatabaseHelper databaseHelper;
    private ArrayList<ProcessActionDataModel> actionModelArrayList;
    private Context context;
    private SparseBooleanArray itemStateArray= new SparseBooleanArray();

    private List<Boolean> isCheckedItem = new ArrayList<>();

    /**
     * Instantiates a new Action adapter.
     *
     * @param activity               the activity
     * @param processActionArrayList the process action array list
     * @param o                      the o
     */
    public ActionAdapter(Activity activity, ArrayList<ProcessActionDataModel> processActionArrayList, Object o) {
        this.context = activity;
        this.actionModelArrayList = processActionArrayList;
    }


    @NonNull
    @Override
    public ActionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_product_tag_layout, parent, false);
        return new ActionAdapter.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionAdapter.MyViewHolder holder, int position) {
        final ProcessActionDataModel actionModel = actionModelArrayList.get(position);
        holder.aSwitch.setOnCheckedChangeListener(null);
        holder.tvNewProductAction.setText(actionModel.getActionName());
        holder.bind(position);

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    String certificate_id = actionModel.getActionId();
                    String certificate_name = actionModel.getActionName();

                    holder.aSwitch.setChecked(true);
                    itemStateArray.put(position, true);

                    GenerateQRCodeFragment.qrActions.put(certificate_id,certificate_name);

                    Log.d("check","data inserted");
                    isCheckedItem.add(true);

                    Log.d("zma ischecked", String.valueOf(isCheckedItem.size()));

                }
                else if(!isChecked) {

                    String certificate_id = actionModel.getActionId();

                    holder.aSwitch.setChecked(false);
                    itemStateArray.put(position, false);
                    GenerateQRCodeFragment.qrActions.remove(certificate_id);

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
        if (actionModelArrayList == null) {
            return 0;
        }
        return actionModelArrayList.size();
    }

    /**
     * Load items.
     *
     * @param tournaments the tournaments
     */
    public void loadItems(ArrayList<ProcessActionDataModel> tournaments) {
        this.actionModelArrayList = tournaments;
        notifyDataSetChanged();
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
         * Instantiates a new My view holder.
         *
         * @param itemView the item view
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            tvNewProductAction = itemView.findViewById(R.id.tvNewProductAction);
            aSwitch = itemView.findViewById(R.id.newSwich);
            //itemView.setOnClickListener(this);

        }

        /**
         * Bind.
         *
         * @param position the position
         */
        void bind(int position) {
            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {

                aSwitch.setChecked(false);
            }
            else {
                aSwitch.setChecked(true);
            }
        }
    }
}