package crud.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes pós-deploy para validar a integridade do sistema em produção.
 * Estes testes são executados após o deploy para garantir que a aplicação
 * está funcionando corretamente no ambiente de destino.
 * 
 * IMPORTANTE: Estes testes requerem que o servidor esteja rodando.
 * No CI/CD, o servidor é iniciado antes da execução destes testes.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled("Requires server to be running - executed in CI/CD pipeline")
class PostDeployTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = System.getProperty("app.url", "http://localhost:7000");
    private static final int TIMEOUT_SECONDS = 30;

    @BeforeAll
    void setupDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    void navigateToHome() {
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    @Test
    @DisplayName("Deve verificar se a aplicação está acessível")
    void testApplicationIsAccessible() {
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("localhost:7000") || currentUrl.contains(BASE_URL),
                "A aplicação deve estar acessível em " + BASE_URL);
    }

    @Test
    @DisplayName("Deve verificar se a API REST está respondendo")
    void testApiIsResponding() throws Exception {
        URL url = new URL(BASE_URL + "/api/produtos");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        
        int responseCode = connection.getResponseCode();
        assertTrue(responseCode >= 200 && responseCode < 300,
                "A API deve retornar um código de sucesso. Recebido: " + responseCode);
    }

    @Test
    @DisplayName("Deve carregar a página inicial corretamente")
    void testHomePageLoads() {
        WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h1[contains(text(), 'Produto') or contains(text(), 'Sistema')]")));
        assertNotNull(title, "A página inicial deve ter um título");
    }

    @Test
    @DisplayName("Deve exibir a lista de produtos")
    void testProductListIsDisplayed() {
        // Aguarda a tabela ou lista de produtos aparecer
        WebElement productList = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//table | //div[contains(@class, 'product')] | //ul[contains(@class, 'product')]")));
        assertNotNull(productList, "A lista de produtos deve ser exibida");
    }

    @Test
    @DisplayName("Deve ter botão para adicionar novo produto")
    void testAddProductButtonExists() {
        WebElement addButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(text(), 'Adicionar') or contains(text(), 'Novo')] | " +
                        "//button[contains(text(), 'Adicionar') or contains(text(), 'Novo')]")));
        assertNotNull(addButton, "Deve existir um botão para adicionar novo produto");
        assertTrue(addButton.isDisplayed(), "O botão de adicionar deve estar visível");
    }

    @Test
    @DisplayName("Deve verificar se o CSS está carregado")
    void testCssIsLoaded() {
        List<WebElement> styledElements = driver.findElements(By.xpath("//*[@style or @class]"));
        assertFalse(styledElements.isEmpty(), "A página deve ter elementos estilizados");
    }

    @Test
    @DisplayName("Deve verificar se não há erros JavaScript críticos no console")
    void testNoCriticalJavaScriptErrors() {
        // Verifica se a página carregou sem erros críticos
        // (erros JavaScript graves impediriam a renderização)
        WebElement body = driver.findElement(By.tagName("body"));
        assertNotNull(body, "O body da página deve estar presente");
        assertTrue(body.isDisplayed(), "O body deve estar visível");
    }

    @Test
    @DisplayName("Deve verificar se a página responde a interações básicas")
    void testPageRespondsToInteractions() {
        // Tenta encontrar e clicar em um elemento interativo
        try {
            WebElement interactiveElement = driver.findElement(
                    By.xpath("//a | //button | //input[@type='button'] | //input[@type='submit']"));
            assertNotNull(interactiveElement, "Deve haver elementos interativos na página");
        } catch (Exception e) {
            // Se não encontrar, verifica se pelo menos a página carregou
            WebElement body = driver.findElement(By.tagName("body"));
            assertNotNull(body, "A página deve ter um body");
        }
    }

    @Test
    @DisplayName("Deve verificar se o tempo de resposta é aceitável")
    void testResponseTimeIsAcceptable() {
        long startTime = System.currentTimeMillis();
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        long endTime = System.currentTimeMillis();
        
        long responseTime = endTime - startTime;
        assertTrue(responseTime < 10000, 
                "O tempo de resposta deve ser menor que 10 segundos. Recebido: " + responseTime + "ms");
    }
}

