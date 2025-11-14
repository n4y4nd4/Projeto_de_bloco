package crud.service;

import crud.exception.PedidoNaoEncontradoException;
import crud.exception.ValidacaoException;
import crud.model.ItemPedido;
import crud.model.Pedido;
import crud.repository.PedidoRepository;
import crud.repository.ProdutoRepository;
import java.util.List;

/**
 * Serviço de pedidos que integra com o sistema de produtos.
 * Valida a existência de produtos antes de criar pedidos.
 * Segue o princípio de responsabilidade única (SRP).
 */
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    
    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }
    
    /**
     * Valida um pedido antes de processar (Fail Early).
     */
    private void validarPedido(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().trim().isEmpty()) {
            throw new ValidacaoException("O nome do cliente é obrigatório.");
        }
        if (pedido.getItens().isEmpty()) {
            throw new ValidacaoException("O pedido deve conter pelo menos um item.");
        }
        
        // Valida que todos os produtos dos itens existem
        for (ItemPedido item : pedido.getItens()) {
            Long produtoId = item.getProduto().getId();
            if (produtoId == null) {
                throw new ValidacaoException("Produto sem ID não pode ser adicionado ao pedido.");
            }
            produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new ValidacaoException(
                            "Produto com ID " + produtoId + " não encontrado."));
        }
    }
    
    /**
     * Cria um novo pedido validando os dados e produtos.
     */
    public Pedido criarPedido(String cliente, List<ItemPedido> itens) {
        Pedido pedido = new Pedido(cliente);
        for (ItemPedido item : itens) {
            pedido.adicionarItem(item);
        }
        validarPedido(pedido);
        return pedidoRepository.save(pedido);
    }
    
    /**
     * Busca todos os pedidos.
     */
    public List<Pedido> buscarTodos() {
        return pedidoRepository.findAll();
    }
    
    /**
     * Busca um pedido pelo ID.
     */
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                              .orElseThrow(() -> new PedidoNaoEncontradoException(id));
    }
    
    /**
     * Atualiza um pedido existente.
     */
    public Pedido atualizarPedido(Long id, String novoCliente, List<ItemPedido> novosItens) {
        Pedido pedidoExistente = buscarPorId(id);
        
        pedidoExistente.setCliente(novoCliente);
        // Limpa itens antigos e adiciona novos
        // Como getItens() retorna lista imutável, precisamos trabalhar com a lista interna
        int tamanhoOriginal = pedidoExistente.getItens().size();
        for (int i = tamanhoOriginal - 1; i >= 0; i--) {
            pedidoExistente.removerItem(i);
        }
        for (ItemPedido item : novosItens) {
            pedidoExistente.adicionarItem(item);
        }
        
        validarPedido(pedidoExistente);
        return pedidoRepository.save(pedidoExistente);
    }
    
    /**
     * Remove um pedido.
     */
    public void deletarPedido(Long id) {
        boolean deletado = pedidoRepository.delete(id);
        if (!deletado) {
            throw new PedidoNaoEncontradoException(id);
        }
    }
}

