package pl.rafik.geoorganizer.model.entity;

import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;

/**
 * User: SG0219139
 * Date: 11/21/13
 */
public class DrbxTaskEntity extends TaskEntity {
    private DbxDatastore mDatastore;
    private DbxTable mTable;
    private DbxRecord mRecord;
}
