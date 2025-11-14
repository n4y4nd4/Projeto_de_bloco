package crud.repository;

import crud.model.Produto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório de produtos implementando a interface genérica Repository.
 * Encapsula a coleção de produtos e fornece operações CRUD.
 * Segue o princípio de responsabilidade única (SRP).
 */
public class ProdutoRepository implements Repository<Produto, Long> {
    private final List<Produto> produtos = new ArrayList<>();
    private final AtomicLong currentId = new AtomicLong(1);

    public Produto save(Produto produto) {
        if (produto.getId() == null) {
            produto.setId(currentId.getAndIncrement());
            produtos.add(produto);
            return produto;
        } else {
            for (int i = 0; i < produtos.size(); i++) {
                if (produtos.get(i).getId().equals(produto.getId())) {
                    produtos.set(i, produto);
                    return produto;
                }
            }
            produtos.add(produto);
            return produto;
        }
    }

    public Optional<Produto> findById(Long id) {
        return produtos.stream()
                       .filter(p -> p.getId().equals(id))
                       .findFirst();
    }

    /**
     * Retorna uma cópia imutável da lista de produtos para evitar modificações externas.
     * Encapsula a coleção seguindo boas práticas de Clean Code.
     */
    @Override
    public List<Produto> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(produtos));
    }

    public boolean delete(Long id) {
        return produtos.removeIf(p -> p.getId().equals(id));
    }

    public void deleteAll() {
        produtos.clear();
        currentId.set(1);
    }
}