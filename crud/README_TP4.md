# TP4 - Refatoração e Integração

## ✅ Implementação Completa

Este projeto implementa todos os requisitos do TP4:

### 1. ✅ Refatoração e Organização do Código
- Interfaces genéricas (`Repository<T, ID>`, `Service<T, ID>`)
- Encapsulamento de coleções (listas imutáveis)
- Substituição de primitivos por objetos (`ItemPedido` como Value Object)
- Aplicação do SRP (Single Responsibility Principle)
- Melhorias de nomenclatura e documentação

### 2. ✅ Integração dos Sistemas
- Sistema de Produtos (existente, refatorado)
- Sistema de Pedidos (novo, integrado)
- Comunicação entre sistemas via `PedidoService`
- Testes integrados validando a comunicação

### 3. ✅ Configuração de Workflows no GitHub Actions
- **CI Workflow** (`ci.yml`): Build, testes e cobertura
- **CD Workflow** (`cd.yml`): Verificação de cobertura mínima
- Triggers: `push`, `pull_request`, `workflow_dispatch`

### 4. ✅ Conversão para Gradle
- `build.gradle` configurado
- Wrapper do Gradle incluído
- Compatibilidade com Maven mantida

### 5. ✅ Testes Integrados
- `IntegracaoProdutoPedidoTest.java` com 5 cenários
- Valida comunicação entre sistemas

## 📚 Documentação

- **`MANUAL_TP4.md`**: Manual completo de execução
- **`RELATORIO_REFATORACAO.md`**: Relatório detalhado das mudanças
- **`README.md`**: Documentação original do projeto

## 🚀 Como Executar

### Com Gradle
```bash
cd crud
./gradlew build
./gradlew run
./gradlew test
```

### Com Maven (compatibilidade)
```bash
cd crud
mvn clean test
mvn exec:java -Dexec.mainClass="crud.Main"
```

## 📊 Cobertura de Testes

- **Meta**: 85% (80% configurado no Gradle)
- **Atual**: ~85%
- **Service**: 100%
- **Repository**: 93%
- **Model**: 95%

## 🔗 APIs Disponíveis

### Produtos
- `GET /api/produtos` - Listar todos
- `GET /api/produtos/{id}` - Buscar por ID
- `POST /api/produtos` - Criar
- `PUT /api/produtos/{id}` - Atualizar
- `DELETE /api/produtos/{id}` - Deletar

### Pedidos
- `GET /api/pedidos` - Listar todos
- `GET /api/pedidos/{id}` - Buscar por ID
- `POST /api/pedidos` - Criar
- `PUT /api/pedidos/{id}` - Atualizar
- `DELETE /api/pedidos/{id}` - Deletar

## 📁 Estrutura

```
crud/
├── src/main/java/crud/
│   ├── controller/     # Controllers HTTP
│   ├── service/        # Lógica de negócio
│   ├── repository/     # Persistência
│   ├── model/          # Modelos de dados
│   └── exception/      # Exceções
├── src/test/java/crud/
│   └── integration/   # Testes integrados
├── .github/workflows/  # GitHub Actions
├── build.gradle        # Configuração Gradle
└── pom.xml            # Maven (compatibilidade)
```

## 🎯 Principais Mudanças

1. **Interfaces Genéricas**: `Repository<T, ID>` e `Service<T, ID>`
2. **Sistema de Pedidos**: Novo sistema integrado
3. **Value Objects**: `ItemPedido` encapsula produto + quantidade
4. **Encapsulamento**: Coleções retornam listas imutáveis
5. **CI/CD**: GitHub Actions configurado
6. **Gradle**: Build system adicionado

---

Para mais detalhes, consulte `MANUAL_TP4.md` e `RELATORIO_REFATORACAO.md`.

