# ⚜ Smart RH - Sistema Backend de Gestão de Pessoas



<div align="center">
   <img src="https://i.imgur.com/w8tTOuT.png" title="source: imgur.com" />
</div>








<div align="center">
   <img src="https://ik.imagekit.io/f9incgeso/smartrh_logo.png?updatedAt=1761189128406" title="Logo Smart RH" width="30%" />
</div>


## 1. Descrição

O **Smart RH** é um projeto de *backend* desenvolvido em Java com Spring Boot. O objetivo principal foi aplicar o Mapeamento Objeto-Relacional (ORM) do JPA/Hibernate para criar um modelo de domínio robusto, simulando um sistema de Recursos Humanos (RH) com **3 entidades interligadas** por relacionamentos $1:N$ e $1:1$.

O sistema implementa o **CRUD (Create, Read, Update)** de forma restrita (sem o método `DELETE` para os recursos de Colaborador e Usuário), e inclui uma **Funcionalidade Especial** de valor agregado.

------

## 2. Sobre esta API

A API foi construída seguindo o estilo arquitetural **RESTful**, expondo *endpoints* para o gerenciamento de `Departamentos`, `Colaboradores` e `Usuarios`.

### 2.1. Principais Funcionalidades

1.  **Gestão de Estrutura:** CRUD completo (incluindo DELETE) para a entidade **`Departamento`** e métodos de busca por descrição.
2.  **Gestão de Colaboradores/Usuários (Restrito):** Implementação de **C**, **R**, **U** para as entidades **`Colaborador`** e **`Usuario`**, **excluindo a operação de `DELETE`** para cumprir o requisito do Desafio 2.
3.  **Relacionamentos e Modelagem:** Mapeamento de relacionamentos $1:N$ e $1:1$ (para separar dados de RH e dados de Login), garantindo a integridade referencial com validação na camada Service.
4.  **Consulta Avançada:** Métodos de busca específica por atributos relacionados (ex: buscar Colaborador por nome, Departamento por descrição, Usuário por e-mail de login).
5.  **✨ Funcionalidade Especial (Cálculo de Salário):** Implementação de um método dedicado (`GET /colaboradores/{id}/salario-liquido`) que simula o **cálculo do Salário Líquido** do colaborador, considerando salário-base, número de dependentes e descontos simulados (INSS/IRPF).

------

## 3. Diagrama de Classes

 estrutura de classes separa as responsabilidades entre dados de RH (`Colaborador`) e dados de acesso (`Usuario`), com os seguintes relacionamentos: 

* **Departamento** se relaciona com **Colaborador** em um modelo $1:N$ (Um para Muitos). 
* **Colaborador** possui a referência da conta **Usuario** em um modelo $1:1$, focando na separação das preocupações (Dados de RH vs. Dados de Acesso).

<div align="center">
    <img src="https://ik.imagekit.io/f9incgeso/Driagrama%20Classes.png?updatedAt=1761228007998" title="Diagrama de Classes" width="70%"/>
</div>

------

## 4. Diagrama Entidade-Relacionamento (DER)

O DER reflete as chaves estrangeiras (`FK`) que interligam as três tabelas principais (`tb_departamentos`, `tb_colaboradores`, `tb_usuarios`).

<div align="center">
    <img src="https://ik.imagekit.io/f9incgeso/DER.png?updatedAt=1761222589590" title="Diagrama Entidade-Relacionamento" />
</div>

------

## 5. Tecnologias utilizadas

| Item                          | Descrição                                                    |
| :---------------------------- | :----------------------------------------------------------- |
| **Servidor**                  | **Tomcat**                             |
| **Linguagem de programação**  | **Java** (versão 17+)                                        |
| **Framework**                 | **Spring Boot**                                              |
| **ORM**                       | **JPA / Hibernate**                              |
| **Banco de dados Relacional** | **MySQL**                                                    |
| **Testes de API**             | **Insomnia** (Testes de todos os endpoints CRUD e o método especial) |
| **Dependências Chave**        | `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `mysql-connector-j`, `spring-boot-starter-validation` |

------

## 6. Configuração e Execução

O projeto é baseado em Maven e utiliza o Spring Boot.

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/TechBloom-GrupoE/Smart-Rh-Backend.git
    ```

2.  **Configurar o Banco de Dados (MySQL):**
    * Crie um banco de dados vazio chamado `smart_rh_db` no seu MySQL.
    * Edite o arquivo `src/main/resources/application.properties` com as suas credenciais de conexão:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/smarth_rh_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
        spring.datasource.username=seu_usuario_mysql
        spring.datasource.password=sua_senha_mysql
        # O JPA criará as tabelas e relacionamentos automaticamente:
        spring.jpa.hibernate.ddl-auto=update
        ```

3.  **Execute a aplicação:**
    * **Via Maven:**
        ```bash
        ./mvnw spring-boot:run
        ```
    * **Via IDE:** Execute a classe principal do Spring Boot (`SmartRhApplication.java`).

A API estará rodando em **`http://localhost:8080`**.
Use o **Insomnia** ou Postman para testar os *endpoints*.

| Endpoint | Descrição | Exemplo de Teste |
| :--- | :--- | :--- |
| **CRUD Completo** | `Departamentos` | `POST http://localhost:8080/departamentos` |
| **Cadastro** | `Usuários` (Login/Acesso) | `POST http://localhost:8080/usuarios/cadastrar` |
| **Busca Específica** | `Colaboradores` | `GET http://localhost:8080/colaboradores/nome/{nome}` |
| **Func. Especial** | Cálculo Salário | `POST http://localhost:8080/colaboradores/calcularsalario/{id}`** |