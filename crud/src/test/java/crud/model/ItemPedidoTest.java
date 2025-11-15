package crud.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {
    @Test
    void testConstrutor() {
        Produto produto = new Produto("Produto Teste", 10.0, 100);
        ItemPedido item = new ItemPedido(produto, 5);

        assertEquals(produto, item.getProduto());
        assertEquals(5, item.getQuantidade());
    }

    @Test
    void testConstrutorComProdutoNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ItemPedido(null, 5);
        });
    }

    @Test
    void testConstrutorComQuantidadeNull() {
        Produto produto = new Produto("Produto", 10.0, 100);
        assertThrows(IllegalArgumentException.class, () -> {
            new ItemPedido(produto, null);
        });
    }

    @Test
    void testConstrutorComQuantidadeZero() {
        Produto produto = new Produto("Produto", 10.0, 100);
        assertThrows(IllegalArgumentException.class, () -> {
            new ItemPedido(produto, 0);
        });
    }

    @Test
    void testConstrutorComQuantidadeNegativa() {
        Produto produto = new Produto("Produto", 10.0, 100);
        assertThrows(IllegalArgumentException.class, () -> {
            new ItemPedido(produto, -1);
        });
    }

    @Test
    void testGetSubtotal() {
        Produto produto = new Produto("Produto", 10.0, 100);
        ItemPedido item = new ItemPedido(produto, 3);

        Double subtotal = item.getSubtotal();
        assertEquals(30.0, subtotal);
    }

    @Test
    void testEqualsMesmoProdutoEMesmaQuantidade() {
        Produto produto1 = new Produto("Produto", 10.0, 100);
        produto1.setId(1L);
        Produto produto2 = new Produto("Produto", 10.0, 100);
        produto2.setId(1L);

        ItemPedido item1 = new ItemPedido(produto1, 5);
        ItemPedido item2 = new ItemPedido(produto2, 5);

        assertEquals(item1, item2);
    }

    @Test
    void testEqualsQuantidadesDiferentes() {
        Produto produto = new Produto("Produto", 10.0, 100);
        produto.setId(1L);

        ItemPedido item1 = new ItemPedido(produto, 5);
        ItemPedido item2 = new ItemPedido(produto, 3);

        assertNotEquals(item1, item2);
    }

    @Test
    void testEqualsMesmaInstancia() {
        Produto produto = new Produto("Produto", 10.0, 100);
        ItemPedido item = new ItemPedido(produto, 5);

        assertEquals(item, item);
    }

    @Test
    void testEqualsNull() {
        Produto produto = new Produto("Produto", 10.0, 100);
        ItemPedido item = new ItemPedido(produto, 5);

        assertNotEquals(item, null);
    }

    @Test
    void testEqualsTipoDiferente() {
        Produto produto = new Produto("Produto", 10.0, 100);
        ItemPedido item = new ItemPedido(produto, 5);

        assertNotEquals(item, "String");
    }

    @Test
    void testHashCode() {
        Produto produto1 = new Produto("Produto", 10.0, 100);
        produto1.setId(1L);
        Produto produto2 = new Produto("Produto", 10.0, 100);
        produto2.setId(1L);

        ItemPedido item1 = new ItemPedido(produto1, 5);
        ItemPedido item2 = new ItemPedido(produto2, 5);

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testToString() {
        Produto produto = new Produto("Produto Teste", 10.0, 100);
        ItemPedido item = new ItemPedido(produto, 5);

        String toString = item.toString();

        assertTrue(toString.contains("produto=Produto Teste"));
        assertTrue(toString.contains("quantidade=5"));
        assertTrue(toString.contains("subtotal="));
    }
}

