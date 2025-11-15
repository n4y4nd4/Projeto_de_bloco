package crud.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Pedido {
    private Long id;
    private String cliente;
    private final List<ItemPedido> itens;
    private LocalDateTime dataCriacao;
    
    public Pedido() {
        this.itens = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
    }
    
    public Pedido(String cliente) {
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCliente() {
        return cliente;
    }
    
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    
    
    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(new ArrayList<>(itens));
    }
    
  
    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        itens.add(item);
    }
    
    
    public void removerItem(int indice) {
        if (indice < 0 || indice >= itens.size()) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        itens.remove(indice);
    }
    
   
    public Double getTotal() {
        return itens.stream()
                .mapToDouble(item -> item.getQuantidade() * item.getProduto().getPreco())
                .sum();
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Pedido{id=" + id + ", cliente='" + cliente + "', total=" + getTotal() + 
               ", itens=" + itens.size() + ", data=" + dataCriacao + "}";
    }
}

