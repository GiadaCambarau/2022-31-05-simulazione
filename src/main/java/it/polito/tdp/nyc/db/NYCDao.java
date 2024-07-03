package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.nyc.model.City;
import it.polito.tdp.nyc.model.Hotspot;

public class NYCDao {
	
	public List<Hotspot> getAllHotspot(){
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		List<Hotspot> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	public List<String> getProviders(){
		String sql = "SELECT  distinct n.Provider "
				+ "FROM nyc_wifi_hotspot_locations n ";
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("Provider"))	;
				}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
	public List<String> getCity(String p){
		String sql = "SELECT  distinct n.City "
				+ "FROM nyc_wifi_hotspot_locations n "
				+ "WHERE n.Provider = ?";
		
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, p);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("City"));
				}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
	public List<City> getPosizione(String p){
		String sql = "SELECT n.City, AVG(n.Latitude) AS lat, AVG(n.Longitude) AS lng "
				+ "FROM nyc_wifi_hotspot_locations n "
				+ "WHERE n.Provider = ? "
				+ "GROUP BY n.City";
		List<City> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, p);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				City c = new City(res.getString("City"), res.getDouble("lat"), res.getDouble("lng"));
				result.add(c);
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
	public int getNHotspot(String p, City c) {
		String sql = "SELECT  COUNT(*) as n "
				+ "FROM nyc_wifi_hotspot_locations n "
				+ "WHERE n.Provider = ? AND n.City= ? ";
		int num =0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, p);
			ResultSet res = st.executeQuery();

			res.next();
			num = res.getInt("n");
			
			conn.close();
			return num;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
}
