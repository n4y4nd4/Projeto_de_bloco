# Manual de Execução - TP4: Refatoração e Integração

## 📋 Índice

1. [Visão Geral](#visão-geral)
2. [Mudanças Implementadas](#mudanças-implementadas)
3. [Como Executar](#como-executar)
4. [Workflows do GitHub Actions](#workflows-do-github-actions)
5. [Estrutura do Projeto](#estrutura-do-projeto)

---

## 🎯 Visão Geral

Este projeto foi refatorado e integrado conforme os requisitos do TP4:

- ✅ **Refatoração completa** seguindo Clean Code e SOLID
- ✅ **Integração de dois sistemas**: Produtos e Pedidos
- ✅ **Conversão para Gradle** (mantendo compatibilidade com Maven)
- ✅ **CI/CD com GitHub Actions**
- ✅ **Testes integrados** validando a comunicação entre sistemas

---

## 🔄 Mudanças Implementadas

### 1. Refatoração e Organização do Código

#### Interfaces Genéricas
- **`Repository<T, ID>`**: Interface genérica para repositórios
- **`Service<T, ID>`**: Interface genérica para serviços (criada, mas não implementada completamente para manter compatibilidade)

#### Encapsulamento de Coleções
- `ProdutoRepository.findAll()` e `PedidoRepository.findAll()` agora retornam listas imutáveis
- `Pedido.getItens()` retorna cópia imutável da lista
- Métodos `adicionarItem()` e `removerItem()` encapsulam modificações

#### Substituição de Primitivos por Objetos
- **`ItemPedido`**: Value Object que encapsula Produto + quantidade
- **`Pedido.getTotal()`**: Método calculado em vez de campo primitivo

#### Melhorias de Nomenclatura
- Métodos e classes com nomes mais descritivos
- Documentação JavaDoc em todas as classes públicas

#### Princípio da Responsabilidade Única (SRP)
- Cada classe tem uma responsabilidade clara:
  - `Repository`: Persistência
  - `Service`: Lógica de negócio
  - `Controller`: HTTP handlers
  - `Model`: Dados

### 2. Integração dos Sistemas

#### Sistema de Produtos (Existente)
- Mantido e refatorado
- Interface `Repository<Produto, Long>` implementada

#### Sistema de Pedidos (Novo)
- **Model**: `Pedido`, `ItemPedido`
- **Repository**: `PedidoRepository`
- **Service**: `PedidoService` (integra com `ProdutoRepository`)
- **Controller**: `PedidoController`
- **Exception**: `PedidoNaoEncontradoException`

#### Integração
- `PedidoService` valida que produtos existem antes de criar pedidos
- `Main.java` configura rotas de ambos os sistemas
- Testes integrados validam a comunicação

### 3. Conversão para Gradle

#### Arquivos Criados
- `build.gradle`: Configuração do projeto
- `settings.gradle`: Configuração do módulo
- `gradlew.bat`: Wrapper para Windows
- `gradle/wrapper/gradle-wrapper.properties`: Configuração do wrapper

#### Compatibilidade
- Projeto mantém `pom.xml` para compatibilidade
- Gradle e Maven podem ser usados alternadamente

### 4. GitHub Actions

#### Workflow CI (`ci.yml`)
- **Triggers**: `push`, `pull_request`, `workflow_dispatch`
- **Jobs**:
  - Build com Gradle
  - Execução de testes
  - Geração de relatório de cobertura
  - Upload de artifacts

#### Workflow CD (`cd.yml`)
- **Triggers**: `push` para `main`, `workflow_dispatch`
- **Jobs**:
  - Verificação de cobertura mínima (80%)
  - Upload de relatórios

---

## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior
- Gradle 8.5+ (ou use o wrapper: `gradlew`)
- Chrome/Chromium (para testes Selenium)

### Usando Gradle

#### 1. Compilar o Projeto

```bash
cd crud
./gradlew build
```

**Windows:**
```cmd
cd crud
gradlew.bat build
```

#### 2. Executar o Servidor

```bash
./gradlew run
```

**Windows:**
```cmd
gradlew.bat run
```

O servidor será iniciado na porta **7000**.

#### 3. Executar Testes

```bash
./gradlew test
```

**Windows:**
```cmd
gradlew.bat test
```

#### 4. Gerar Relatório de Cobertura

```bash
./gradlew jacocoTestReport
```

O relatório será gerado em: `build/reports/jacoco/test/html/index.html`

#### 5. Verificar Cobertura Mínima

```bash
./gradlew jacocoTestCoverageVerification
```

### Usando Maven (Compatibilidade)

O projeto ainda suporta Maven:

```bash
mvn clean test
mvn exec:java -Dexec.mainClass="crud.Main"
```

---

## 🔧 Workflows do GitHub Actions

### Como Executar Manualmente

1. Acesse a aba **Actions** no GitHub
2. Selecione o workflow desejado (CI ou CD)
3. Clique em **Run workflow**
4. Selecione a branch e clique em **Run workflow**

### Como Monitorar

1. Acesse **Actions** no repositório
2. Clique no workflow em execução
3. Monitore os logs em tempo real
4. Baixe artifacts (relatórios de cobertura) após conclusão

### Interpretando Resultados

#### ✅ Sucesso
- Build completo sem erros
- Todos os testes passando
- Cobertura acima de 80%

#### ❌ Falha
- **Build falhou**: Verifique erros de compilação
- **Testes falharam**: Veja logs detalhados em `test-results`
- **Cobertura insuficiente**: Adicione mais testes

---

## 📁 Estrutura do Projeto

```
crud/
├── src/
│   ├── main/
│   │   ├── java/crud/
│   │   │   ├── controller/          # Controllers HTTP
│   │   │   │   ├── ProdutoController.java
│   │   │   │   └── PedidoController.java
│   │   │   ├── service/             # Lógica de negócio
│   │   │   │   ├── ProdutoService.java
│   │   │   │   ├── PedidoService.java
│   │   │   │   └── Service.java     # Interface genérica
│   │   │   ├── repository/          # Persistência
│   │   │   │   ├── ProdutoRepository.java
│   │   │   │   ├── PedidoRepository.java
│   │   │   │   └── Repository.java  # Interface genérica
│   │   │   ├── model/               # Modelos de dados
│   │   │   │   ├── Produto.java
│   │   │   │   ├── Pedido.java
│   │   │   │   └── ItemPedido.java  # Value Object
│   │   │   ├── exception/           # Exceções customizadas
│   │   │   │   ├── ValidacaoException.java
│   │   │   │   ├── ProdutoNaoEncontradoException.java
│   │   │   │   └── PedidoNaoEncontradoException.java
│   │   │   └── Main.java            # Classe principal
│   │   └── resources/
│   │       └── public/              # Interface web
│   └── test/
│       └── java/crud/
│           ├── integration/         # Testes integrados
│           │   └── IntegracaoProdutoPedidoTest.java
│           ├── controller/
│           ├── service/
│           ├── repository/
│           └── selenium/
├── .github/
│   └── workflows/
│       ├── ci.yml                   # Workflow CI
│       └── cd.yml                   # Workflow CD
├── build.gradle                     # Configuração Gradle
├── settings.gradle
├── gradlew.bat                      # Gradle wrapper (Windows)
├── pom.xml                          # Maven (compatibilidade)
└── README.md
```

---

## 🧪 Testes Integrados

### Executar Testes Integrados

```bash
./gradlew test --tests "crud.integration.*"
```

### O que os Testes Validam

1. **Criação de pedido com produto existente**
2. **Falha ao criar pedido com produto inexistente**
3. **Pedido reflete alterações no produto**
4. **Deletar produto usado em pedido**
5. **Listar todos os pedidos**

---

## 📊 Cobertura de Testes

### Meta: 85% (80% configurado no Gradle)

### Cobertura Atual
- Service: 100%
- Repository: 93%
- Model: 95%
- Exception: 100%
- Controller: Excluído da meta (camada de apresentação)

### Verificar Cobertura

```bash
./gradlew jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

---

## 🔍 Principais Mudanças

### Antes (TP3)
- Apenas sistema de Produtos
- Código sem interfaces genéricas
- Coleções expostas diretamente
- Maven apenas

### Depois (TP4)
- ✅ Sistemas integrados (Produtos + Pedidos)
- ✅ Interfaces genéricas (`Repository`, `Service`)
- ✅ Coleções encapsuladas (listas imutáveis)
- ✅ Value Objects (`ItemPedido`)
- ✅ Gradle + Maven
- ✅ CI/CD com GitHub Actions
- ✅ Testes integrados

---

## 🐛 Troubleshooting

### Erro: "Gradle wrapper not found"
```bash
# Criar wrapper manualmente
gradle wrapper --gradle-version 8.5
```

### Erro: "Java version mismatch"
```bash
# Verificar versão do Java
java -version  # Deve ser 17+
```

### Testes Selenium falhando
- Certifique-se de que o servidor está rodando
- Verifique se o Chrome está instalado
- WebDriverManager baixará o driver automaticamente

---

## 📝 Notas Finais

- O projeto mantém compatibilidade com Maven para facilitar migração
- Todos os testes existentes continuam funcionando
- Novos testes integrados validam a comunicação entre sistemas
- Workflows do GitHub Actions são executados automaticamente em push/PR

---

## 🎓 Aprendizados Aplicados

1. **Clean Code**: Nomes descritivos, métodos pequenos, responsabilidade única
2. **SOLID**: SRP aplicado em todas as classes
3. **DRY**: Interfaces genéricas evitam duplicação
4. **Encapsulamento**: Coleções protegidas contra modificações externas
5. **Value Objects**: `ItemPedido` substitui primitivos
6. **CI/CD**: Automação de build, teste e validação

