package ch.uzh.csg.foodchain.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;

import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;

import java.util.List;
import java.util.Map;

import ch.uzh.csg.foodchain.Classes.BasePrint;
import ch.uzh.csg.foodchain.Classes.Common;
import ch.uzh.csg.foodchain.Classes.MsgDialog;
import ch.uzh.csg.foodchain.Classes.MsgHandle;
import ch.uzh.csg.foodchain.Classes.PrinterModelInfo;
import ch.uzh.csg.foodchain.Classes.PrinterPreference;
import ch.uzh.csg.foodchain.R;

/**
 * The type Base printer setting activity.
 */
public abstract class BasePrinterSettingActivity  extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @TargetApi(12)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false))
                        Common.mUsbRequest = 1;
                    else
                        Common.mUsbRequest = 2;
                }
            }
        }
    };
    private final String INVALID_PARAM_VALUE = "";
    /**
     * The Shared preferences.
     */
    public SharedPreferences sharedPreferences;
    /**
     * The Model info.
     */
    PrinterModelInfo modelInfo = new PrinterModelInfo();
    /**
     * The M list.
     */
    public List<PrinterInfo.PrinterSettingItem> mList;
    /**
     * The BasePrint
     */
    private BasePrint basePrint = null;
    /**
     * The MsgHandle
     */
    private MsgHandle msgHandle;
    /**
     * The MsgDialog
     */
    private MsgDialog msgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        msgDialog = new MsgDialog(this);
        msgHandle = new MsgHandle(this, msgDialog);
        basePrint = new PrinterPreference(this, msgHandle, msgDialog);

        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        basePrint.setBluetoothAdapter(bluetoothAdapter);

    }

    /**
     * Called when [Printer Settings] button is tapped
     */
    public void printerSettingsButtonOnClick() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    /**
     * show the BACK message
     */
    private void showTips() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.end_title)
                .setMessage(R.string.end_message)
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
     * get the BluetoothAdapter
     */
    private BluetoothAdapter getBluetoothAdapter() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            final Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(enableBtIntent);
        }
        return bluetoothAdapter;
    }

    /**
     * Gets usb device.
     *
     * @param usbManager the usb manager
     * @return the usb device
     */
    @TargetApi(12)
    protected UsbDevice getUsbDevice(UsbManager usbManager) {
        if (basePrint.getPrinterInfo().port != PrinterInfo.Port.USB) {
            return null;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            Message msg = msgHandle.obtainMessage(Common.MSG_WRONG_OS);
            msgHandle.sendMessage(msg);
            return null;
        }

        UsbDevice usbDevice = basePrint.getUsbDevice(usbManager);
        if (usbDevice == null) {
            Message msg = msgHandle.obtainMessage(Common.MSG_NO_USB);
            msgHandle.sendMessage(msg);
            return null;
        }

        return usbDevice;
    }

    /**
     * A USB driver is acquired.
     *
     * @param activity
     * @return
     */
    @TargetApi(12)
    private boolean createUsbDevice(BaseActivity activity) {

        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        UsbDevice usbDevice = activity.getUsbDevice(usbManager);
        if (usbDevice == null) {
            return false;
        }
        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(BaseActivity.ACTION_USB_PERMISSION), 0);
        usbManager.requestPermission(usbDevice, permissionIntent);
        return true;
    }

    /**
     * check for USB
     * @return boolean
     */
    @TargetApi(12)
    private boolean checkUSB() {
        if (basePrint.getPrinterInfo().port != PrinterInfo.Port.USB) {
            return true;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            Message msg = msgHandle.obtainMessage(Common.MSG_WRONG_OS);
            msgHandle.sendMessage(msg);
            return false;
        }
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        UsbDevice usbDevice = basePrint.getUsbDevice(usbManager);
        if (usbDevice == null) {
            Message msg = msgHandle.obtainMessage(Common.MSG_NO_USB);
            msgHandle.sendMessage(msg);
            return false;
        }
        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(ACTION_USB_PERMISSION), 0);
        registerReceiver(mUsbReceiver, new IntentFilter(ACTION_USB_PERMISSION));
        if (!usbManager.hasPermission(usbDevice)) {
            Common.mUsbRequest = 0;
            usbManager.requestPermission(usbDevice, permissionIntent);
        } else {
            Common.mUsbRequest = 1;
        }
        return true;
    }

    /**
     * set data of a particular ListPreference
     *
     * @param key   the key
     * @param value the value
     */
    @SuppressWarnings("deprecation")
    protected void setPreferenceValue(String key, String value) {

        try {
            ListPreference printerValuePreference = (ListPreference) getPreferenceScreen()
                    .findPreference(key);

            int index = printerValuePreference.findIndexOfValue(value);
            if (index >= 0) {
                printerValuePreference.setSummary(printerValuePreference
                        .getEntries()[index]);
                printerValuePreference.setValue(value);
            } else {
                printerValuePreference.setSummary(getResources().getString(
                        R.string.text_no_data));
                printerValuePreference.setValue("");
            }
        } catch (Exception e) {

        }

    }

    /**
     * set data of a particular EditTextPreference
     *
     * @param key   the key
     * @param value the value
     */
    @SuppressWarnings("deprecation")
    protected void setEditValue(String key, String value) {

        try {
            EditTextPreference printerValuePreference = (EditTextPreference) getPreferenceScreen()
                    .findPreference(key);

            if (!value.equals("")) {
                printerValuePreference.setSummary(value);
                printerValuePreference.setText(value);
            } else {
                printerValuePreference.setSummary(getResources().getString(
                        R.string.text_no_data));
                printerValuePreference.setText("");
            }
        } catch (Exception e) {

        }
    }

    /**
     * set data of a particular ListPreference
     *
     * @param value the value
     */
    @SuppressWarnings("deprecation")
    public void setPreferenceValue(String value) {

        try {
            String data = sharedPreferences.getString(value, "");

            ListPreference printerValuePreference = (ListPreference) getPreferenceScreen()
                    .findPreference(value);
            printerValuePreference.setOnPreferenceChangeListener(this);
            if (data.equals("")) {
                printerValuePreference.setValue(INVALID_PARAM_VALUE);
                sharedPreferences.edit().putString(value, INVALID_PARAM_VALUE)
                        .commit();
            }

            int index = printerValuePreference.findIndexOfValue(data);
            if (index >= 0) {
                printerValuePreference.setSummary(printerValuePreference
                        .getEntries()[index]);
            } else {
                printerValuePreference.setSummary(getResources().getString(
                        R.string.text_no_data));
            }
        } catch (Exception e) {

        }

    }

    /**
     * set data of a particular EditTextPreference
     *
     * @param value the value
     */
    @SuppressWarnings("deprecation")
    public void setEditValue(String value) {

        try {
            String name = sharedPreferences.getString(value, "");
            EditTextPreference printerValuePreference = (EditTextPreference) getPreferenceScreen()
                    .findPreference(value);
            printerValuePreference.setOnPreferenceChangeListener(this);

            if (!name.equals("")) {
                printerValuePreference.setSummary(name);
            } else {
                printerValuePreference.setSummary(getResources().getString(
                        R.string.text_no_data));
            }
        } catch (Exception e) {

        }
    }

    /**
     * Called when a Preference has been changed by the user. This is called
     * before the state of the Preference is about to be updated and before the
     * state is persisted.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (newValue != null) {

            if (preference instanceof ListPreference) {

                ((ListPreference) preference).setValue(newValue.toString());
                preference.setSummary(((ListPreference) preference).getEntry());

            } else if (preference instanceof EditTextPreference) {
                String data = newValue.toString();
                if ("".equals(data)) {
                    preference.setSummary(getResources().getString(
                            R.string.text_no_data));

                } else {
                    preference.setSummary(newValue.toString());

                }
            }

            return true;
        }

        return false;

    }

    /**
     * Create settings map map.
     *
     * @return the map
     */
    protected abstract Map<PrinterInfo.PrinterSettingItem, String> createSettingsMap();

    /**
     * Save settings.
     *
     * @param settings the settings
     */
    public abstract void saveSettings(Map<PrinterInfo.PrinterSettingItem, String> settings);

    /**
     * Called when [Printer Settings] button is tapped
     */
    public void setPrinterSettingsButtonOnClick() {

        Map<PrinterInfo.PrinterSettingItem, String> settings = createSettingsMap();
        if (!checkUSB())
            return;
        ((PrinterPreference) basePrint).updatePrinterSetting(settings);

    }

    /**
     * Called when [Printer Settings] button is tapped
     */
    public void getPrinterSettingsButtonOnClick() {

        try {
            if (!checkUSB())
                return;
            final Handler handler = new Handler();
            PrinterPreference.PrinterPreListener listener = new PrinterPreference.PrinterPreListener() {
                @Override
                public void finish(final PrinterStatus status,
                                   final Map<PrinterInfo.PrinterSettingItem, String> settings) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (status.errorCode == PrinterInfo.ErrorCode.ERROR_NONE) {
                                saveSettings(settings);
                            }

                        }
                    });
                }
            };

            ((PrinterPreference) basePrint).getPrinterSetting(mList, listener);

        } catch (Exception e) {

        }
    }

}
