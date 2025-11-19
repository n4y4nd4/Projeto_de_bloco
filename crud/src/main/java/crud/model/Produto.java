package crud.model;

import java.util.Objects;

/**
 * Modelo imutável de Produto.
 * Segue o princípio de imutabilidade: uma vez criado, não pode ser modificado diretamente.
 * Para atualizações, deve-se criar uma nova instância.
 */
public class Produto {
    private final Long id;
    private final String nome;
    private final Double preco;
    private final Integer estoque;

    /**
     * Construtor para criação de novos produtos (sem ID).
     * O ID será atribuído pelo repositório.
     */
    public Produto(String nome, Double preco, Integer estoque) {
        this(null, nome, preco, estoque);
    }

    /**
     * Construtor completo para criação de produtos com ID.
     * Usado internamente pelo repositório para criar instâncias imutáveis.
     */
    public Produto(Long id, String nome, Double preco, Integer estoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    /**
     * Construtor vazio para deserialização JSON.
     * @deprecated Use o construtor com parâmetros para garantir imutabilidade.
     */
    @Deprecated
    public Produto() {
        this.id = null;
        this.nome = null;
        this.preco = null;
        this.estoque = null;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Double getPreco() {
        return preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    /**
     * Cria uma nova instância de Produto com ID atualizado.
     * Método de consulta que retorna nova instância imutável.
     */
    public Produto comId(Long novoId) {
        return new Produto(novoId, this.nome, this.preco, this.estoque);
    }

    /**
     * Cria uma nova instância de Produto com nome atualizado.
     * Método de consulta que retorna nova instância imutável.
     */
    public Produto comNome(String novoNome) {
        return new Produto(this.id, novoNome, this.preco, this.estoque);
    }

    /**
     * Cria uma nova instância de Produto com preço atualizado.
     * Método de consulta que retorna nova instância imutável.
     */
    public Produto comPreco(Double novoPreco) {
        return new Produto(this.id, this.nome, novoPreco, this.estoque);
    }

    /**
     * Cria uma nova instância de Produto com estoque atualizado.
     * Método de consulta que retorna nova instância imutável.
     */
    public Produto comEstoque(Integer novoEstoque) {
        return new Produto(this.id, this.nome, this.preco, novoEstoque);
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
        String precoFormatado = (preco != null) ? String.format("%.2f", preco).replace(".", ",") : "0,00";
        return "ID: " + id + ", Nome: " + nome + ", Preço: " + precoFormatado + ", Estoque: " + estoque;
    }
}