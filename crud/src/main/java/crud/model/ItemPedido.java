package crud.model;

import java.util.Objects;


public class ItemPedido {
    private final Produto produto;
    private final Integer quantidade;
    
    public ItemPedido(Produto produto, Integer quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        this.produto = produto;
        this.quantidade = quantidade;
    }
    
    public Produto getProduto() {
        return produto;
    }
    
    public Integer getQuantidade() {
        return quantidade;
    }
    
    
    public Double getSubtotal() {
        return produto.getPreco() * quantidade;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(produto, that.produto) && 
               Objects.equals(quantidade, that.quantidade);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(produto, quantidade);
    }
    
    @Override
    public String toString() {
        return "ItemPedido{produto=" + produto.getNome() + 
               ", quantidade=" + quantidade + 
               ", subtotal=" + getSubtotal() + "}";
    }
}

