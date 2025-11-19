package crud.controller;

import crud.exception.PedidoNaoEncontradoException;
import crud.exception.ValidacaoException;
import crud.model.ItemPedido;
import crud.model.Pedido;
import crud.model.Produto;
import crud.service.PedidoService;
import io.javalin.http.Context;
import io.javalin.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class PedidoControllerTest {
    private PedidoService service;
    private PedidoController controller;
    private Context ctx;

    @BeforeEach
    void setUp() {
        service = mock(PedidoService.class);
        controller = new PedidoController(service);
        ctx = mock(Context.class);

        when(ctx.status(anyInt())).thenReturn(ctx);
    }

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
    void testBuscarTodos_Sucesso() {
        List<Pedido> pedidos = new ArrayList<>();
        Produto produto = new Produto(1L, "Produto", 10.0, 100);
        Pedido pedido1 = new Pedido("Cliente 1");
        pedido1.adicionarItem(new ItemPedido(produto, 2));
        pedidos.add(pedido1);

        when(service.buscarTodos()).thenReturn(pedidos);

        controller.buscarTodos(ctx);

        verify(service).buscarTodos();
        verify(ctx).json(pedidos);
    }

    @Test
    void testBuscarPorId_Sucesso() {
        Long id = 1L;
        Produto produto = new Produto(1L, "Produto", 10.0, 100);
        Pedido pedido = new Pedido("Cliente");
        pedido.setId(id);
        pedido.adicionarItem(new ItemPedido(produto, 2));

        Validator<Long> pathParamMock = createPathParamMock(id);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        when(service.buscarPorId(id)).thenReturn(pedido);

        controller.buscarPorId(ctx);

        verify(service).buscarPorId(id);
        verify(ctx).json(pedido);
    }

    @Test
    void testBuscarPorId_PedidoNaoEncontrado() {
        Long id = 999L;
        Validator<Long> pathParamMock = createPathParamMock(id);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        when(service.buscarPorId(id)).thenThrow(new PedidoNaoEncontradoException(id));

        controller.buscarPorId(ctx);

        verify(service).buscarPorId(id);
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).status(404);
        verify(ctx).json(captor.capture());
        assertTrue(captor.getValue().get("message").contains("Pedido com ID 999 não encontrado"));
    }

    @Test
    void testBuscarPorId_IDInvalido() {
        Validator<Long> pathParamMock = createPathParamMockWithException(new NumberFormatException());
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);

        controller.buscarPorId(ctx);

        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).status(400);
        verify(ctx).json(captor.capture());
        assertEquals("ID inválido.", captor.getValue().get("message"));
    }

    @Test
    void testCriarPedido_Sucesso() {
        Produto produto = new Produto(1L, "Produto", 10.0, 100);
        Pedido pedidoRequest = new Pedido("Cliente");
        pedidoRequest.adicionarItem(new ItemPedido(produto, 2));

        Pedido pedidoCriado = new Pedido("Cliente");
        pedidoCriado.setId(1L);
        pedidoCriado.adicionarItem(new ItemPedido(produto, 2));

        when(ctx.bodyAsClass(Pedido.class)).thenReturn(pedidoRequest);
        when(service.criar(pedidoRequest)).thenReturn(pedidoCriado);

        controller.criarPedido(ctx);

        verify(service).criar(pedidoRequest);
        verify(ctx).status(201);
        verify(ctx).json(pedidoCriado);
    }

    @Test
    void testCriarPedido_ValidacaoException() {
        Produto produto = new Produto(1L, "Produto", 10.0, 100);
        Pedido pedidoRequest = new Pedido("");
        pedidoRequest.adicionarItem(new ItemPedido(produto, 2));

        when(ctx.bodyAsClass(Pedido.class)).thenReturn(pedidoRequest);
        when(service.criar(pedidoRequest)).thenThrow(new ValidacaoException("O nome do cliente é obrigatório."));

        controller.criarPedido(ctx);

        verify(service).criar(pedidoRequest);
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).status(400);
        verify(ctx).json(captor.capture());
        assertEquals("O nome do cliente é obrigatório.", captor.getValue().get("message"));
    }

    @Test
    void testCriarPedido_DadosInvalidos() {
        when(ctx.bodyAsClass(Pedido.class)).thenThrow(new RuntimeException("Erro ao desserializar"));

        controller.criarPedido(ctx);

        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).status(400);
        verify(ctx).json(captor.capture());
        assertEquals("Dados inválidos ou formato incorreto.", captor.getValue().get("message"));
    }

    @Test
    void testAtualizarPedido_Sucesso() {
        Long id = 1L;
        Produto produto = new Produto(1L, "Produto", 10.0, 100);
        Pedido pedidoRequest = new Pedido("Cliente Atualizado");
        pedidoRequest.adicionarItem(new ItemPedido(produto, 3));

        Pedido pedidoAtualizado = new Pedido("Cliente Atualizado");
        pedidoAtualizado.setId(id);
        pedidoAtualizado.adicionarItem(new ItemPedido(produto, 3));

        Validator<Long> pathParamMock = createPathParamMock(id);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        when(ctx.bodyAsClass(Pedido.class)).thenReturn(pedidoRequest);
        when(service.atualizar(id, pedidoRequest)).thenReturn(pedidoAtualizado);

        controller.atualizarPedido(ctx);

        verify(service).atualizar(id, pedidoRequest);
        verify(ctx).status(200);
        verify(ctx).json(pedidoAtualizado);
    }

    @Test
    void testAtualizarPedido_PedidoNaoEncontrado() {
        Long id = 999L;
        Produto produto = new Produto(1L, "Produto", 10.0, 100);
        Pedido pedidoRequest = new Pedido("Cliente");
        pedidoRequest.adicionarItem(new ItemPedido(produto, 2));

        Validator<Long> pathParamMock = createPathParamMock(id);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        when(ctx.bodyAsClass(Pedido.class)).thenReturn(pedidoRequest);
        when(service.atualizar(id, pedidoRequest)).thenThrow(new PedidoNaoEncontradoException(id));

        controller.atualizarPedido(ctx);

        verify(service).atualizar(id, pedidoRequest);
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).status(404);
        verify(ctx).json(captor.capture());
        assertTrue(captor.getValue().get("message").contains("Pedido com ID 999 não encontrado"));
    }

    @Test
    void testAtualizarPedido_ValidacaoException() {
        Long id = 1L;
        Produto produto = new Produto(1L, "Produto", 10.0, 100);
        Pedido pedidoRequest = new Pedido("");
        pedidoRequest.adicionarItem(new ItemPedido(produto, 2));

        Validator<Long> pathParamMock = createPathParamMock(id);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        when(ctx.bodyAsClass(Pedido.class)).thenReturn(pedidoRequest);
        when(service.atualizar(id, pedidoRequest)).thenThrow(new ValidacaoException("O nome do cliente é obrigatório."));

        controller.atualizarPedido(ctx);

        verify(service).atualizar(id, pedidoRequest);
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).status(400);
        verify(ctx).json(captor.capture());
        assertEquals("O nome do cliente é obrigatório.", captor.getValue().get("message"));
    }

    @Test
    void testAtualizarPedido_DadosInvalidos() {
        Long id = 1L;
        Validator<Long> pathParamMock = createPathParamMock(id);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        when(ctx.bodyAsClass(Pedido.class)).thenThrow(new RuntimeException("Erro ao desserializar"));

        controller.atualizarPedido(ctx);

        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).status(400);
        verify(ctx).json(captor.capture());
        assertEquals("Dados inválidos ou formato incorreto.", captor.getValue().get("message"));
    }

    @Test
    void testDeletarPedido_Sucesso() {
        Long id = 1L;
        Validator<Long> pathParamMock = createPathParamMock(id);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        doNothing().when(service).deletar(id);

        controller.deletarPedido(ctx);

        verify(service).deletar(id);
        verify(ctx).status(200);
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).json(captor.capture());
        assertEquals("Pedido deletado com sucesso.", captor.getValue().get("message"));
    }

    @Test
    void testDeletarPedido_PedidoNaoEncontrado() {
        Long id = 999L;
        Validator<Long> pathParamMock = createPathParamMock(id);
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);
        doThrow(new PedidoNaoEncontradoException(id)).when(service).deletar(id);

        controller.deletarPedido(ctx);

        verify(service).deletar(id);
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).status(404);
        verify(ctx).json(captor.capture());
        assertTrue(captor.getValue().get("message").contains("Pedido com ID 999 não encontrado"));
    }

    @Test
    void testDeletarPedido_IDInvalido() {
        Validator<Long> pathParamMock = createPathParamMockWithException(new NumberFormatException());
        when(ctx.pathParamAsClass(eq("id"), eq(Long.class))).thenReturn(pathParamMock);

        controller.deletarPedido(ctx);

        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ctx).status(400);
        verify(ctx).json(captor.capture());
        assertEquals("ID inválido.", captor.getValue().get("message"));
    }
}

