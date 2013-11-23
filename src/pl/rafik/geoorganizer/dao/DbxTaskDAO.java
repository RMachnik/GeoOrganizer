package pl.rafik.geoorganizer.dao;

import android.content.Context;
import com.dropbox.sync.android.*;
import pl.rafik.geoorganizer.dbx.DbxStart;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;
import pl.rafik.geoorganizer.model.entity.TaskEntity;

import java.util.ArrayList;
import java.util.List;

import static pl.rafik.geoorganizer.model.entity.TaskOpenHelper.*;

/**
 * User: SG0219139
 * Date: 11/21/13
 */
public class DbxTaskDAO implements ITaskDAO {
    private DbxDatastore mDatastore;
    private DbxTable mTable;
    private String APP_KEY;
    private String APP_SECRET;
    private DbxAccountManager dbxAccountManager;

    public DbxTaskDAO(Context context) {
        initialiseDbx(context);
        mTable = mDatastore.getTable(TABLE_NAME);
    }

    private DbxFields convertTaskToDbxFields(TaskEntity task) {
        DbxFields taskFields = new DbxFields().set(TASK_NOTE, task.getNote()).set(TASK_ADDRESS, task.getLocalistationAddress()).set(TASK_DATE, task.getData()).set(TASK_LATITUDE, task.getLatitude()).set(TASK_LONGITUDE, task.getLongitude()).set(TASK_PRIORITY, task.getPriority()).set(TASK_STATUS, task.getStatus());
        return taskFields;
    }

    private TaskEntity convertDbxFieldsToTaskEntity(DbxRecord dbxFields) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setData(dbxFields.getString(TASK_DATE));
        taskEntity.setLatitude(dbxFields.getString(TASK_LATITUDE));
        taskEntity.setLocalistationAddress(dbxFields.getString(TASK_ADDRESS));
        taskEntity.setLongitude(dbxFields.getString(TASK_LONGITUDE));
        taskEntity.setNote(dbxFields.getString(TASK_NOTE));
        taskEntity.setStatus(dbxFields.getString(TASK_STATUS));
        taskEntity.setPriority(dbxFields.getString(TASK_PRIORITY));
        taskEntity.setId(dbxFields.getId());
        return taskEntity;
    }

    @Override
    public String addTask(TaskEntity task) throws DbxException {
        DbxFields taskFields = convertTaskToDbxFields(task);
        DbxRecord dbxRecord = mTable.insert(taskFields);
        mDatastore.sync();
        return dbxRecord.getId();
    }

    @Override
    public List<TaskEntity> getAllTasks() throws DbxException {
        List<TaskEntity> taskEntityList = new ArrayList<>();
        for (DbxRecord field : mTable.query().asList()) {
            TaskEntity taskEntity = convertDbxFieldsToTaskEntity(field);
            taskEntityList.add(taskEntity);
        }
        mDatastore.sync();
        return taskEntityList;
    }

    @Override
    public TaskEntity getTask(String id) throws DbxException {
        return convertDbxFieldsToTaskEntity(mTable.get(id));
    }

    @Override
    public List<TaskEntity> getTasks(GeoLocalisation localisation) throws DbxException {
        List<TaskEntity> taskEntityList = new ArrayList<>();
        DbxFields queryParams = new DbxFields().set(TASK_STATUS, "NOT");
        for (DbxRecord field : mTable.query(queryParams).asList()) {
            TaskEntity taskEntity = convertDbxFieldsToTaskEntity(field);
            taskEntityList.add(taskEntity);
        }
        mDatastore.sync();
        return taskEntityList;
    }

    @Override
    public List<TaskEntity> getFutureTasks() {
        return null;
    }

    @Override
    public List<TaskEntity> getPastTasks() {
        return null;
    }

    @Override
    public List<TaskEntity> getNotDoneTasks() {
        return null;
    }

    @Override
    public List<TaskEntity> getDoneTasks() {
        return null;
    }

    @Override
    public int updateTask(TaskEntity ent) {
        return 0;
    }

    @Override
    public int deleteTask(String id) {
        return 0;
    }

    @Override
    public TaskEntity getTask(GeoLocalisation localisation) {
        return null;
    }

    @Override
    public List<TaskEntity> getActualTasks() throws DbxException {
        List<TaskEntity> taskEntityList = new ArrayList<>();
        DbxFields queryParams = new DbxFields().set(TASK_STATUS, "NOT");
        for (DbxRecord field : mTable.query(queryParams).asList()) {
            TaskEntity taskEntity = convertDbxFieldsToTaskEntity(field);
            taskEntityList.add(taskEntity);
        }
        mDatastore.sync();
        return taskEntityList;
    }

    @Override
    public int makeDone(String id) {
        return 0;
    }

    @Override
    public int makeNotDone(String id) {
        return 0;
    }

    private void initialiseDbx(Context context) {
        if (null == mDatastore) {
            try {
                if (!DbxStart.dbxDatastore.isOpen())
                    mDatastore = DbxDatastore.openDefault(DbxStart.dbxAccountManager.getLinkedAccount());
                else {
                    mDatastore = DbxStart.dbxDatastore;
                }
                mDatastore.sync();
            } catch (DbxException e) {
                e.printStackTrace();
            }
        }
    }
}
