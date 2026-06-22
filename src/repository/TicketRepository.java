package repository;
import model.Ticket;
import database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository {
    
    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error findAll tickets: " + e.getMessage());
        }
        return tickets;
    }

    public Ticket findById(String id) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findById ticket: " + e.getMessage());
        }
        return null;
    }

    public List<Ticket> findByEventId(String eventId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE event_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findByEventId tickets: " + e.getMessage());
        }
        return tickets;
    }

    public List<Ticket> findByUserId(String userId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findByUserId tickets: " + e.getMessage());
        }
        return tickets;
    }

    public List<Ticket> findByStatus(String status) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE status = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findByStatus tickets: " + e.getMessage());
        }
        return tickets;
    }

    public boolean save(Ticket ticket) {
        String sql = "INSERT INTO tickets (id, event_id, user_id, category, quantity, "
                   + "unit_price, total_price, purchase_date, status, refund_amount) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setTicketParams(ps, ticket);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error save ticket: " + e.getMessage());
            return false;
        }
    }

    public boolean save(Ticket ticket, Connection conn) throws SQLException {
        String sql = "INSERT INTO tickets (id, event_id, user_id, category, quantity, "
                   + "unit_price, total_price, purchase_date, status, refund_amount) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setTicketParams(ps, ticket);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Ticket ticket) {
        String sql = "UPDATE tickets SET event_id = ?, user_id = ?, category = ?, "
                   + "quantity = ?, unit_price = ?, total_price = ?, purchase_date = ?, "
                   + "status = ?, refund_amount = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticket.getEventId());
            ps.setString(2, ticket.getUserId());
            ps.setString(3, ticket.getCategory());
            ps.setInt(4, ticket.getQuantity());
            ps.setDouble(5, ticket.getUnitPrice());
            ps.setDouble(6, ticket.getTotalPrice());
            ps.setString(7, ticket.getPurchaseDate());
            ps.setString(8, ticket.getStatus());
            ps.setDouble(9, ticket.getRefundAmount());
            ps.setString(10, ticket.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update ticket: " + e.getMessage());
            return false;
        }
    }

    public boolean updateRefund(String ticketId, String status, double refundAmount) {
        String sql = "UPDATE tickets SET status = ?, refund_amount = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setDouble(2, refundAmount);
            ps.setString(3, ticketId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updateRefund ticket: " + e.getMessage());
            return false;
        }
    }

    public boolean updateRefund(String ticketId, String status, double refundAmount,
                                Connection conn) throws SQLException {
        String sql = "UPDATE tickets SET status = ?, refund_amount = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setDouble(2, refundAmount);
            ps.setString(3, ticketId);
            return ps.executeUpdate() > 0;
        }
    }

    public String generateId() {
        String sql = "SELECT id FROM tickets ORDER BY id DESC LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("id");
                int num = Integer.parseInt(lastId.split("-")[1]);
                return String.format("TKT-%03d", num + 1);
            }
        } catch (SQLException e) {
            System.err.println("Error generateId ticket: " + e.getMessage());
        }
        return "TKT-001";
    }

    private void setTicketParams(PreparedStatement ps, Ticket ticket) throws SQLException {
        ps.setString(1, ticket.getId());
        ps.setString(2, ticket.getEventId());
        ps.setString(3, ticket.getUserId());
        ps.setString(4, ticket.getCategory());
        ps.setInt(5, ticket.getQuantity());
        ps.setDouble(6, ticket.getUnitPrice());
        ps.setDouble(7, ticket.getTotalPrice());
        ps.setString(8, ticket.getPurchaseDate());
        ps.setString(9, ticket.getStatus());
        ps.setDouble(10, ticket.getRefundAmount());
    }

    private Ticket mapRow(ResultSet rs) throws SQLException {
        return new Ticket(
            rs.getString("id"),
            rs.getString("event_id"),
            rs.getString("user_id"),
            rs.getString("category"),
            rs.getInt("quantity"),
            rs.getDouble("unit_price"),
            rs.getDouble("total_price"),
            rs.getString("purchase_date"),
            rs.getString("status"),
            rs.getDouble("refund_amount")
        );
    }
}