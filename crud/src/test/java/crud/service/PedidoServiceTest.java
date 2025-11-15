package crud.service;

import crud.exception.PedidoNaoEncontradoException;
import crud.exception.ValidacaoException;
import crud.model.ItemPedido;
import crud.model.Pedido;
import crud.model.Produto;
import crud.repository.PedidoRepository;
import crud.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {
    private PedidoRepository pedidoRepository;
    private ProdutoRepository produtoRepository;
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        pedidoRepository = new PedidoRepository();
        produtoRepository = new ProdutoRepository();
        pedidoService = new PedidoService(pedidoRepository, produtoRepository);
    }

    @Test
    void testCriarPedidoComClienteVazio() {
        Produto produto = produtoRepository.save(new Produto("Produto Teste", 10.0, 100));
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 2));

        Pedido pedido = new Pedido("");
        for (ItemPedido item : itens) {
            pedido.adicionarItem(item);
        }

        assertThrows(ValidacaoException.class, () -> {
            pedidoService.criar(pedido);
        });
    }

    @Test
    void testCriarPedidoComClienteNull() {
        Produto produto = produtoRepository.save(new Produto("Produto Teste", 10.0, 100));
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 2));

        Pedido pedido = new Pedido();
        pedido.setCliente(null);
        for (ItemPedido item : itens) {
            pedido.adicionarItem(item);
        }

        assertThrows(ValidacaoException.class, () -> {
            pedidoService.criar(pedido);
        });
    }

    @Test
    void testCriarPedidoSemItens() {
        Pedido pedido = new Pedido("Cliente Teste");

        assertThrows(ValidacaoException.class, () -> {
            pedidoService.criar(pedido);
        });
    }

    @Test
    void testCriarPedidoComProdutoInexistente() {
        Produto produtoFake = new Produto("Produto Fake", 10.0, 100);
        produtoFake.setId(999L);
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produtoFake, 2));

        Pedido pedido = new Pedido("Cliente Teste");
        for (ItemPedido item : itens) {
            pedido.adicionarItem(item);
        }

        assertThrows(ValidacaoException.class, () -> {
            pedidoService.criar(pedido);
        });
    }

    @Test
    void testCriarPedidoComQuantidadeZero() {
        Produto produto = produtoRepository.save(new Produto("Produto Teste", 10.0, 100));
        
        // ItemPedido lança IllegalArgumentException ao criar com quantidade inválida
        assertThrows(IllegalArgumentException.class, () -> {
            new ItemPedido(produto, 0);
        });
    }

    @Test
    void testCriarPedidoComQuantidadeNegativa() {
        Produto produto = produtoRepository.save(new Produto("Produto Teste", 10.0, 100));
        
        // ItemPedido lança IllegalArgumentException ao criar com quantidade inválida
        assertThrows(IllegalArgumentException.class, () -> {
            new ItemPedido(produto, -1);
        });
    }

    @Test
    void testCriarPedidoValido() {
        Produto produto = produtoRepository.save(new Produto("Produto Teste", 10.0, 100));
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 2));

        Pedido pedido = pedidoService.criarPedido("Cliente Teste", itens);

        assertNotNull(pedido.getId());
        assertEquals("Cliente Teste", pedido.getCliente());
        assertEquals(1, pedido.getItens().size());
        assertEquals(20.0, pedido.getTotal());
    }

    @Test
    void testBuscarTodosPedidos() {
        Produto produto = produtoRepository.save(new Produto("Produto", 10.0, 100));
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 1));

        pedidoService.criarPedido("Cliente 1", itens);
        pedidoService.criarPedido("Cliente 2", itens);

        List<Pedido> pedidos = pedidoService.buscarTodos();
        assertEquals(2, pedidos.size());
    }

    @Test
    void testBuscarPorIdPedidoExistente() {
        Produto produto = produtoRepository.save(new Produto("Produto", 10.0, 100));
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 1));

        Pedido pedido = pedidoService.criarPedido("Cliente", itens);
        Pedido encontrado = pedidoService.buscarPorId(pedido.getId());

        assertNotNull(encontrado);
        assertEquals(pedido.getId(), encontrado.getId());
        assertEquals("Cliente", encontrado.getCliente());
    }

    @Test
    void testBuscarPorIdPedidoInexistente() {
        assertThrows(PedidoNaoEncontradoException.class, () -> {
            pedidoService.buscarPorId(999L);
        });
    }

    @Test
    void testBuscarPorIdOptional() {
        Produto produto = produtoRepository.save(new Produto("Produto", 10.0, 100));
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 1));

        Pedido pedido = pedidoService.criarPedido("Cliente", itens);
        Optional<Pedido> encontrado = pedidoService.buscarPorIdOptional(pedido.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(pedido.getId(), encontrado.get().getId());
    }

    @Test
    void testBuscarPorIdOptionalInexistente() {
        Optional<Pedido> encontrado = pedidoService.buscarPorIdOptional(999L);
        assertFalse(encontrado.isPresent());
    }

    @Test
    void testAtualizarPedido() {
        Produto produto1 = produtoRepository.save(new Produto("Produto 1", 10.0, 100));
        Produto produto2 = produtoRepository.save(new Produto("Produto 2", 20.0, 100));

        List<ItemPedido> itensIniciais = new ArrayList<>();
        itensIniciais.add(new ItemPedido(produto1, 2));
        Pedido pedido = pedidoService.criarPedido("Cliente Original", itensIniciais);

        List<ItemPedido> novosItens = new ArrayList<>();
        novosItens.add(new ItemPedido(produto2, 3));
        Pedido pedidoAtualizado = pedidoService.atualizarPedido(pedido.getId(), "Cliente Atualizado", novosItens);

        assertEquals("Cliente Atualizado", pedidoAtualizado.getCliente());
        assertEquals(1, pedidoAtualizado.getItens().size());
        assertEquals(60.0, pedidoAtualizado.getTotal());
    }

    @Test
    void testAtualizarPedidoComMetodoInterface() {
        Produto produto1 = produtoRepository.save(new Produto("Produto 1", 10.0, 100));
        Produto produto2 = produtoRepository.save(new Produto("Produto 2", 20.0, 100));

        List<ItemPedido> itensIniciais = new ArrayList<>();
        itensIniciais.add(new ItemPedido(produto1, 2));
        Pedido pedido = pedidoService.criarPedido("Cliente Original", itensIniciais);

        List<ItemPedido> novosItens = new ArrayList<>();
        novosItens.add(new ItemPedido(produto2, 3));
        Pedido pedidoRequest = new Pedido("Cliente Atualizado");
        for (ItemPedido item : novosItens) {
            pedidoRequest.adicionarItem(item);
        }

        Pedido pedidoAtualizado = pedidoService.atualizar(pedido.getId(), pedidoRequest);

        assertEquals("Cliente Atualizado", pedidoAtualizado.getCliente());
        assertEquals(1, pedidoAtualizado.getItens().size());
        assertEquals(60.0, pedidoAtualizado.getTotal());
    }

    @Test
    void testAtualizarPedidoInexistente() {
        Produto produto = produtoRepository.save(new Produto("Produto", 10.0, 100));
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 1));
        Pedido pedidoRequest = new Pedido("Cliente");
        for (ItemPedido item : itens) {
            pedidoRequest.adicionarItem(item);
        }

        assertThrows(PedidoNaoEncontradoException.class, () -> {
            pedidoService.atualizar(999L, pedidoRequest);
        });
    }

    @Test
    void testAtualizarPedidoComItensNull() {
        Produto produto = produtoRepository.save(new Produto("Produto", 10.0, 100));
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 1));
        Pedido pedido = pedidoService.criarPedido("Cliente", itens);

        Pedido pedidoRequest = new Pedido("Cliente Atualizado");

        assertThrows(ValidacaoException.class, () -> {
            pedidoService.atualizar(pedido.getId(), pedidoRequest);
        });
    }

    @Test
    void testDeletarPedido() {
        Produto produto = produtoRepository.save(new Produto("Produto", 10.0, 100));
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 1));
        Pedido pedido = pedidoService.criarPedido("Cliente", itens);

        pedidoService.deletar(pedido.getId());

        assertThrows(PedidoNaoEncontradoException.class, () -> {
            pedidoService.buscarPorId(pedido.getId());
        });
    }

    @Test
    void testDeletarPedidoComMetodoAuxiliar() {
        Produto produto = produtoRepository.save(new Produto("Produto", 10.0, 100));
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 1));
        Pedido pedido = pedidoService.criarPedido("Cliente", itens);

        pedidoService.deletarPedido(pedido.getId());

        assertThrows(PedidoNaoEncontradoException.class, () -> {
            pedidoService.buscarPorId(pedido.getId());
        });
    }

    @Test
    void testDeletarPedidoInexistente() {
        assertThrows(PedidoNaoEncontradoException.class, () -> {
            pedidoService.deletar(999L);
        });
    }

    @Test
    void testCriarPedidoComItensNull() {
        // Quando itens é null, o pedido é criado sem itens, mas a validação falha
        Pedido pedido = new Pedido("Cliente");
        
        assertThrows(ValidacaoException.class, () -> {
            pedidoService.criar(pedido);
        });
    }

    @Test
    void testCriarPedidoComItemProdutoNull() {
        // ItemPedido não aceita produto null, então testamos diretamente
        assertThrows(IllegalArgumentException.class, () -> {
            new ItemPedido(null, 1);
        });
    }

    @Test
    void testCriarPedidoComItemProdutoSemId() {
        Produto produto = new Produto("Produto", 10.0, 100);
        // Não salva no repositório, então não tem ID
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 1));

        Pedido pedido = new Pedido("Cliente");
        for (ItemPedido item : itens) {
            pedido.adicionarItem(item);
        }

        assertThrows(ValidacaoException.class, () -> {
            pedidoService.criar(pedido);
        });
    }
}

