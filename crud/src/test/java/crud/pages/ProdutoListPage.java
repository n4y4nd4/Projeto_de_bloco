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
    private final WebDriverWait wait;
    private final String url = "http://localhost:7000/";

    // Seletores
    private final By tableBody = By.id("product-list");
    private final By addButton = By.id("add-btn");
    private final By alertMessage = By.id("alert-message");

    public ProdutoListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        System.out.println("[LOG] Abrindo página de listagem: " + url);
        driver.get(url);
    }

    public void clickAddButton() {
        System.out.println("[LOG] Clicando no botão de adicionar produto...");
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(addButton));
        addBtn.click();
    }

    public boolean isProductListed(String nome) {
        System.out.println("[LOG] Verificando se produto aparece na tabela: " + nome);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(tableBody));

            String tableContent = driver.findElement(tableBody).getText();
            System.out.println("\n=== TABELA ATUAL ===\n" + tableContent + "\n====================\n");

            boolean contains = tableContent.contains(nome);
            System.out.println("[LOG] Produto encontrado? " + contains);
            return contains;

        } catch (Exception e) {
            System.out.println("[LOG] ERRO AO LER TABELA: " + e.getMessage());
            return false;
        }
    }

    public void clickDeleteButton(Long id) {
        System.out.println("[LOG] Clicando no botão de deletar ID: " + id);
        By deleteBtn = By.id("delete-btn-" + id);
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
        deleteButton.click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        System.out.println("[LOG] Confirmação de exclusão aceita");
    }

    public void clickEditButton(Long id) {
        System.out.println("[LOG] Clicando no botão de editar ID: " + id);
        By editBtn = By.id("edit-btn-" + id);
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(editBtn));
        editButton.click();
        // Aguarda que a navegação para a página de edição seja iniciada
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public Long getProductIdByName(String nome) {
        System.out.println("[LOG] Buscando ID do produto: " + nome);
        try {
            // Aguarda que a tabela seja carregada
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("product-list")));
            
            // Procura pela linha que contém o nome do produto
            WebElement tableBody = driver.findElement(By.id("product-list"));
            java.util.List<org.openqa.selenium.WebElement> rows = tableBody.findElements(By.tagName("tr"));
            
            for (org.openqa.selenium.WebElement row : rows) {
                java.util.List<org.openqa.selenium.WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() >= 2 && cells.get(1).getText().equals(nome)) {
                    String idText = cells.get(0).getText();
                    System.out.println("[LOG] ID encontrado: " + idText);
                    return Long.parseLong(idText);
                }
            }
            System.out.println("[LOG] Produto não encontrado na lista");
            return null;
        } catch (Exception e) {
            System.out.println("[LOG] ERRO AO BUSCAR ID: " + e.getMessage());
            return null;
        }
    }

    public String getAlertMessage() {
        System.out.println("[LOG] Aguardando alerta após operação...");
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage));
            System.out.println("[LOG] Alerta capturado: " + alert.getText());
            return alert.getText();
        } catch (Exception e) {
            System.out.println("[LOG] ALERTA NÃO APARECEU");
            return "ALERTA DE SUCESSO NÃO ENCONTRADO (TIMEOUT)";
        }
    }

}