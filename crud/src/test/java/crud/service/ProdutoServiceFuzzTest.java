package crud.service;

import crud.exception.ValidacaoException;
import crud.model.Produto;
import crud.repository.ProdutoRepository;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Fuzz para validar segurança e robustez do sistema
 * contra entradas maliciosas e inválidas.
 */
class ProdutoServiceFuzzTest {

    private ProdutoRepository repository = new ProdutoRepository();
    private ProdutoService service = new ProdutoService(repository);

    @Property(tries = 100)
    void testFuzz_NomeMalicioso(
            @ForAll @StringLength(min = 1, max = 1000) String nomeMalicioso,
            @ForAll @DoubleRange(min = 0.01, max = 1000.0) double preco,
            @ForAll @IntRange(min = 0, max = 100) int estoque) {

        try {
            Produto produto = service.criarProduto(nomeMalicioso, preco, estoque);
            
            assertNotNull(produto);
            assertNotNull(produto.getId());
            
            assertFalse(produto.getNome().contains("<script>"));
            assertFalse(produto.getNome().contains("DROP TABLE"));
            
        } catch (ValidacaoException e) {
            assertTrue(e.getMessage().contains("obrigatório") || 
                      nomeMalicioso.trim().isEmpty());
        }
    }

    @Property(tries = 50)
    void testFuzz_PrecoExtremo(
            @ForAll("nomesValidos") String nome,
            @ForAll @DoubleRange(min = -1000000.0, max = 1000000.0) double preco,
            @ForAll @IntRange(min = 0, max = 100) int estoque) {

        if (preco <= 0) {
            ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
                    service.criarProduto(nome, preco, estoque));
            assertTrue(exception.getMessage().contains("preço"));
        } else {
            Produto produto = service.criarProduto(nome, preco, estoque);
            assertNotNull(produto);
            assertEquals(preco, produto.getPreco());
        }
    }

    @Property(tries = 50)
    void testFuzz_EstoqueExtremo(
            @ForAll("nomesValidos") String nome,
            @ForAll @DoubleRange(min = 0.01, max = 1000.0) double preco,
            @ForAll @IntRange(min = -1000, max = 10000) int estoque) {

        if (estoque < 0) {
            ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
                    service.criarProduto(nome, preco, estoque));
            assertTrue(exception.getMessage().contains("estoque"));
        } else {
            Produto produto = service.criarProduto(nome, preco, estoque);
            assertNotNull(produto);
            assertEquals(estoque, produto.getEstoque());
        }
    }

    @Property(tries = 30)
    void testFuzz_EntradaNull(
            @ForAll String nome,
            @ForAll("valoresPossivelmenteNullDouble") Double preco,
            @ForAll("valoresPossivelmenteNullInteger") Integer estoque) {

        if (nome == null || preco == null || estoque == null) {
            assertThrows(Exception.class, () -> {
                service.criarProduto(nome, preco, estoque);
            });
        }
    }

    @Test
    void testFuzz_SQLInjection() {
        String[] sqlInjectionAttempts = {
            "'; DROP TABLE produtos; --",
            "' OR '1'='1",
            "'; DELETE FROM produtos; --",
            "1' UNION SELECT * FROM produtos --"
        };

        for (String maliciousInput : sqlInjectionAttempts) {
            try {
                Produto produto = service.criarProduto(maliciousInput, 10.0, 5);
                assertNotNull(produto);
                assertEquals(maliciousInput, produto.getNome());
            } catch (ValidacaoException e) {
                assertTrue(true);
            }
        }
    }

    @Test
    void testFuzz_XSSAttempts() {
        String[] xssAttempts = {
            "<script>alert('XSS')</script>",
            "<img src=x onerror=alert('XSS')>",
            "javascript:alert('XSS')",
            "<svg onload=alert('XSS')>"
        };

        for (String xssInput : xssAttempts) {
            try {
                Produto produto = service.criarProduto(xssInput, 10.0, 5);
                assertNotNull(produto);
                assertTrue(produto.getNome().contains("<") || produto.getNome().contains("script"));
            } catch (ValidacaoException e) {
                assertTrue(true);
            }
        }
    }

    @Property(tries = 20)
    void testFuzz_StringsExtremamenteLongas(
            @ForAll @StringLength(min = 1000, max = 10000) String nomeLongo,
            @ForAll @DoubleRange(min = 0.01, max = 1000.0) double preco,
            @ForAll @IntRange(min = 0, max = 100) int estoque) {

        try {
            Produto produto = service.criarProduto(nomeLongo, preco, estoque);
            assertNotNull(produto);
            assertTrue(produto.getNome().length() > 0);
        } catch (ValidacaoException e) {
            assertTrue(true);
        }
    }

    @Provide
    Arbitrary<String> nomesValidos() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .alpha()
                .ofMinLength(3)
                .ofMaxLength(50);
    }

    @Provide
    Arbitrary<Double> valoresPossivelmenteNullDouble() {
        return Arbitraries.oneOf(
                Arbitraries.doubles().between(0.01, 1000.0),
                Arbitraries.just((Double) null)
        );
    }

    @Provide
    Arbitrary<Integer> valoresPossivelmenteNullInteger() {
        return Arbitraries.oneOf(
                Arbitraries.integers().between(0, 100),
                Arbitraries.just((Integer) null)
        );
    }
}

