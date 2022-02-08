package com.chipset.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLiteDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);
    private static final HikariConfig CONFIG = new HikariConfig();
    private static final HikariDataSource DS;

    static {
        try {
            final File dbFile = new File("src/main/resources/data/members.db");

            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    LOGGER.info("Created new database file");
                } else {
                    LOGGER.info("Could not create database file");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        CONFIG.setJdbcUrl("jdbc:sqlite:src/main/resources/data/members.db");
        CONFIG.setConnectionTestQuery("SELECT 1");
        CONFIG.addDataSourceProperty("cachePrepStmts", "true");
        CONFIG.addDataSourceProperty("prepStmtCacheSize", 250);
        CONFIG.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        DS = new HikariDataSource(CONFIG);

        try (final Statement statement = getConnection().createStatement()) {
            // language = SQLite
            statement.execute("CREATE TABLE IF NOT EXISTS member_data(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "member_id TEXT NOT NULL," +
                    "guild_id TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "b_day TEXT DEFAULT NULL," +
                    "GE INTEGER DEFAULT 0 NOT NULL," +
                    "multiplier REAL DEFAULT 1 NOT NULL," +
                    "roles BLOB NOT NULL," +
                    "rank INTEGER DEFAULT 1 NOT NULL" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SQLiteDataSource() { }

    public static Connection getConnection() throws SQLException {
        return DS.getConnection();
    }

    private static void addMember(Guild guild, Member member) {
        PreparedStatement ps;

        try (Connection conn = SQLiteDataSource.getConnection() ) {
            // language = SQLite
            String stmt = "INSERT INTO member_data" +
                    "(member_id, guild_id, name, roles)" +
                    "VALUES " +
                    "(?, ?, ?, ?);";

            ps = conn.prepareStatement(stmt);

            ps.setString(1, member.getId());
            ps.setString(2, guild.getId());
            ps.setString(3, member.getUser().getName());
            ps.setString(4, member.getRoles().toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMember(Guild guild, Member member) {
        PreparedStatement ps;

        try (Connection conn = SQLiteDataSource.getConnection() ) {
            // check if member exists
            String stmt = "SELECT * FROM member_data WHERE member_id = ? AND guild_id = ?;";

            ps = conn.prepareStatement(stmt);
            ps.setString(1, member.getId());
            ps.setString(2, guild.getId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // language = SQLite
                stmt = "UPDATE member_data SET name = ?, roles = ? WHERE member_id = ? AND guild_id = ?;";

                ps = conn.prepareStatement(stmt);
                ps.setString(1, member.getUser().getName());
                ps.setString(2, member.getRoles().toString());
                //noinspection JpaQueryApiInspection
                ps.setString(3, member.getId());
                //noinspection JpaQueryApiInspection
                ps.setString(4, guild.getId());

                ps.executeUpdate();
            } else {
                addMember(guild, member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setBirthday(Guild guild, Member member, String birthday) {
        PreparedStatement ps;

        try (Connection conn = SQLiteDataSource.getConnection() ) {
            // language = SQLite
            String stmt = "UPDATE member_data SET b_day = ? WHERE member_id = ? AND guild_id = ?;";

            ps = conn.prepareStatement(stmt);
            ps.setString(1, birthday);
            ps.setString(2, member.getId());
            ps.setString(3, guild.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getInfo(Guild guild, Member member) {
        // TODO: what info should be shown?
    }

    public static void editRank(Guild guild, Member member, int rank) {
        PreparedStatement ps;

        try (Connection conn = SQLiteDataSource.getConnection() ) {
            // language = SQLite
            String stmt = "UPDATE member_data SET rank = ? WHERE member_id = ? AND guild_id = ?;";

            ps = conn.prepareStatement(stmt);
            ps.setInt(1, rank);
            ps.setString(2, member.getId());
            ps.setString(3, guild.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
