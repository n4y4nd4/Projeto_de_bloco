package crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import crud.controller.PedidoController;
import crud.controller.ProdutoController;
import crud.repository.PedidoRepository;
import crud.repository.ProdutoRepository;
import crud.service.PedidoService;
import crud.service.ProdutoService;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JsonMapper;
import java.lang.reflect.Type;

/**
 * Classe principal que integra os sistemas de Produtos e Pedidos.
 * Configura o servidor Javalin e registra todas as rotas.
 * Segue o princípio de responsabilidade única: apenas configuração e inicialização.
 */
public class Main {
    // Repositórios compartilhados
    private static final ProdutoRepository produtoRepository = new ProdutoRepository();
    private static final PedidoRepository pedidoRepository = new PedidoRepository();
    
    // Services
    private static final ProdutoService produtoService = new ProdutoService(produtoRepository);
    private static final PedidoService pedidoService = new PedidoService(pedidoRepository, produtoRepository);
    
    // Controllers
    private static final ProdutoController produtoController = new ProdutoController(produtoService);
    private static final PedidoController pedidoController = new PedidoController(pedidoService);

    public static void main(String[] args) {
        startServer();
    }

    /**
     * Inicia o servidor Javalin e configura todas as rotas dos sistemas integrados.
     * 
     * @return Instância do servidor Javalin
     */
    public static Javalin startServer() {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
            config.jsonMapper(new JavalinJackson());
        }).start(7000);

        // Rotas do sistema de Produtos
        app.get("/api/produtos", produtoController::buscarTodos);
        app.get("/api/produtos/{id}", produtoController::buscarPorId);
        app.post("/api/produtos", produtoController::criarProduto);
        app.put("/api/produtos/{id}", produtoController::atualizarProduto);
        app.delete("/api/produtos/{id}", produtoController::deletarProduto);
        app.delete("/api/produtos/deleteall", ctx -> {
            produtoRepository.deleteAll();
            ctx.status(200).json(java.util.Map.of("message", "Repositório de produtos limpo."));
        });

        // Rotas do sistema de Pedidos (integração)
        app.get("/api/pedidos", pedidoController::buscarTodos);
        app.get("/api/pedidos/{id}", pedidoController::buscarPorId);
        app.post("/api/pedidos", pedidoController::criarPedido);
        app.put("/api/pedidos/{id}", pedidoController::atualizarPedido);
        app.delete("/api/pedidos/{id}", pedidoController::deletarPedido);
        app.delete("/api/pedidos/deleteall", ctx -> {
            pedidoRepository.deleteAll();
            ctx.status(200).json(java.util.Map.of("message", "Repositório de pedidos limpo."));
        });

        return app;
    }

    // Classe auxiliar interna para o Javalin usar o Jackson
    public static class JavalinJackson implements JsonMapper {
        private final ObjectMapper mapper = new ObjectMapper();

        @Override
        public <T> T fromJsonString(String json, Type targetType) {
            try {
                return mapper.readValue(json, mapper.constructType(targetType));
            } catch (Exception e) {
                throw new RuntimeException("Erro ao desserializar JSON: " + json, e);
            }
        }

        @Override
        public String toJsonString(Object obj, Type type) {
            try {
                return mapper.writeValueAsString(obj);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao serializar JSON", e);
            }
        }
    }
}