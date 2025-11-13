package crud.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
        return driver.findElement(alertMessage).getText();
    }
    
    public boolean isAlertVisible() {
        return driver.findElement(alertMessage).isDisplayed();
    }
}