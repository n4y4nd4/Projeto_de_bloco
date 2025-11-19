package crud.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {
    @Test
    void testCriacaoProdutoValido() {
        Produto produto = new Produto("Laptop", 5000.0, 10);
        assertAll(
            () -> assertEquals("Laptop", produto.getNome()),
            () -> assertEquals(5000.0, produto.getPreco()),
            () -> assertEquals(10, produto.getEstoque()),
            () -> assertNull(produto.getId())
        );
    }

    @Test
    void testEqualsEHashCode() {
        Produto produto1 = new Produto(1L, "Monitor", 1200.0, 5);
        Produto produto2 = new Produto(1L, "Monitor", 1200.0, 5);
        Produto produto3 = new Produto(2L, "Teclado", 100.0, 20);

        assertEquals(produto1, produto2);
        assertEquals(produto1.hashCode(), produto2.hashCode());
        assertNotEquals(produto1, produto3);
        assertNotEquals(produto1.hashCode(), produto3.hashCode());
    }

    @Test
    void testToString() {
        Produto produto = new Produto(10L, "Mouse", 50.50, 50);
        String esperado = "ID: 10, Nome: Mouse, Preço: 50,50, Estoque: 50";
        assertEquals(esperado, produto.toString());
    }
}