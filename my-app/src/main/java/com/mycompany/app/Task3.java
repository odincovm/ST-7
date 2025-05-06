package com.mycompany.app;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Task3 {

    private static final String API_URL =
        "https://api.open-meteo.com/v1/forecast?"
      + "latitude=56&longitude=44"
      + "&hourly=temperature_2m,rain"
      + "&current=cloud_cover"
      + "&timezone=Europe%2FMoscow"
      + "&forecast_days=1"
      + "&wind_speed_unit=ms";

    public static void generateForecast() {
        try {
            // 1) HTTP‑GET запрос
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 2) Читаем ответ
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            // 3) Парсим JSON
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(sb.toString());
            JSONObject hourly = (JSONObject) root.get("hourly");

            JSONArray times = (JSONArray) hourly.get("time");
            JSONArray temps = (JSONArray) hourly.get("temperature_2m");
            JSONArray rains = (JSONArray) hourly.get("rain");

            // 4) Формируем текст таблицы
            StringBuilder table = new StringBuilder();
            table.append(String.format("%-3s %-20s %-12s %-12s%n",
                                      "№", "Дата/время", "Температура", "Осадки (мм)"));
            for (int i = 0; i < times.size(); i++) {
                String t = (String) times.get(i);
                Object temp = temps.get(i);
                Object rain = rains.get(i);
                table.append(String.format("%-3d %-20s %-12s %-12s%n",
                                          i+1, t, temp.toString(), rain.toString()));
            }

            String out = table.toString();

            // 5) Печать в консоль
            System.out.println(out);

            // 6) Запись в файл ../result/forecast.txt
            File outFile = new File("../result/forecast.txt");
            File dir = outFile.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try (FileWriter writer = new FileWriter(outFile, false)) {
                writer.write(out);
            }
            System.out.println("Written forecast to " + outFile.getPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Если хочешь запустить Task3 самостоятельно:
    public static void main(String[] args) {
        generateForecast();
    }
}
