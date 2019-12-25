/**
 * Activity of printing image/prn files
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */
package ch.uzh.csg.foodchain.Activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.brother.ptouch.sdk.PrinterInfo;


import java.util.ArrayList;

import ch.uzh.csg.foodchain.Classes.Common;
import ch.uzh.csg.foodchain.Classes.ImagePrint;
import ch.uzh.csg.foodchain.Classes.MsgDialog;
import ch.uzh.csg.foodchain.Classes.MsgHandle;
import ch.uzh.csg.foodchain.Classes.MultiImagePrint;
import ch.uzh.csg.foodchain.R;
import ch.uzh.csg.foodchain.Utils.GeneralUtils;

/**
 * The type Print image activity.
 */
public class PrintImageActivity extends BaseActivity {

    private final ArrayList<String> mFiles = new ArrayList<String>();
    private ImageView mImageView;
    private Button mBtnPrint, mBtnPrinterSend;
    private Button mMultiPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_image);

        if (GeneralUtils.progress != null)
            GeneralUtils.progress.dismiss();

        mBtnPrinterSend = findViewById(R.id.btnPrinterSend);
        mBtnPrinterSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
        Button btnSelectFile = (Button) findViewById(R.id.btnSelectFile);
        btnSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFileButtonOnClick();
            }
        });

        Button btnPrinterSettings = (Button) findViewById(R.id.btnPrinterSettings);
        btnPrinterSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printerSettingsButtonOnClick();

            }
        });

        Button btnPrinterStatus = (Button) findViewById(R.id.btnPrinterStatus);
        btnPrinterStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printerStatusButtonOnClick();

            }
        });
        Button btnSendFile = (Button) findViewById(R.id.btnSendFile);
        btnSendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFileButtonOnClick();

            }
        });

        mMultiPrint = (Button) findViewById(R.id.btnMultiPrint);
        mMultiPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printMultiFileButtonOnClick();

            }
        });

        mMultiPrint.setEnabled(false);

        // initialization for Activity
        mBtnPrint = (Button) findViewById(R.id.btnPrint);
        mBtnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printButtonOnClick();
                String file = getIntent().getStringExtra("filepath");
                setDisplayFile(file);


            }
        });


        CheckBox chkMutilSelect = (CheckBox) this
                .findViewById(R.id.chkMultipleSelect);
        chkMutilSelect
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton arg0,
                                                 boolean arg1) {
                        checkMultiSelect(arg1);
                    }
                });

        mImageView = (ImageView) this.findViewById(R.id.genrateQrCodeImage);

        // initialization for printing
        msgDialog = new MsgDialog(this);
        msgHandle = new MsgHandle(this, msgDialog);
        basePrint = new ImagePrint(this, msgHandle, msgDialog);

        // when use bluetooth print set the adapter
        BluetoothAdapter bluetoothAdapter = super.getBluetoothAdapter();
        basePrint.setBluetoothAdapter(bluetoothAdapter);
    }

    /**
     * Called when [select file] button is tapped
     */
    @Override
    public void selectFileButtonOnClick() {

        try {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(this);

            // call File Explorer Activity to select a image or prn file
            final String imagePrnPath = Environment.getExternalStorageDirectory()
                    + "/FoodChain/"
                    + "/Files/QRCodeImage.png";
            final Intent fileList = new Intent(PrintImageActivity.this,
                    FileListActivity.class);
            fileList.putExtra(Common.INTENT_TYPE_FLAG, Common.FILE_SELECT_PRN_IMAGE);
            fileList.putExtra(Common.INTENT_FILE_NAME, imagePrnPath);
            startActivityForResult(fileList, Common.FILE_SELECT_PRN_IMAGE);
        } catch (Exception e) {

        }
    }

    /**
     * Called when [Print] button is tapped
     */
    @Override
    public void printButtonOnClick() {
        try {
            // set the printing data
            ((ImagePrint) basePrint).setFiles(mFiles);

            if (!checkUSB())
                return;

            // call function to print
            basePrint.print();
        } catch (Exception e) {

        }
    }

    /**
     * Called when [Print] button is tapped
     */
    public void printMultiFileButtonOnClick() {
        try {
            basePrint = new MultiImagePrint(this, msgHandle, msgDialog);

            // set the printing data
            ((MultiImagePrint) basePrint).setFiles(mFiles);

            if (!checkUSB())
                return;

            // call function to print
            basePrint.print();

            basePrint = new ImagePrint(this, msgHandle, msgDialog);
        } catch (Exception e) {

        }
    }

    /**
     * Called when [Printer Status] button is tapped
     */
    private void printerStatusButtonOnClick() {
        try {
            if (!checkUSB())
                return;
            basePrint.getPrinterStatus();
        } catch (Exception e) {

        }
    }

    /**
     * Called when [Printer Status] button is tapped
     */
    private void sendFileButtonOnClick() {

        try {
            // set the printing data
            ((ImagePrint) basePrint).setFiles(mFiles);

            if (!checkUSB())
                return;

            sendFile();
        } catch (Exception e) {

        }
    }


    /**
     * Launch the thread to print
     */
    private void sendFile() {
         try {
             SendFileThread getTread = new SendFileThread();
             getTread.start();
         } catch (Exception e) {

         }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional data
     * from it.
     */
    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);

            // set the image/prn file
            if (resultCode == RESULT_OK
                    && requestCode == Common.FILE_SELECT_PRN_IMAGE) {
                final String strRtn = data.getStringExtra(Common.INTENT_FILE_NAME);
                setImageOrPrintFile(strRtn);
            }
        } catch (Exception e) {

        }
    }

    /**
     * set the image/prn file
     */
    private void setImageOrPrintFile(String file) {
        CheckBox chkMultiSelect = (CheckBox) this
                .findViewById(R.id.chkMultipleSelect);
        TextView tvSelectedFiles = (TextView) findViewById(R.id.tvSelectedFiles);

        try {
            if (chkMultiSelect.isChecked()) {
                if (!mFiles.contains(file)) {
                    mFiles.add(file);

                    int count = mFiles.size();
                    String str = "";
                    for (int i = 0; i < count; i++) {
                        str = str + mFiles.get(i) + "\n";
                    }
                    tvSelectedFiles.setText(str);
                }
            } else {
                setDisplayFile(file);
            }
            mMultiPrint.setEnabled(true);
            mBtnPrint.setEnabled(true);
        } catch (Exception e) {

        }
    }

    /**
     * set the selected file to display
     */
    @SuppressWarnings("deprecation")
    private void setDisplayFile(String file) {
        mFiles.clear();
        mFiles.add(file);

        ((TextView) findViewById(R.id.tvSelectedFiles)).setText(file);

        try {
            if (Common.isImageFile(file)) {

                WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                int displayWidth = display.getWidth();
                int displayHeight = display.getHeight();
                Button btnMultiPrint = (Button) findViewById(R.id.btnMultiPrint);

                int[] location = new int[2];
                btnMultiPrint.getLocationOnScreen(location);

                int height = displayHeight - location[1]
                        - btnMultiPrint.getHeight();
                Bitmap mBitmap = Common.fileToBitmap(file, displayWidth, height);

                mImageView.setImageBitmap(mBitmap);
            } else {
                mImageView.setImageBitmap(null);
            }
        } catch (Exception e) {

        }
    }

    /**
     * set the status of controls when the [Multi Select] CheckBox is checked or
     * not
     */
    private void checkMultiSelect(boolean isVisible) {
        mFiles.clear();
        mBtnPrint.setEnabled(false);
        mMultiPrint.setEnabled(false);

        TextView tvSelectedFiles = (TextView) findViewById(R.id.tvSelectedFiles);
        tvSelectedFiles.setText("");

        try {
            if (isVisible) {
                mImageView.setImageBitmap(null);
            }
        } catch (Exception e) {

        }
    }

    /**
     * Thread for getting the printer's status
     */
    private class SendFileThread extends Thread {
        @Override
        public void run() {

            try {
                // set info. for printing
                basePrint.setPrinterInfo();

                // start message
                Message msg = msgHandle.obtainMessage(Common.MSG_PRINT_START);
                msgHandle.sendMessage(msg);

                basePrint.getPrinter().startCommunication();

                int count = ((ImagePrint) basePrint).getFiles().size();

                for (int i = 0; i < count; i++) {

                    String strFile = ((ImagePrint) basePrint).getFiles().get(i);

                    basePrint.setPrintResult(basePrint.getPrinter().sendBinaryFile(strFile));

                    // if error, stop print next files
                    if (basePrint.getPrintResult().errorCode != PrinterInfo.ErrorCode.ERROR_NONE) {
                        break;
                    }
                }


                basePrint.getPrinter().endCommunication();

                // end message
                msgHandle.setResult(basePrint.showResult());
                msgHandle.setBattery(basePrint.getBatteryDetail());
                msg = msgHandle.obtainMessage(Common.MSG_PRINT_END);
                msgHandle.sendMessage(msg);
            } catch (Exception e) {

            }

        }
    }

    /**
     * Send mail.
     */
    public void sendMail() {
        try {
            String path = Environment.getExternalStorageDirectory()
                    + "/FoodChain/"
                    + "/Files/QRCodeImage.png";
            Log.d("path", path);
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.putExtra(android.content.Intent.EXTRA_EMAIL, "softwrengr@gmail.com");
            intent.setType("application/image");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            intent.putExtra(Intent.EXTRA_TEXT, "Enter your message");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        } catch (Exception e) {

        }
    }
}
