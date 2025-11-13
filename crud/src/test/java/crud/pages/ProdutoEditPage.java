package crud.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProdutoEditPage {
    private final WebDriver driver;
    
    // Seletores
    private final By idInput = By.id("id");
    private final By nomeInput = By.id("nome");
    private final By precoInput = By.id("preco");
    private final By estoqueInput = By.id("estoque");
    private final By updateButton = By.id("update-btn");
    private final By alertMessage = By.id("alert-message");

    public ProdutoEditPage(WebDriver driver) {
        this.driver = driver;
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
        return driver.findElement(alertMessage).getText();
    }
    
    public boolean isAlertVisible() {
        return driver.findElement(alertMessage).isDisplayed();
    }
}
