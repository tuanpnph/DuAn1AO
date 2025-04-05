    package util;

    import java.sql.Connection;
    import java.sql.DriverManager;

    public class DBConnection {
        private static Connection conn;

        public static Connection getConnection() {
            try {
                if (conn == null || conn.isClosed()) {
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    String dbUser = "sa";
                    String dbPass = "123456";
                    String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyBanAo;encrypt=true;trustServerCertificate=true;";
                    conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                    System.out.println("Kết nối thành công");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Kết nối thất bại");
            }
            return conn;
        }
    }
