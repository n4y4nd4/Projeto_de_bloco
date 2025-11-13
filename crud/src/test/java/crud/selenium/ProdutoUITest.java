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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProdutoUITest {

    private WebDriver driver;
    private Javalin app;
    private ProdutoListPage listPage;
    private ProdutoAddPage addPage;
    private ProdutoEditPage editPage;

    @BeforeAll
    void setupJavalin() {
        app = Main.startServer();
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // Rodar em modo headless no CI/local sem GUI
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
    }

    @BeforeEach
    void setupTest() {
        listPage = new ProdutoListPage(driver);
        addPage = new ProdutoAddPage(driver);
        editPage = new ProdutoEditPage(driver);
        
        // Limpa o estado da aplicação antes de cada teste
        try {
            driver.get("http://localhost:7000/api/produtos/deleteall"); 
        } catch (Exception ignored) {}
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        if (app != null) {
            app.stop();
        }
    }

    // --- Teste do Fluxo CRUD ---
    
    @Test
    @Order(1)
    void testFluxoCRUDCompleto() {
        String nomeOriginal = "Teclado Mecânico X";
        String nomeEditado = "Teclado Mecânico Pro V2";

        // C: Criar Produto (Sucesso)
        listPage.open();
        listPage.clickAddButton();
        addPage.fillForm(nomeOriginal, "450.00", "50");
        addPage.clickSaveButton();
        
        // R: Listagem e Confirmação
        assertTrue(driver.getCurrentUrl().contains("index.html"));
        assertTrue(listPage.getAlertMessage().contains("adicionado com sucesso"));
        assertTrue(listPage.isProductListed(nomeOriginal));
        
        // U: Editar Produto
        listPage.clickEditButton(1L); 
        editPage.fillForm(nomeEditado, "600.50", "30");
        editPage.clickUpdateButton();
        
        // R: Listagem e Confirmação da Edição
        assertTrue(driver.getCurrentUrl().contains("index.html"));
        assertTrue(listPage.getAlertMessage().contains("atualizado com sucesso"));
        assertTrue(listPage.isProductListed(nomeEditado));
        assertFalse(listPage.isProductListed(nomeOriginal));

        // D: Excluir Produto
        listPage.clickDeleteButton(1L);
        
        // R: Listagem e Confirmação da Exclusão
        assertTrue(driver.getCurrentUrl().contains("index.html"));
        assertTrue(listPage.getAlertMessage().contains("excluído com sucesso"));
        assertFalse(listPage.isProductListed(nomeEditado));
    }
    
    // --- Testes Negativos e Parametrizados ---

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
        
        assertTrue(addPage.isAlertVisible());
        assertTrue(addPage.getAlertMessage().contains(mensagemEsperada));
        
        assertTrue(driver.getCurrentUrl().contains("add.html"));
    }
}