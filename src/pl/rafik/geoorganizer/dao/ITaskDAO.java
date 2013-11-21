package pl.rafik.geoorganizer.dao;

import java.util.List;

import com.dropbox.sync.android.DbxException;
import pl.rafik.geoorganizer.model.dto.GeoLocalisation;
import pl.rafik.geoorganizer.model.entity.TaskEntity;

public interface ITaskDAO {

	/**
	 * Metoda dodaje task.
	 * 
	 * @param task
	 * @return
	 */
	public Long addTask(TaskEntity task) throws DbxException;

	/**
	 * Metoda zwraca wszystkie taski z bazy.
	 * 
	 * @return
	 */
	public List<TaskEntity> getAllTasks() throws DbxException;

	/**
	 * Metoda zwraca task o podanym identyfikatorze.
	 * 
	 * @param id
	 * @return
	 */
	public TaskEntity getTask(Long id) throws DbxException;

	/**
	 * Metoda pobiera liste taskow zwiazanych z dana lokalizacja.
	 * 
	 * @param localisation
	 * @return
	 */
	public List<TaskEntity> getTasks(GeoLocalisation localisation);

	/**
	 * Metoda pobiera liste zadan do wykonania ktorych dedline jest przyszly.
	 * 
	 * @return
	 */
	public List<TaskEntity> getFutureTasks();

	/**
	 * Metoda pobiera przeszle zadania gdzie dedline ich ulegl juz
	 * przedawnieniu.
	 * 
	 * @return
	 */
	public List<TaskEntity> getPastTasks();

	/**
	 * Metoda pobiera taski ktore nie zostalywykonane.
	 * 
	 * @return
	 */
	public List<TaskEntity> getNotDoneTasks();

	/**
	 * Metoda pobiera wykonane zadania.
	 * 
	 * @return
	 */
	public List<TaskEntity> getDoneTasks();

	/**
	 * Metoda aktualizujaca wydarzenie.
	 * 
	 * @param ent
	 * @return
	 */
	public int updateTask(TaskEntity ent);

	/**
	 * Metoda usuwajaca rekord bazy.
	 * 
	 * @param id
	 * @return
	 */
	public int deleteTask(Long id);

	/**
	 * Metoda pobiera task zgodny z lokalizacja.
	 * 
	 * @param localisation
	 * @return
	 */
	public TaskEntity getTask(GeoLocalisation localisation);

	/**
	 * Zwraca liste aktualnych zdan, not done i future.
	 * 
	 * @return
	 */
	public List<TaskEntity> getActualTasks();

	/**
	 * Zmienia status zadania na wykonany.
	 * 
	 * @param id
	 * @return
	 */
	public int makeDone(Long id);

	/**
	 * Zmienia status zadania na nie wykonane.
	 * 
	 * @param id
	 * @return
	 */
	public int makeNotDone(Long id);
}
