package crud.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProdutoListPage {
    private final WebDriver driver;
    private final WebDriverWait wait; // Adicionar WebDriverWait
    private final String url = "http://localhost:7000/";

    
    // Seletores
    private final By tableBody = By.id("product-list");
    private final By addButton = By.id("add-btn");
    private final By alertMessage = By.id("alert-message");

    public ProdutoListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Inicializa espera de 5 segundos
    }

    public void open() {
        driver.get(url);
    }

    public void clickAddButton() {
        driver.findElement(addButton).click();
    }

    public boolean isProductListed(String nome) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(tableBody));
        WebElement tbody = driver.findElement(tableBody);

        List<WebElement> rows = tbody.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            if (row.getText().contains(nome)) {
                return true;
            }
        }
        return false;
    }

    public void clickDeleteButton(Long id) {
        driver.findElement(By.id("delete-btn-" + id)).click();
        driver.switchTo().alert().accept(); // Confirma o alerta de exclusão
    }

    public void clickEditButton(Long id) {
        driver.findElement(By.id("edit-btn-" + id)).click();
    }
    
    public String getAlertMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage));
        return driver.findElement(alertMessage).getText();
    }
}
