package crud.integration;

import crud.Main;
import crud.model.ItemPedido;
import crud.model.Pedido;
import crud.model.Produto;
import crud.repository.PedidoRepository;
import crud.repository.ProdutoRepository;
import crud.service.PedidoService;
import crud.service.ProdutoService;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class IntegracaoProdutoPedidoTest {
    
    private static Javalin app;
    private static ProdutoRepository produtoRepository;
    private static PedidoRepository pedidoRepository;
    private static ProdutoService produtoService;
    private static PedidoService pedidoService;
    
    @BeforeAll
    static void setUpAll() {
        produtoRepository = new ProdutoRepository();
        pedidoRepository = new PedidoRepository();
        produtoService = new ProdutoService(produtoRepository);
        pedidoService = new PedidoService(pedidoRepository, produtoRepository);
        
        app = Main.startServer();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @BeforeEach
    void setUp() {
        produtoRepository.deleteAll();
        pedidoRepository.deleteAll();
    }
    
    @AfterAll
    static void tearDown() {
        if (app != null) {
            app.stop();
        }
    }
    
    @Test
    void testCriarPedidoComProdutoExistente() {
        // Cria um produto
        Produto produto = produtoService.criarProduto("Produto Teste", 10.0, 100);
        assertNotNull(produto.getId());
        
        // Cria um pedido com o produto
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 5));
        
        Pedido pedido = pedidoService.criarPedido("Cliente Teste", itens);
        
        assertNotNull(pedido.getId());
        assertEquals("Cliente Teste", pedido.getCliente());
        assertEquals(1, pedido.getItens().size());
        assertEquals(50.0, pedido.getTotal());
    }
    
    @Test
    void testCriarPedidoComProdutoInexistente() {
        // Tenta criar um pedido com um produto que não existe
        Produto produtoFake = new Produto(999L, "Produto Fake", 10.0, 100);
        
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produtoFake, 5));
        
        assertThrows(Exception.class, () -> {
            pedidoService.criarPedido("Cliente Teste", itens);
        });
    }
    
    @Test
    void testPedidoMantemPrecoOriginalDoProduto() {
        // Cria produto e pedido
        Produto produto = produtoService.criarProduto("Produto Original", 10.0, 100);
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 5));
        Pedido pedido = pedidoService.criarPedido("Cliente", itens);
        
        Double totalOriginal = pedido.getTotal();
        assertEquals(50.0, totalOriginal);
        
        // Atualiza o preço do produto (cria nova instância imutável)
        produtoService.atualizarProduto(produto.getId(), "Produto Atualizado", 20.0, 100);
        
        // Busca o pedido novamente - deve manter o preço original
        // porque ItemPedido referencia a instância original do produto (imutável)
        Pedido pedidoAtualizado = pedidoService.buscarPorId(pedido.getId());
        
        // O pedido mantém o preço original do produto quando foi criado
        // Isso é o comportamento correto com imutabilidade
        assertEquals(50.0, pedidoAtualizado.getTotal());
    }
    
    @Test
    void testDeletarProdutoUsadoEmPedido() {
        // Cria produto e pedido
        Produto produto = produtoService.criarProduto("Produto", 10.0, 100);
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto, 5));
        Pedido pedido = pedidoService.criarPedido("Cliente", itens);
        
        assertNotNull(pedido.getId());
        
        // Deleta o produto
        produtoService.deletarProduto(produto.getId());
        
        Pedido pedidoEncontrado = pedidoService.buscarPorId(pedido.getId());
        assertNotNull(pedidoEncontrado);
    }
    
    @Test
    void testListarTodosOsPedidos() {
        // Cria produtos
        Produto produto1 = produtoService.criarProduto("Produto 1", 10.0, 100);
        Produto produto2 = produtoService.criarProduto("Produto 2", 20.0, 100);
        
        // Cria pedidos
        List<ItemPedido> itens1 = new ArrayList<>();
        itens1.add(new ItemPedido(produto1, 2));
        pedidoService.criarPedido("Cliente 1", itens1);
        
        List<ItemPedido> itens2 = new ArrayList<>();
        itens2.add(new ItemPedido(produto2, 3));
        pedidoService.criarPedido("Cliente 2", itens2);
        
        // Lista todos os pedidos
        List<Pedido> pedidos = pedidoService.buscarTodos();
        
        assertEquals(2, pedidos.size());
    }
    
    @Test
    void testAtualizarPedidoComItensInvalidos() {
        Produto produto1 = produtoService.criarProduto("Produto A", 10.0, 50);

        List<ItemPedido> itensIniciais = new ArrayList<>();
        itensIniciais.add(new ItemPedido(produto1, 2));
        Pedido pedido = pedidoService.criarPedido("Cliente X", itensIniciais);

        // Tenta atualizar com um produto inexistente
        Produto produtoFake = new Produto("Produto Fake", 100.0, 10);
        produtoFake = new Produto(999L, "Produto Fake", 10.0, 100);
        List<ItemPedido> itensAtualizadosInvalidos = new ArrayList<>();
        itensAtualizadosInvalidos.add(new ItemPedido(produtoFake, 1));

        Pedido pedidoRequestInvalido = new Pedido("Cliente X");
        for (ItemPedido item : itensAtualizadosInvalidos) {
            pedidoRequestInvalido.adicionarItem(item);
        }
        
        Exception exception = assertThrows(Exception.class, () -> {
            pedidoService.atualizar(pedido.getId(), pedidoRequestInvalido);
        });
        assertTrue(exception.getMessage().contains("Produto com ID 999 não encontrado."));
    }
}

