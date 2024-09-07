package pl.blackwaterapi.store.modes;

import org.bukkit.scheduler.BukkitRunnable;
import pl.blackwater.core.Core;
import pl.blackwaterapi.store.Callback;
import pl.blackwaterapi.store.Store;
import pl.blackwaterapi.utils.Logger;
import pl.blackwaterapi.utils.TimeUtil;
import pl.blackwaterapi.utils.Timming;

import java.sql.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreMySQL implements Store
{
    private String host;
    private String user;
    private String pass;
    private String name;
    private String prefix;
    private int port;
    private Connection conn;
    private long time;
    private Executor executor;
    private AtomicInteger ai;
    
    public StoreMySQL(String host, int port, String user, String pass, String name, String prefix) {
        super();
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.name = name;
        this.prefix = prefix;
        this.executor = Executors.newSingleThreadExecutor();
        this.time = System.currentTimeMillis();
        this.ai = new AtomicInteger();
        new BukkitRunnable() {
            public void run() {
                if (System.currentTimeMillis() - StoreMySQL.this.time > TimeUtil.SECOND.getTime(30)) {
                    StoreMySQL.this.update(false, "DO 1");
                }
            }
        }.runTaskTimerAsynchronously(Core.getPlugin(), 600L, 600L);
    }
    
    @Override
    public boolean connect() {
        Timming t = new Timming("MySQL ping").start();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Logger.info("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.name, this.user, this.pass);
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.name, this.user, this.pass);
            Logger.info("Connected to the MySQL server!", "Connection ping " + t.stop().getExecutingTime() + "ms!");
            return true;
        }
        catch (ClassNotFoundException e) {
            Logger.warning("JDBC driver not found!", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        catch (SQLException e2) {
            Logger.warning("Can not connect to a MySQL server!", "Error: " + e2.getMessage());
            e2.printStackTrace();
        }
        return false;
    }
    
    @Override
    public void update(boolean now, String update) {
        this.time = System.currentTimeMillis();
        Runnable r = () -> {
            try {
                StoreMySQL.this.conn.createStatement().executeUpdate(update.replace("%P%", StoreMySQL.this.prefix));
            }
            catch (SQLException e) {
                Logger.warning("An error occurred with given query '" + update.replace("%P%", StoreMySQL.this.prefix) + "'!", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        };
        if (now) {
            r.run();
        }
        else this.executor.execute(r);
    }
    
    @Override
    public ResultSet update(String update) {
        try {
            Statement statement = this.conn.createStatement();
            statement.executeUpdate(update.replace("%P%", this.prefix), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs;
            }
        }
        catch (SQLException e) {
            Logger.warning("An error occurred with given query '" + update.replace("%P%", this.prefix) + "'!", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public void disconnect() {
        if (this.conn != null) {
            try {
                this.conn.close();
            }
            catch (SQLException e) {
                Logger.warning("Can not close the connection to the MySQL server!", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void reconnect() {
        this.connect();
    }
    
    @Override
    public boolean isConnected() {
        try {
            return !this.conn.isClosed() || this.conn == null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public ResultSet query(String query) {
        try {
            return this.conn.createStatement().executeQuery(query.replace("%P%", this.prefix));
        }
        catch (SQLException e) {
            Logger.warning("An error occurred with given query '" + query.replace("%P%", this.prefix) + "'!", "Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void query(String query, Callback<ResultSet> cb) {
        new Thread(() -> {
            try {
                ResultSet rs = StoreMySQL.this.conn.createStatement().executeQuery(query.replace("%P%", StoreMySQL.this.prefix));
                cb.done(rs);
            }
            catch (SQLException e) {
                Logger.warning("An error occurred with given query '" + query.replace("%P%", StoreMySQL.this.prefix) + "'!", "Error: " + e.getMessage());
                cb.error(e);
                e.printStackTrace();
            }
        }, "MySQL Thread #" + this.ai.getAndIncrement()).start();
    }
    
    @Override
    public Connection getConnection() {
        return this.conn;
    }
}
