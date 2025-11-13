package crud.service;

import crud.exception.ProdutoNaoEncontradoException;
import crud.exception.ValidacaoException;
import crud.model.Produto;
import crud.repository.ProdutoRepository;
import net.jqwik.api.*;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.IntRange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoServiceTest {

    private ProdutoRepository repository = new ProdutoRepository();
    private ProdutoService service = new ProdutoService(repository);

    // --- Partições Equivalentes e Análise de Limites (Testes de Sucesso) ---

    @ParameterizedTest
    @CsvSource({
            "Produto A, 1.0, 0", // Limite inferior do Preço e Estoque
            "Produto B, 9999.99, 1000", // Valor Típico Superior
            "Produto C, 0.01, 1" // Limite inferior do Preço
    })
    void testCriarProduto_Sucesso(String nome, double preco, int estoque) {
        Produto produto = service.criarProduto(nome, preco, estoque);
        assertNotNull(produto.getId());
        assertEquals(nome, produto.getNome());
        assertEquals(preco, produto.getPreco());
        assertEquals(estoque, produto.getEstoque());
    }

    @Test
    void testBuscarTodos_Vazio() {
        assertTrue(service.buscarTodos().isEmpty());
    }

    @Test
    void testBuscarTodos_Preenchido() {
        service.criarProduto("P1", 10.0, 1);
        service.criarProduto("P2", 20.0, 2);
        List<Produto> produtos = service.buscarTodos();
        assertEquals(2, produtos.size());
    }

    @Test
    void testBuscarPorId_Existente() {
        Produto p1 = service.criarProduto("P1", 10.0, 1);
        Produto encontrado = service.buscarPorId(p1.getId());
        assertEquals(p1.getId(), encontrado.getId());
    }

    @Test
    void testAtualizarProduto_Sucesso() {
        Produto p = service.criarProduto("Antigo", 100.0, 10);
        Long id = p.getId();

        Produto atualizado = service.atualizarProduto(id, "Novo", 150.0, 5);

        assertEquals(id, atualizado.getId());
        assertEquals("Novo", atualizado.getNome());
        assertEquals(150.0, atualizado.getPreco());
        assertEquals(5, atualizado.getEstoque());
    }

    @Test
    void testDeletarProduto_Sucesso() {
        Produto p = service.criarProduto("A Deletar", 10.0, 1);
        Long id = p.getId();

        assertDoesNotThrow(() -> service.deletarProduto(id));
        assertTrue(service.buscarTodos().isEmpty());
    }

    // --- Testes de Falha (Exceções) ---

    @ParameterizedTest
    @CsvSource({
            "'', 10.0, 10, O nome do produto é obrigatório.", // Nome vazio (Limite)
            "'   ', 10.0, 10, O nome do produto é obrigatório.", // Nome em branco
            "Produto X, 0.0, 10, O preço deve ser maior que zero.", // Preço = 0 (Limite)
            "Produto Y, -5.0, 10, O preço deve ser maior que zero.", // Preço negativo
            "Produto Z, 10.0, -1, O estoque não pode ser negativo." // Estoque negativo (Limite)
    })
    void testCriarProduto_FalhaValidacao(String nome, double preco, int estoque, String mensagemEsperada) {
        ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
                service.criarProduto(nome, preco, estoque));
        assertEquals(mensagemEsperada, exception.getMessage());
    }

    @Test
    void testBuscarPorId_Inexistente() {
        ProdutoNaoEncontradoException exception = assertThrows(ProdutoNaoEncontradoException.class, () ->
                service.buscarPorId(999L));
        assertEquals("Produto com ID 999 não encontrado.", exception.getMessage());
    }

    @Test
    void testAtualizarProduto_Inexistente() {
        assertThrows(ProdutoNaoEncontradoException.class, () ->
                service.atualizarProduto(999L, "Novo", 10.0, 1));
    }

    @Test
    void testAtualizarProduto_FalhaValidacao() {
        Produto p = service.criarProduto("P", 10.0, 1);
        Long id = p.getId();

        ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
                service.atualizarProduto(id, "Invalido", -1.0, 1));
        assertEquals("O preço deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void testDeletarProduto_Inexistente() {
        ProdutoNaoEncontradoException exception = assertThrows(ProdutoNaoEncontradoException.class, () ->
                service.deletarProduto(999L));
        assertEquals("Produto com ID 999 não encontrado.", exception.getMessage());
    }

    // --- Testes de Propriedades (Jqwik) ---

    @Property(tries = 50)
    void testCriacaoProdutoPropriedades(
            @ForAll("nomesValidos") String nome,
            @ForAll @DoubleRange(min = 0.01, max = 1000.0) double preco,
            @ForAll @IntRange(min = 0, max = 100) int estoque) {

        Produto produto = service.criarProduto(nome, preco, estoque);

        assertNotNull(produto.getId());
        assertEquals(nome, produto.getNome());
        assertEquals(preco, produto.getPreco());
        assertEquals(estoque, produto.getEstoque());
    }

    @Property(tries = 50)
    void testAtualizacaoProdutoPropriedades(
            @ForAll("nomesValidos") String nomeOriginal,
            @ForAll @DoubleRange(min = 0.01, max = 1000.0) double precoOriginal,
            @ForAll @IntRange(min = 0, max = 100) int estoqueOriginal,
            @ForAll("nomesValidos") String nomeNovo,
            @ForAll @DoubleRange(min = 0.01, max = 1000.0) double precoNovo,
            @ForAll @IntRange(min = 0, max = 100) int estoqueNovo) {

        Produto produto = service.criarProduto(nomeOriginal, precoOriginal, estoqueOriginal);
        Long id = produto.getId();

        Produto atualizado = service.atualizarProduto(id, nomeNovo, precoNovo, estoqueNovo);

        assertEquals(id, atualizado.getId());
        assertEquals(nomeNovo, atualizado.getNome());
        assertEquals(precoNovo, atualizado.getPreco());
        assertEquals(estoqueNovo, atualizado.getEstoque());

        Produto persistido = repository.findById(id).get();
        assertEquals(nomeNovo, persistido.getNome());
    }

    @Property(tries = 10)
    void testValidacaoPropriedades_NomeVazio(
        @ForAll("nomesInvalidos") String nomeInvalido,
        @ForAll @DoubleRange(min = 1.0, max = 100.0) double preco,
        @ForAll @IntRange(min = 1, max = 10) int estoque) {

    ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
        service.criarProduto(nomeInvalido, preco, estoque));
    assertEquals("O nome do produto é obrigatório.", exception.getMessage());
    }

    @Provide
    Arbitrary<String> nomesValidos() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .alpha()
                .ofMinLength(3)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> nomesInvalidos() {
        return Arbitraries.of("", " ", "   ");
    }
}