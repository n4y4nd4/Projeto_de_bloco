package crud.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProdutoAddPage {
    private final WebDriver driver;
    
    // Seletores
    private final By nomeInput = By.id("nome");
    private final By precoInput = By.id("preco");
    private final By estoqueInput = By.id("estoque");
    private final By saveButton = By.id("save-btn");
    private final By alertMessage = By.id("alert-message");

    public ProdutoAddPage(WebDriver driver) {
        this.driver = driver;
    }

    public void fillForm(String nome, String preco, String estoque) {
        driver.findElement(nomeInput).sendKeys(nome);
        driver.findElement(precoInput).sendKeys(preco);
        driver.findElement(estoqueInput).sendKeys(estoque);
    }

    public void clickSaveButton() {
        driver.findElement(saveButton).click();
    }

    public String getAlertMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage));
        return driver.findElement(alertMessage).getText();
    }
    
    public boolean isAlertVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}