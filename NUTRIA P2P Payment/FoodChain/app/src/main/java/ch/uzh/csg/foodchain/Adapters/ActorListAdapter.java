package ch.uzh.csg.foodchain.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ch.uzh.csg.foodchain.Activities.PrintImageActivity;
import ch.uzh.csg.foodchain.Fragments.AllMapFragments;
import ch.uzh.csg.foodchain.Fragments.ConsumerFragments.QRScaningFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.GenerateQRCodeFragment;
import ch.uzh.csg.foodchain.Models.ScannedQRModel.PreviousProductTag;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.AlertUtils;
import ch.uzh.csg.foodchain.Utils.Configuration;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static ch.uzh.csg.foodchain.Adapters.ProducersAdapter.Qrcode;

/**
 * The type Actor list adapter.
 */
public class ActorListAdapter extends RecyclerView.Adapter<ActorListAdapter.MyViewHolder> {

    private ArrayList<PreviousProductTag> editLicensesModelArrayList;
    private Context context;
    /**
     * The Alert dialog.
     */
    android.support.v7.app.AlertDialog alertDialog;
    private Bitmap bitmap, bm;
    /**
     * The Product tag hash.
     */
    String productTagHash;
    /**
     * The constant QRcodeWidth.
     */
    public final static int QRcodeWidth = 500;

    /**
     * Instantiates a new Actor list adapter.
     *
     * @param context                    the context
     * @param editLicensesModelArrayList the edit licenses model array list
     */
    public ActorListAdapter(Context context, ArrayList<PreviousProductTag> editLicensesModelArrayList) {
        this.context = context;
        this.editLicensesModelArrayList = editLicensesModelArrayList;

        Log.d("APICALLSCANNINGADAPTER:", "APICALLSCANNING");
    }

    @NonNull
    @Override
    public ActorListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actor_list_layout, parent, false);
        return new ActorListAdapter.MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            final PreviousProductTag model = editLicensesModelArrayList.get(position);

            if (Qrcode != null && !Qrcode.isEmpty()) {
                holder.tvItems.setText(Qrcode);
                productTagHash=Qrcode;
            } else {
                holder.tvItems.setText(QRScaningFragment.strBarCodeValue);
                productTagHash=QRScaningFragment.strBarCodeValue;
            }


            holder.producerName.setText(model.getProductTagProducer().getProducerName());
            holder.licencesNumber.setText(model.getProductTagProducer().getLicenceNumber());
            holder.url.setText(model.getProductTagProducer().getUrl());

            if (Extras.userType.equals("consumer")) {

                holder.print.setVisibility(View.GONE);

            }
        } catch (NullPointerException ex) {
            ex.getMessage();
        }

        holder.gotoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("coordinates", editLicensesModelArrayList);
                Log.d("TOWORDSMAP:", "APICALLSCANNING");

                Fragment fragment = new AllMapFragments();
                fragment.setArguments(bundle);
                ((AppCompatActivity) context).getFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, fragment).addToBackStack("actorlist").commit();

            }
        });

        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        generatingPrintQR();
                    }
                }, 1000);

            }

        });

    }

    /**
     * generating QR progress start
     */
    private void startProgress() {
        GeneralUtils.progressDialog(context, context.getString(R.string.generating_qr_code));
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
         * The Tv items.
         */
        TextView tvItems, /**
         * The Producer name.
         */
        producerName, /**
         * The Licences number.
         */
        licencesNumber, /**
         * The Url.
         */
        url;
        /**
         * The Print.
         */
        Button print, /**
         * The Goto map.
         */
        gotoMap;
        /**
         * The Linear layout.
         */
        LinearLayout linearLayout;
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


            tvItems = itemView.findViewById(R.id.tvItems);
            producerName = itemView.findViewById(R.id.prodName);
            licencesNumber = itemView.findViewById(R.id.licenseNumber);
            url = itemView.findViewById(R.id.url);

            print = itemView.findViewById(R.id.printbtn);
            gotoMap = itemView.findViewById(R.id.gotomap);

            linearLayout = itemView.findViewById(R.id.actorLayout);
            sharedPreferences = context.getSharedPreferences(Configuration.MY_PREF, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    /**
     * Text to image encode bitmap.
     *
     * @param Value the value
     * @return the bitmap
     * @throws WriterException the writer exception
     */
    Bitmap TextToImageEncode(String Value) throws WriterException {
        Log.d("ValueDunno: ", Value);

        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        context.getResources().getColor(R.color.black) : context.getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    /**
     * save generated QR image
     * @param image
     */
    private void storeImage(Bitmap image) {

        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {

            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new
                File(Environment.getExternalStorageDirectory()
                + "/FoodChain/"
                + "/Files");


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "perviousQRCodeImage.png");
        return mediaFile;
    }

    /**
     * Method string.
     *
     * @param str the str
     * @return the string
     */
//Convert a view to bitmap
    public String method(String str) {

        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    /**
     * generate print of QR image
     */
    private void generatingPrintQR() {

        try {

            bitmap = TextToImageEncode(productTagHash);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        storeImage(bitmap);

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)

                .setTitleText("Success")
                .setContentText(context.getString(R.string.qr_is_ready))

                .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        Intent intent = new Intent(context, PrintImageActivity.class);
                        intent.putExtra("filepath", Environment.getExternalStorageDirectory()
                                + "/FoodChain/"
                                + "/Files/perviousQRCodeImage.png");
                        context.startActivity(intent);
                    }
                })
                .show();

        if (GeneralUtils.progress != null)
            GeneralUtils.progress.dismiss();

    }
}

