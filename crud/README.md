# Sistema CRUD de Produtos

Sistema completo de gerenciamento de produtos desenvolvido em Java com interface web, incluindo operações de cadastro, listagem, edição e exclusão.

## 📋 Índice

- [Requisitos](#requisitos)
- [Instalação](#instalação)
- [Como Executar](#como-executar)
- [Executando os Testes](#executando-os-testes)
- [Interpretando os Resultados](#interpretando-os-resultados)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Cobertura de Testes](#cobertura-de-testes)

## 🔧 Requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Chrome/Chromium (para testes Selenium)
- Navegador web moderno (Chrome, Firefox, Edge)

## 📦 Instalação

1. Clone o repositório ou extraia os arquivos
2. Certifique-se de que o Java 17 está instalado:
   ```bash
   java -version
   ```
3. Certifique-se de que o Maven está instalado:
   ```bash
   mvn -version
   ```

## 🚀 Como Executar

### 1. Compilar o Projeto

```bash
cd crud
mvn clean compile
```

### 2. Iniciar o Servidor

```bash
mvn exec:java -Dexec.mainClass="crud.Main"
```

O servidor será iniciado na porta **7000**. Acesse no navegador:
- **Interface Web**: http://localhost:7000
- **API REST**: http://localhost:7000/api/produtos

### 3. Usar a Interface Web

1. Abra o navegador e acesse `http://localhost:7000`
2. Clique em "Adicionar Novo Produto" para cadastrar
3. Use os botões "Editar" e "Excluir" na tabela para gerenciar produtos

## 🧪 Executando os Testes

### Executar Todos os Testes

```bash
mvn test
```

### Executar Testes Específicos

#### Testes Unitários
```bash
mvn test -Dtest=ProdutoTest
mvn test -Dtest=ProdutoServiceTest
mvn test -Dtest=ProdutoRepositoryTest
```

#### Testes de Interface (Selenium)
```bash
mvn test -Dtest=ProdutoUITest
```

**⚠️ Importante**: Os testes Selenium requerem que o servidor esteja rodando. Execute em dois terminais:

**Terminal 1** (Servidor):
```bash
mvn exec:java -Dexec.mainClass="crud.Main"
```

**Terminal 2** (Testes):
```bash
mvn test -Dtest=ProdutoUITest
```

### Gerar Relatório de Cobertura

```bash
mvn clean test jacoco:report
```

O relatório será gerado em: `target/site/jacoco/index.html`

Abra o arquivo HTML no navegador para visualizar a cobertura detalhada.

## 📊 Interpretando os Resultados

### Relatório de Testes

Após executar `mvn test`, os resultados são salvos em:
- **Relatórios de texto**: `target/surefire-reports/*.txt`
- **Relatórios XML**: `target/surefire-reports/TEST-*.xml`

**Exemplo de saída bem-sucedida**:
```
Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
```

### Relatório de Cobertura (JaCoCo)

O relatório mostra:
- **Cobertura de Instruções**: Percentual de código executado
- **Cobertura de Branches**: Percentual de decisões testadas
- **Cobertura de Linhas**: Linhas de código cobertas
- **Cobertura de Métodos**: Métodos testados

**Meta de Cobertura**: 85% (configurado no `pom.xml`)

**Cobertura Atual**:
- Service: 100%
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
- Execute `mvn jacoco:check` para verificar regras
- Adicione testes para métodos não cobertos
- Revise o relatório HTML para identificar lacunas

## 📁 Estrutura do Projeto

```
crud/
├── src/
│   ├── main/
│   │   ├── java/crud/
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── service/         # Lógica de negócio
│   │   │   ├── repository/      # Acesso a dados
│   │   │   ├── model/           # Modelos de dados
│   │   │   ├── exception/       # Exceções customizadas
│   │   │   └── Main.java        # Classe principal
│   │   └── resources/
│   │       └── public/          # Interface web (HTML/CSS/JS)
│   └── test/
│       └── java/crud/
│           ├── model/            # Testes do modelo
│           ├── service/          # Testes do serviço
│           ├── repository/       # Testes do repositório
│           ├── selenium/         # Testes de interface
│           └── pages/            # Page Objects (POM)
├── target/                       # Arquivos compilados e relatórios
├── pom.xml                       # Configuração Maven
└── README.md                     # Este arquivo
```

## 🛠 Tecnologias Utilizadas

### Backend
- **Java 17**: Linguagem de programação
- **Javalin 5.6.3**: Framework web leve
- **Jackson**: Serialização JSON
- **Maven**: Gerenciamento de dependências

### Frontend
- **HTML5**: Estrutura
- **CSS3**: Estilização
- **JavaScript (ES6+)**: Interatividade

### Testes
- **JUnit 5**: Framework de testes
- **Jqwik**: Testes baseados em propriedades
- **Selenium WebDriver**: Automação de interface
- **JaCoCo**: Análise de cobertura

## 📈 Cobertura de Testes

### Tipos de Testes Implementados

1. **Testes Unitários**: Model, Service, Repository
2. **Testes de Integração**: Fluxo completo CRUD
3. **Testes de Interface**: Selenium com Page Object Model
4. **Testes Parametrizados**: Múltiplos cenários
5. **Testes de Propriedades**: Jqwik para validação aleatória
6. **Testes de Validação**: Entradas inválidas e erros

### Estratégias de Teste

- **Partições Equivalentes**: Valores limites e típicos
- **Análise de Limites**: Valores mínimo, máximo e fronteira
- **Testes de Falha**: Simulação de erros e exceções
- **Fuzz Testing**: Entradas aleatórias e maliciosas

## 🔒 Segurança

- Validação de entrada no backend
- Mensagens de erro que não expõem detalhes técnicos
- Tratamento de exceções robusto
- Sanitização de dados de entrada

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
1. Execute `mvn jacoco:report`
2. Abra `target/site/jacoco/index.html`
3. Identifique métodos não cobertos
4. Adicione testes específicos

## 📝 Licença

Este projeto foi desenvolvido para fins educacionais.

## 👥 Autores

Desenvolvido como parte de trabalho prático de engenharia de software.

---

**Última atualização**: 2024

