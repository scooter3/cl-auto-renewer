package com.pslin.net;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author plin
 */
public class CraigslistRenew {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        Logger logger = Logger.getLogger("com.gargoylesoftware.htmlunit");
        logger.setLevel(Level.OFF);
        WebDriver driver;
        if(args.length == 0) {
            throw new RuntimeException("Driver not valid. Use 'html' or 'chrome'.");
        }
        if(args[0].equalsIgnoreCase("html")) {
            driver = new HtmlUnitDriver();
        } else if(args[0].equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else {
            throw new RuntimeException("Driver not valid. Use 'html' or 'chrome'.");
        }
        renew(driver);
    }

    private static void renew(WebDriver driver) {
        System.out.println("Navigating to http://losangeles.craigslist.org");
        driver.get("http://losangeles.craigslist.org");
        WebElement myAccountLink = driver.findElement(By.linkText("my account"));
        myAccountLink.click();

        // Haven't logged in yet
        if(driver.findElement(By.className("loginBox")) != null) {
            System.out.println("logging in");
            WebElement emailInput = driver.findElement(By.id("inputEmailHandle"));
            WebElement passwordInput = driver.findElement(By.id("inputPassword"));

            emailInput.sendKeys("ACCOUNT_EMAIL_ADDRESS");
            passwordInput.sendKeys("PASSWORD");

            emailInput.submit();
        }

        WebDriverWait wait = new WebDriverWait(driver, 5);

        boolean finished = false;
        int count = 0;
        List<String> postingTitles = new ArrayList<>();
        while(!finished) {
            try {
                // Find post to renew
                WebElement renew = driver.findElement(By.xpath("//input[@value='renew']"));
                if(renew != null) {
                    renew.submit();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("managestatus")));

                    String postTitle = driver.findElement(By.className("postingtitletext")).getText();
                    postingTitles.add(postTitle);

                    System.out.println("Renewed post: " + postTitle);
                    count++;
                    driver.get("https://accounts.craigslist.org/login/home");
                }
            } catch (NoSuchElementException e) {
                finished = true;
                if(count == 0) {
                    System.out.println("Nothing to renew.");
                } else {
                    System.out.println("Renewed " + count + " postings");
                }
            }
        }

        StringBuilder emailBody = new StringBuilder();
        if(count == 0) {
            emailBody.append("Nothing to renew.");
        } else {
            emailBody.append("Renewed posts:\n");
            for(String title : postingTitles) {
                emailBody.append(title).append("\n");
            }
            emailBody.append("\n").append("Renewed " + count + " postings");
        }

        EmailService emailService = new EmailService();
        emailService.sendSimpleEmail("TO_EMAIL_ADDRESS", "Craigslist Renew Completed", emailBody.toString());

        driver.quit();
    }
}
