package crud.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProdutoListPage {
    private final WebDriver driver;
    private final String url = "http://localhost:7000/";
    
    // Seletores
    private final By tableBody = By.id("product-list");
    private final By addButton = By.id("add-btn");
    private final By alertMessage = By.id("alert-message");

    public ProdutoListPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(url);
    }

    public void clickAddButton() {
        driver.findElement(addButton).click();
    }

    public boolean isProductListed(String nome) {
        // Use the tableBody field instead of repeating the XPath
        List<WebElement> tbodys = driver.findElements(tableBody);
        if (tbodys.isEmpty()) {
            return false;
        }
        WebElement tbody = tbodys.get(0);
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
        return driver.findElement(alertMessage).getText();
    }
}