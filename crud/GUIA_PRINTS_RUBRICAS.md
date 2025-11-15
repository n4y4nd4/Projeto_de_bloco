# Guia de Prints para Comprovação das Rubricas

Este documento indica **exatamente onde** tirar prints (screenshots) para comprovar cada rubrica do TP4.

---

## 📋 Rubrica 1: Identificou e eliminou problemas no código

**Onde tirar o print:**

### Print 1.1: Interfaces genéricas eliminando redundância
**Arquivo:** `crud/src/main/java/crud/repository/Repository.java`
- **Linhas 1-51**: Mostra a interface genérica `Repository<T, ID>` que elimina duplicação entre `ProdutoRepository` e `PedidoRepository`
- **Print:** Mostrar a interface completa com os comentários JavaDoc

### Print 1.2: Encapsulamento de coleções (eliminando exposição direta)
**Arquivo:** `crud/src/main/java/crud/repository/ProdutoRepository.java`
- **Linhas 39-42**: Método `findAll()` retornando lista imutável
```java
@Override
public List<Produto> findAll() {
    return Collections.unmodifiableList(new ArrayList<>(produtos));
}
```
- **Print:** Mostrar essas linhas destacadas

### Print 1.3: Encapsulamento no modelo Pedido
**Arquivo:** `crud/src/main/java/crud/model/Pedido.java`
- **Linhas 44-46**: `getItens()` retorna lista imutável
- **Linhas 49-54**: Método `adicionarItem()` encapsula modificação
- **Linhas 57-62**: Método `removerItem()` encapsula remoção
- **Print:** Mostrar esses três métodos juntos

### Print 1.4: Value Object eliminando primitivos
**Arquivo:** `crud/src/main/java/crud/model/ItemPedido.java`
- **Linhas 1-54**: Classe completa mostrando encapsulamento de Produto + quantidade
- **Print:** Mostrar a classe completa

**📸 RESULTADO:** Tire 1 print mostrando a interface `Repository.java` (linhas 1-51) - isso comprova a eliminação de redundância através de interfaces genéricas.

---

## 📋 Rubrica 2: Refatorou código guiado por testes

**Onde tirar o print:**

### Print 2.1: Testes criados após refatoração
**Arquivo:** `crud/src/test/java/crud/service/PedidoServiceTest.java`
- **Linhas 1-316**: Arquivo completo com 22 testes
- **Print:** Mostrar o início do arquivo (linhas 1-50) mostrando a estrutura de testes

### Print 2.2: Testes de integração validando refatoração
**Arquivo:** `crud/src/test/java/crud/integration/IntegracaoProdutoPedidoTest.java`
- **Linhas 1-176**: Testes integrados validando a comunicação entre sistemas
- **Print:** Mostrar um teste específico, por exemplo `testCriarPedidoComProdutoExistente` (linhas 40-60)

### Print 2.3: Resultado dos testes executados
**Terminal/Console:**
- Execute: `mvn test` ou `./gradlew test`
- **Print:** Mostrar a saída final com "Tests run: 153, Failures: 0, Errors: 0"
- **Ou:** Mostrar o relatório de cobertura indicando ≥90%

**📸 RESULTADO:** Tire 1 print do terminal mostrando "Tests run: 153, Failures: 0, Errors: 0" após executar `mvn test` - isso comprova que o código foi refatorado e mantém todos os testes passando.

---

## 📋 Rubrica 3: Criou e configurou workflows no GitHub Actions

**Onde tirar o print:**

### Print 3.1: Workflow CI completo
**Arquivo:** `crud/.github/workflows/ci.yml`
- **Linhas 1-57**: Arquivo completo mostrando:
  - Triggers (linhas 3-9)
  - Job `build-and-test` (linhas 11-55)
  - Steps organizados (linhas 14-55)
- **Print:** Mostrar o arquivo completo no editor

### Print 3.2: Workflow CD completo
**Arquivo:** `crud/.github/workflows/cd.yml`
- **Linhas 1-42**: Arquivo completo mostrando:
  - Triggers (linhas 3-6)
  - Job `coverage-check` (linhas 9-41)
  - Steps de validação (linhas 12-41)
- **Print:** Mostrar o arquivo completo no editor

### Print 3.3: Estrutura de jobs e steps
**No GitHub:**
- Acesse: `https://github.com/SEU_USUARIO/SEU_REPO/actions`
- **Print:** Mostrar a lista de workflows executados com os jobs visíveis

**📸 RESULTADO:** Tire 1 print do arquivo `ci.yml` completo (linhas 1-57) mostrando a estrutura de jobs e steps - isso comprova a criação e configuração de workflows.

---

## 📋 Rubrica 4: Integrou actions do GitHub Marketplace

**Onde tirar o print:**

### Print 4.1: Action checkout@v4
**Arquivo:** `crud/.github/workflows/ci.yml`
- **Linhas 15-16**: 
```yaml
- name: Checkout code
  uses: actions/checkout@v4
```
- **Print:** Mostrar essas linhas destacadas

### Print 4.2: Action setup-java@v4
**Arquivo:** `crud/.github/workflows/ci.yml`
- **Linhas 18-23**:
```yaml
- name: Set up JDK 17
  uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'
    cache: 'gradle'
```
- **Print:** Mostrar essas linhas destacadas

### Print 4.3: Action upload-artifact@v4
**Arquivo:** `crud/.github/workflows/ci.yml`
- **Linhas 41-47** e **49-55**: Uso de `actions/upload-artifact@v4`
- **Print:** Mostrar uma dessas seções

**📸 RESULTADO:** Tire 1 print mostrando as linhas 15-23 do `ci.yml` (checkout@v4 e setup-java@v4) - isso comprova a integração de actions do GitHub Marketplace.

---

## 📋 Rubrica 5: Gerenciou runners no GitHub Actions

**Onde tirar o print:**

### Print 5.1: Configuração de runner
**Arquivo:** `crud/.github/workflows/ci.yml`
- **Linha 12**: `runs-on: ubuntu-latest`
- **Arquivo:** `crud/.github/workflows/cd.yml`
- **Linha 10**: `runs-on: ubuntu-latest`
- **Print:** Mostrar essas linhas em ambos os arquivos

### Print 5.2: Configuração de ambiente (working-directory)
**Arquivo:** `crud/.github/workflows/ci.yml`
- **Linhas 27, 31, 35, 38**: Uso de `working-directory: ./crud`
- **Print:** Mostrar uma dessas linhas com o contexto

### Print 5.3: No GitHub (se houver runners customizados)
**No GitHub:**
- Settings → Actions → Runners
- **Print:** Mostrar a página de runners (se aplicável)

**📸 RESULTADO:** Tire 1 print mostrando a linha 12 do `ci.yml` (`runs-on: ubuntu-latest`) junto com uma linha que usa `working-directory` (ex: linha 27) - isso comprova o gerenciamento de runners e ambientes.

---

## 📋 Rubrica 6: Implementou esteiras de CI/CD eficientes

**Onde tirar o print:**

### Print 6.1: Triggers automatizados
**Arquivo:** `crud/.github/workflows/ci.yml`
- **Linhas 3-9**: Triggers configurados
```yaml
on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]
  workflow_dispatch:
```
- **Print:** Mostrar essas linhas destacadas

### Print 6.2: Pipeline completo (jobs e steps)
**Arquivo:** `crud/.github/workflows/ci.yml`
- **Linhas 10-56**: Job completo com todos os steps
- **Print:** Mostrar o job completo (linhas 11-55)

### Print 6.3: Pipeline de CD
**Arquivo:** `crud/.github/workflows/cd.yml`
- **Linhas 1-42**: Pipeline de CD completo
- **Print:** Mostrar o arquivo completo

### Print 6.4: Execução no GitHub
**No GitHub:**
- Acesse: `https://github.com/SEU_USUARIO/SEU_REPO/actions`
- Clique em uma execução recente
- **Print:** Mostrar a visualização do pipeline com todos os steps executados

**📸 RESULTADO:** Tire 1 print mostrando as linhas 3-9 do `ci.yml` (triggers) + linhas 10-56 (pipeline completo) - isso comprova a implementação de esteiras CI/CD com triggers automatizados e pipeline claro.

---

## 📝 Resumo dos Prints Necessários

Para comprovar todas as 6 rubricas, você precisa de **6 prints**:

1. **Rubrica 1:** `Repository.java` (linhas 1-51) - Interface genérica
2. **Rubrica 2:** Terminal com resultado `mvn test` mostrando "Tests run: 153, Failures: 0"
3. **Rubrica 3:** `ci.yml` completo (linhas 1-57) - Estrutura de workflows
4. **Rubrica 4:** `ci.yml` (linhas 15-23) - Actions do Marketplace
5. **Rubrica 5:** `ci.yml` (linha 12 + linha 27) - Configuração de runners
6. **Rubrica 6:** `ci.yml` (linhas 3-9 + linhas 10-56) - Triggers e pipeline completo

---

## 🎯 Dica Extra

Se quiser ser mais completo, você pode também mostrar:

- **Cobertura de testes:** Relatório JaCoCo (após `mvn jacoco:report`)
- **Execução no GitHub:** Screenshot da página Actions mostrando workflows executados
- **Estrutura de pastas:** `.github/workflows/` com os dois arquivos

