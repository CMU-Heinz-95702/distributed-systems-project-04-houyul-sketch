package project4task2;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

import java.text.SimpleDateFormat;

import java.util.*;

/**
 * Contacts the university API, parses a result, and writes a log entry
 * into MongoDB. Also computes statistics for the dashboard.
 *
 * @author Houyu Lin
 * @andrewID houyul
 * @email houyul@andrew.cmu.edu
 */
public class GetUniversityModel {

    /* MongoDB connection string */
    private static final ConnectionString connStr =
            new ConnectionString(
                    "mongodb+srv://houyul_db_user:lhypro1119@" +
                            "cluster0.npfx3jo.mongodb.net/Project4Task2" +
                            "?retryWrites=true&w=majority&appName=Cluster0"

            );

    private static final MongoClientSettings settings =
            MongoClientSettings.builder()
                    .applyConnectionString(connStr)
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();

    /* Dashboard data */
    public static List<String> allLogs = new ArrayList<>();
    public static int totalRequests = 0;
    public static double avgResults = 0.0;
    public static String popularCountry = "";

    private static final Map<String, Integer> countryCounter = new HashMap<>();

    /**
     * Calls the API for a given country, returns one sample university,
     * and logs the query into MongoDB.
     */
    public static JSONObject query(String country) {

        String api = "http://universities.hipolabs.com/search?country=" + country;
        String response = fetch(api);

        if (response == null || response.isBlank()) return null;

        JSONArray arr = (JSONArray) JSONValue.parse(response);
        if (arr == null || arr.isEmpty()) return null;

        JSONObject one = (JSONObject) arr.get(0);
        int size = arr.size();

        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());

        writeLog(country, size, now);

        return one;
    }

    /**
     * Loads logs from MongoDB and computes summary statistics.
     */
    public static void loadDashboard() {

        MongoClient client = MongoClients.create(settings);
        MongoDatabase db = client.getDatabase("Project4Task2");
        MongoCollection<Document> table = db.getCollection("university_logs");

        allLogs.clear();
        countryCounter.clear();
        totalRequests = 0;
        avgResults = 0.0;

        double sum = 0;

        for (Document d : table.find()) {

            String country = d.getString("country");
            int count = d.getInteger("count", 0);
            String time = d.getString("time");

            String entry = country + " | " + count + " | " + time;
            allLogs.add(entry);

            sum += count;
            totalRequests++;

            countryCounter.put(country,
                    countryCounter.getOrDefault(country, 0) + 1);
        }

        if (totalRequests > 0) {
            avgResults = sum / totalRequests;
        }

        popularCountry = "";
        int most = 0;

        for (var e : countryCounter.entrySet()) {
            if (e.getValue() > most) {
                most = e.getValue();
                popularCountry = e.getKey();
            }
        }
    }

    /**
     * Inserts a log entry into MongoDB.
     */
    private static void writeLog(String country, int count, String time) {

        MongoClient client = MongoClients.create(settings);
        MongoDatabase db = client.getDatabase("Project4Task2");
        MongoCollection<Document> table = db.getCollection("university_logs");

        Document doc = new Document("country", country)
                .append("count", count)
                .append("time", time);

        table.insertOne(doc);
    }

    /**
     * Helper method for sending GET requests.
     */
    private static String fetch(String link) {
        try {
            URL u = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder out = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
                out.append(line);

            br.close();
            return out.toString();

        } catch (IOException e) {
            return null;
        }
    }
}
