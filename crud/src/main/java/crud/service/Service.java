package crud.service;

import java.util.List;

/**
 * Interface genérica para serviços, seguindo o princípio de abstração.
 * Define operações básicas de CRUD que podem ser reutilizadas.
 * 
 * @param <T> Tipo da entidade
 * @param <ID> Tipo do identificador
 */
public interface Service<T, ID> {
    /**
     * Cria uma nova entidade.
     * 
     * @param entity Entidade a ser criada
     * @return Entidade criada com ID atribuído
     */
    T criar(T entity);
    
    /**
     * Busca todas as entidades.
     * 
     * @return Lista de todas as entidades
     */
    List<T> buscarTodos();
    
    /**
     * Busca uma entidade pelo ID.
     * 
     * @param id Identificador da entidade
     * @return Entidade encontrada
     * @throws RuntimeException se a entidade não for encontrada
     */
    T buscarPorId(ID id);
    
    /**
     * Atualiza uma entidade existente.
     * 
     * @param id Identificador da entidade
     * @param entity Entidade com os dados atualizados
     * @return Entidade atualizada
     * @throws RuntimeException se a entidade não for encontrada
     */
    T atualizar(ID id, T entity);
    
    /**
     * Remove uma entidade pelo ID.
     * 
     * @param id Identificador da entidade
     * @throws RuntimeException se a entidade não for encontrada
     */
    void deletar(ID id);
}

