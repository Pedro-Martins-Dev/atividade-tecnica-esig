# Atividade Técnica ESIG - Java

## Estrutura do Projeto

O projeto segue uma arquitetura MVC (Model-View-Controller) organizada da seguinte forma:

### `src/main/java`
Pacote principal contendo o código fonte:

| Pacote       | Descrição |
|--------------|-----------|
| `bean`       | Classes controladoras que integram front-end e back-end |
| `converter`  | Classes para conversão de dados (ex: para exibição) |
| `dao`        | Data Access Objects - responsáveis pela comunicação com o banco de dados |
| `exception`  | Classes personalizadas para tratamento de erros |
| `model`      | Entidades que representam as tabelas do banco |
| `util`       | Classes utilitárias e mensagens do sistema |

### `src/main/resources`
- `persistence.xml`: Arquivo de configuração JPA para conexão com banco de dados

### `src/main/webapp`
Front-end e configurações web:

| Diretório   | Conteúdo |
|-------------|----------|
| `WEB-INF`   | web.xml e configurações |
| `pages`     | Telas do sistema (JSF/XHTML) |
| `resources` | CSS, JS e assets |

## Pré-requisitos

- Java 8
- Payara Server 5.2022.1
- PostgreSQL
- Maven

## Configuração

1. Criar banco de dados PostgreSQL:
   ```sql
   CREATE DATABASE crud_esig;