/**
 * ImagePrint for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */
package ch.uzh.csg.foodchain.Classes;

import android.content.Context;

import com.brother.ptouch.sdk.PrinterInfo.ErrorCode;

import java.util.ArrayList;

/**
 * The type Image print.
 */
public class ImagePrint extends BasePrint {

    private ArrayList<String> mImageFiles;

    /**
     * Instantiates a new Image print.
     *
     * @param context the context
     * @param mHandle the m handle
     * @param mDialog the m dialog
     */
    public ImagePrint(Context context, MsgHandle mHandle, MsgDialog mDialog) {
        super(context, mHandle, mDialog);
    }

    /**
     * set print data
     *
     * @return the files
     */
    public ArrayList<String> getFiles() {
        return mImageFiles;
    }

    /**
     * set print data
     *
     * @param files the files
     */
    public void setFiles(ArrayList<String> files) {
        mImageFiles = files;
    }

    /**
     * do the particular print
     */
    @Override
    protected void doPrint() {

        int count = mImageFiles.size();

        for (int i = 0; i < count; i++) {

            String strFile = mImageFiles.get(i);

            mPrintResult = mPrinter.printFile(strFile);

            // if error, stop print next files
            if (mPrintResult.errorCode != ErrorCode.ERROR_NONE) {
                break;
            }
        }
    }

}