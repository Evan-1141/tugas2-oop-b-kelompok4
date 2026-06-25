package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manager untuk koneksi database SQLite.
 * 
 * Database disimpan sebagai file "database.db" di root project.
 * File ini akan dibuat otomatis jika belum ada.
 * 
 * Contoh penggunaan di Repository:
 * 
 * try (Connection conn = DatabaseManager.getConnection()) {
 * PreparedStatement ps = conn.prepareStatement("SELECT * FROM events WHERE id =
 * ?");
 * ps.setString(1, eventId);
 * ResultSet rs = ps.executeQuery();
 * while (rs.next()) {
 * String nama = rs.getString("nama");
 * double harga = rs.getDouble("harga_dasar");
 * // ...
 * }
 * }
 */
public class DatabaseManager {

        private static final String DB_URL = "jdbc:sqlite:database.db";

        /**
         * Mendapatkan koneksi baru ke database SQLite.
         * 
         * PENTING: Selalu gunakan try-with-resources agar koneksi otomatis ditutup!
         * 
         * try (Connection conn = DatabaseManager.getConnection()) {
         * // gunakan conn disini ...
         * } // conn otomatis ditutup disini
         */
        public static Connection getConnection() throws SQLException {
                Connection conn = DriverManager.getConnection(DB_URL);
                // Aktifkan foreign key support (di SQLite ini harus diaktifkan per koneksi)
                try (Statement stmt = conn.createStatement()) {
                        stmt.execute("PRAGMA foreign_keys = ON");
                }
                return conn;
        }

        /**
         * Inisialisasi database: buat semua tabel jika belum ada.
         * Dipanggil sekali saat server pertama kali dijalankan.
         * 
         * Tambahkan CREATE TABLE IF NOT EXISTS untuk setiap tabel yang dibutuhkan.
         */
        public static void initialize() {
                try (Connection conn = getConnection();
                                Statement stmt = conn.createStatement()) {

                        // =============================================
                        // BUAT TABEL-TABEL KALIAN DISINI
                        // Gunakan CREATE TABLE IF NOT EXISTS agar aman dijalankan berulang.
                        // =============================================
                        stmt.execute(
                                        "CREATE TABLE IF NOT EXISTS users ("
                                                        + "  id TEXT PRIMARY KEY,"
                                                        + "  name TEXT NOT NULL,"
                                                        + "  email TEXT NOT NULL UNIQUE,"
                                                        + "  phone TEXT,"
                                                        + "  role TEXT DEFAULT 'buyer', "
                                                        + "  created_at TEXT DEFAULT (datetime('now')),"
                                                        + "  updated_at TEXT "
                                                        + ")");

                        stmt.execute(
                                        "CREATE TABLE IF NOT EXISTS venues ("
                                                        + "  id TEXT PRIMARY KEY,"
                                                        + "  name TEXT NOT NULL,"
                                                        + "  address TEXT NOT NULL,"
                                                        + "  max_capacity INTEGER NOT NULL,"
                                                        + "  created_at TEXT DEFAULT (datetime('now')),"
                                                        + "  updated_at TEXT "
                                                        + ")");

                        stmt.execute(
                                        "CREATE TABLE IF NOT EXISTS events ("
                                                        + "  id TEXT PRIMARY KEY,"
                                                        + "  type TEXT NOT NULL,"
                                                        + "  name TEXT NOT NULL,"
                                                        + "  venue_id TEXT NOT NULL,"
                                                        + "  organizer_id TEXT NOT NULL,"
                                                        + "  date TEXT NOT NULL,"
                                                        + "  base_price REAL NOT NULL,"
                                                        + "  artist TEXT,"
                                                        + "  speaker TEXT,"
                                                        + "  team TEXT,"
                                                        + "  created_at TEXT DEFAULT (datetime('now')),"
                                                        + "  updated_at TEXT, "
                                                        + "  FOREIGN KEY (venue_id) REFERENCES venues(id),"
                                                        + "  FOREIGN KEY (organizer_id) REFERENCES users(id)"
                                                        + ")");

                        stmt.execute(
                                        "CREATE TABLE IF NOT EXISTS capacities ("
                                                        + "  id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                        + "  event_id TEXT NOT NULL,"
                                                        + "  category TEXT NOT NULL,"
                                                        + "  total INTEGER NOT NULL,"
                                                        + "  filled INTEGER DEFAULT 0,"
                                                        + "  FOREIGN KEY (event_id) REFERENCES events(id)"
                                                        + ")");

                        stmt.execute(
                                        "CREATE TABLE IF NOT EXISTS tickets ("
                                                        + "  id TEXT PRIMARY KEY,"
                                                        + "  event_id TEXT NOT NULL,"
                                                        + "  user_id TEXT NOT NULL, "
                                                        + "  category TEXT NOT NULL,"
                                                        + "  quantity INTEGER NOT NULL,"
                                                        + "  unit_price REAL NOT NULL,"
                                                        + "  total_price REAL NOT NULL,"
                                                        + "  purchase_date TEXT DEFAULT (date('now')),"
                                                        + "  status TEXT DEFAULT 'active',"
                                                        + "  refund_amount REAL DEFAULT 0,"
                                                        + "  FOREIGN KEY (event_id) REFERENCES events(id),"
                                                        + "  FOREIGN KEY (user_id) REFERENCES users(id)"
                                                        + ")");

                        seedData(conn);

                        System.out.println("Database berhasil diinisialisasi.");

                } catch (SQLException e) {
                        System.err.println("Gagal inisialisasi database: " + e.getMessage());
                        e.printStackTrace();
                }
        }

        private static void seedData(Connection conn) throws SQLException {
                Statement stmt = conn.createStatement();

                // Data Dummy Users
                stmt.execute(
                                "INSERT OR IGNORE INTO users "
                                                + "(id, name, email, phone, role) "
                                                + "VALUES "
                                                + "('USR-001','Danendra Evan','evan@mail.com','081234567890','organizer')");

                stmt.execute(
                                "INSERT OR IGNORE INTO users "
                                                + "(id, name, email, phone, role) "
                                                + "VALUES "
                                                + "('USR-002','Gus Weda','gus@mail.com','081234567891','organizer')");

                stmt.execute(
                                "INSERT OR IGNORE INTO users "
                                                + "(id, name, email, phone, role) "
                                                + "VALUES "
                                                + "('USR-003','Aldo','aldo@mail.com','081234567892','buyer')");

                stmt.execute(
                                "INSERT OR IGNORE INTO users "
                                                + "(id, name, email, phone, role) "
                                                + "VALUES "
                                                + "('USR-004','Angga','angga@mail.com','081234567893','buyer')");

                // Data Dummy Venues
                stmt.execute(
                                "INSERT OR IGNORE INTO venues "
                                                + "(id, name, address, max_capacity) "
                                                + "VALUES "
                                                + "('VNU-001','BNDCC','Nusa Dua, Bali',5000)");

                stmt.execute(
                                "INSERT OR IGNORE INTO venues "
                                                + "(id, name, address, max_capacity) "
                                                + "VALUES "
                                                + "('VNU-002','Auditorium Udayana','Jimbaran, Bali',1000)");

                stmt.execute(
                                "INSERT OR IGNORE INTO venues "
                                                + "(id, name, address, max_capacity) "
                                                + "VALUES "
                                                + "('VNU-003','Lapangan Renon','Denpasar, Bali',10000)");

                // Data Dummy Events
                stmt.execute(
                                "INSERT OR IGNORE INTO events "
                                                + "(id, type, name, venue_id, organizer_id, date, base_price) "
                                                + "VALUES "
                                                + "('EVT-001','concert','Bali Music Festival 2026','VNU-001','USR-001','2027-08-15',250000)");

                stmt.execute(
                                "INSERT OR IGNORE INTO events "
                                                + "(id, type, name, venue_id, organizer_id, date, base_price) "
                                                + "VALUES "
                                                + "('EVT-002','seminar','AI Future Summit 2026','VNU-002','USR-002','2027-09-10',250000)");

                stmt.execute(
                                "INSERT OR IGNORE INTO events "
                                                + "(id, type, name, venue_id, organizer_id, date, base_price) "
                                                + "VALUES "
                                                + "('EVT-003','sport_match','Bali United vs Persija','VNU-003','USR-001','2027-10-15',100000)");

                // Data Dummy Tickets
                stmt.execute(
                                "INSERT OR IGNORE INTO tickets "
                                                + "(id, event_id, user_id, category, quantity, unit_price, total_price, purchase_date, status, refund_amount) "
                                                + "VALUES "
                                                + "('TKT-001','EVT-001','USR-003','VIP',2,750000,1500000,'2026-06-26','active',0)");

                stmt.execute(
                                "INSERT OR IGNORE INTO tickets "
                                                + "(id, event_id, user_id, category, quantity, unit_price, total_price, purchase_date, status, refund_amount) "
                                                + "VALUES "
                                                + "('TKT-002','EVT-002','USR-004','Regular',2,250000,500000,'2026-06-26','active',0)");
                stmt.execute(
                                "INSERT OR IGNORE INTO tickets "
                                                + "(id, event_id, user_id, category, quantity, unit_price, total_price, purchase_date, status, refund_amount) "
                                                + "VALUES "
                                                + "('TKT-003','EVT-003','USR-003','VIP',1,250000,250000,'2026-06-26','active',0)");

                // Data Dummy Capacities
                stmt.execute(
                                "INSERT OR IGNORE INTO capacities (event_id, category, total, filled) VALUES ('EVT-001','VIP',100,10)");

                stmt.execute(
                                "INSERT OR IGNORE INTO capacities (event_id, category, total, filled) VALUES ('EVT-001','Regular',500,50)");

                stmt.execute(
                                "INSERT OR IGNORE INTO capacities (event_id, category, total,filled) VALUES ('EVT-001','Festival',1000,100)");

                stmt.execute(
                                "INSERT OR IGNORE INTO capacities (event_id, category, total, filled) VALUES ('EVT-002','Regular',200,20)");

                stmt.execute(
                                "INSERT OR IGNORE INTO capacities (event_id, category, total, filled) VALUES ('EVT-003','Tribune',1000,100)");

                stmt.execute(
                                "INSERT OR IGNORE INTO capacities (event_id, category, total, filled) VALUES ('EVT-003','VIP',200,20)");

                stmt.execute(
                                "INSERT OR IGNORE INTO capacities (event_id, category, total, filled) VALUES ('EVT-003','VVIP',50,5)");

                System.out.println("Seed data berhasil dijalankan");
        }
}
