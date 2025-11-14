package crud.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class ProdutoEditPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Seletores
    private final By idInput = By.id("id");
    private final By nomeInput = By.id("nome");
    private final By precoInput = By.id("preco");
    private final By estoqueInput = By.id("estoque");
    private final By updateButton = By.id("update-btn");
    private final By alertMessage = By.id("alert-message");

    public ProdutoEditPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }


    public Long getProductId() {
        return Long.parseLong(driver.findElement(idInput).getAttribute("value"));
    }

    public void fillForm(String nome, String preco, String estoque) {
        driver.findElement(nomeInput).clear();
        driver.findElement(nomeInput).sendKeys(nome);
        
        driver.findElement(precoInput).clear();
        driver.findElement(precoInput).sendKeys(preco);
        
        driver.findElement(estoqueInput).clear();
        driver.findElement(estoqueInput).sendKeys(estoque);
    }

    public void clickUpdateButton() {
        driver.findElement(updateButton).click();
    }

    public String getAlertMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage));
        return driver.findElement(alertMessage).getText();
    }

    public boolean isAlertVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}