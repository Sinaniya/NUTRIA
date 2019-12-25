package ch.uzh.csg.foodchain.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.brother.ptouch.sdk.PrinterInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ch.uzh.csg.foodchain.R;

/**
 * The type Bluetooth settings activity.
 */
public class BluetoothSettingsActivity extends BasePrinterSettingActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_cutomer_layout);
        addPreferencesFromResource(R.xml.bluetooth_settings);


        Button btGetPrinterSettings = (Button) findViewById(R.id.btGetPrinterSettings);
        btGetPrinterSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrinterSettingsButtonOnClick();

            }
        });

        Button btSetPrinterSettings = (Button) findViewById(R.id.btSetPrinterSettings);
        btSetPrinterSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPrinterSettingsButtonOnClick();

            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        updateValue();
        mList = Arrays.asList(PrinterInfo.PrinterSettingItem.BT_ISDISCOVERABLE,
                PrinterInfo.PrinterSettingItem.BT_DEVICENAME,
                PrinterInfo.PrinterSettingItem.BT_BOOTMODE, PrinterInfo.PrinterSettingItem.BT_AUTO_CONNECTION);

    }


    /**
     * update information about new connection and device
     */
    private void updateValue() {

        setPreferenceValue("bt_isdiscoverable");
        setPreferenceValue("bt_bootmode");
        setEditValue("bt_devicename");
        setPreferenceValue("bt_auto_connection");
    }

    /**
     * create newly connected device settings
     */
    protected Map<PrinterInfo.PrinterSettingItem, String> createSettingsMap() {

        Map<PrinterInfo.PrinterSettingItem, String> settings = new HashMap<PrinterInfo.PrinterSettingItem, String>();
        settings.put(PrinterInfo.PrinterSettingItem.BT_ISDISCOVERABLE,
                sharedPreferences.getString("bt_isdiscoverable", ""));
        settings.put(PrinterInfo.PrinterSettingItem.BT_DEVICENAME,
                sharedPreferences.getString("bt_devicename", ""));

        settings.put(PrinterInfo.PrinterSettingItem.BT_BOOTMODE,
                sharedPreferences.getString("bt_bootmode", ""));

        settings.put(PrinterInfo.PrinterSettingItem.BT_AUTO_CONNECTION,
                sharedPreferences.getString("bt_auto_connection", ""));

        return settings;

    }

    /**
     * save settings for current device
     * @param settings the settings
     */
    public void saveSettings(Map<PrinterInfo.PrinterSettingItem, String> settings) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        for (PrinterInfo.PrinterSettingItem str : settings.keySet()) {
            switch (str) {
                case BT_ISDISCOVERABLE:
                    setPreferenceValue("bt_isdiscoverable", settings.get(str));
                    break;

                case BT_DEVICENAME:
                    setEditValue("bt_devicename", settings.get(str));
                    break;
                case BT_BOOTMODE:
                    setPreferenceValue("bt_bootmode", settings.get(str));
                    break;
                case BT_AUTO_CONNECTION:
                    setPreferenceValue("bt_auto_connection", settings.get(str));
                    break;
                default:
                    break;
            }
        }
        editor.apply();
        updateValue();

    }

}
