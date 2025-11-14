package crud.selenium;

import crud.Main;
import crud.pages.ProdutoAddPage;
import crud.pages.ProdutoEditPage;
import crud.pages.ProdutoListPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.javalin.Javalin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProdutoUITest {

    private WebDriver driver;
    private Javalin app;
    private ProdutoListPage listPage;
    private ProdutoAddPage addPage;
    private ProdutoEditPage editPage;

    private ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        String chromeBinaryPath = "C:\\Arquivos de Programas\\Google\\Chrome\\Application\\chrome.exe";
        options.setBinary(chromeBinaryPath);
        return options;
    }

    @BeforeAll
    void setupJavalin() {
        app = Main.startServer();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(createChromeOptions());
    }

    @BeforeEach
    void setupTest() {
        listPage = new ProdutoListPage(driver);
        addPage = new ProdutoAddPage(driver);
        editPage = new ProdutoEditPage(driver);

        try {
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:7000/api/produtos/deleteall").openConnection();
            connection.setRequestMethod("DELETE");
            connection.connect();
            connection.getResponseCode();
            connection.disconnect();
        } catch (Exception e) {
            System.err.println("[ERROR] Falhou ao limpar o estado: " + e.getMessage());
        }
    }

    @AfterAll
    void tearDown() {
        if (driver != null) driver.quit();
        if (app != null) app.stop();
    }

    @Test
    @Order(1)
    void testFluxoCRUDCompleto() {
        String nomeOriginal = "Teclado Mecânico X";
        String nomeEditado = "Teclado Mecânico Pro V2";

        // Criar
        listPage.open();
        listPage.clickAddButton();
        addPage.fillForm(nomeOriginal, "450.00", "50");
        addPage.clickSaveButton();

        assertTrue(driver.getCurrentUrl().contains("localhost:7000"),
                "Não redirecionou para a página inicial");
        
        // Aguarda que a lista seja atualizada após criar o produto
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("product-list")));
        
        assertTrue(listPage.isProductListed(nomeOriginal),
                "Produto recém-criado não encontrado na lista");

        // Obtém o ID real do produto criado
        Long produtoId = listPage.getProductIdByName(nomeOriginal);
        assertNotNull(produtoId, "Não foi possível obter o ID do produto criado");

        // Editar
        listPage.clickEditButton(produtoId);
        editPage.fillForm(nomeEditado, "600.50", "30");
        editPage.clickUpdateButton();

        assertTrue(driver.getCurrentUrl().contains("localhost:7000"),
                "Não voltou para a listagem após edição");
        
        // Aguarda que a lista seja atualizada após editar
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("product-list")));
        
        assertTrue(listPage.isProductListed(nomeEditado),
                "Nome editado não encontrado");
        assertFalse(listPage.isProductListed(nomeOriginal),
                "Nome antigo ainda aparece na lista");

        // Deletar
        listPage.clickDeleteButton(produtoId);

        assertTrue(driver.getCurrentUrl().contains("localhost:7000"),
                "Não voltou para a listagem após exclusão");
        
        // Aguarda que a lista seja atualizada após deletar
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("product-list")));
        
        assertFalse(listPage.isProductListed(nomeEditado),
                "Produto deveria ter sido removido");
    }

    @ParameterizedTest
    @CsvSource({
            "Nome Inválido, 0.0, 10, O preço deve ser maior que zero.",
            "Nome Inválido, 100.0, -1, O estoque não pode ser negativo.",
            "'', 100.0, 10, O nome do produto é obrigatório."
    })
    void testCadastro_CenariosInvalidos(String nome, String preco, String estoque, String mensagemEsperada) {
        listPage.open();
        listPage.clickAddButton();

        addPage.fillForm(nome, preco, estoque);
        addPage.clickSaveButton();

        boolean alertaVisivel = addPage.isAlertVisible();
        String mensagemAlerta = addPage.getAlertMessage();

        assertTrue(alertaVisivel, "Alerta não apareceu");
        assertTrue(mensagemAlerta.contains(mensagemEsperada),
                "Mensagem de erro diferente do esperado");

        assertTrue(driver.getCurrentUrl().contains("add.html"),
                "Não deveria redirecionar com dados inválidos");
    }

}
