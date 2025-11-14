package crud.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class ProdutoAddPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By nomeInput = By.id("nome");
    private final By precoInput = By.id("preco");
    private final By estoqueInput = By.id("estoque");
    private final By saveButton = By.id("save-btn");
    private final By alertMessage = By.id("alert-message");

    public ProdutoAddPage(WebDriver driver) {

        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillForm(String nome, String preco, String estoque) {
        // Aguarda que os elementos estejam visíveis e clicáveis antes de interagir
        WebElement nomeEl = wait.until(ExpectedConditions.elementToBeClickable(nomeInput));
        nomeEl.clear();
        nomeEl.sendKeys(nome);

        WebElement precoEl = wait.until(ExpectedConditions.elementToBeClickable(precoInput));
        precoEl.clear();
        precoEl.sendKeys(preco);

        WebElement estoqueEl = wait.until(ExpectedConditions.elementToBeClickable(estoqueInput));
        estoqueEl.clear();
        estoqueEl.sendKeys(estoque);
    }


    public void clickSaveButton() {
        System.out.println("[LOG] Clicando no botão de salvar...");
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveBtn.click();
    }

    public boolean isAlertVisible() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage));
            return alert.isDisplayed() && !alert.getText().isBlank();
        } catch (Exception e) {
            System.out.println("[LOG] ALERTA NÃO APARECEU (TIMEOUT)");
            return false;
        }
    }

    public String getAlertMessage() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage));
            String text = alert.getText();
            System.out.println("[LOG] Alerta capturado: " + text);
            return text;
        } catch (Exception e) {
            System.out.println("[LOG] ALERTA NÃO APARECEU (TIMEOUT)");
            return "ALERTA DE ERRO NÃO ENCONTRADO (TIMEOUT)";
        }
    }


}