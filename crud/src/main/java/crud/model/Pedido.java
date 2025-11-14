package crud.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Modelo de Pedido que integra com o sistema de Produtos.
 * Encapsula a lista de itens do pedido e calcula o total automaticamente.
 * Segue princípios de Clean Code: encapsulamento de coleções e imutabilidade parcial.
 */
public class Pedido {
    private Long id;
    private String cliente;
    private final List<ItemPedido> itens;
    private LocalDateTime dataCriacao;
    
    // Construtor vazio para Jackson/JSON
    public Pedido() {
        this.itens = new ArrayList<>();
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
    
    /**
     * Retorna uma cópia imutável da lista de itens.
     * Encapsula a coleção para evitar modificações externas.
     */
    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(new ArrayList<>(itens));
    }
    
    /**
     * Adiciona um item ao pedido.
     * Encapsula a adição de itens na coleção.
     */
    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        itens.add(item);
    }
    
    /**
     * Remove um item do pedido pelo índice.
     */
    public void removerItem(int indice) {
        if (indice < 0 || indice >= itens.size()) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        itens.remove(indice);
    }
    
    /**
     * Calcula o total do pedido somando os valores dos itens.
     * Substitui valores primitivos por cálculo derivado (Clean Code).
     */
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

