package pl.rafik.geoorganizer.services;

import java.util.ArrayList;
import java.util.List;

import pl.rafik.geoorganizer.model.dto.TaskDTO;
import android.content.Context;

public interface IProximityAlertService {

	
	/**
	 * Metoda dodajaca alert.
	 * 
	 * @return
	 */
	public boolean addProximityAlert(TaskDTO dto);

	/**
	 * Metoda dodajaca alerty z listy podanych Taskow.
	 * 
	 * @param caller
	 * @param reminders
	 * @return
	 */
	public boolean addProximityAlerts(Context caller,
			ArrayList<TaskDTO> reminders);

	/**
	 * Metoda usuwajaca alert.
	 * 
	 * @param reminder
	 */

	public void removeAlert(TaskDTO reminder);

	/**
	 * Metoda oblicza czas jaki ma byc wazne wydazrenie.
	 * 
	 * @param dto
	 */
	public long computeExpirationTime(TaskDTO dto);
	
	/**
	 * Metoda aktualizujaca proximity alerty po zmianie ustawien.
	 * @param list
	 */
	public void actualizeProximities(List<TaskDTO> list);
}
