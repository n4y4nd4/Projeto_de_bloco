# Sistema CRUD de Produtos e Pedidos

[![CI - Build and Test](https://github.com/USERNAME/REPO/actions/workflows/ci.yml/badge.svg)](https://github.com/USERNAME/REPO/actions/workflows/ci.yml)
[![Security Analysis](https://github.com/USERNAME/REPO/actions/workflows/security.yml/badge.svg)](https://github.com/USERNAME/REPO/actions/workflows/security.yml)
[![CD - Deploy](https://github.com/USERNAME/REPO/actions/workflows/deploy.yml/badge.svg)](https://github.com/USERNAME/REPO/actions/workflows/deploy.yml)
[![Code Coverage](https://img.shields.io/badge/coverage-90%25-brightgreen)](https://github.com/USERNAME/REPO)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue)](https://maven.apache.org/)
[![Gradle](https://img.shields.io/badge/Gradle-8.0+-blue)](https://gradle.org/)

Sistema completo de gerenciamento de produtos e pedidos desenvolvido em Java com interface web, incluindo operações de cadastro, listagem, edição e exclusão para ambos os módulos. O sistema de pedidos está integrado com o sistema de produtos, permitindo criar pedidos com itens de produtos. Projeto com CI/CD completo, análise de segurança automatizada e cobertura de testes acima de 90%.

## 📋 Índice

- [Requisitos](#requisitos)
- [Instalação](#instalação)
- [Como Executar](#como-executar)
- [Executando os Testes](#executando-os-testes)
- [Interpretando os Resultados](#interpretando-os-resultados)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Cobertura de Testes](#cobertura-de-testes)
- [Segurança](#segurança)
- [Endpoints da API REST](#endpoints-da-api-rest)
- [Troubleshooting](#troubleshooting)

## 🔧 Requisitos

- Java 17 ou superior
- Maven 3.6+ **OU** Gradle 8.0+ (o projeto suporta ambos)
- Chrome/Chromium (para testes Selenium)
- Navegador web moderno (Chrome, Firefox, Edge)

## 📦 Instalação

1. Clone o repositório ou extraia os arquivos
2. Certifique-se de que o Java 17 está instalado:
   ```bash
   java -version
   ```
3. Certifique-se de que o Maven **OU** Gradle está instalado:
   ```bash
   # Para Maven
   mvn -version
   
   # Para Gradle
   gradle -version
   ```

## 🚀 Como Executar

### 1. Compilar o Projeto

**Usando Maven:**
```bash
cd crud
mvn clean compile
```

**Usando Gradle:**
```bash
cd crud
gradle build
```

### 2. Iniciar o Servidor

**Usando Maven:**
```bash
mvn exec:java -Dexec.mainClass="crud.Main"
```

**Usando Gradle:**
```bash
gradle run
```

O servidor será iniciado na porta **7000**. Acesse no navegador:
- **Interface Web**: http://localhost:7000
- **API REST Produtos**: http://localhost:7000/api/produtos
- **API REST Pedidos**: http://localhost:7000/api/pedidos

### 3. Usar a Interface Web

1. Abra o navegador e acesse `http://localhost:7000`
2. Clique em "Adicionar Novo Produto" para cadastrar produtos
3. Use os botões "Editar" e "Excluir" na tabela para gerenciar produtos
4. Os pedidos podem ser criados via API REST (veja seção de Endpoints abaixo)

## 🧪 Executando os Testes

### Executar Todos os Testes

**Usando Maven:**
```bash
mvn test
```

**Usando Gradle:**
```bash
gradle test
```

### Executar Testes Específicos

#### Testes Unitários - Produtos
**Maven:**
```bash
mvn test -Dtest=ProdutoTest
mvn test -Dtest=ProdutoServiceTest
mvn test -Dtest=ProdutoRepositoryTest
mvn test -Dtest=ProdutoControllerTest
```

**Gradle:**
```bash
gradle test --tests ProdutoTest
gradle test --tests ProdutoServiceTest
gradle test --tests ProdutoRepositoryTest
gradle test --tests ProdutoControllerTest
```

#### Testes Unitários - Pedidos
**Maven:**
```bash
mvn test -Dtest=PedidoTest
mvn test -Dtest=PedidoServiceTest
mvn test -Dtest=PedidoRepositoryTest
mvn test -Dtest=PedidoControllerTest
mvn test -Dtest=ItemPedidoTest
```

**Gradle:**
```bash
gradle test --tests PedidoTest
gradle test --tests PedidoServiceTest
gradle test --tests PedidoRepositoryTest
gradle test --tests PedidoControllerTest
gradle test --tests ItemPedidoTest
```

#### Testes de Integração
**Maven:**
```bash
mvn test -Dtest=IntegracaoProdutoPedidoTest
```

**Gradle:**
```bash
gradle test --tests IntegracaoProdutoPedidoTest
```

#### Testes de Interface (Selenium)
**Maven:**
```bash
mvn test -Dtest=ProdutoUITest
mvn test -Dtest=ProdutoUINetworkTest
mvn test -Dtest=PostDeployTest
```

**Gradle:**
```bash
gradle test --tests ProdutoUITest
gradle test --tests ProdutoUINetworkTest
gradle test --tests PostDeployTest
```

**⚠️ Importante**: Os testes Selenium requerem que o servidor esteja rodando. Execute em dois terminais:

**Terminal 1** (Servidor):
```bash
# Maven
mvn exec:java -Dexec.mainClass="crud.Main"

# Gradle
gradle run
```

**Terminal 2** (Testes):
```bash
# Maven
mvn test -Dtest=ProdutoUITest

# Gradle
gradle test --tests ProdutoUITest
```

### Gerar Relatório de Cobertura

**Maven:**
```bash
mvn clean test jacoco:report
```
O relatório será gerado em: `target/site/jacoco/index.html`

**Gradle:**
```bash
gradle clean test jacocoTestReport
```
O relatório será gerado em: `build/reports/jacoco/test/html/index.html`

Abra o arquivo HTML no navegador para visualizar a cobertura detalhada.

## 📊 Interpretando os Resultados

### Relatório de Testes

**Maven:** Após executar `mvn test`, os resultados são salvos em:
- **Relatórios de texto**: `target/surefire-reports/*.txt`
- **Relatórios XML**: `target/surefire-reports/TEST-*.xml`

**Gradle:** Após executar `gradle test`, os resultados são salvos em:
- **Relatórios HTML**: `build/reports/tests/test/index.html`
- **Relatórios XML**: `build/test-results/test/*.xml`

**Exemplo de saída bem-sucedida**:
```
Tests run: 25+, Failures: 0, Errors: 0, Skipped: 0
```

### Relatório de Cobertura (JaCoCo)

O relatório mostra:
- **Cobertura de Instruções**: Percentual de código executado
- **Cobertura de Branches**: Percentual de decisões testadas
- **Cobertura de Linhas**: Linhas de código cobertas
- **Cobertura de Métodos**: Métodos testados

**Meta de Cobertura**: 90% de instruções e 85% de branches (configurado no `pom.xml` e `build.gradle`)

**Cobertura Atual**:
- Service (Produto/Pedido): 100%
- Exception: 100%
- Model: 95%
- Repository: 93%
- Controller: 59% (excluído da meta)

### Interpretando Falhas de Teste

#### Testes Unitários Falhando
- Verifique se os dados de teste estão corretos
- Confirme que as dependências estão instaladas
- Revise as mensagens de erro nos relatórios

#### Testes Selenium Falhando
- Certifique-se de que o servidor está rodando na porta 7000
- Verifique se o Chrome está instalado
- Confirme que o WebDriverManager consegue baixar o driver

#### Cobertura Abaixo da Meta
- **Maven:** Execute `mvn jacoco:check` para verificar regras
- **Gradle:** Execute `gradle jacocoTestCoverageVerification` para verificar regras
- Adicione testes para métodos não cobertos
- Revise o relatório HTML para identificar lacunas

## 📁 Estrutura do Projeto

```
crud/
├── src/
│   ├── main/
│   │   ├── java/crud/
│   │   │   ├── controller/      # Controladores REST
│   │   │   │   ├── ProdutoController.java
│   │   │   │   └── PedidoController.java
│   │   │   ├── service/         # Lógica de negócio
│   │   │   │   ├── ProdutoService.java
│   │   │   │   ├── PedidoService.java
│   │   │   │   └── Service.java (interface base)
│   │   │   ├── repository/      # Acesso a dados
│   │   │   │   ├── ProdutoRepository.java
│   │   │   │   ├── PedidoRepository.java
│   │   │   │   └── Repository.java (interface base)
│   │   │   ├── model/           # Modelos de dados
│   │   │   │   ├── Produto.java
│   │   │   │   ├── Pedido.java
│   │   │   │   └── ItemPedido.java
│   │   │   ├── exception/       # Exceções customizadas
│   │   │   │   ├── ProdutoNaoEncontradoException.java
│   │   │   │   ├── PedidoNaoEncontradoException.java
│   │   │   │   └── ValidacaoException.java
│   │   │   └── Main.java        # Classe principal
│   │   └── resources/
│   │       └── public/          # Interface web (HTML/CSS/JS)
│   │           ├── index.html
│   │           ├── add.html
│   │           ├── edit.html
│   │           └── style.css
│   └── test/
│       └── java/crud/
│           ├── model/            # Testes do modelo
│           │   ├── ProdutoTest.java
│           │   ├── PedidoTest.java
│           │   └── ItemPedidoTest.java
│           ├── service/          # Testes do serviço
│           │   ├── ProdutoServiceTest.java
│           │   ├── ProdutoServiceFuzzTest.java
│           │   └── PedidoServiceTest.java
│           ├── repository/       # Testes do repositório
│           │   ├── ProdutoRepositoryTest.java
│           │   └── PedidoRepositoryTest.java
│           ├── controller/       # Testes dos controladores
│           │   ├── ProdutoControllerTest.java
│           │   └── PedidoControllerTest.java
│           ├── integration/      # Testes de integração
│           │   └── IntegracaoProdutoPedidoTest.java
│           ├── selenium/         # Testes de interface
│           │   ├── ProdutoUITest.java
│           │   ├── ProdutoUINetworkTest.java
│           │   └── PostDeployTest.java
│           └── pages/            # Page Objects (POM)
├── target/                       # Arquivos compilados (Maven)
├── build/                        # Arquivos compilados (Gradle)
├── pom.xml                       # Configuração Maven
├── build.gradle                  # Configuração Gradle
└── README.md                     # Este arquivo
```

## 🛠 Tecnologias Utilizadas

### Backend
- **Java 17**: Linguagem de programação
- **Javalin 5.6.3**: Framework web leve
- **Jackson 2.16.1**: Serialização JSON
- **Maven 3.6+** ou **Gradle 8.0+**: Gerenciamento de dependências e build

### Frontend
- **HTML5**: Estrutura
- **CSS3**: Estilização
- **JavaScript (ES6+)**: Interatividade

### Testes
- **JUnit 5.10.1**: Framework de testes
- **Jqwik 1.8.0**: Testes baseados em propriedades
- **Selenium WebDriver 4.16.1**: Automação de interface
- **WebDriverManager 5.6.3**: Gerenciamento automático de drivers
- **Mockito 4.11.0**: Mocking para testes
- **JaCoCo 0.8.11**: Análise de cobertura

## 📈 Cobertura de Testes

### Tipos de Testes Implementados

1. **Testes Unitários**: Model (Produto, Pedido, ItemPedido), Service (ProdutoService, PedidoService), Repository (ProdutoRepository, PedidoRepository), Controller (ProdutoController, PedidoController)
2. **Testes de Integração**: Fluxo completo CRUD entre Produtos e Pedidos
3. **Testes de Interface**: Selenium com Page Object Model para a interface web de produtos
4. **Testes Parametrizados**: Múltiplos cenários para validação
5. **Testes de Propriedades**: Jqwik para validação aleatória (Fuzz Testing)
6. **Testes de Validação**: Entradas inválidas e erros
7. **Testes de Rede**: Verificação de comunicação HTTP com a API

### Estratégias de Teste

- **Partições Equivalentes**: Valores limites e típicos
- **Análise de Limites**: Valores mínimo, máximo e fronteira
- **Testes de Falha**: Simulação de erros e exceções
- **Fuzz Testing**: Entradas aleatórias e maliciosas

## 🔒 Segurança

- Validação de entrada no backend para produtos e pedidos
- Mensagens de erro que não expõem detalhes técnicos
- Tratamento de exceções robusto com exceções customizadas
- Sanitização de dados de entrada
- Validação de integridade referencial (pedidos validam produtos existentes)

## 🌐 Endpoints da API REST

### Produtos

- `GET /api/produtos` - Lista todos os produtos
- `GET /api/produtos/{id}` - Busca produto por ID
- `POST /api/produtos` - Cria novo produto
- `PUT /api/produtos/{id}` - Atualiza produto existente
- `DELETE /api/produtos/{id}` - Remove produto por ID
- `DELETE /api/produtos/deleteall` - Remove todos os produtos

### Pedidos

- `GET /api/pedidos` - Lista todos os pedidos
- `GET /api/pedidos/{id}` - Busca pedido por ID
- `POST /api/pedidos` - Cria novo pedido (requer produtos válidos)
- `PUT /api/pedidos/{id}` - Atualiza pedido existente
- `DELETE /api/pedidos/{id}` - Remove pedido por ID
- `DELETE /api/pedidos/deleteall` - Remove todos os pedidos

### Exemplo de Uso da API

**Criar um Produto:**
```bash
curl -X POST http://localhost:7000/api/produtos \
  -H "Content-Type: application/json" \
  -d '{"nome": "Produto Exemplo", "preco": 29.99, "estoque": 100}'
```

**Criar um Pedido:**
```bash
curl -X POST http://localhost:7000/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "cliente": "João Silva",
    "itens": [
      {"produtoId": 1, "quantidade": 2},
      {"produtoId": 2, "quantidade": 1}
    ]
  }'
```

## 🐛 Troubleshooting

### Erro: "Porta 7000 já em uso"
```bash
# Windows
netstat -ano | findstr :7000
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:7000 | xargs kill -9
```

### Erro: "ChromeDriver não encontrado"
O WebDriverManager baixa automaticamente. Se falhar:
1. Verifique conexão com internet
2. Instale Chrome manualmente
3. Configure o caminho no código

### Erro: "Cobertura abaixo da meta"
1. **Maven:** Execute `mvn jacoco:report` e abra `target/site/jacoco/index.html`
2. **Gradle:** Execute `gradle jacocoTestReport` e abra `build/reports/jacoco/test/html/index.html`
3. Identifique métodos não cobertos
4. Adicione testes específicos

### Erro: "Produto não encontrado ao criar pedido"
- Certifique-se de que os produtos existem antes de criar pedidos
- Use `GET /api/produtos` para listar produtos disponíveis
- Os IDs dos produtos devem corresponder a produtos válidos no sistema

## 📝 Licença

Este projeto foi desenvolvido para fins educacionais.

## 👥 Autores

Desenvolvido como parte de trabalho prático de engenharia de software.

---

**Última atualização**: 2024

