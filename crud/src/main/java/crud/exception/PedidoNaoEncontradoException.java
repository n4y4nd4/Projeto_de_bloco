package crud.exception;

/**
 * Exceção lançada quando um pedido não é encontrado.
 * Segue o padrão estabelecido por ProdutoNaoEncontradoException.
 */
public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException(Long id) {
        super("Pedido com ID " + id + " não encontrado.");
    }
}

