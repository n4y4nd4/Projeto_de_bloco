package crud.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {
    @Test
    void testConstrutorPadrao() {
        Pedido pedido = new Pedido();

        assertNull(pedido.getId());
        assertNull(pedido.getCliente());
        assertNotNull(pedido.getItens());
        assertTrue(pedido.getItens().isEmpty());
        assertNotNull(pedido.getDataCriacao());
    }

    @Test
    void testConstrutorComCliente() {
        Pedido pedido = new Pedido("Cliente Teste");

        assertNull(pedido.getId());
        assertEquals("Cliente Teste", pedido.getCliente());
        assertNotNull(pedido.getItens());
        assertTrue(pedido.getItens().isEmpty());
        assertNotNull(pedido.getDataCriacao());
    }

    @Test
    void testSetId() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        assertEquals(1L, pedido.getId());
    }

    @Test
    void testSetCliente() {
        Pedido pedido = new Pedido();
        pedido.setCliente("Novo Cliente");

        assertEquals("Novo Cliente", pedido.getCliente());
    }

    @Test
    void testSetDataCriacao() {
        Pedido pedido = new Pedido();
        LocalDateTime novaData = LocalDateTime.now().minusDays(1);
        pedido.setDataCriacao(novaData);

        assertEquals(novaData, pedido.getDataCriacao());
    }

    @Test
    void testAdicionarItem() {
        Pedido pedido = new Pedido("Cliente");
        Produto produto = new Produto("Produto", 10.0, 100);
        ItemPedido item = new ItemPedido(produto, 2);

        pedido.adicionarItem(item);

        assertEquals(1, pedido.getItens().size());
        assertEquals(item, pedido.getItens().get(0));
    }

    @Test
    void testAdicionarItemNull() {
        Pedido pedido = new Pedido("Cliente");

        assertThrows(IllegalArgumentException.class, () -> {
            pedido.adicionarItem(null);
        });
    }

    @Test
    void testRemoverItem() {
        Pedido pedido = new Pedido("Cliente");
        Produto produto = new Produto("Produto", 10.0, 100);
        ItemPedido item1 = new ItemPedido(produto, 1);
        ItemPedido item2 = new ItemPedido(produto, 2);

        pedido.adicionarItem(item1);
        pedido.adicionarItem(item2);

        pedido.removerItem(0);

        assertEquals(1, pedido.getItens().size());
        assertEquals(item2, pedido.getItens().get(0));
    }

    @Test
    void testRemoverItemIndiceInvalido() {
        Pedido pedido = new Pedido("Cliente");
        Produto produto = new Produto("Produto", 10.0, 100);
        pedido.adicionarItem(new ItemPedido(produto, 1));

        assertThrows(IndexOutOfBoundsException.class, () -> {
            pedido.removerItem(10);
        });
    }

    @Test
    void testRemoverItemIndiceNegativo() {
        Pedido pedido = new Pedido("Cliente");
        Produto produto = new Produto("Produto", 10.0, 100);
        pedido.adicionarItem(new ItemPedido(produto, 1));

        assertThrows(IndexOutOfBoundsException.class, () -> {
            pedido.removerItem(-1);
        });
    }

    @Test
    void testGetItensRetornaListaImutavel() {
        Pedido pedido = new Pedido("Cliente");
        Produto produto = new Produto("Produto", 10.0, 100);
        pedido.adicionarItem(new ItemPedido(produto, 1));

        List<ItemPedido> itens = pedido.getItens();

        assertThrows(UnsupportedOperationException.class, () -> {
            itens.add(new ItemPedido(produto, 2));
        });
    }

    @Test
    void testGetTotal() {
        Pedido pedido = new Pedido("Cliente");
        Produto produto1 = new Produto("Produto 1", 10.0, 100);
        Produto produto2 = new Produto("Produto 2", 20.0, 100);

        pedido.adicionarItem(new ItemPedido(produto1, 2));
        pedido.adicionarItem(new ItemPedido(produto2, 3));

        Double total = pedido.getTotal();
        assertEquals(80.0, total); // (10 * 2) + (20 * 3) = 20 + 60 = 80
    }

    @Test
    void testGetTotalSemItens() {
        Pedido pedido = new Pedido("Cliente");
        Double total = pedido.getTotal();
        assertEquals(0.0, total);
    }

    @Test
    void testEqualsMesmoId() {
        Pedido pedido1 = new Pedido("Cliente 1");
        pedido1.setId(1L);
        Pedido pedido2 = new Pedido("Cliente 2");
        pedido2.setId(1L);

        assertEquals(pedido1, pedido2);
    }

    @Test
    void testEqualsIdsDiferentes() {
        Pedido pedido1 = new Pedido("Cliente");
        pedido1.setId(1L);
        Pedido pedido2 = new Pedido("Cliente");
        pedido2.setId(2L);

        assertNotEquals(pedido1, pedido2);
    }

    @Test
    void testEqualsMesmaInstancia() {
        Pedido pedido = new Pedido("Cliente");
        assertEquals(pedido, pedido);
    }

    @Test
    void testEqualsNull() {
        Pedido pedido = new Pedido("Cliente");
        assertNotEquals(pedido, null);
    }

    @Test
    void testEqualsTipoDiferente() {
        Pedido pedido = new Pedido("Cliente");
        assertNotEquals(pedido, "String");
    }

    @Test
    void testHashCode() {
        Pedido pedido1 = new Pedido("Cliente 1");
        pedido1.setId(1L);
        Pedido pedido2 = new Pedido("Cliente 2");
        pedido2.setId(1L);

        assertEquals(pedido1.hashCode(), pedido2.hashCode());
    }

    @Test
    void testToString() {
        Pedido pedido = new Pedido("Cliente Teste");
        pedido.setId(1L);
        Produto produto = new Produto("Produto", 10.0, 100);
        pedido.adicionarItem(new ItemPedido(produto, 2));

        String toString = pedido.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("cliente='Cliente Teste'"));
        assertTrue(toString.contains("total="));
        assertTrue(toString.contains("itens=1"));
    }
}

