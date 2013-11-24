package pl.rafik.geoorganizer.services;

import java.util.List;

import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;
import pl.rafik.geoorganizer.model.dto.TaskDTO;

/**
 * Serwis umozliwiajacy manipulowanie na danych zwiazanych z wydarzeniem.
 * 
 * @author rafal.machnik
 * 
 */
public interface ITaskService {

	/**
	 * Metoda dodajaca nowego taska do bazy.
	 * 
	 * @param dto
	 * @return
	 */
	public String addNewTask(TaskDTO dto) throws DbxException;

	/**
	 * Metoda zwraca wszystkie taski z bazy.
	 * 
	 * @return
	 */
	public List<TaskDTO> getAllTasks() throws DbxException;

	/**
	 * Metoda zwracajaca taska o podanym ID.
	 * 
	 * @param id
	 * @return
	 */
	public TaskDTO getTask(String id) throws DbxException;

	/**
	 * Metoda pobiera task zwiazany z danym punktem lokalizacyjnym.
	 * 
	 * @param point
	 * @return
	 */
	public List<TaskDTO> getTasks(GeoLocalisation point) throws DbxException;

	/**
	 * Metoda pobierajaca taski ktore nalezy wykonac i ich dedline jest czasem
	 * przyszlym.
	 * 
	 * @return
	 */
	public List<TaskDTO> getFutureTasks() throws DbxException;

	/**
	 * Pobiera zdania ktorych status oznaczony jest jako NOT DONE.
	 * 
	 * @return
	 */
	public List<TaskDTO> getNotDoneTasks() throws DbxException;

	/**
	 * Pobiera zadania wykonane status oznaczony jest jako DONE
	 * 
	 * @return
	 */
	public List<TaskDTO> getDoneTasks() throws DbxException;

	/**
	 * Pobiera zadania z przeszla data waznosci.
	 * 
	 * @return
	 */
	public List<TaskDTO> getPastTasks() throws DbxException;

	/**
	 * Metoda aktualizujaca rekrod bazy danych.
	 * 
	 * @param dto
	 * @return zwraca liczbe zaktualizowanych zdarzen
	 */
	public int updateTask(TaskDTO dto) throws DbxException;

	/**
	 * Metoda usuwajaca dane wydarzenie.
	 * 
	 * @param id
	 * @return zwraca liczbe usunietych wierszy
	 */
	public int deleteTask(String id) throws DbxException;

	/**
	 * Metoda pobiera task zgodny z lokalizacja.
	 * 
	 * @param localisation
	 * @return
	 */
	public TaskDTO getTask(GeoLocalisation localisation) throws DbxException;

	/**
	 * Zwraca liste niewykonanych aktualnych zadan.
	 * 
	 * @return
	 */
	public List<TaskDTO> getActualTasks() throws DbxException;

	/**
	 * Zmienia status zadania na wykonany.
	 * 
	 * @param id
	 * @return
	 */
	public int makeDone(String id) throws DbxException;

	/**
	 * Zmienia status zdania na niewykonane.
	 * 
	 * @param id
	 * @return
	 */
	public int makeNotDone(String id) throws DbxException;
}
