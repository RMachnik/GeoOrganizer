package pl.rafik.geoorganizer.services;


public interface ILocalisationService {

/**
 *  Metoda zwracajaca liste opisow adresowych podanej lokacji.
 * @param latitude
 * @param longitude
 */

	public void getAdress(double latitude, double longitude);

/**
 * Metoda zwracajaca liste adresow podanego obiektu po jego nazwie.
 * @param name
 */

	public void getAddresFromName(String name);

}
