package crud.repository;

import crud.model.Produto;
import net.jqwik.api.*;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

class ProdutoRepositoryTest {

    // initialize inline so jqwik property methods also see a non-null repository
    private ProdutoRepository repository = new ProdutoRepository();

    @Test
    void testSaveNovoProduto() {
        Produto produto = new Produto("Mouse", 50.0, 10);
        Produto salvo = repository.save(produto);

        assertNotNull(salvo.getId());
        assertEquals(1L, salvo.getId());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void testSaveAtualizarProdutoExistente() {
        Produto produto = new Produto("Teclado", 100.0, 5);
        Produto salvo = repository.save(produto);
        Long id = salvo.getId();

        salvo.setPreco(120.0);
        Produto atualizado = repository.save(salvo);

        assertEquals(id, atualizado.getId());
        assertEquals(120.0, atualizado.getPreco());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void testFindByIdExistente() {
        Produto p1 = repository.save(new Produto("A", 10.0, 1));
        Produto p2 = repository.save(new Produto("B", 20.0, 2));

        Optional<Produto> encontrado = repository.findById(p1.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("A", encontrado.get().getNome());
    }

    @Test
    void testFindByIdInexistente() {
        repository.save(new Produto("A", 10.0, 1));
        Optional<Produto> encontrado = repository.findById(99L);
        assertFalse(encontrado.isPresent());
    }

    @Test
    void testFindAllVazio() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void testFindAllPreenchido() {
        repository.save(new Produto("A", 1.0, 1));
        repository.save(new Produto("B", 2.0, 2));

        List<Produto> produtos = repository.findAll();
        assertEquals(2, produtos.size());
    }

    @Test
    void testDeleteExistente() {
        Produto p = repository.save(new Produto("A", 10.0, 1));
        boolean deletado = repository.delete(p.getId());

        assertTrue(deletado);
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void testDeleteInexistente() {
        repository.save(new Produto("A", 10.0, 1));
        boolean deletado = repository.delete(99L);
        assertFalse(deletado);
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void testDeleteAll() {
        repository.save(new Produto("A", 1.0, 1));
        repository.save(new Produto("B", 2.0, 2));
        repository.deleteAll();
        assertTrue(repository.findAll().isEmpty());

        Produto p = repository.save(new Produto("Novo", 1.0, 1));
        assertEquals(1L, p.getId());
    }

    @Property(tries = 100)
    void testPersistenciaPropriedades(
            @ForAll("nomes") String nome,
            @ForAll @DoubleRange(min = 0.01, max = 1000.0) double preco,
            @ForAll @IntRange(min = 0, max = 100) int estoque) {

        Produto produto = new Produto(nome, preco, estoque);
        Produto salvo = repository.save(produto);

        assertNotNull(salvo.getId());
        assertTrue(repository.findById(salvo.getId()).isPresent());
        assertEquals(nome, repository.findById(salvo.getId()).get().getNome());
        assertEquals(preco, repository.findById(salvo.getId()).get().getPreco());
    }

    @Provide
    Arbitrary<String> nomes() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .alpha()
                .ofMinLength(3)
                .ofMaxLength(20);
    }
}