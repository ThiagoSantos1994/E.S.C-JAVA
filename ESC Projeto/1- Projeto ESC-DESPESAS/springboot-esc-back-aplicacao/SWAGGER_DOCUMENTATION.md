# 📘 Documentação Swagger - API ESC

## ✅ Swagger Implementado e Funcional!

A documentação Swagger/OpenAPI foi implementada com sucesso para todos os controllers da aplicação.

---

## 🌐 Como Acessar o Swagger

Após iniciar a aplicação Spring Boot, acesse a documentação interativa através das URLs:

### **Swagger UI (Interface Visual)**
```
http://localhost:8020/swagger-ui.html
```

### **JSON da API (OpenAPI Specification)**
```
http://localhost:8020/v2/api-docs
```

---

## 📊 Controllers Documentados

Todos os controllers foram documentados com anotações Swagger:

| Controller | Tag | Endpoints |
|------------|-----|-----------|
| **LoginController** | Autenticação | Autenticação e gerenciamento de sessão |
| **SessaoController** | Autenticação | Validação de sessão |
| **BackupController** | Backup | Processar backup da base de dados |
| **ParametrosController** | Parâmetros | Configurações do sistema |
| **LembretesController** | Lembretes | Gerenciamento de lembretes |
| **ConsolidacaoController** | Consolidação | Consolidações de despesas |
| **DespesasParceladasController** | Despesas Parceladas | Gerenciamento de despesas parceladas |
| **LancamentosMensaisController** | Lançamentos Financeiros | Lançamentos mensais consolidados |
| **LancamentosFinanceirosController** | Lançamentos Financeiros | Lançamentos e despesas mensais |
| **DetalheDespesasController** | Detalhe Despesas | Detalhes de despesas mensais |

---

## 🎯 Recursos do Swagger Implementados

### ✅ **Anotações Utilizadas**

- **`@Api`** - Define a tag e descrição do controller
- **`@ApiOperation`** - Descreve a operação do endpoint
- **`@ApiParam`** - Documenta os parâmetros de entrada
- **`@ApiResponses`** - Define os possíveis códigos de resposta HTTP

### ✅ **Funcionalidades**

- 📝 Documentação completa de todos os endpoints
- 🔍 Filtros por tags (grupos de endpoints)
- 🧪 **Try it out** - Teste direto na interface
- 📋 Visualização de schemas de request/response
- 🔐 Documentação de códigos de status HTTP

---

## 🚀 Como Testar

### **1. Inicie a aplicação:**
```bash
mvn spring-boot:run
```

### **2. Acesse o Swagger UI:**
```
http://localhost:8080/swagger-ui.html
```

### **3. Teste um endpoint:**

1. Escolha um controller (ex: "Autenticação")
2. Selecione um endpoint (ex: POST /api/login/autenticar)
3. Clique em **"Try it out"**
4. Preencha os parâmetros necessários
5. Clique em **"Execute"**
6. Veja a resposta em tempo real

---

## 📦 Dependências Adicionadas

```xml
<!-- Swagger 2.9.2 (compatível com Spring Boot 2.1.x) -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
<!-- Dependências para resolver conflitos -->
<dependency>
    <groupId>org.springframework.plugin</groupId>
    <artifactId>spring-plugin-core</artifactId>
    <version>1.2.0.RELEASE</version>
</dependency>
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>20.0</version>
</dependency>
```

---

## ⚙️ Configuração

A configuração do Swagger está em:
```
src/main/java/br/com/esc/backend/config/SwaggerConfiguration.java
```

**Principais configurações:**
- **Base Package:** `br.com.esc.backend.controller`
- **API Info:** Título, descrição, versão, contato
- **Tags:** Agrupamento lógico dos endpoints

---

## 📋 Exemplo de Uso

### **Endpoint: POST /api/login/autenticar**

**Request Body:**
```json
{
  "usuario": "admin",
  "senha": "senha123"
}
```

**Response 200 (Success):**
```json
{
  "idLogin": 1,
  "mensagem": "Usuario autenticado com sucesso!",
  "nomeUsuario": "admin",
  "autenticacao": "Bearer -",
  "autorizado": true
}
```

**Response 401 (Unauthorized):**
```json
{
  "idLogin": -1,
  "mensagem": "Usuario e/ou senha invalidos.",
  "autorizado": false
}
```

---

## 🎨 Personalização

Para personalizar a documentação, edite:
```java
// SwaggerConfiguration.java

private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
            .title("Seu Título")
            .description("Sua Descrição")
            .version("1.0.0")
            .contact(new Contact("Nome", "URL", "email@example.com"))
            .build();
}
```

---

## 🔒 Segurança

**Nota:** O Swagger expõe a documentação da API. Em produção:

1. Desabilite o Swagger ou restrinja o acesso
2. Use autenticação para acessar a documentação
3. Configure CORS apropriadamente

**Para desabilitar em produção:**
```properties
# application.properties
springfox.documentation.enabled=false
```

---

## 🐛 Troubleshooting

### **Swagger UI não carrega:**
- Verifique se a aplicação está rodando
- Acesse: `http://localhost:8080/swagger-ui.html` (com .html)
- Verifique logs por erros de inicialização

### **Endpoints não aparecem:**
- Verifique se o package está correto: `br.com.esc.backend.controller`
- Confirme que os controllers têm `@RestController`
- Rebuild o projeto: `mvn clean install`

### **Erros ao testar endpoints:**
- Verifique se o servidor está respondendo
- Confirme os parâmetros obrigatórios
- Verifique logs de erro da aplicação

---

## ✅ Checklist de Implementação

- ✅ Dependências Swagger adicionadas no pom.xml
- ✅ SwaggerConfiguration criada
- ✅ Todos os controllers documentados com @Api
- ✅ Endpoints documentados com @ApiOperation
- ✅ Parâmetros documentados com @ApiParam
- ✅ Respostas documentadas com @ApiResponses
- ✅ Swagger UI acessível e funcional

---

## 📚 Documentação Adicional

- **Springfox:** https://springfox.github.io/springfox/
- **OpenAPI Specification:** https://swagger.io/specification/
- **Swagger UI:** https://swagger.io/tools/swagger-ui/

---

## 🎉 Status

**✅ SWAGGER TOTALMENTE IMPLEMENTADO E FUNCIONAL!**

Acesse agora: http://localhost:8080/swagger-ui.html

