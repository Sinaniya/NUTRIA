package ch.uzh.csg.foodchain.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import ch.uzh.csg.foodchain.Classes.Common;
import ch.uzh.csg.foodchain.Classes.PrinterModelInfo;
import ch.uzh.csg.foodchain.Fragments.AllMapFragments;
import ch.uzh.csg.foodchain.Fragments.CheckUserFragment;
import ch.uzh.csg.foodchain.Fragments.ProducerFragments.GenerateQRCodeFragment;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.Extras;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;
import ch.uzh.csg.foodchain.interfaces.CertificateAdditionCallback;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity implements CertificateAdditionCallback {

    private final int PERMISSION_WRITE_EXTERNAL_STORAGE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check user login if login then redirect to QR code screen otherwise redirect to cheker user session screen
        if (GeneralUtils.isLoggedIn(MainActivity.this)) {
            GeneralUtils.connectFragment(MainActivity.this, new GenerateQRCodeFragment());
        } else {
            GeneralUtils.connectFragment(MainActivity.this, new CheckUserFragment());
        }
        init();
        getDataFromIntent();
        setPreferences();
    }

    @Override
    public void getUserDefinedCertificate(List<String> userCertificates) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
     * copy .bin file (RJ paper size info.) to
     * /mnt/sdcard/customPaperFileSet/ .bin file is made by Printer Setting
     * Tool which can be downloaded from the Brother Net Site
     */
    private void init() {

        try {
            raw2file("RJ4030_102mm.bin", R.raw.rj4030_102mm);
            raw2file("RJ4030_102mm152mm.bin", R.raw.rj4030_102mm152mm);
            raw2file("RJ4040_102mm.bin", R.raw.rj4040_102mm);
            raw2file("RJ4040_102mm152mm.bin", R.raw.rj4040_102mm152mm);
            raw2file("RJ4030Ai_102mm.bin", R.raw.rj4030ai_102mm);
            raw2file("RJ4030Ai_102mm152mm.bin", R.raw.rj4030ai_102mm152mm);

            raw2file("RJ3050_76mm.bin", R.raw.rj3050_76mm);
            raw2file("RJ3150_76mm.bin", R.raw.rj3150_76mm);
            raw2file("RJ3150_76mm44mm.bin", R.raw.rj3150_76mm44mm);
            raw2file("RJ3050Ai_76mm.bin", R.raw.rj3050ai_76mm);
            raw2file("RJ3150Ai_76mm.bin", R.raw.rj3150ai_76mm);
            raw2file("RJ3150Ai_76mm44mm.bin", R.raw.rj3150ai_76mm44mm);
            raw2file("RJ2030_50mm.bin", R.raw.rj2030_50mm);
            raw2file("RJ2050_50mm.bin", R.raw.rj2050_50mm);
            raw2file("RJ2030_58mm.bin", R.raw.rj2030_58mm);
            raw2file("RJ2050_58mm.bin", R.raw.rj2050_58mm);
            raw2file("RJ2140_58mm.bin", R.raw.rj2140_58mm);
            raw2file("RJ2140_50x85mm.bin", R.raw.rj2140_50x85mm);
            raw2file("RJ2150_58mm.bin", R.raw.rj2150_58mm);
            raw2file("RJ2150_50x85mm.bin", R.raw.rj2150_50x85mm);


            raw2file("TD2020_57mm.bin", R.raw.td2020_57mm);
            raw2file("TD2020_40mm40mm.bin", R.raw.td2020_40mm40mm);
            raw2file("TD2120_57mm.bin", R.raw.td2120_57mm);
            raw2file("TD2120_40mm40mm.bin", R.raw.td2120_40mm40mm);
            raw2file("TD2130_57mm.bin", R.raw.td2130_57mm);
            raw2file("TD2130_40mm40mm.bin", R.raw.td2130_40mm40mm);

            raw2file("TD4100N_102mm152mm.bin", R.raw.td4100n_102mmx152mm);
            raw2file("TD4000_102mm.bin", R.raw.td4000_102mm);
            raw2file("TD4000_102mm152mm.bin", R.raw.td4000_102mmx152mm);

            raw2file("RJ4230B_58mm.bin", R.raw.rj4230b_58mm);
            raw2file("RJ4230B_102mm.bin", R.raw.rj4230b_102mm);
            raw2file("RJ4230B_102mm152mm.bin", R.raw.rj4230b_102mm152mm);

        } catch (Exception ignored) {
        }

    }

    /**
     * Asking user for write permission on storage
     * @return boolean
     */
    private boolean isPermitWriteStorage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        if (!isPermitWriteStorage()) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * check if user granted access or not
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.unable_access),
                            Toast.LENGTH_SHORT).show();
                } else {
                    init();
                }
            }

        }
    }

    private void setPreferences() {

        try {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        // initialization for print
        Printer printer = new Printer();
        PrinterInfo printerInfo = printer.getPrinterInfo();
        if (printerInfo == null) {
            printerInfo = new PrinterInfo();
            printer.setPrinterInfo(printerInfo);

        }
        if (sharedPreferences.getString("printerModel", "").equals("")) {

                String printerModel = printerInfo.printerModel.toString();
                PrinterModelInfo.Model model = PrinterModelInfo.Model.valueOf(printerModel);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("printerModel", printerModel);
                editor.putString("port", printerInfo.port.toString());
                editor.putString("address", printerInfo.ipAddress);
                editor.putString("macAddress", printerInfo.macAddress);

                // Override SDK default paper size
                editor.putString("paperSize", model.getDefaultPaperSize());

                editor.putString("orientation", printerInfo.orientation.toString());
                editor.putString("numberOfCopies",
                        Integer.toString(printerInfo.numberOfCopies));
                editor.putString("halftone", printerInfo.halftone.toString());
                editor.putString("printMode", printerInfo.printMode.toString());
                editor.putString("pjCarbon", Boolean.toString(printerInfo.pjCarbon));
                editor.putString("pjDensity",
                        Integer.toString(printerInfo.pjDensity));
                editor.putString("pjFeedMode", printerInfo.pjFeedMode.toString());
                editor.putString("align", printerInfo.align.toString());
                editor.putString("leftMargin",
                        Integer.toString(printerInfo.margin.left));
                editor.putString("valign", printerInfo.valign.toString());
                editor.putString("topMargin",
                        Integer.toString(printerInfo.margin.top));
                editor.putString("customPaperWidth",
                        Integer.toString(printerInfo.customPaperWidth));
                editor.putString("customPaperLength",
                        Integer.toString(printerInfo.customPaperLength));
                editor.putString("customFeed",
                        Integer.toString(printerInfo.customFeed));
                editor.putString("paperPosition",
                        printerInfo.paperPosition.toString());
                editor.putString("customSetting",
                        sharedPreferences.getString("customSetting", ""));
                editor.putString("rjDensity",
                        Integer.toString(printerInfo.rjDensity));
                editor.putString("rotate180",
                        Boolean.toString(printerInfo.rotate180));
                editor.putString("dashLine", Boolean.toString(printerInfo.dashLine));

                editor.putString("peelMode", Boolean.toString(printerInfo.peelMode));
                editor.putString("mode9", Boolean.toString(printerInfo.mode9));
                editor.putString("pjSpeed", Integer.toString(printerInfo.pjSpeed));
                editor.putString("pjPaperKind", printerInfo.pjPaperKind.toString());
                editor.putString("printerCase",
                        printerInfo.rollPrinterCase.toString());
                editor.putString("printQuality", printerInfo.printQuality.toString());
                editor.putString("skipStatusCheck",
                        Boolean.toString(printerInfo.skipStatusCheck));
                editor.putString("checkPrintEnd", printerInfo.checkPrintEnd.toString());
                editor.putString("imageThresholding",
                        Integer.toString(printerInfo.thresholdingValue));
                editor.putString("scaleValue",
                        Double.toString(printerInfo.scaleValue));
                editor.putString("trimTapeAfterData",
                        Boolean.toString(printerInfo.trimTapeAfterData));
                editor.putString("enabledTethering",
                        Boolean.toString(printerInfo.enabledTethering));


                editor.putString("processTimeout",
                        Integer.toString(printerInfo.timeout.processTimeoutSec));
                editor.putString("sendTimeout",
                        Integer.toString(printerInfo.timeout.sendTimeoutSec));
                editor.putString("receiveTimeout",
                        Integer.toString(printerInfo.timeout.receiveTimeoutSec));
                editor.putString("connectionTimeout",
                        Integer.toString(printerInfo.timeout.connectionWaitMSec));
                editor.putString("closeWaitTime",
                        Integer.toString(printerInfo.timeout.closeWaitDisusingStatusCheckSec));

                editor.putString("overwrite",
                        Boolean.toString(printerInfo.overwrite));
                editor.putString("savePrnPath", printerInfo.savePrnPath);
                editor.putString("softFocusing",
                        Boolean.toString(printerInfo.softFocusing));
                editor.putString("rawMode",
                        Boolean.toString(printerInfo.rawMode));
                editor.putString("workPath", printerInfo.workPath);
                editor.putString("useLegacyHalftoneEngine",
                        Boolean.toString(printerInfo.useLegacyHalftoneEngine));
                editor.apply();
            }

        } catch(NullPointerException e) {

            Extras.responseErrorDialog("Invalid data", this);
        }

    }

    /**
     * parse given file name
     * @param fileName
     * @return fileName
     */
    private String parseFileName(String fileName) {

        if (fileName.contains("content://")) {
            if (getIntent().getExtras() == null) {
                return "";
            }
            final Uri imageUri = Uri.parse(
                    getIntent().getExtras().get("android.intent.extra.STREAM").toString());
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri, projection, null, null, null);
            if (cursor == null) {
                return "";
            }
            int columnIndex = 0;
            try {
                columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            } catch (IllegalArgumentException e) {
                return "";
            }
            cursor.moveToFirst();
            fileName = cursor.getString(columnIndex);
            cursor.close();
        } else if (fileName.contains("file://")) {
            fileName = Uri.decode(fileName);
            fileName = fileName.substring(7);
        }
        return fileName;
    }


    /**
     * save data from intent
     * @param intent
     * @return
     */
    private String saveDataFromIntent(Intent intent) {
        String fileName = "";

        try {
            Uri uri;
            if (Intent.ACTION_SEND.equals(intent.getAction())) {
                uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            } else {
                uri = getIntent().getData();
            }
            if (uri == null) {
                return "";
            }
            Cursor cursor = getContentResolver().query(uri, new String[]{
                    MediaStore.MediaColumns.DISPLAY_NAME
            }, null, null, null);
            if (cursor == null) {
                return "";
            }
            cursor.moveToFirst();
            int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);

            String folder = Environment.getExternalStorageDirectory()
                    .toString() + "/com.brother.ptouch.sdk/";
            File newdir = new File(folder);
            if (!newdir.exists()) {
                newdir.mkdir();
            }

            if (nameIndex >= 0) {
                fileName = folder + cursor.getString(nameIndex);
            }
            cursor.close();
            File dstFile = new File(fileName);
            OutputStream output = new FileOutputStream(dstFile);
            InputStream input = new BufferedInputStream(getContentResolver().openInputStream(uri));

            int DEFAULT_BUFFER_SIZE = 1024 * 4;
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            input.close();
            output.close();
        } catch (IOException e) {
            fileName = "";
        } catch (NullPointerException e){

        }

        return fileName;
    }

    /**
     * get file data from intent
     */
    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        // Get file path from intent
        if (Intent.ACTION_SEND.equals(intent.getAction())
                || Intent.ACTION_VIEW.equals(intent.getAction())) {

            String fileName = "";
            // get the data of Intent.ACTION_SEND from other application
            if (Intent.ACTION_SEND.equals(intent.getAction())) {
                if (intent.getExtras() == null) {
                    return;
                }
                fileName = intent.getExtras()
                        .get("android.intent.extra.STREAM").toString();
                fileName = parseFileName(fileName);

            } else {

                Uri uri = intent.getData();
                if (uri == null) {
                    return;
                }
                fileName = uri.toString();
                if (fileName == null) {
                    return;
                }
                fileName = parseFileName(fileName);
            }
            if (fileName == null || "".equals(fileName)) {
                fileName = saveDataFromIntent(intent);
            }

            if (fileName == null || fileName.equals("")) {
                return;
            }
            // launch the PrintImage Activity when it is a image file or prn
            // file
            if (Common.isImageFile(fileName) || Common.isPrnFile(fileName)) {
                Intent printerList = new Intent(this, PrintImageActivity.class);
                printerList.putExtra(Common.INTENT_FILE_NAME, fileName);
                startActivity(printerList);
            }

        }
    }

    /**
     * show the closing message
     */
    private void showTips() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.end_title)
                .setMessage(R.string.close_message)
                .setCancelable(false)
                .setPositiveButton(R.string.button_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                                final int which) {

                                finish();
                            }
                        })
                .setNegativeButton(R.string.button_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                                final int which) {
                            }
                        }).create();
        alertDialog.show();
    }

    /**
     * copy from raw in resource
     */
    private void raw2file(String fileName, int fileID) {

        File newdir = new File(Common.CUSTOM_PAPER_FOLDER);
        if (!newdir.exists()) {
            newdir.mkdir();
        }
        File dstFile = new File(Common.CUSTOM_PAPER_FOLDER + fileName);
        if (!dstFile.exists()) {
            try {
                InputStream input;
                OutputStream output;
                input = this.getResources().openRawResource(fileID);
                output = new FileOutputStream(dstFile);
                int DEFAULT_BUFFER_SIZE = 1024 * 4;
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                int n;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                input.close();
                output.close();
            } catch (IOException ignored) {
            } catch (NullPointerException e){

            }
        }
    }

    /**
     * on back pressed inside fragment
     */
    @Override
    public void onBackPressed() {
        Fragment f = this.getFragmentManager().findFragmentById(R.id.fragment_container);

        if(f instanceof GenerateQRCodeFragment) {

            showTips();

        } else if (f instanceof GenerateQRCodeFragment) {

        } else {

            super.onBackPressed();
        }
    }

}
