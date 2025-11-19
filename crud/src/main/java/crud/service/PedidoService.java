package crud.service;

import crud.exception.PedidoNaoEncontradoException;
import crud.exception.ValidacaoException;
import crud.model.ItemPedido;
import crud.model.Pedido;
import crud.repository.PedidoRepository;
import crud.repository.ProdutoRepository;
import java.util.List;
import java.util.Optional;

/**
 * Serviço de pedidos que integra com o sistema de produtos.
 * Valida a existência de produtos antes de criar pedidos.
 * Segue o princípio de responsabilidade única (SRP).
 */
public class PedidoService implements Service<Pedido, Long> {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository; // Para validar produtos

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    /**
     * Valida um pedido usando guard clauses.
     * Verifica cliente, itens e existência de produtos.
     */
    private void validarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new ValidacaoException("Pedido não pode ser nulo.");
        }
        
        if (pedido.getCliente() == null || pedido.getCliente().trim().isEmpty()) {
            throw new ValidacaoException("O nome do cliente é obrigatório.");
        }
        
        if (pedido.getItens().isEmpty()) {
            throw new ValidacaoException("Um pedido deve ter pelo menos um item.");
        }
        
        validarItensPedido(pedido.getItens());
    }
    
    /**
     * Valida os itens de um pedido.
     * Verifica se produtos existem e se quantidades são válidas.
     */
    private void validarItensPedido(List<ItemPedido> itens) {
        for (ItemPedido item : itens) {
            if (item == null) {
                throw new ValidacaoException("Item de pedido não pode ser nulo.");
            }
            
            if (item.getProduto() == null || item.getProduto().getId() == null) {
                throw new ValidacaoException("Item de pedido com produto inválido.");
            }
            
            // Valida se o produto existe no repositório de produtos
            produtoRepository.findById(item.getProduto().getId())
                             .orElseThrow(() -> new ValidacaoException(
                                 "Produto com ID " + item.getProduto().getId() + " não encontrado."));

            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new ValidacaoException("A quantidade do item deve ser maior que zero.");
            }
        }
    }

    /**
     * Cria um novo pedido (método da interface Service).
     */
    @Override
    public Pedido criar(Pedido pedidoRequest) {
        validarPedido(pedidoRequest);
        return pedidoRepository.save(pedidoRequest);
    }

    /**
     * Cria um novo pedido (método auxiliar para facilitar uso).
     */
    public Pedido criarPedido(String cliente, List<ItemPedido> itens) {
        Pedido pedido = new Pedido(cliente);
        if (itens != null) {
            for (ItemPedido item : itens) {
                pedido.adicionarItem(item);
            }
        }
        return criar(pedido);
    }

    /**
     * Busca todos os pedidos.
     */
    @Override
    public List<Pedido> buscarTodos() {
        return pedidoRepository.findAll();
    }

    /**
     * Busca um pedido por ID (método da interface Service).
     */
    @Override
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                              .orElseThrow(() -> new PedidoNaoEncontradoException(id));
    }

    /**
     * Busca um pedido por ID retornando Optional (método auxiliar).
     */
    public Optional<Pedido> buscarPorIdOptional(Long id) {
        return pedidoRepository.findById(id);
    }
    
    /**
     * Atualiza um pedido existente (método da interface Service).
     * Usa guard clauses para validação prévia.
     */
    @Override
    public Pedido atualizar(Long id, Pedido pedidoRequest) {
        if (id == null) {
            throw new ValidacaoException("ID do pedido não pode ser nulo.");
        }
        
        if (pedidoRequest == null) {
            throw new ValidacaoException("Dados do pedido não podem ser nulos.");
        }
        
        Pedido pedidoExistente = buscarPorId(id);
        
        pedidoExistente.setCliente(pedidoRequest.getCliente());
        // Limpa itens antigos e adiciona novos
        // Como getItens() retorna lista imutável, precisamos trabalhar com a lista interna
        int tamanhoOriginal = pedidoExistente.getItens().size();
        for (int i = tamanhoOriginal - 1; i >= 0; i--) {
            pedidoExistente.removerItem(i);
        }
        if (pedidoRequest.getItens() != null) {
            for (ItemPedido item : pedidoRequest.getItens()) {
                pedidoExistente.adicionarItem(item);
            }
        }
        
        validarPedido(pedidoExistente);
        return pedidoRepository.save(pedidoExistente);
    }

    /**
     * Atualiza um pedido existente (método auxiliar para facilitar uso).
     */
    public Pedido atualizarPedido(Long id, String novoCliente, List<ItemPedido> novosItens) {
        Pedido pedidoExistente = buscarPorId(id);
        
        pedidoExistente.setCliente(novoCliente);
        // Limpa itens antigos e adiciona novos
        int tamanhoOriginal = pedidoExistente.getItens().size();
        for (int i = tamanhoOriginal - 1; i >= 0; i--) {
            pedidoExistente.removerItem(i);
        }
        if (novosItens != null) {
            for (ItemPedido item : novosItens) {
                pedidoExistente.adicionarItem(item);
            }
        }
        
        validarPedido(pedidoExistente);
        return pedidoRepository.save(pedidoExistente);
    }
    
    /**
     * Remove um pedido (método da interface Service).
     */
    @Override
    public void deletar(Long id) {
        boolean deletado = pedidoRepository.delete(id);
        if (!deletado) {
            throw new PedidoNaoEncontradoException(id);
        }
    }

    /**
     * Remove um pedido (método auxiliar para facilitar uso).
     */
    public void deletarPedido(Long id) {
        deletar(id);
    }
}

