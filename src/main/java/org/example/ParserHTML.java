package org.example;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserHTML {
    private ArrayList<String> countryLinks = new ArrayList<>();// Список для хранения ссылок на страницы стран
    private HashMap<String, Integer> countryIdMap = new HashMap<>(); // Создаем карту для хранения соответствия названия страны и ее id в базе данных
    private  HashMap<String, Integer> siteIdMap = new HashMap<>();
    private  HashMap<String, Integer> unitIdMap = new HashMap<>();
    private  HashMap<String, Integer> companyIdMap = new HashMap<>();// назваание unit и id site

    public void parseHTML2BD(Connector connector) throws SQLException, IOException {
        fillCountryLinks();
        Connection connection = connector.getConnection();
        fillCountryIdMap(connection);
        fillSites(connection);
        fillUnits(connection);
        }

    private void fillUnits(Connection connection) throws SQLException, IOException {
        String insertQuery = "INSERT INTO public.units (name, site_id, status, type, model, owner_id, operator_id, net_capacity, design_net_capacity, gross_capacity, thermal_capacity, construction_start_date, first_criticaly_date, first_grid_connection, commercial_operation_date, permanent_shutdown_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        // Перебор ссылок на страницы
        for (int i = 1 ; i<2500; i++) {
            System.out.println(i);
            try {
                Document unitDoc = Jsoup.connect("https://pris.iaea.org/PRIS/CountryStatistics/ReactorDetails.aspx?current="+i).get();
                String unitName = unitDoc.selectFirst("#MainContent_MainContent_lblReactorName").text().trim();
                System.out.println(unitName);
                int site_id = siteIdMap.get(unitName);
                String status = unitDoc.select("#MainContent_MainContent_lblReactorStatus").text().trim();
                String type = unitDoc.select("#MainContent_MainContent_lblType").text().trim();
                String model = unitDoc.select("#MainContent_MainContent_lblModel").text().trim();
                String owner = unitDoc.select("#MainContent_MainContent_hypOwnerUrl").text().trim();
                if (!companyIdMap.containsKey(owner))
                    companyIdMap.put(owner, (companyIdMap.isEmpty()) ? 1 : companyIdMap.size());
                int ownerId = companyIdMap.get(owner);
                String operator = unitDoc.select("#MainContent_MainContent_hypOwnerUrl").text().trim();
                if (!companyIdMap.containsKey(operator))
                    companyIdMap.put(operator, (companyIdMap.isEmpty()) ? 1 : companyIdMap.size());
                int operatorId = companyIdMap.get(operator);
                int net_capacity = Integer.parseInt(unitDoc.select("#MainContent_MainContent_lblNetCapacity").text().trim());
                int design_net_capacity = Integer.parseInt(unitDoc.select("#MainContent_MainContent_lblDesignNetCapacity").text().trim());
                int gross_capacity = Integer.parseInt(unitDoc.select("#MainContent_MainContent_lblGrossCapacity").text().trim());
                int thermal_capacity = Integer.parseInt(unitDoc.select("#MainContent_MainContent_lblThermalCapacity").text().trim());
                String construction_start_date = unitDoc.select("#MainContent_MainContent_lblModel").text().trim();
                String first_criticaly_date = unitDoc.select("#MainContent_MainContent_lblModel").text().trim();
                String first_grid_connection = unitDoc.select("#MainContent_MainContent_lblModel").text().trim();
                String commercial_operation_date = unitDoc.select("#MainContent_MainContent_lblModel").text().trim();
                String permanent_shutdown_date = unitDoc.select("#MainContent_MainContent_lblModel").text().trim();

            // Установка значений в запросе на вставку
            statement.setString(1, unitName);
            statement.setInt(2, site_id);
            statement.setString(3, status);
            statement.setString(4, type);
            statement.setString(5, model);
            statement.setInt(6, ownerId);
            statement.setInt(7, operatorId);
            statement.setInt(8, net_capacity);
            statement.setInt(9, design_net_capacity);
            statement.setInt(10, gross_capacity);
            statement.setInt(11, thermal_capacity);
            statement.setDate(12, Date.valueOf(construction_start_date));
            statement.setDate(13, Date.valueOf(first_criticaly_date));
            statement.setDate(14, Date.valueOf(first_grid_connection));
            statement.setDate(15, Date.valueOf(commercial_operation_date));
            statement.setDate(16, Date.valueOf(permanent_shutdown_date));
            // Установите остальные параметры для запроса

            // Выполнение запроса на вставку
            statement.executeUpdate();
            }catch (Exception e) {

            }
        }
    }

    private void fillSites(Connection connection) throws SQLException, IOException {
        String insertSiteSql = "INSERT INTO public.sites (name, location, country_id) VALUES (?, ?, ?)";
        // Проходим по каждой стране и извлекаем данные о реакторах
        PreparedStatement insertSiteStatement = connection.prepareStatement(insertSiteSql);
        // Проходим по каждой стране и извлекаем данные о реакторах
        for (String countryLink : countryLinks) {
            // Получаем страницу страны
            Document countryDoc = Jsoup.connect(countryLink).get();
            // Получаем название страны из заголовка
            String countryName = countryDoc.selectFirst("#MainContent_MainContent_lblCountryName").text();
            String[] countryNameParts = countryName.split(",");
            countryName = countryNameParts[0].trim();
            // Получаем id страны из карты
            int countryId = countryIdMap.get(countryName);

            // Ищем таблицу с реакторами
            Element table = countryDoc.selectFirst("#content > div > table");
            // Извлекаем данные о реакторах
            Elements rows = table.select("tr");
            for (int i = 1; i < rows.size(); i++) { // Начинаем с 1, чтобы пропустить заголовок таблицы
                Element row = rows.get(i);
                Elements cols = row.select("td");
                String reactorName = cols.get(0).text();
                // Обработка имени реактора
                String[] reactorNameParts = reactorName.split("-");
                String siteName;
                switch (reactorName) {
                    case "SUPER-PHENIX":
                        siteName = "SUPER-PHENIX";
                        break;
                    case "THTR-300":
                        siteName = "THTR-300";
                        break;
                    case "BREST-OD-300":
                        siteName = "BREST-OD-300";
                        break;
                    default:
                        siteName = (reactorNameParts.length <= 2) ? reactorNameParts[0].trim() : reactorNameParts[0].trim() + "-" + reactorNameParts[1].trim();;
                }
                if (siteIdMap.containsKey(siteName)) continue;
                siteIdMap.put(siteName, (siteIdMap.isEmpty()) ? 1 : siteIdMap.size());
                unitIdMap.put(reactorName.trim(), siteIdMap.get(siteName));
                // Получаем местоположение реактора
                String location = cols.get(3).text();
                // Добавляем данные в подготовленный SQL-запрос
                insertSiteStatement.setString(1, siteName);
                insertSiteStatement.setString(2, location);
                insertSiteStatement.setInt(3, countryId);
                // Выполняем запрос
                insertSiteStatement.executeUpdate();
            }
        }
    }


    private void fillCountryIdMap(Connection connection) throws SQLException {
        // Получаем id для каждой страны из базы данных и добавляем в карту
        String sql = "SELECT country, id FROM public.countries";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String countryName = resultSet.getString("country");
                int countryId = resultSet.getInt("id");
                countryIdMap.put(countryName, countryId);
            }
        }
    }

    private void fillCountryLinks() throws IOException {
        // Получаем страницу со списком стран
        Document doc = Jsoup.connect("https://pris.iaea.org/PRIS/CountryStatistics/CountryStatisticsLandingPage.aspx").get();
        // Ищем все ссылки на страницах стран
        Elements links = doc.select("a[href^=/PRIS/CountryStatistics/CountryDetails.aspx?current=]");
        // Проходимся по каждой ссылке и добавляем ее в список
        for (Element link : links) {
            String linkHref = link.attr("href");
            String countryLink = "https://pris.iaea.org" + linkHref;
            countryLinks.add(countryLink);
        }
    }
}
