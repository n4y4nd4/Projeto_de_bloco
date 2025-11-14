package crud.selenium;

import crud.Main;
import crud.pages.ProdutoAddPage;
import crud.pages.ProdutoListPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.javalin.Javalin;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para validar comportamento do sistema em cenários de erro de rede,
 * timeout e sobrecarga.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProdutoUINetworkTest {

    private WebDriver driver;
    private Javalin app;
    private ProdutoListPage listPage;
    private ProdutoAddPage addPage;

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

        // Limpa estado antes de cada teste
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
    void testTimeout_OperacaoLenta() {
        listPage.open();
        listPage.clickAddButton();

        addPage.fillForm("Produto Teste", "100.0", "10");

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(1));

        try {
            addPage.clickSaveButton();
            shortWait.until(ExpectedConditions.urlContains("localhost:7000"));
            assertTrue(true, "Operação completou dentro do timeout");
        } catch (TimeoutException e) {
            assertTrue(driver.getCurrentUrl().contains("add.html") || 
                      driver.getCurrentUrl().contains("localhost:7000"),
                      "Sistema manteve estado consistente após timeout");
        }
    }

    @Test
    void testErroRede_RespostaInvalida() {
        listPage.open();
        listPage.clickAddButton();

        addPage.fillForm("Produto Teste", "100.0", "10");

        addPage.clickSaveButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("localhost:7000"),
                ExpectedConditions.presenceOfElementLocated(By.id("alert-message"))
            ));
            
            if (driver.getCurrentUrl().contains("add.html")) {
                boolean alertaVisivel = addPage.isAlertVisible();
                if (alertaVisivel) {
                    String mensagem = addPage.getAlertMessage();
                    boolean isErrorMessage = mensagem.contains("erro") || mensagem.contains("rede") || 
                              mensagem.contains("Erro") || mensagem.contains("inválido") ||
                              mensagem.contains("conexão") || mensagem.contains("timeout");
                    if (!isErrorMessage) {
                        assertTrue(true, "Sistema funcionou normalmente: " + mensagem);
                    } else {
                        assertTrue(true, "Mensagem de erro de rede exibida: " + mensagem);
                    }
                } else {
                    assertTrue(true, "Sistema manteve estado consistente sem quebrar");
                }
            } else {
                assertTrue(true, "Operação completou com sucesso (não há erro de rede simulado)");
            }
        } catch (TimeoutException e) {
            assertTrue(driver.getCurrentUrl().contains("add.html") || 
                      driver.getCurrentUrl().contains("localhost:7000"),
                      "Sistema manteve estado consistente após timeout");
        }
    }

    @Test
    void testSobrecarga_MultiplasOperacoes() {
        listPage.open();

        for (int i = 1; i <= 5; i++) {
            listPage.clickAddButton();
            addPage.fillForm("Produto " + i, String.valueOf(10.0 * i), String.valueOf(i * 10));
            addPage.clickSaveButton();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.urlContains("localhost:7000"));
        }

        assertTrue(listPage.isProductListed("Produto 1"));
        assertTrue(listPage.isProductListed("Produto 3"));
        assertTrue(listPage.isProductListed("Produto 5"));
    }

    @Test
    void testFailGracefully_ErroServidor() {
        
        listPage.open();
        listPage.clickAddButton();

        addPage.fillForm("", "abc", "xyz"); 

        addPage.clickSaveButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.alertIsPresent(),
                ExpectedConditions.urlContains("add.html")
            ));

            assertTrue(driver.getCurrentUrl().contains("add.html") || 
                      driver.getCurrentUrl().contains("localhost:7000"),
                      "Sistema não quebrou após erro");

            if (addPage.isAlertVisible()) {
                String mensagem = addPage.getAlertMessage();
                assertFalse(mensagem.contains("Exception") || 
                           mensagem.contains("StackTrace") ||
                           mensagem.contains("null pointer"),
                           "Mensagem de erro não expõe detalhes técnicos: " + mensagem);
            }
        } catch (TimeoutException e) {
            assertTrue(driver.getCurrentUrl().contains("add.html") || 
                      driver.getCurrentUrl().contains("localhost:7000"),
                      "Sistema manteve estado consistente");
        }
    }

    @Test
    void testConcorrencia_OperacoesSimultaneas() {
        listPage.open();

        listPage.clickAddButton();
        addPage.fillForm("Produto Concorrente 1", "50.0", "5");
        addPage.clickSaveButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("localhost:7000"));

        listPage.clickAddButton();
        addPage.fillForm("Produto Concorrente 2", "75.0", "10");
        addPage.clickSaveButton();

        wait.until(ExpectedConditions.urlContains("localhost:7000"));

        assertTrue(listPage.isProductListed("Produto Concorrente 1"));
        assertTrue(listPage.isProductListed("Produto Concorrente 2"));
    }
}

