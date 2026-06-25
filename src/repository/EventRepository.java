package repository;

import model.Event;
import model.Concert;
import model.Seminar;
import model.SportMatch;
import database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EventRepository {

    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";

        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Event e = mapRow(rs);
                if (e != null)
                    events.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error findAll events: " + e.getMessage());
        }
        return events;
    }

    public Event findById(String id) {
        String sql = "SELECT * FROM events WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findById event: " + e.getMessage());
        }
        return null;
    }

    public List<Event> findByType(String type) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE type = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Event e = mapRow(rs);
                    if (e != null)
                        events.add(e);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findByType event: " + e.getMessage());
        }
        return events;
    }

    public List<Event> findByDateFrom(String dateFrom) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE date >= ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dateFrom);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Event e = mapRow(rs);
                    if (e != null)
                        events.add(e);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findByDateFrom events: " + e.getMessage());
        }
        return events;
    }

    public boolean save(Event event) {
        String sql = "INSERT INTO events (id, type, name, venue_id, organizer_id, "
                + "date, base_price, artist, speaker, team, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            setEventParams(ps, event);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error save event: " + e.getMessage());
            return false;
        }
    }

    public boolean save(Event event, Connection conn) throws SQLException {
        String sql = "INSERT INTO events (id, type, name, venue_id, organizer_id, "
                + "date, base_price, artist, speaker, team, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setEventParams(ps, event);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Event event) {
        String sql = "UPDATE events SET name = ?, venue_id = ?, organizer_id = ?, "
                + "date = ?, base_price = ?, artist = ?, speaker = ?, team = ?, updated_at = ? "
                + "WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, event.getName());
            ps.setString(2, event.getVenueId());
            ps.setString(3, event.getOrganizerId());
            ps.setString(4, event.getDate());
            ps.setDouble(5, event.getBasePrice());
            ps.setNull(6, Types.VARCHAR);
            ps.setNull(7, Types.VARCHAR);
            ps.setNull(8, Types.VARCHAR);

            if (event instanceof Concert) {
                ps.setString(6, ((Concert) event).getArtist());
            } else if (event instanceof Seminar) {
                ps.setString(7, ((Seminar) event).getSpeaker());
            } else if (event instanceof SportMatch) {
                ps.setString(8, ((SportMatch) event).getTeam());
            }
            ps.setString(9, event.getUpdatedAt());
            ps.setString(10, event.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update event: " + e.getMessage());
            return false;
        }
    }

    public boolean existsByVenueAndDate(String venueId, String date) {
        String sql = "SELECT COUNT(*) FROM events WHERE venue_id = ? AND date = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, venueId);
            ps.setString(2, date);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error existsByVenueAndDate: " + e.getMessage());
        }
        return false;
    }

    public void saveCapacities(String eventId, List<Map<String, Object>> capacities) {
        String sql = "INSERT INTO capacities (event_id, category, total, filled) "
                + "VALUES (?, ?, ?, 0)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Map<String, Object> cap : capacities) {
                ps.setString(1, eventId);
                ps.setString(2, (String) cap.get("category"));
                ps.setInt(3, (int) cap.get("total"));
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            System.err.println("Error saveCapacities: " + e.getMessage());
        }
    }

    public void saveCapacities(String eventId, List<Map<String, Object>> capacities,
            Connection conn) throws SQLException {
        String sql = "INSERT INTO capacities (event_id, category, total, filled) "
                + "VALUES (?, ?, ?, 0)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Map<String, Object> cap : capacities) {
                ps.setString(1, eventId);
                ps.setString(2, (String) cap.get("category"));
                ps.setInt(3, (int) cap.get("total"));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public List<Map<String, Object>> findCapacitiesByEventId(String eventId) {
        List<Map<String, Object>> capacities = new ArrayList<>();
        String sql = "SELECT * FROM capacities WHERE event_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> cap = new HashMap<>();
                    cap.put("id", rs.getInt("id"));
                    cap.put("event_id", rs.getString("event_id"));
                    cap.put("category", rs.getString("category"));
                    cap.put("total", rs.getInt("total"));
                    cap.put("filled", rs.getInt("filled"));
                    capacities.add(cap);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findCapacitiesByEventId: " + e.getMessage());
        }
        return capacities;
    }

    public int getRemainingCapacity(String eventId, String category) {
        String sql = "SELECT (total - filled) AS remaining FROM capacities "
                + "WHERE event_id = ? AND category = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eventId);
            ps.setString(2, category);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("remaining");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getRemainingCapacity: " + e.getMessage());
        }
        return 0;
    }

    public boolean incrementFilled(String eventId, String category, int quantity) {
        String sql = "UPDATE capacities SET filled = filled + ? "
                + "WHERE event_id = ? AND category = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, eventId);
            ps.setString(3, category);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error incrementFilled: " + e.getMessage());
            return false;
        }
    }

    public boolean incrementFilled(String eventId, String category, int quantity,
            Connection conn) throws SQLException {
        String sql = "UPDATE capacities SET filled = filled + ? "
                + "WHERE event_id = ? AND category = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, eventId);
            ps.setString(3, category);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean decrementFilled(String eventId, String category, int quantity) {
        String sql = "UPDATE capacities SET filled = filled - ? "
                + "WHERE event_id = ? AND category = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, eventId);
            ps.setString(3, category);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error decrementFilled: " + e.getMessage());
            return false;
        }
    }

    public boolean decrementFilled(String eventId, String category, int quantity,
            Connection conn) throws SQLException {
        String sql = "UPDATE capacities SET filled = filled - ? "
                + "WHERE event_id = ? AND category = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, eventId);
            ps.setString(3, category);
            return ps.executeUpdate() > 0;
        }
    }

    public String generateId() {
        String sql = "SELECT id FROM events ORDER BY id DESC LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("id");
                int num = Integer.parseInt(lastId.split("-")[1]);
                return String.format("EVT-%03d", num + 1);
            }
        } catch (SQLException e) {
            System.err.println("Error generateId event: " + e.getMessage());
        }
        return "EVT-001";
    }

    private void setEventParams(PreparedStatement ps, Event event) throws SQLException {
        ps.setString(1, event.getId());
        ps.setString(2, event.getEventType());
        ps.setString(3, event.getName());
        ps.setString(4, event.getVenueId());
        ps.setString(5, event.getOrganizerId());
        ps.setString(6, event.getDate());
        ps.setDouble(7, event.getBasePrice());
        ps.setNull(8, Types.VARCHAR);
        ps.setNull(9, Types.VARCHAR);
        ps.setNull(10, Types.VARCHAR);
        if (event instanceof Concert) {
            ps.setString(8, ((Concert) event).getArtist());
        } else if (event instanceof Seminar) {
            ps.setString(9, ((Seminar) event).getSpeaker());
        } else if (event instanceof SportMatch) {
            ps.setString(10, ((SportMatch) event).getTeam());
        }
        ps.setString(11, event.getCreatedAt());
        ps.setString(12, event.getUpdatedAt());
    }

    private Event mapRow(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String type = rs.getString("type");
        String name = rs.getString("name");
        String venueId = rs.getString("venue_id");
        String organizerId = rs.getString("organizer_id");
        String date = rs.getString("date");
        double basePrice = rs.getDouble("base_price");
        String createdAt = rs.getString("created_at");
        String updatedAt = rs.getString("updated_at");

        switch (type) {
            case "concert":
                String artist = rs.getString("artist");
                return new Concert(id, name, venueId, organizerId,
                        date, basePrice, createdAt, artist, updatedAt);
            case "seminar":
                String speaker = rs.getString("speaker");
                return new Seminar(id, name, venueId, organizerId,
                        date, basePrice, createdAt, speaker, updatedAt);
            case "sport_match":
                String team = rs.getString("team");
                return new SportMatch(id, name, venueId, organizerId,
                        date, basePrice, createdAt, team, updatedAt);
            default:
                System.err.println("Tipe event tidak dikenal: " + type);
                return null;
        }
    }

    public Map<String, Integer> getRemainingCapacity(String eventId) {

        Map<String, Integer> capacities = new LinkedHashMap<>();

        String sql = "SELECT category, total, filled "
                + "FROM capacities "
                + "WHERE event_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eventId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String category = rs.getString("category");

                int total = rs.getInt("total");

                int filled = rs.getInt("filled");

                capacities.put(
                        category.toLowerCase(),
                        total - filled);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return capacities;
    }
}