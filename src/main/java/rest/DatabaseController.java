package rest;

import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.Database;
import database.DatabaseReader;
import database.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {

    private Database database;

    public DatabaseController() {
        try {
            database = new DatabaseReader("test.json").read();
        } catch (Exception e) {
            database = new Database("test.json");
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    public Result tables() {
        return database.query("list tables");
    }

    @RequestMapping(value = "/drop_table", method = RequestMethod.DELETE)
    public Result dropTable(@RequestParam(name = "table") String tableName) {
        return database.query(String.format("drop table", tableName));
    }

    @RequestMapping(value = "/create_table", method = RequestMethod.POST)
    public Result createTable(@RequestParam(name = "columns") String columns, @RequestParam(name = "table") String tableName) {
        return database.query(String.format("create table %s (%s)", tableName, columns));
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Result createTable(@RequestParam(name = "columns") String columns, @RequestParam(name = "table") String tableName, @RequestParam(name = "condition", defaultValue = "") String condition) {
        String query = String.format("select %s from %s", columns, tableName, columns);
        if (!condition.isEmpty()) query += String.format(" where %s", condition);
        return database.query(query);
    }
}