package pl.rafik.geoorganizer.dao;

import com.dropbox.sync.android.*;
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

    public DbxTaskDAO(DbxDatastore datastore) {
        this.mDatastore = datastore;
        mTable = mDatastore.getTable(TABLE_NAME);
    }

    private DbxFields convertTaskToDbxFields(TaskEntity task) {
        DbxFields taskFields = new DbxFields().set(TASK_NOTE, task.getNote()).set(TASK_ADDRESS, task.getLocalistationAddress()).set(TASK_DATE, task.getData()).set(TASK_LATITUDE, task.getLatitude()).set(TASK_LONGITUDE, task.getLongitude()).set(TASK_PRIORITY, task.getPriority()).set(TASK_STATUS, task.getStatus());
        return taskFields;
    }

    private TaskEntity convertDbxFieldsToTaskEntity(DbxFields dbxFields) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setData(dbxFields.getString(TASK_DATE));
        taskEntity.setLatitude(dbxFields.getString(TASK_LATITUDE));
        taskEntity.setLocalistationAddress(dbxFields.getString(TASK_ADDRESS));
        taskEntity.setLongitude(dbxFields.getString(TASK_LONGITUDE));
        taskEntity.setNote(dbxFields.getString(TASK_NOTE));
        taskEntity.setStatus(dbxFields.getString(TASK_STATUS));
        taskEntity.setPriority(dbxFields.getString(TASK_PRIORITY));
        return taskEntity;
    }

    @Override
    public Long addTask(TaskEntity task) throws DbxException {
        DbxFields taskFields = convertTaskToDbxFields(task);
        DbxRecord dbxRecord = mTable.insert(taskFields);
        mDatastore.sync();
        return Long.valueOf(dbxRecord.getId());
    }

    @Override
    public List<TaskEntity> getAllTasks() throws DbxException {
        List<TaskEntity> taskEntityList = new ArrayList<>();

        for (DbxFields field : mTable.query()) {
            TaskEntity taskEntity = convertDbxFieldsToTaskEntity(field);
            taskEntityList.add(taskEntity);
        }
        return taskEntityList;
    }

    @Override
    public TaskEntity getTask(Long id) throws DbxException {
        return convertDbxFieldsToTaskEntity(mTable.get(String.valueOf(id)));
    }

    @Override
    public List<TaskEntity> getTasks(GeoLocalisation localisation) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskEntity> getFutureTasks() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskEntity> getPastTasks() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskEntity> getNotDoneTasks() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskEntity> getDoneTasks() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int updateTask(TaskEntity ent) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int deleteTask(Long id) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TaskEntity getTask(GeoLocalisation localisation) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskEntity> getActualTasks() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int makeDone(Long id) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int makeNotDone(Long id) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
