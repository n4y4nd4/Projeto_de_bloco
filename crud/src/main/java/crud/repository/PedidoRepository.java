package crud.repository;

import crud.model.Pedido;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


public class PedidoRepository implements Repository<Pedido, Long> {
    private final List<Pedido> pedidos = new ArrayList<>();
    private final AtomicLong currentId = new AtomicLong(1);
    
    /**
     * Salva um pedido. Se não tiver ID, cria novo. Se tiver, atualiza.
     * Mantém compatibilidade com modelo mutável de Pedido.
     */
    @Override
    public Pedido save(Pedido pedido) {
        if (pedido.getId() == null) {
            pedido.setId(currentId.getAndIncrement());
            pedidos.add(pedido);
            return pedido;
        } else {
            for (int i = 0; i < pedidos.size(); i++) {
                if (pedidos.get(i).getId().equals(pedido.getId())) {
                    pedidos.set(i, pedido);
                    return pedido;
                }
            }
            pedidos.add(pedido);
            return pedido;
        }
    }
    
    @Override
    public Optional<Pedido> findById(Long id) {
        return pedidos.stream()
                     .filter(p -> p.getId().equals(id))
                     .findFirst();
    }
    
   
    @Override
    public List<Pedido> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(pedidos));
    }
    
    @Override
    public boolean delete(Long id) {
        return pedidos.removeIf(p -> p.getId().equals(id));
    }
    
    @Override
    public void deleteAll() {
        pedidos.clear();
        currentId.set(1);
    }
}

