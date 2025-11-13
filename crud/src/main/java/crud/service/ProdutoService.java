package crud.service;

import crud.exception.ProdutoNaoEncontradoException;
import crud.exception.ValidacaoException;
import crud.model.Produto;
import crud.repository.ProdutoRepository;
import java.util.List;

public class ProdutoService {
    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    private void validarProduto(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new ValidacaoException("O nome do produto é obrigatório.");
        }
        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            throw new ValidacaoException("O preço deve ser maior que zero.");
        }
        if (produto.getEstoque() == null || produto.getEstoque() < 0) {
            throw new ValidacaoException("O estoque não pode ser negativo.");
        }
    }

    public Produto criarProduto(String nome, Double preco, Integer estoque) {
        Produto produto = new Produto(nome, preco, estoque);
        validarProduto(produto);
        return repository.save(produto);
    }

    public List<Produto> buscarTodos() {
        return repository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    public Produto atualizarProduto(Long id, String novoNome, Double novoPreco, Integer novoEstoque) {
        Produto produtoExistente = buscarPorId(id);

        Produto produtoAtualizado = new Produto(novoNome, novoPreco, novoEstoque);
        validarProduto(produtoAtualizado);

        produtoExistente.setNome(novoNome);
        produtoExistente.setPreco(novoPreco);
        produtoExistente.setEstoque(novoEstoque);

        return repository.save(produtoExistente);
    }

    public void deletarProduto(Long id) {
        boolean deletado = repository.delete(id);
        if (!deletado) {
            throw new ProdutoNaoEncontradoException(id);
        }
    }
}