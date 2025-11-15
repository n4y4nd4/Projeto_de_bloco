package crud.controller;

import crud.exception.PedidoNaoEncontradoException;
import crud.exception.ValidacaoException;
import crud.model.Pedido;
import crud.service.PedidoService;
import io.javalin.http.Context;
import java.util.Map;


public class PedidoController {
    private final PedidoService service;
    
    public PedidoController(PedidoService service) {
        this.service = service;
    }
    
    // GET /api/pedidos
    public void buscarTodos(Context ctx) {
        ctx.json(service.buscarTodos());
    }
    
    // GET /api/pedidos/{id}
    public void buscarPorId(Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class).get();
            Pedido pedido = service.buscarPorId(id);
            ctx.json(pedido);
        } catch (PedidoNaoEncontradoException e) {
            ctx.status(404).json(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("message", "ID inválido."));
        }
    }
    
    // POST /api/pedidos
    public void criarPedido(Context ctx) {
        try {
            Pedido pedidoRequest = ctx.bodyAsClass(Pedido.class);
            Pedido novoPedido = service.criar(pedidoRequest);
            ctx.status(201).json(novoPedido);
        } catch (ValidacaoException e) {
            ctx.status(400).json(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("message", "Dados inválidos ou formato incorreto."));
        }
    }
    
    // PUT /api/pedidos/{id}
    public void atualizarPedido(Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class).get();
            Pedido pedidoRequest = ctx.bodyAsClass(Pedido.class);
            
            Pedido pedidoAtualizado = service.atualizar(id, pedidoRequest);
            ctx.status(200).json(pedidoAtualizado);
        } catch (PedidoNaoEncontradoException e) {
            ctx.status(404).json(Map.of("message", e.getMessage()));
        } catch (ValidacaoException e) {
            ctx.status(400).json(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("message", "Dados inválidos ou formato incorreto."));
        }
    }
    
    // DELETE /api/pedidos/{id}
    public void deletarPedido(Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class).get();
            service.deletar(id);
            ctx.status(200).json(Map.of("message", "Pedido deletado com sucesso."));
        } catch (PedidoNaoEncontradoException e) {
            ctx.status(404).json(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("message", "ID inválido."));
        }
    }
}

