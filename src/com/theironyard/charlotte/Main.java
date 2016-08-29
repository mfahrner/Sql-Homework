package com.theironyard.charlotte;

import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws SQLException {
	// write your code here
        Server.createWebServer().start();

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        Statement stmt = conn.createStatement();

        stmt.execute("CREATE TABLE IF NOT EXISTS restaurants (id IDENTITY, name VARCHAR, type VARCHAR, rating INT)");

        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();

                    return new ModelAndView(m, "home.html");
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-restaurant",
                ((request, response) -> {
                    String name = request.queryParams("name");
                    String type = request.queryParams("type");
                    int rating = Integer.valueOf(request.queryParams("rating"));

                    Restaurant.insertRestaurant(conn, name, type, rating);

                    response.redirect("/");
                    return "";
                })
        );
    }
}
