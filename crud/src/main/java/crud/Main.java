package crud;

import crud.controller.ProdutoController;
import crud.repository.ProdutoRepository;
import crud.service.ProdutoService;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
// Removed invalid import

public class Main {
    
    // Variáveis estáticas para acesso pelos testes
    private static ProdutoRepository repository = new ProdutoRepository();
    private static ProdutoService service = new ProdutoService(repository);
    private static ProdutoController controller = new ProdutoController(service);

    public static void main(String[] args) {
        startServer();
    }
    
    public static Javalin startServer() {
        // Inicia o servidor Javalin
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7000);

        // Rotas da API REST
        app.get("/api/produtos", controller::buscarTodos);
        app.get("/api/produtos/{id}", controller::buscarPorId);
        app.post("/api/produtos", controller::criarProduto);
        app.put("/api/produtos/{id}", controller::atualizarProduto);
        app.delete("/api/produtos/{id}", controller::deletarProduto);
        
        // Rota de Limpeza Exclusiva para Testes Selenium
        app.get("/api/produtos/deleteall", ctx -> {
            repository.deleteAll();
            ctx.status(200).json(java.util.Map.of("message", "Repositório limpo."));
        });
        
        return app;
    }
    
}