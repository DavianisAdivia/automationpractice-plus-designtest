package test.sorabel;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */

    private WebDriver driver;

    @Test
    public void verifyFunctionalityOfSortByLowestPrice() {
        driver.get("http://automationpractice.com");

        //Navigate to product category
        driver.findElement(By.xpath("//a[@class='sf-with-ul'][contains(text(),'Women')]")).click();

        //Sort By
        Select select = new Select(driver.findElement(By.xpath("//select[@id='selectProductSort']")));
        select.selectByVisibleText("Price: Lowest first");

        //Wait till sorted and assert result (Here the bug found)
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//body[@id='category']/div[@id='page']/div[@class='columns-container']/div[@id='columns']/div[@class='row']/div[@id='center_column']/ul[@class='product_list grid row']/p[1]")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        int count = driver.findElements(By.xpath("//div[@class='right-block']//span[@class='price product-price']")).size();
        if (count > 1) {
            for (int i = 1; i < count; i++) {
                int j = i-1;
                String str1 = driver.findElement(By.xpath("//li['"+j+"']" + "//div[@class='right-block']//span[@class='price product-price']")).getText().replace("$","").replace(".","");
                String str2 = driver.findElement(By.xpath("//li['"+i+"']" + "//div[@class='right-block']//span[@class='price product-price']")).getText().replace("$","").replace(".","");
                Assert.assertTrue("data '"+j+"' NOT lower or equal than data '"+i+"'",Integer.parseInt(str1) <= Integer.parseInt(str2));
            }
        }

        driver.quit();
    }

    @Test
    public void verifySearchBoxWithInputProductNotFound() {
        driver.get("http://automationpractice.com");

        //Input into search field
        driver.findElement(By.id("search_query_top")).sendKeys("abcdxxx");
        driver.findElement(By.xpath("//button[@name='submit_search']")).click();

        //Wait no result and assert
        try {
            WebDriverWait wait = new WebDriverWait(driver, 3);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='alert alert-warning']")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue("Result NOT null", driver.findElement(By.xpath("//span[@class='heading-counter']")).getText().contains("0"));

        driver.quit();
    }

    @Before
    public void initializeBrowser() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        String args = "--start-maximized;--test-type;--no-sandbox;--ignore-certificate-errors;" +
                "--disable-popup-blocking;--disable-default-apps;--disable-extensions-file-access-check;--incognito;" +
                "--disable-infobars;--disable-gpu";
        Arrays.stream(args.split(";")).forEach(options::addArguments);
        driver = new ChromeDriver(options);
    }

    @After
    public void closeBrowser() {
        driver.quit();
    }


}
