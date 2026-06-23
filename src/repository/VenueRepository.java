package repository;
import model.Venue;
import database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueRepository {
    
    public List<Venue> findAll() {
        List<Venue> venues = new ArrayList<>();
        String sql = "SELECT * FROM venues";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                venues.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error findAll venues: " + e.getMessage());
        }
        return venues;
    }

    public Venue findById(String id) {
        String sql = "SELECT * FROM venues WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findById venue: " + e.getMessage());
        }
        return null;
    }

    public boolean save(Venue venue) {
        String sql = "INSERT INTO venues (id, name, address, max_capacity, created_at) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, venue.getId());
            ps.setString(2, venue.getName());
            ps.setString(3, venue.getAddress());
            ps.setInt(4, venue.getMaxCapacity());
            ps.setString(5, venue.getCreatedAt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error save venue: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Venue venue) {
        String sql = "UPDATE venues SET name = ?, address = ?, max_capacity = ? "
                   + "WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, venue.getName());
            ps.setString(2, venue.getAddress());
            ps.setInt(3, venue.getMaxCapacity());
            ps.setString(4, venue.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update venue: " + e.getMessage());
            return false;
        }
    }

    public String generateId() {
        String sql = "SELECT id FROM venues ORDER BY id DESC LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("id");
                int num = Integer.parseInt(lastId.split("-")[1]);
                return String.format("VNU-%03d", num + 1);
            }
        } catch (SQLException e) {
            System.err.println("Error generateId venue: " + e.getMessage());
        }
        return "VNU-001";
    }

    private Venue mapRow(ResultSet rs) throws SQLException {
        return new Venue(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("address"),
            rs.getInt("max_capacity"), 
            rs.getString("created_at")
        );
    }
}