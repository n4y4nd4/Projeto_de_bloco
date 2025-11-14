package crud.model;

import java.util.Objects;

public class Produto {
    private Long id;
    private String nome;
    private Double preco;
    private Integer estoque;

    // --- INÍCIO DA CORREÇÃO ---
    // Construtor vazio (obrigatório para o Jackson/JSON)
    public Produto() {
    }
    // --- FIM DA CORREÇÃO ---

    public Produto(String nome, Double preco, Integer estoque) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        // Lida com 'preco' nulo se o construtor vazio for usado
        // Formata o preço com vírgula como separador decimal (formato brasileiro)
        String precoFormatado = (preco != null) ? String.format("%.2f", preco).replace(".", ",") : "0,00";
        return "ID: " + id + ", Nome: " + nome + ", Preço: " + precoFormatado + ", Estoque: " + estoque;
    }
}