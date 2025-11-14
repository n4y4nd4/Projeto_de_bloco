package crud.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    public Long getProductId() {
        String id = driver.findElement(idInput).getAttribute("value");
        System.out.println("[LOG] ID obtido na tela de edição: " + id);
        return Long.parseLong(id);
    }

    public void fillForm(String nome, String preco, String estoque) {
        System.out.println("[LOG] Preenchendo formulário de edição...");
        
        // Aguarda que a página de edição carregue completamente
        wait.until(ExpectedConditions.urlContains("edit.html"));
        
        // Aguarda que o JavaScript carregue o produto e exiba o formulário
        // O formulário começa com display:none e só é exibido após carregar o produto
        By editForm = By.id("edit-form");
        
        // Aguarda até que o formulário esteja visível OU que apareça mensagem de erro
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(editForm),
                ExpectedConditions.presenceOfElementLocated(By.id("not-found-msg"))
            ));
        } catch (Exception e) {
            // Se não encontrou nem formulário nem mensagem de erro, tenta aguardar mais
            System.out.println("[LOG] Aguardando carregamento do produto...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Verifica se há mensagem de erro
        try {
            WebElement notFoundMsg = driver.findElement(By.id("not-found-msg"));
            if (notFoundMsg.isDisplayed() && !notFoundMsg.getText().isEmpty()) {
                throw new RuntimeException("Produto não encontrado: " + notFoundMsg.getText());
            }
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // Não há mensagem de erro, continua
        }
        
        // Aguarda que o formulário esteja visível
        wait.until(ExpectedConditions.visibilityOfElementLocated(editForm));
        
        // Aguarda que todos os elementos estejam presentes e visíveis
        wait.until(ExpectedConditions.visibilityOfElementLocated(idInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(nomeInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(precoInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(estoqueInput));

        // Aguarda que os elementos estejam clicáveis
        WebElement nomeEl = wait.until(ExpectedConditions.elementToBeClickable(nomeInput));
        nomeEl.clear();
        nomeEl.sendKeys(nome);

        WebElement precoEl = wait.until(ExpectedConditions.elementToBeClickable(precoInput));
        precoEl.clear();
        precoEl.sendKeys(preco);

        WebElement estoqueEl = wait.until(ExpectedConditions.elementToBeClickable(estoqueInput));
        estoqueEl.clear();
        estoqueEl.sendKeys(estoque);

        System.out.println("[LOG] Dados atualizados para: " + nome + ", " + preco + ", " + estoque);
    }

    public void clickUpdateButton() {
        System.out.println("[LOG] Clicando no botão de atualizar...");
        WebElement updateBtn = wait.until(ExpectedConditions.elementToBeClickable(updateButton));
        updateBtn.click();
    }

    public String getAlertMessage() {
        System.out.println("[LOG] Aguardando alerta após edição...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage));
        String text = driver.findElement(alertMessage).getText();
        System.out.println("[LOG] Alerta encontrado: " + text);
        return text;
    }


    public boolean isAlertVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}