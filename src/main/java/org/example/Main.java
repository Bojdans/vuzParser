package org.example;

import org.hibernate.Session;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "chromedriver/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        String baseUrl = "https://proverili.ru/catalog?page=";
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Парсер выключается......");
            driver.quit();
        }));
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Vuz.class);
        
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(registry);
        int currentPage = 1;
        try {
            while (currentPage <= 216) {
                driver.get(baseUrl + currentPage);
                System.out.println("Загружаем: " + baseUrl + currentPage);

                List<WebElement> vuziElementi = driver.findElements(By.cssSelector(".educational-card"));
                ArrayList<Vuz> vuziki = new ArrayList<>();
                vuziElementi.forEach(elementik -> {
                    String name = elementik.findElement(By.cssSelector(".educational-card__name")).getText().contains("-") ? elementik.findElement(By.cssSelector(".educational-card__name")).getText().substring(elementik.findElement(By.cssSelector(".educational-card__name")).getText().indexOf("-") + 1).trim() : elementik.findElement(By.cssSelector(".educational-card__name")).getText();
                    String abbreviation = elementik.findElement(By.cssSelector(".educational-card__name")).getText().contains("-") ? elementik.findElement(By.cssSelector(".educational-card__name")).getText().substring(0, elementik.findElement(By.cssSelector(".educational-card__name")).getText().indexOf("-") - 1).trim() : null;
                    String paid = elementik.findElement(By.cssSelector(".educational-card__contents")).findElements(By.cssSelector(".educational-card__content")).size() == 3 ? elementik.findElement(By.cssSelector(".educational-card__contents")).findElements(By.cssSelector(".educational-card__content")).get(1).getText().replace("\n", " ").trim() : null;
                    String free = elementik.findElement(By.cssSelector(".educational-card__contents")).findElements(By.cssSelector(".educational-card__content")).size() == 3 ? elementik.findElement(By.cssSelector(".educational-card__contents")).findElements(By.cssSelector(".educational-card__content")).get(2).getText().replace("\n", " ").trim() : null;
                    int freeScore = free.contains("балл")
                            ? (free.substring(free.indexOf("от") + 3, free.indexOf("балл")).trim().equals("-")
                            ? -1
                            : Integer.parseInt(free.substring(free.indexOf("от") + 3, free.indexOf("балл")).trim().replace("\n", " ").replace("", "")))
                            : -1;

                    int freeSeats = free.contains("мест")
                            ? (free.substring(free.indexOf("/") + 2, free.indexOf("мест")).trim().equals("-")
                            ? -1
                            : Integer.parseInt(free.substring(free.indexOf("/") + 2, free.indexOf("мест")).trim().replace("\n", " ").replace(" ", "")))
                            : -1;

                    int paidScore = paid.contains("балл")
                            ? (paid.substring(paid.indexOf("от") + 3, paid.indexOf("балл")).trim().equals("-")
                            ? -1
                            : Integer.parseInt(paid.substring(paid.indexOf("от") + 3, paid.indexOf("балл")).trim().replace("\n", " ").replace(" ", "")))
                            : -1;
                    int paidSeats = paid.contains("мест")
                            ? (paid.substring(paid.indexOf("/") + 2, paid.indexOf("мест")).trim().equals("-")
                            ? -1
                            : Integer.parseInt(paid.substring(paid.indexOf("/") + 2, paid.indexOf("мест")).trim().replace("\n", " ").replace(" ", "")))
                            : -1;

                    vuziki.add(new Vuz(name, abbreviation, freeSeats, freeScore, paidSeats, paidScore));
                });
                System.out.println(vuziki);


                vuziki.forEach(vuzik -> {
                    try (Session session = sessionFactory.openSession()) {
                        session.beginTransaction();
                        session.save(vuzik);
                        session.getTransaction().commit();
                    } catch (Exception e) {
                        System.out.println("Произошла ошибка при взаимодействии с бд: " + e.getMessage());
                    }
                });
                currentPage++;
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}