package crud.repository;

import crud.model.ItemPedido;
import crud.model.Pedido;
import crud.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PedidoRepositoryTest {
    private PedidoRepository repository;
    private Produto produto;

    @BeforeEach
    void setUp() {
        repository = new PedidoRepository();
        produto = new Produto(1L, "Produto Teste", 10.0, 100);
    }

    @Test
    void testSaveNovoPedido() {
        Pedido pedido = new Pedido("Cliente Teste");
        pedido.adicionarItem(new ItemPedido(produto, 2));

        Pedido salvo = repository.save(pedido);

        assertNotNull(salvo.getId());
        assertEquals("Cliente Teste", salvo.getCliente());
        assertEquals(1, salvo.getItens().size());
    }

    @Test
    void testSavePedidoComIdExistente() {
        Pedido pedido1 = new Pedido("Cliente 1");
        pedido1.adicionarItem(new ItemPedido(produto, 1));
        Pedido salvo1 = repository.save(pedido1);
        Long id = salvo1.getId();

        Pedido pedido2 = new Pedido("Cliente 2");
        pedido2.setId(id);
        pedido2.adicionarItem(new ItemPedido(produto, 2));
        Pedido salvo2 = repository.save(pedido2);

        assertEquals(id, salvo2.getId());
        assertEquals("Cliente 2", salvo2.getCliente());
    }

    @Test
    void testSavePedidoComIdPreDefinido() {
        Pedido pedido = new Pedido("Cliente");
        pedido.setId(100L);
        pedido.adicionarItem(new ItemPedido(produto, 1));

        Pedido salvo = repository.save(pedido);

        assertEquals(100L, salvo.getId());
    }

    @Test
    void testFindByIdExistente() {
        Pedido pedido = new Pedido("Cliente");
        pedido.adicionarItem(new ItemPedido(produto, 1));
        Pedido salvo = repository.save(pedido);

        Optional<Pedido> encontrado = repository.findById(salvo.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(salvo.getId(), encontrado.get().getId());
        assertEquals("Cliente", encontrado.get().getCliente());
    }

    @Test
    void testFindByIdInexistente() {
        Optional<Pedido> encontrado = repository.findById(999L);
        assertFalse(encontrado.isPresent());
    }

    @Test
    void testFindAllVazio() {
        List<Pedido> pedidos = repository.findAll();
        assertTrue(pedidos.isEmpty());
    }

    @Test
    void testFindAllComPedidos() {
        Pedido pedido1 = new Pedido("Cliente 1");
        pedido1.adicionarItem(new ItemPedido(produto, 1));
        repository.save(pedido1);

        Pedido pedido2 = new Pedido("Cliente 2");
        pedido2.adicionarItem(new ItemPedido(produto, 2));
        repository.save(pedido2);

        List<Pedido> pedidos = repository.findAll();

        assertEquals(2, pedidos.size());
    }

    @Test
    void testFindAllRetornaListaImutavel() {
        Pedido pedido = new Pedido("Cliente");
        pedido.adicionarItem(new ItemPedido(produto, 1));
        repository.save(pedido);

        List<Pedido> pedidos = repository.findAll();

        assertThrows(UnsupportedOperationException.class, () -> {
            pedidos.add(new Pedido("Novo"));
        });
    }

    @Test
    void testDeleteExistente() {
        Pedido pedido = new Pedido("Cliente");
        pedido.adicionarItem(new ItemPedido(produto, 1));
        Pedido salvo = repository.save(pedido);

        boolean deletado = repository.delete(salvo.getId());

        assertTrue(deletado);
        assertFalse(repository.findById(salvo.getId()).isPresent());
    }

    @Test
    void testDeleteInexistente() {
        boolean deletado = repository.delete(999L);
        assertFalse(deletado);
    }

    @Test
    void testDeleteAll() {
        Pedido pedido1 = new Pedido("Cliente 1");
        pedido1.adicionarItem(new ItemPedido(produto, 1));
        repository.save(pedido1);

        Pedido pedido2 = new Pedido("Cliente 2");
        pedido2.adicionarItem(new ItemPedido(produto, 2));
        repository.save(pedido2);

        repository.deleteAll();

        List<Pedido> pedidos = repository.findAll();
        assertTrue(pedidos.isEmpty());
    }

    @Test
    void testDeleteAllResetaIds() {
        Pedido pedido1 = new Pedido("Cliente 1");
        pedido1.adicionarItem(new ItemPedido(produto, 1));
        repository.save(pedido1);

        repository.deleteAll();

        Pedido pedido2 = new Pedido("Cliente 2");
        pedido2.adicionarItem(new ItemPedido(produto, 1));
        Pedido salvo = repository.save(pedido2);

        assertEquals(1L, salvo.getId());
    }
}

