package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings(int anno, String shape) {
		String sql = "SELECT * FROM sighting WHERE shape=? AND YEAR(DATETIME)=?";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			st.setString(1, shape);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state").toLowerCase(), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public void loadAllStates(Map<String, State> mappaStati) {
		String sql = "SELECT DISTINCT * FROM state";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id").toLowerCase(), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				if(!mappaStati.containsKey(state.getId())) {
					mappaStati.put(state.getId(), state);
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<String> getAllShapes(int anno) {
		String sql = "SELECT DISTINCT shape FROM sighting WHERE YEAR(DATETIME)=?";
		List<String> result = new ArrayList<String>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(rs.getString(1));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Arco> getAllEdges(int anno, String shape, Map<String, State> mappaStati) {
		String sql = "SELECT s1.state, s2.state, COUNT(*) AS peso " + 
				"FROM sighting s1, sighting s2, neighbor " + 
				"WHERE s1.shape=s2.shape AND s1.shape=? AND  YEAR(s1.datetime)=YEAR(s2.datetime) AND YEAR(s1.datetime)=? " + 
				"AND s1.state=neighbor.state1 AND s2.state=neighbor.state2 AND neighbor.state1<neighbor.state2 " + 
				"GROUP BY s1.state, s2.state";
		List<Arco> result = new ArrayList<Arco>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, shape);
			st.setInt(2, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State s1 = mappaStati.get(rs.getString(1).toLowerCase());
				State s2 = mappaStati.get(rs.getString(2).toLowerCase());
				result.add(new Arco(s1, s2, rs.getInt(3)));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<String> tuttiGliStati() {
		String sql = "SELECT DISTINCT * FROM state";
		List<String> result = new ArrayList<String>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(rs.getString("id").toLowerCase());
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		return result;
	}
	
	public List<String> getStatiVicini(String partenza) {
		String sql = "SELECT DISTINCT state2 FROM neighbor WHERE state1=?";
		List<String> result = new ArrayList<String>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, partenza.toUpperCase());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(rs.getString(1).toLowerCase());
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		return result;
	}
	
	



}

