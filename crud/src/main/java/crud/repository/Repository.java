package crud.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface genérica para repositórios, seguindo o princípio de abstração.
 * Permite reutilização de código e facilita testes.
 * 
 * @param <T> Tipo da entidade
 * @param <ID> Tipo do identificador
 */
public interface Repository<T, ID> {
    /**
     * Salva uma entidade. Se a entidade já possui ID, atualiza.
     * Caso contrário, cria uma nova entidade.
     * 
     * @param entity Entidade a ser salva
     * @return Entidade salva (com ID atribuído se for nova)
     */
    T save(T entity);
    
    /**
     * Busca uma entidade pelo ID.
     * 
     * @param id Identificador da entidade
     * @return Optional contendo a entidade se encontrada
     */
    Optional<T> findById(ID id);
    
    /**
     * Retorna todas as entidades.
     * 
     * @return Lista de todas as entidades
     */
    List<T> findAll();
    
    /**
     * Remove uma entidade pelo ID.
     * 
     * @param id Identificador da entidade
     * @return true se a entidade foi removida, false caso contrário
     */
    boolean delete(ID id);
    
    /**
     * Remove todas as entidades.
     */
    void deleteAll();
}

