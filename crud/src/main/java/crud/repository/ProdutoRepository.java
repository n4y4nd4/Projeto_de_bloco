package crud.repository;

import crud.model.Produto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


public class ProdutoRepository implements Repository<Produto, Long> {
    private final List<Produto> produtos = new ArrayList<>();
    private final AtomicLong currentId = new AtomicLong(1);

    /**
     * Salva um produto. Se não tiver ID, cria novo. Se tiver, atualiza.
     * Retorna uma nova instância imutável com ID atribuído.
     */
    public Produto save(Produto produto) {
        if (produto.getId() == null) {
            Produto produtoComId = produto.comId(currentId.getAndIncrement());
            produtos.add(produtoComId);
            return produtoComId;
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