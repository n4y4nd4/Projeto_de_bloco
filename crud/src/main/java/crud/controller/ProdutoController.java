package crud.controller;

import crud.exception.ProdutoNaoEncontradoException;
import crud.exception.ValidacaoException;
import crud.model.Produto;
import crud.service.ProdutoService;
import io.javalin.http.Context;
import java.util.Map;

public class ProdutoController {
    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    // GET /api/produtos
    public void buscarTodos(Context ctx) {
        ctx.json(service.buscarTodos());
    }

    // GET /api/produtos/{id}
    public void buscarPorId(Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class).get();
            Produto produto = service.buscarPorId(id);
            ctx.json(produto);
        } catch (ProdutoNaoEncontradoException e) {
            ctx.status(404).json(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("message", "ID inválido."));
        }
    }

    // POST /api/produtos
    public void criarProduto(Context ctx) {
        try {
            Produto produtoRequest = ctx.bodyAsClass(Produto.class);
            Produto novoProduto = service.criarProduto(
                produtoRequest.getNome(), 
                produtoRequest.getPreco(), 
                produtoRequest.getEstoque()
            );
            ctx.status(201).json(novoProduto);
        } catch (ValidacaoException e) {
            ctx.status(400).json(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("message", "Dados inválidos ou formato incorreto."));
        }
    }

    // PUT /api/produtos/{id}
    public void atualizarProduto(Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class).get();
            Produto produtoRequest = ctx.bodyAsClass(Produto.class);
            
            Produto produtoAtualizado = service.atualizarProduto(
                id,
                produtoRequest.getNome(), 
                produtoRequest.getPreco(), 
                produtoRequest.getEstoque()
            );
            ctx.status(200).json(produtoAtualizado);
        } catch (ProdutoNaoEncontradoException e) {
            ctx.status(404).json(Map.of("message", e.getMessage()));
        } catch (ValidacaoException e) {
            ctx.status(400).json(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("message", "Dados inválidos ou formato incorreto."));
        }
    }

    // DELETE /api/produtos/{id}
    public void deletarProduto(Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class).get();
            service.deletarProduto(id);
            ctx.status(200).json(Map.of("message", "Produto deletado com sucesso."));
        } catch (ProdutoNaoEncontradoException e) {
            ctx.status(404).json(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("message", "ID inválido."));
        }
    }
}