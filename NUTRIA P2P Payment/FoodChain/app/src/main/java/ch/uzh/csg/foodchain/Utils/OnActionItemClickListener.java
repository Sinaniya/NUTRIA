package ch.uzh.csg.foodchain.Utils;

import ch.uzh.csg.foodchain.Models.AllCertificateModel;
import ch.uzh.csg.foodchain.Models.ProcessActionDataModel;


/**
 * The interface On action item click listener.
 */
public interface OnActionItemClickListener {
    /**
     * On item click.
     *
     * @param item the item
     */
    void onItemClick(ProcessActionDataModel item);
}
