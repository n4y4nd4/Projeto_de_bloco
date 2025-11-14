package crud.controller;

import crud.exception.ProdutoNaoEncontradoException;
import crud.exception.ValidacaoException;
import crud.model.Produto;
import crud.service.ProdutoService;
import io.javalin.http.Context;
import io.javalin.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProdutoControllerTest {

    private ProdutoService service;
    private ProdutoController controller;
    private Context ctx;

    @BeforeEach
    void setUp() {
        service = mock(ProdutoService.class);
        controller = new ProdutoController(service);
        ctx = mock(Context.class);
        // Configura o mock para retornar ele mesmo quando status() é chamado,
        // permitindo encadeamento como ctx.status(200).json(...)
        when(ctx.status(anyInt())).thenReturn(ctx);
    }

    @Test
    void testBuscarTodos_Sucesso() {
        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto("Produto 1", 10.0, 5));
        produtos.add(new Produto("Produto 2", 20.0, 10));

        when(service.buscarTodos()).thenReturn(produtos);

        controller.buscarTodos(ctx);

        verify(service).buscarTodos();
        verify(ctx).json(produtos);
    }

    @Test
    void testBuscarTodos_Vazio() {
        when(service.buscarTodos()).thenReturn(new ArrayList<>());

        controller.buscarTodos(ctx);

        verify(service).buscarTodos();
        verify(ctx).json(any(List.class));
    }

    // Helper method to create a mock Validator that responds to get() calls
    @SuppressWarnings("unchecked")
    private Validator<Long> createPathParamMock(Long value) {
        Validator<Long> validator = mock(Validator.class);
        when(validator.get()).thenReturn(value);
        return validator;
    }

    @SuppressWarnings("unchecked")
    private Validator<Long> createPathParamMockWithException(RuntimeException exception) {
        Validator<Long> validator = mock(Validator.class);
        when(validator.get()).thenThrow(exception);
        return validator;
    }

    @Test
    void testBuscarPorId_Sucesso() {
        Produto produto = new Produto("Produto", 10.0, 5);
        produto.setId(1L);

        Validator<Long> pathParamMock = createPathParamMock(1L);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        when(service.buscarPorId(1L)).thenReturn(produto);

        controller.buscarPorId(ctx);

        verify(service).buscarPorId(1L);
        verify(ctx).json(produto);
        verify(ctx, never()).status(anyInt());
    }

    @Test
    void testBuscarPorId_ProdutoNaoEncontrado() {
        Validator<Long> pathParamMock = createPathParamMock(999L);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        when(service.buscarPorId(999L)).thenThrow(new ProdutoNaoEncontradoException(999L));

        controller.buscarPorId(ctx);

        verify(service).buscarPorId(999L);
        verify(ctx).status(404);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).json(captor.capture());
        assertTrue(captor.getValue().containsKey("message"));
    }

    @Test
    void testBuscarPorId_IDInvalido() {
        Validator<Long> pathParamMock = createPathParamMockWithException(new NumberFormatException());
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);

        controller.buscarPorId(ctx);

        verify(ctx).status(400);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).json(captor.capture());
        assertEquals("ID inválido.", captor.getValue().get("message"));
    }

    @Test
    void testCriarProduto_Sucesso() {
        Produto produtoRequest = new Produto("Novo Produto", 15.0, 10);
        Produto produtoSalvo = new Produto("Novo Produto", 15.0, 10);
        produtoSalvo.setId(1L);

        when(ctx.bodyAsClass(Produto.class)).thenReturn(produtoRequest);
        when(service.criarProduto(anyString(), anyDouble(), anyInt())).thenReturn(produtoSalvo);

        controller.criarProduto(ctx);

        verify(service).criarProduto("Novo Produto", 15.0, 10);
        verify(ctx).status(201);
        verify(ctx).json(produtoSalvo);
    }

    @ParameterizedTest
    @CsvSource({
            "'', 10.0, 10",
            "'   ', 10.0, 10",
            "Produto, 0.0, 10",
            "Produto, -5.0, 10",
            "Produto, 10.0, -1"
    })
    void testCriarProduto_ValidacaoException(String nome, double preco, int estoque) {
        Produto produtoRequest = new Produto(nome, preco, estoque);
        String mensagemEsperada = nome.trim().isEmpty() ? "O nome do produto é obrigatório." :
                                 preco <= 0 ? "O preço deve ser maior que zero." :
                                 "O estoque não pode ser negativo.";

        when(ctx.bodyAsClass(Produto.class)).thenReturn(produtoRequest);
        when(service.criarProduto(anyString(), anyDouble(), anyInt()))
                .thenThrow(new ValidacaoException(mensagemEsperada));

        controller.criarProduto(ctx);

        verify(ctx).status(400);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).json(captor.capture());
        assertEquals(mensagemEsperada, captor.getValue().get("message"));
    }

    @Test
    void testCriarProduto_ErroGenerico() {
        Produto produtoRequest = new Produto("Produto", 10.0, 5);

        when(ctx.bodyAsClass(Produto.class)).thenReturn(produtoRequest);
        when(service.criarProduto(anyString(), anyDouble(), anyInt()))
                .thenThrow(new RuntimeException("Erro inesperado"));

        controller.criarProduto(ctx);

        verify(ctx).status(400);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).json(captor.capture());
        assertEquals("Dados inválidos ou formato incorreto.", captor.getValue().get("message"));
    }

    @Test
    void testAtualizarProduto_Sucesso() {
        Produto produtoRequest = new Produto("Produto Atualizado", 20.0, 15);
        Produto produtoAtualizado = new Produto("Produto Atualizado", 20.0, 15);
        produtoAtualizado.setId(1L);

        Validator<Long> pathParamMock = createPathParamMock(1L);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        when(ctx.bodyAsClass(Produto.class)).thenReturn(produtoRequest);
        when(service.atualizarProduto(anyLong(), anyString(), anyDouble(), anyInt()))
                .thenReturn(produtoAtualizado);

        controller.atualizarProduto(ctx);

        verify(service).atualizarProduto(1L, "Produto Atualizado", 20.0, 15);
        verify(ctx).status(200);
        verify(ctx).json(produtoAtualizado);
    }

    @Test
    void testAtualizarProduto_ProdutoNaoEncontrado() {
        Produto produtoRequest = new Produto("Produto", 10.0, 5);

        Validator<Long> pathParamMock = createPathParamMock(999L);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        when(ctx.bodyAsClass(Produto.class)).thenReturn(produtoRequest);
        when(service.atualizarProduto(anyLong(), anyString(), anyDouble(), anyInt()))
                .thenThrow(new ProdutoNaoEncontradoException(999L));

        controller.atualizarProduto(ctx);

        verify(ctx).status(404);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).json(captor.capture());
        assertTrue(captor.getValue().containsKey("message"));
    }

    @Test
    void testAtualizarProduto_ValidacaoException() {
        Produto produtoRequest = new Produto("Produto", -10.0, 5);

        Validator<Long> pathParamMock = createPathParamMock(1L);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        when(ctx.bodyAsClass(Produto.class)).thenReturn(produtoRequest);
        when(service.atualizarProduto(anyLong(), anyString(), anyDouble(), anyInt()))
                .thenThrow(new ValidacaoException("O preço deve ser maior que zero."));

        controller.atualizarProduto(ctx);

        verify(ctx).status(400);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).json(captor.capture());
        assertEquals("O preço deve ser maior que zero.", captor.getValue().get("message"));
    }

    @Test
    void testDeletarProduto_Sucesso() {
        Validator<Long> pathParamMock = createPathParamMock(1L);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        doNothing().when(service).deletarProduto(1L);

        controller.deletarProduto(ctx);

        verify(service).deletarProduto(1L);
        verify(ctx).status(200);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).json(captor.capture());
        assertEquals("Produto deletado com sucesso.", captor.getValue().get("message"));
    }

    @Test
    void testDeletarProduto_ProdutoNaoEncontrado() {
        Validator<Long> pathParamMock = createPathParamMock(999L);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        doThrow(new ProdutoNaoEncontradoException(999L)).when(service).deletarProduto(999L);

        controller.deletarProduto(ctx);

        verify(service).deletarProduto(999L);
        verify(ctx).status(404);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).json(captor.capture());
        assertTrue(captor.getValue().containsKey("message"));
    }

    @Test
    void testDeletarProduto_IDInvalido() {
        Validator<Long> pathParamMock = createPathParamMockWithException(new NumberFormatException());
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);

        controller.deletarProduto(ctx);

        verify(ctx).status(400);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).json(captor.capture());
        assertEquals("ID inválido.", captor.getValue().get("message"));
    }
}

