package pl.rafik.geoorganizer.model.dto;

public class GeoLocalisation {

	private String latitude;
	private String longitude;
	private String localistationAddress;

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLocalistationAddress() {
		return localistationAddress;
	}

	public void setLocalistationAddress(String localistationAdress) {
		this.localistationAddress = localistationAdress;
	}

    @Override
    public String toString() {
        return "GeoLocalisation{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", localistationAddress='" + localistationAddress + '\'' +
                '}';
    }
}
