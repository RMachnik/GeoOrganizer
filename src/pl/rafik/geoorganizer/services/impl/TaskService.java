package pl.rafik.geoorganizer.services.impl;

import android.app.Application;
import android.content.Context;
import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.dao.DbxTaskDAO;
import pl.rafik.geoorganizer.dao.ITaskDAO;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.model.entity.TaskEntity;
import pl.rafik.geoorganizer.services.IProximityAlertService;
import pl.rafik.geoorganizer.services.ITaskService;

import java.util.ArrayList;
import java.util.List;

public class TaskService implements ITaskService {

    private ITaskDAO daoService;
    private IProximityAlertService proxiService;

    public TaskService(Context context) {
        daoService = new DbxTaskDAO(context);
        proxiService = new ProximityAlertService(context);
    }

    @Override
    public String addNewTask(TaskDTO dto) throws DbxException {
        TaskEntity ent = new TaskEntity();
        ent.setData(dto.getDate());
        ent.setLatitude(dto.getLocalisation().getLatitude());
        ent.setLongitude(dto.getLocalisation().getLongitude());
        ent.setNote(dto.getNote());
        ent.setPriority(dto.getPriority());
        ent.setStatus(dto.getStatus());
        ent.setLocalistationAddress(dto.getLocalisation()
                .getLocalistationAddress());

        return daoService.addTask(ent);
    }

    @Override
    public TaskDTO getTask(String id) throws DbxException {
        TaskEntity ent = new TaskEntity();
        ent = daoService.getTask(String.valueOf(id));
        TaskDTO dto = new TaskDTO();
        dto.setId(String.valueOf(id));
        dto.setDate(ent.getData());
        GeoLocalisation geo = new GeoLocalisation();
        geo.setLatitude(ent.getLatitude());
        geo.setLongitude(ent.getLongitude());
        geo.setLocalistationAddress(ent.getLocalistationAddress());
        dto.setLocalisation(geo);
        dto.setNote(ent.getNote());
        dto.setPriority(ent.getPriority());
        dto.setStatus(ent.getStatus());

        return dto;
    }

    @Override
    public List<TaskDTO> getTasks(GeoLocalisation point) throws DbxException {
        List<TaskDTO> dtoList = new ArrayList<TaskDTO>();
        List<TaskEntity> entList = new ArrayList<TaskEntity>();
        entList = daoService.getTasks(point);
        for (TaskEntity ent : entList) {
            TaskDTO dto = new TaskDTO();
            dto.setId(ent.getId());
            dto.setDate(ent.getData());
            GeoLocalisation geo = new GeoLocalisation();
            geo.setLatitude(ent.getLatitude());
            geo.setLongitude(ent.getLongitude());
            geo.setLocalistationAddress(ent.getLocalistationAddress());
            dto.setLocalisation(geo);
            dto.setNote(ent.getNote());
            dto.setPriority(ent.getPriority());
            dto.setStatus(ent.getStatus());
            dtoList.add(dto);

        }
        return dtoList;
    }

    @Override
    public List<TaskDTO> getFutureTasks() {
        List<TaskDTO> dtoList = new ArrayList<TaskDTO>();
        List<TaskEntity> entList = new ArrayList<TaskEntity>();
        entList = daoService.getFutureTasks();
        for (TaskEntity ent : entList) {
            TaskDTO dto = new TaskDTO();
            dto.setId(ent.getId());
            dto.setDate(ent.getData());
            GeoLocalisation geo = new GeoLocalisation();
            geo.setLatitude(ent.getLatitude());
            geo.setLongitude(ent.getLongitude());
            geo.setLocalistationAddress(ent.getLocalistationAddress());
            dto.setLocalisation(geo);
            dto.setNote(ent.getNote());
            dto.setPriority(ent.getPriority());
            dto.setStatus(ent.getStatus());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<TaskDTO> getNotDoneTasks() {
        List<TaskDTO> dtoList = new ArrayList<TaskDTO>();
        List<TaskEntity> entList = new ArrayList<TaskEntity>();
        entList = daoService.getNotDoneTasks();
        for (TaskEntity ent : entList) {
            TaskDTO dto = new TaskDTO();
            dto.setId(ent.getId());
            dto.setDate(ent.getData());
            GeoLocalisation geo = new GeoLocalisation();
            geo.setLatitude(ent.getLatitude());
            geo.setLongitude(ent.getLongitude());
            geo.setLocalistationAddress(ent.getLocalistationAddress());
            dto.setLocalisation(geo);
            dto.setNote(ent.getNote());
            dto.setPriority(ent.getPriority());
            dto.setStatus(ent.getStatus());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<TaskDTO> getDoneTasks() {
        List<TaskDTO> dtoList = new ArrayList<TaskDTO>();
        List<TaskEntity> entList = new ArrayList<TaskEntity>();
        entList = daoService.getDoneTasks();
        for (TaskEntity ent : entList) {
            TaskDTO dto = new TaskDTO();
            dto.setId(ent.getId());
            dto.setDate(ent.getData());
            GeoLocalisation geo = new GeoLocalisation();
            geo.setLatitude(ent.getLatitude());
            geo.setLongitude(ent.getLongitude());
            geo.setLocalistationAddress(ent.getLocalistationAddress());
            dto.setLocalisation(geo);
            dto.setNote(ent.getNote());
            dto.setPriority(ent.getPriority());
            dto.setStatus(ent.getStatus());
            dtoList.add(dto);

        }
        return dtoList;
    }

    @Override
    public List<TaskDTO> getPastTasks() {
        List<TaskDTO> dtoList = new ArrayList<TaskDTO>();
        List<TaskEntity> entList = new ArrayList<TaskEntity>();
        entList = daoService.getPastTasks();
        for (TaskEntity ent : entList) {
            TaskDTO dto = new TaskDTO();
            dto.setId(ent.getId());
            dto.setDate(ent.getData());
            GeoLocalisation geo = new GeoLocalisation();
            geo.setLatitude(ent.getLatitude());
            geo.setLongitude(ent.getLongitude());
            geo.setLocalistationAddress(ent.getLocalistationAddress());
            dto.setLocalisation(geo);
            dto.setNote(ent.getNote());
            dto.setPriority(ent.getPriority());
            dto.setStatus(ent.getStatus());
            dtoList.add(dto);

        }
        return dtoList;
    }

    @Override
    public int updateTask(TaskDTO dto) {
        TaskEntity ent = new TaskEntity();
        ent.setData(dto.getDate());
        ent.setLatitude(dto.getLocalisation().getLatitude());
        ent.setLongitude(dto.getLocalisation().getLongitude());
        ent.setNote(dto.getNote());
        ent.setPriority(dto.getPriority());
        ent.setStatus(dto.getStatus());
        ent.setLocalistationAddress(dto.getLocalisation()
                .getLocalistationAddress());
        ent.setId(dto.getId());
        return daoService.updateTask(ent);
    }

    @Override
    public int deleteTask(String id) throws DbxException {
        proxiService.removeAlert(this.getTask(String.valueOf(id)));
        return daoService.deleteTask(String.valueOf(id));
    }

    @Override
    public TaskDTO getTask(GeoLocalisation localisation) {
        TaskEntity ent = new TaskEntity();
        TaskDTO dto = new TaskDTO();
        ent = daoService.getTask(localisation);
        dto.setId(ent.getId());
        dto.setDate(ent.getData());
        GeoLocalisation geo = new GeoLocalisation();
        geo.setLatitude(ent.getLatitude());
        geo.setLongitude(ent.getLongitude());
        geo.setLocalistationAddress(ent.getLocalistationAddress());
        dto.setLocalisation(geo);
        dto.setNote(ent.getNote());
        dto.setPriority(ent.getPriority());
        dto.setStatus(ent.getStatus());
        return dto;

    }

    @Override
    public List<TaskDTO> getActualTasks() throws DbxException {
        List<TaskDTO> dtoList = new ArrayList<TaskDTO>();
        List<TaskEntity> entList = new ArrayList<TaskEntity>();
        entList = daoService.getActualTasks();
        for (TaskEntity ent : entList) {
            TaskDTO dto = new TaskDTO();
            dto.setId(ent.getId());
            dto.setDate(ent.getData());
            GeoLocalisation geo = new GeoLocalisation();
            geo.setLatitude(ent.getLatitude());
            geo.setLongitude(ent.getLongitude());
            geo.setLocalistationAddress(ent.getLocalistationAddress());
            dto.setLocalisation(geo);
            dto.setNote(ent.getNote());
            dto.setPriority(ent.getPriority());
            dto.setStatus(ent.getStatus());
            dtoList.add(dto);

        }
        return dtoList;
    }

    @Override
    public int makeDone(String id) throws DbxException {
        proxiService.removeAlert(this.getTask(id));
        return daoService.makeDone(id);
    }

    @Override
    public int makeNotDone(String id) throws DbxException {
        proxiService.addProximityAlert(this.getTask(id));
        return daoService.makeNotDone(id);
    }

    @Override
    public List<TaskDTO> getAllTasks() throws DbxException {
        List<TaskDTO> dtoList = new ArrayList<TaskDTO>();
        List<TaskEntity> entList = new ArrayList<TaskEntity>();
        entList = daoService.getAllTasks();
        for (TaskEntity ent : entList) {
            TaskDTO dto = new TaskDTO();
            dto.setId(ent.getId());
            dto.setDate(ent.getData());
            GeoLocalisation geo = new GeoLocalisation();
            geo.setLatitude(ent.getLatitude());
            geo.setLongitude(ent.getLongitude());
            geo.setLocalistationAddress(ent.getLocalistationAddress());
            dto.setLocalisation(geo);
            dto.setNote(ent.getNote());
            dto.setPriority(ent.getPriority());
            dto.setStatus(ent.getStatus());
            dtoList.add(dto);

        }
        return dtoList;
    }
}
