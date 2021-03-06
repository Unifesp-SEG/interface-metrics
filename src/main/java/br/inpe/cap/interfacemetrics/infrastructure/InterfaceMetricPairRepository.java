package br.inpe.cap.interfacemetrics.infrastructure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.inpe.cap.interfacemetrics.domain.InterfaceMetric;
import br.inpe.cap.interfacemetrics.domain.InterfaceMetricPair;
import br.inpe.cap.interfacemetrics.domain.OccurrencesCombination;

public class InterfaceMetricPairRepository extends BaseRepository {

	private String table = "interface_metrics_pairs";

	public InterfaceMetricPairRepository(RepositoryType repositoryType) {
		table += repositoryType.getSufix();
	}

	public void deleteTable() throws Exception {
		String sql = "DELETE from " + table;

		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sql);

		stmt.close();
		conn.close();
	}

	public void deletePairs(InterfaceMetric interfaceMetric) throws Exception {
		String sql = "DELETE from " + table + " where interface_metrics_a = " + interfaceMetric.getId();

		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sql);

		stmt.close();
		conn.close();
	}

	public void insertPairs(InterfaceMetric a, OccurrencesCombination combination, List<InterfaceMetric> occurrences) throws Exception {
		
		if(occurrences.isEmpty())
			return;
		
		Connection conn = getConnection();
		PreparedStatement ps = null;

		for(InterfaceMetric b : occurrences){
			String sql = "INSERT into " + table + " (interface_metrics_a, interface_metrics_b, search_type) values (?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, a.getId());
			ps.setLong(2, b.getId());
			ps.setString(3, combination.getName());
			ps.executeUpdate();
		}

		ps.close();
		conn.close();
	}

	public List<InterfaceMetricPair> getPairs(InterfaceMetric interfaceMetric) throws Exception {
		return this.getPairs(interfaceMetric, null);
	}
	
	public List<InterfaceMetricPair> getPairs(InterfaceMetric interfaceMetric, OccurrencesCombination combination) throws Exception {
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();

		String sql = "SELECT * FROM " + table + " where interface_metrics_a = " + interfaceMetric.getId();
		if(combination != null)
			sql += " AND search_type = '" + combination.getName() + "'";
		ResultSet rs = stmt.executeQuery(sql);

		List<InterfaceMetricPair> list = new ArrayList<InterfaceMetricPair>();

		while (rs.next()) {
			InterfaceMetricPair interfaceMetricPair = new InterfaceMetricPair(rs);
			list.add(interfaceMetricPair);
		}

		stmt.close();
		conn.close();

		return list;
	}
	
	public int countAll() throws Exception {
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();

		String sql = "SELECT count(*) as total FROM " + table;
		ResultSet rs = stmt.executeQuery(sql);

		int total = 0;
		while (rs.next()) {
			total = rs.getInt("total");
		}

		stmt.close();
		conn.close();

		return total;
	}
	
	public int countAllByCombination(OccurrencesCombination combination) throws Exception {
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();

		String sql = "SELECT count(*) as total FROM " + table + " WHERE search_type = '" + combination.getName() + "'";
		ResultSet rs = stmt.executeQuery(sql);

		int total = 0;
		while (rs.next()) {
			total = rs.getInt("total");
		}

		stmt.close();
		conn.close();

		return total;
	}
}
