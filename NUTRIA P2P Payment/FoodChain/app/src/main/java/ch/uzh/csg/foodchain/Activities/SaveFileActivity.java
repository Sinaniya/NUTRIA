/**
 * Activity of printing image/prn files
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */
package ch.uzh.csg.foodchain.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.io.File;

import ch.uzh.csg.foodchain.Classes.Common;
import ch.uzh.csg.foodchain.R;

/**
 * The type Save file activity.
 */
@SuppressWarnings("ALL")
public class SaveFileActivity extends Activity {

    @SuppressWarnings("WeakerAccess")
    private
    TextView tvSelectedFolder;
    @SuppressWarnings("WeakerAccess")
    private
    EditText etSelectedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_file_path);

        Button btnSelectFolder = (Button) findViewById(R.id.btnSelectFolder);
        btnSelectFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFolderButtonOnClick();
            }
        });
        Button btnSetSavePrnPath = (Button) findViewById(R.id.btnSetSavePrnPath);
        btnSetSavePrnPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSavePrnPathOnClick();
            }
        });
        Button btnCancelSavePrnPath = (Button) findViewById(R.id.btnCancelSavePrnPath);
        btnCancelSavePrnPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelSavePrnPathOnClick();
            }
        });

        tvSelectedFolder = (TextView) findViewById(R.id.folderPath);
        etSelectedFile = (EditText) findViewById(R.id.savePrnPath);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == RESULT_OK) {

                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(this);
                final String folder = prefs
                        .getString(Common.PREFES_SAVE_FOLDER, "");
                tvSelectedFolder.setText(folder);
            }
        } catch (Exception e) {

        }

    }

    /**
     * Called when [select file] button is tapped
     * call File Explorer Activity to select a image or print file
     */
    @SuppressWarnings("WeakerAccess")
    private void selectFolderButtonOnClick() {

        try {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(this);

            final String folder = prefs.getString(Common.PREFES_SAVE_FOLDER, "");
            final Intent fileList = new Intent(SaveFileActivity.this,
                    FileListActivity.class);
            fileList.putExtra(Common.INTENT_TYPE_FLAG, Common.FOLDER_SELECT);
            fileList.putExtra(Common.INTENT_FILE_NAME, folder);
            startActivityForResult(fileList, Common.FOLDER_SELECT);
        } catch (Exception e) {

        }
    }

    /**
     * save print path
     */
    @SuppressWarnings("WeakerAccess")
    private void setSavePrnPathOnClick() {
         try {
             String fileName = etSelectedFile.getText().toString();
             String folder = (String) tvSelectedFolder.getText();
             File newdir = new File(folder);
             /* return to the previous Activity */
             final Intent returnIntent = new Intent();
             if ("".equals(fileName) || "".equals(folder)) {
                 returnIntent.putExtra("savePrnPath", "");
                 setResult(RESULT_OK, returnIntent);
                 return;
             }
             if (newdir.exists()) {
                 returnIntent.putExtra("savePrnPath", folder + "/" + fileName);
                 setResult(RESULT_OK, returnIntent);

             } else {
                 setResult(RESULT_CANCELED, returnIntent);

             }

             finish();
         } catch (Exception e) {

         }

    }

    /**
     * unsave print path
     */
    @SuppressWarnings("WeakerAccess")
    private void cancelSavePrnPathOnClick() {

        try {
            final Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            finish();
        } catch (Exception e) {

        }

    }
}
