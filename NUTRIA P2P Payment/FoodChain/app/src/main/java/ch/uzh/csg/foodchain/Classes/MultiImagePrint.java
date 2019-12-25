/**
 * ImagePrint for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */
package ch.uzh.csg.foodchain.Classes;

import android.content.Context;

import java.util.ArrayList;

/**
 * The type Multi image print.
 */
public class MultiImagePrint extends BasePrint {

    private ArrayList<String> mImageFiles;

    /**
     * Instantiates a new Multi image print.
     *
     * @param context the context
     * @param mHandle the m handle
     * @param mDialog the m dialog
     */
    public MultiImagePrint(Context context, MsgHandle mHandle, MsgDialog mDialog) {
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
        mPrintResult = mPrinter.printFileList(mImageFiles);

    }

}