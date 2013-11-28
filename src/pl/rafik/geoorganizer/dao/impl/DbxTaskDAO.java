package pl.rafik.geoorganizer.dao.impl;

import android.content.Context;
import com.dropbox.sync.android.*;
import pl.rafik.geoorganizer.dao.ITaskDAO;
import pl.rafik.geoorganizer.activities.dbx.DbxStart;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;
import pl.rafik.geoorganizer.model.entity.TaskEntity;
import pl.rafik.geoorganizer.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static pl.rafik.geoorganizer.model.entity.TaskOpenHelper.*;

/**
 * User: SG0219139
 * Date: 11/21/13
 */
public class DbxTaskDAO implements ITaskDAO {
    private DbxDatastore mDatastore;
    private DbxTable mTable;

    public DbxTaskDAO(Context context) {
        DbxStart dbxStart = new DbxStart();
        mDatastore = dbxStart.getOpenedDatastore();
        mTable = mDatastore.getTable(TABLE_NAME);

    }

    private DbxFields convertTaskToDbxFields(TaskEntity task) {
        DbxFields taskFields = new DbxFields().set(TASK_NOTE, task.getNote()).set(TASK_ADDRESS, task.getLocalistationAddress()).set(TASK_DATE, task.getData()).set(TASK_LATITUDE, task.getLatitude()).set(TASK_LONGITUDE, task.getLongitude()).set(TASK_PRIORITY, task.getPriority()).set(TASK_STATUS, task.getStatus());
        return taskFields;
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
        mDatastore.sync();
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
        mDatastore.sync();
        return convertDbxFieldsToTaskEntity(mTable.get(id));
    }

    @Override
    public List<TaskEntity> getTasks(GeoLocalisation localisation) throws DbxException {
        mDatastore.sync();
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
    public List<TaskEntity> getFutureTasks() throws DbxException {
        List<TaskEntity> allTasks = getActualTasks();
        List<TaskEntity> results = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        for (TaskEntity ent : allTasks) {
            Calendar tmp = Calendar.getInstance();
            String dtime[] = ent.getData().split(" ");
            DateUtil.parseDate(tmp, dtime);
            if (c.before(tmp))
                results.add(ent);
        }
        return results;
    }

    @Override
    public List<TaskEntity> getPastTasks() throws DbxException {
        List<TaskEntity> allTasks = getActualTasks();
        List<TaskEntity> results = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        for (TaskEntity ent : allTasks) {
            Calendar tmp = Calendar.getInstance();
            String dtime[] = ent.getData().split(" ");
            DateUtil.parseDate(tmp, dtime);
            if (!c.before(tmp))
                results.add(ent);
        }
        return results;
    }

    @Override
    public List<TaskEntity> getNotDoneTasks() throws DbxException {
        mDatastore.sync();
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
    public List<TaskEntity> getDoneTasks() throws DbxException {
        mDatastore.sync();
        List<TaskEntity> taskEntityList = new ArrayList<>();
        DbxFields queryParams = new DbxFields().set(TASK_STATUS, "DONE");
        for (DbxRecord field : mTable.query(queryParams).asList()) {
            TaskEntity taskEntity = convertDbxFieldsToTaskEntity(field);
            taskEntityList.add(taskEntity);
        }
        mDatastore.sync();
        return taskEntityList;
    }

    @Override
    public int updateTask(TaskEntity ent) throws DbxException {
        mDatastore.sync();
        DbxRecord oldRecord;
        if (!mTable.get(ent.getId()).isDeleted()) {
            oldRecord = mTable.get(ent.getId());
            updateRecord(ent, oldRecord);
        } else return -1;
        mDatastore.sync();
        return 1;


    }

    @Override
    public int deleteTask(String id) {
        return 1;
    }

    @Override
    public TaskEntity getTask(GeoLocalisation localisation) throws DbxException {
        mDatastore.sync();
        DbxFields parameters = new DbxFields().set(TASK_ADDRESS, localisation.getLocalistationAddress()).set(TASK_LATITUDE, localisation.getLatitude()).set(TASK_LONGITUDE, localisation.getLongitude());
        return convertDbxFieldsToTaskEntity(mTable.query(parameters).asList().get(0));
    }

    @Override
    public List<TaskEntity> getActualTasks() throws DbxException {
        mDatastore.sync();
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
    public int makeDone(String id) throws DbxException {
        mDatastore.sync();
        if (!mTable.get(id).isDeleted())
            mTable.get(id).set(TASK_STATUS, DONE);
        else return -1;
        mDatastore.sync();
        return 1;

    }

    @Override
    public int makeNotDone(String id) throws DbxException {
        mDatastore.sync();
        if (!mTable.get(id).isDeleted())
            mTable.get(id).set(TASK_STATUS, NOT_DONE);
        else return -1;
        mDatastore.sync();
        return 1;
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

    private void updateRecord(TaskEntity ent, DbxRecord oldRecord) {
        oldRecord.set(TASK_LONGITUDE, ent.getLongitude());
        oldRecord.set(TASK_LATITUDE, ent.getLatitude());
        oldRecord.set(TASK_NOTE, ent.getNote());
        oldRecord.set(TASK_DATE, ent.getData());
        oldRecord.set(TASK_PRIORITY, ent.getPriority());
        oldRecord.set(TASK_STATUS, ent.getStatus());
        oldRecord.set(TASK_ADDRESS, ent.getLocalistationAddress());
    }
}
