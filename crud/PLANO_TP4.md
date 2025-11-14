# Plano de Implementação - TP4

## Objetivos
1. Refatorar código seguindo Clean Code e SOLID
2. Criar segundo sistema (Pedidos) e integrar com Produtos
3. Converter de Maven para Gradle
4. Configurar GitHub Actions (CI/CD)
5. Criar testes integrados
6. Documentar mudanças

## Estrutura do Segundo Sistema (Pedidos)
- Model: Pedido (id, cliente, produtos, total, data)
- Repository: PedidoRepository
- Service: PedidoService
- Controller: PedidoController

## Refatorações Planejadas
1. Criar interfaces genéricas (Repository, Service)
2. Aplicar Value Objects (Preco, Estoque)
3. Encapsular coleções
4. Melhorar nomes e responsabilidades
5. Aplicar imutabilidade onde possível

## Workflows GitHub Actions
1. Build e Test
2. Coverage Report
3. Code Quality (opcional)

