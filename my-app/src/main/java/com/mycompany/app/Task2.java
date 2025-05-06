package com.mycompany.app;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Task2 {

    /**
     * Делает HTTP GET-запрос к api.ipify.org и возвращает
     * публичный IPv4-адрес в виде строки.
     */
    public static String fetchPublicIP() {
        String apiUrl = "https://api.ipify.org/?format=json";
        try {
            // Открываем соединение
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Читаем ответ
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            // Парсим JSON
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(sb.toString());
            return (String) obj.get("ip");

        } catch (Exception e) {
            System.err.println("Failed to fetch IP: " + e.toString());
            return null;
        }
    }

    /**
     * Выводит IP в консоль. Этот метод можно вызвать из App.java.
     */
    public static void printPublicIP() {
        String ip = fetchPublicIP();
        if (ip != null) {
            System.out.println("Your public IPv4 address: " + ip);
        } else {
            System.out.println("Could not retrieve public IP.");
        }
    }

}
