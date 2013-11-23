package pl.rafik.geoorganizer.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.dao.ITaskDAO;
import pl.rafik.geoorganizer.dao.TaskDAO;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.model.entity.TaskEntity;
import android.content.Context;
import pl.rafik.geoorganizer.services.IProximityAlertService;
import pl.rafik.geoorganizer.services.ITaskService;

public class TaskService implements ITaskService {

	private ITaskDAO daoService;
	private IProximityAlertService proxiService;

	public TaskService(Context context) {
		daoService = new TaskDAO(context);
		proxiService = new ProximityAlertService(context);
	}

	@Override
	public Long addNewTask(TaskDTO dto) throws DbxException {
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
	public TaskDTO getTask(Long id) throws DbxException {
		TaskEntity ent = new TaskEntity();
		ent = daoService.getTask(id);
		TaskDTO dto = new TaskDTO();
		dto.setId(id);
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
	public List<TaskDTO> getTasks(GeoLocalisation point) {
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
	public int deleteTask(Long id) throws DbxException {
		proxiService.removeAlert(this.getTask(id));
		return daoService.deleteTask(id);
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
	public List<TaskDTO> getActualTasks() {
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
	public int makeDone(Long id) throws DbxException {
		proxiService.removeAlert(this.getTask(id));
		return daoService.makeDone(id);
	}

	@Override
	public int makeNotDone(Long id) throws DbxException {
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
