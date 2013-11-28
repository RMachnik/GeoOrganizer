package pl.rafik.geoorganizer.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.dao.ITaskDAO;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;
import pl.rafik.geoorganizer.model.entity.TaskEntity;
import pl.rafik.geoorganizer.model.entity.TaskOpenHelper;
import pl.rafik.geoorganizer.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskDAO extends TaskOpenHelper implements ITaskDAO {

    public TaskDAO(Context context) {
        super(context);

    }

    @Override
    public String addTask(TaskEntity task) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues content = new ContentValues();
            putTaskInContent(task, content);

            return String.valueOf(db.insert(TABLE_NAME, null, content));
        } finally {
            db.close();
        }
    }

    @Override
    public TaskEntity getTask(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String args[] = {""};
            args[0] = String.valueOf(id);
            Cursor cur = db.query(TABLE_NAME, null, ID + "=?", args, null,
                    null, null);
            TaskEntity ent = new TaskEntity();
            if (cur.moveToFirst()) {
                convertToEntity(id, cur, ent);
                return ent;
            } else
                return null;
        } finally {
            db.close();
        }
    }

    @Override
    public List<TaskEntity> getTasks(GeoLocalisation localisation) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<TaskEntity> list = new ArrayList<TaskEntity>();
        try {
            if (!localisation.getLatitude().isEmpty()
                    && !localisation.getLongitude().isEmpty()) {
                String sql = "select * from " + TABLE_NAME + " where "
                        + TASK_LATITUDE + "=? and " + TASK_LONGITUDE + "=? )";
                Cursor cur = db.rawQuery(
                        sql,
                        new String[]{localisation.getLatitude(),
                                localisation.getLongitude(),});
                if (cur.moveToFirst()) {
                    do {
                        TaskEntity ent = new TaskEntity();
                        convertToEntity(String.valueOf(cur.getLong(cur.getColumnIndex(ID))), cur, ent);
                        list.add(ent);
                    } while (cur.moveToNext());
                }
                return list;
                // tutaj mozna dodac jakas nazwe lokalizacji cos w ten desen
            } else if (!localisation.getLocalistationAddress().isEmpty()) {
                String sql = "select * from " + TABLE_NAME + " where "
                        + TASK_ADDRESS + "=?";
                Cursor cur = db
                        .rawQuery(sql, new String[]{localisation
                                .getLocalistationAddress()});
                if (cur.moveToFirst()) {
                    do {
                        TaskEntity ent = new TaskEntity();
                        convertToEntity(String.valueOf(cur.getLong(cur.getColumnIndex(ID))), cur, ent);
                        list.add(ent);

                    } while (cur.moveToNext());
                }
                return list;
            }
        } finally {
            db.close();
        }

        return null;
    }

    @Override
    public TaskEntity getTask(GeoLocalisation localisation) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (!localisation.getLatitude().isEmpty()
                && !localisation.getLongitude().isEmpty()) {
            String sql = "select * from " + TABLE_NAME;
            Cursor cur = db.rawQuery(sql, null);
            if (cur.moveToFirst()) {
                if (cur.getBlob(cur.getColumnIndex(TASK_LATITUDE)).equals(
                        localisation.getLatitude())
                        && cur.getBlob(cur.getColumnIndex(TASK_LONGITUDE))
                        .equals(localisation.getLongitude())) {
                    TaskEntity ent = new TaskEntity();
                    populateTaskEntityFromCursor(cur, ent);
                    return ent;
                } else
                    return null;

            } else
                return null;

        }
        return null;
    }

    @Override
    public List<TaskEntity> getNotDoneTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<TaskEntity> list = new ArrayList<TaskEntity>();
        String sql = "select * from " + TABLE_NAME + " where " + TASK_STATUS
                + "=?";
        try {

            Cursor cur = db.rawQuery(sql, new String[]{NOT_DONE});
            Log.d("LISTA", String.valueOf(cur.getCount()));
            if (cur.moveToFirst()) {
                do {
                    TaskEntity ent = new TaskEntity();
                    convertToEntity(String.valueOf(cur.getLong(cur.getColumnIndex(ID))), cur, ent);
                    list.add(ent);
                } while (cur.moveToNext());
            }

            return list;
        } finally {
            db.close();
        }
    }

    @Override
    public List<TaskEntity> getDoneTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<TaskEntity> list = new ArrayList<TaskEntity>();
        String sql = "select * from " + TABLE_NAME + " where " + TASK_STATUS
                + "=?";
        try {
            Cursor cur = db.rawQuery(sql, new String[]{DONE});
            if (cur.moveToFirst()) {
                do {
                    TaskEntity ent = new TaskEntity();
                    convertToEntity(String.valueOf(cur.getLong(cur.getColumnIndex(ID))), cur, ent);
                    list.add(ent);
                } while (cur.moveToNext());
            }
            return list;
        } finally {
            db.close();
        }
    }

    @Override
    public List<TaskEntity> getFutureTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<TaskEntity> list = new ArrayList<TaskEntity>();
        String sql = "select * from " + TABLE_NAME;
        Calendar c = Calendar.getInstance();
        try {
            Cursor cur = db.rawQuery(sql, null);
            if (cur.moveToFirst()) {
                do {
                    TaskEntity ent = new TaskEntity();
                    convertToEntity(String.valueOf(cur.getLong(cur.getColumnIndex(ID))), cur, ent);
                    Log.d("dao", ent.getData());
                    Calendar tmp = Calendar.getInstance();
                    String dtime[] = ent.getData().split(" ");
                    DateUtil.parseDate(tmp, dtime);
                    if (c.before(tmp))
                        list.add(ent);
                } while (cur.moveToNext());
            }
            return list;

        } finally {
            db.close();
        }
    }

    @Override
    public List<TaskEntity> getPastTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<TaskEntity> list = new ArrayList<TaskEntity>();
        String sql = "select * from " + TABLE_NAME;
        Calendar c = Calendar.getInstance();
        try {
            Cursor cur = db.rawQuery(sql, null);
            if (cur.moveToFirst()) {
                do {
                    TaskEntity ent = new TaskEntity();
                    convertToEntity(String.valueOf(cur.getLong(cur.getColumnIndex(ID))), cur, ent);
                    Calendar tmp = Calendar.getInstance();
                    String dtime[] = ent.getData().split(" ");
                    DateUtil.parseDate(tmp, dtime);
                    if (c.after(tmp))
                        list.add(ent);
                } while (cur.moveToNext());
            }
            return list;
        } finally {
            db.close();
        }
    }

    @Override
    public int updateTask(TaskEntity task) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues content = new ContentValues();
            putTaskInContent(task, content);
            Log.d("id", String.valueOf(task.getId()));
            return db.update(TABLE_NAME, content, ID + "=?",
                    new String[]{String.valueOf(task.getId())});
        } finally {
            db.close();
        }
    }

    @Override
    public int deleteTask(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            return db.delete(TABLE_NAME, ID + "=?",
                    new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }

    @Override
    public List<TaskEntity> getActualTasks() {
        List<TaskEntity> lista = this.getNotDoneTasks();
        List<TaskEntity> list = new ArrayList<TaskEntity>();
        Calendar c = Calendar.getInstance();
        for (TaskEntity ent : lista) {
            Calendar tmp = Calendar.getInstance();
            String dtime[] = ent.getData().split(" ");
            DateUtil.parseDate(tmp, dtime);
            if (c.before(tmp))
                list.add(ent);
        }
        return list;
    }

    @Override
    public int makeDone(String id) throws DbxException {
        TaskEntity ent = this.getTask(id);
        ent.setStatus(DONE);
        return this.updateTask(ent);

    }

    @Override
    public int makeNotDone(String id) throws DbxException {
        TaskEntity ent = this.getTask(id);
        ent.setStatus(NOT_DONE);
        return this.updateTask(ent);
    }

    @Override
    public List<TaskEntity> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<TaskEntity> list = new ArrayList<TaskEntity>();
        String sql = "select * from " + TABLE_NAME;
        try {
            Cursor cur = db.rawQuery(sql, null);
            if (cur.moveToFirst()) {
                do {
                    TaskEntity ent = new TaskEntity();
                    convertToEntity(String.valueOf(cur.getLong(cur.getColumnIndex(ID))), cur, ent);
                    list.add(ent);
                } while (cur.moveToNext());
            }
            return list;
        } finally {
            db.close();
        }
    }

    private void putTaskInContent(TaskEntity task, ContentValues content) {
        content.put(TASK_ADDRESS, task.getLocalistationAddress());
        content.put(TASK_DATE, task.getData());
        content.put(TASK_LATITUDE, task.getLatitude().getBytes());
        content.put(TASK_LONGITUDE, task.getLongitude().getBytes());
        content.put(TASK_NOTE, task.getNote());
        content.put(TASK_PRIORITY, task.getPriority());
        content.put(TASK_STATUS, task.getStatus());
    }

    private void convertToEntity(String id, Cursor cur, TaskEntity ent) {
        ent.setId(id);
        ent.setLatitude(new String(cur.getBlob(cur
                .getColumnIndex(TASK_LATITUDE))));
        ent.setLongitude(new String(cur.getBlob(cur
                .getColumnIndex(TASK_LONGITUDE))));
        ent.setLocalistationAddress(cur.getString(cur
                .getColumnIndex(TASK_ADDRESS)));
        ent.setData(cur.getString(cur.getColumnIndex(TASK_DATE)));
        ent.setNote(cur.getString(cur.getColumnIndex(TASK_NOTE)));
        ent.setPriority(cur.getString(cur.getColumnIndex(TASK_PRIORITY)));
        ent.setStatus(cur.getString(cur.getColumnIndex(TASK_STATUS)));
    }

    private void populateTaskEntityFromCursor(Cursor cur, TaskEntity ent) {
        ent.setId(String.valueOf(cur.getLong(cur.getColumnIndex(ID))));
        ent.setLatitude(cur.getString(cur
                .getColumnIndex(TASK_LATITUDE)));
        ent.setLatitude(new String(cur.getBlob(cur
                .getColumnIndex(TASK_LATITUDE))));
        ent.setLongitude(new String(cur.getBlob(cur
                .getColumnIndex(TASK_LONGITUDE))));
        ent.setData(cur.getString(cur.getColumnIndex(TASK_DATE)));
        ent.setNote(cur.getString(cur.getColumnIndex(TASK_NOTE)));
        ent.setPriority(cur.getString(cur
                .getColumnIndex(TASK_PRIORITY)));
        ent.setStatus(cur.getString(cur.getColumnIndex(TASK_STATUS)));
    }

}
