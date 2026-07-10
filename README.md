# CRUD Relacional - UFS :bar_chart:

Aplicação em Java (JavaFX + Hibernate/JPA) para gerenciamento acadêmico de uma universidade, com persistência em um banco de dados relacional PostgreSQL hospedado na AWS RDS. Projeto desenvolvido para a disciplina de Engenharia de Dados.

## Funcionalidades 🚀

Baseado no Banco de Dados relacional disponibilizado pelo professor, criamos as funcionalidades CRUD, respeitando todas as relações e definições presente no banco de dados original.

- **Gestão de Usuários**: cadastro, consulta, atualização e remoção de usuários (CPF, nome, e-mail, telefone, login, senha, data de nascimento).
- **Gestão de Estudantes**: vínculo de usuários a matrículas estudantis, com ano de ingresso e coeficiente (MC).
- **Gestão de Cursos**: cadastro de cursos com grau, turno, campus e nível.
- **Gestão de Vínculos**: associação entre estudantes e cursos, com status e datas de entrada/saída.

## Tecnologias :computer:

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| JavaFX | 21.0.2 |
| Hibernate ORM | 6.5.2.Final |
| PostgreSQL Driver | 42.7.3 |
| Maven | - |
| Banco de dados | PostgreSQL (AWS RDS) |

## Arquitetura 🗂️

O projeto segue uma separação em camadas:

```
src/main/java/
├── main/            → Ponto de entrada da aplicação (Main.java)
├── view/            → Inicialização da janela JavaFX (SistemaAcademico.java)
├── controller/       → Controladores JavaFX (um por tela: Usuarios, Estudantes, Cursos, Vinculos, Main)
├── model/            → Entidades JPA (Usuario, Estudante, Curso, Vinculo)
│   ├── enums/         → Enums que espelham os tipos ENUM do PostgreSQL (TipoGrau, TipoTurno, TipoNivel, StatusEstudante)
│   └── converters/    → Conversores JPA (@Converter) entre os enums Java e o texto salvo no banco
├── dao/              → Camada de acesso a dados (BaseDAO + DAOs específicos por entidade)
└── conexao/          → JPAUtil: fábrica única e compartilhada de conexão com o banco (EntityManagerFactory)

src/main/resources/
├── GUI/              → Telas FXML (Janela, Usuarios, Estudantes, Cursos, Vinculos)
├── Imagens/          → Recursos visuais (logo da UFS)
└── META-INF/         → persistence.xml (configuração da unidade de persistência JPA)
```

### Camada de conexão (`conexao.JPAUtil`)

A `EntityManagerFactory` é criada **uma única vez** e compartilhada entre todos os controllers, evitando o custo de recriá-la (leitura do `persistence.xml`, validação do mapeamento de entidades) a cada troca de tela. Ela é fechada automaticamente quando a aplicação é encerrada (`SistemaAcademico.stop()`).

### Tipos enumerados (`model.enums` / `model.converters`)

Algumas colunas do banco (`grau`, `turno`, `nivel` em `Curso`; `status` em `Vinculo`) são tipos `ENUM` nativos do PostgreSQL. Essas colunas são mapeadas para enums Java correspondentes através de conversores JPA (`AttributeConverter`), garantindo que a aplicação só permita valores válidos (via `ComboBox` na interface) e evitando erros de tipo na hora de gravar no banco.

## Configuração do banco de dados ⚙️

A conexão é feita via variáveis de ambiente, para não expor credenciais no código-fonte. O `persistence.xml` não contém `url`/`user`/`password`; esses valores são passados em tempo de execução (veja `JPAUtil.java` e `main.Main`).

Variáveis esperadas:

| Variável | Descrição | Exemplo |
|---|---|---|
| `DB_URL` | URL JDBC do banco | `jdbc:postgresql://<endpoint-rds>:5432/<database>?stringtype=unspecified` |
| `DB_USER` | Usuário do banco | `postgres` |
| `DB_PASSWORD` | Senha do banco | - |

> ⚠️ O parâmetro `?stringtype=unspecified` na URL é necessário porque o banco usa tipos `ENUM`/domínio customizados do PostgreSQL — sem ele, o driver JDBC envia os parâmetros como `varchar` e o PostgreSQL recusa o cast implícito.

### Rodando pelo VS Code 🔄

As configurações de execução ficam em `.vscode/launch.json`. Rode especificamente pela aba **"Run and Debug"**, selecionando a configuração com as variáveis de ambiente configuradas (não use o botão "Run" acima do `main()`, pois ele ignora o `env` do `launch.json`).

Exemplo de configuração:
```json
{
    "type": "java",
    "name": "Rodar Projeto AWS",
    "request": "launch",
    "mainClass": "main.Main",
    "projectName": "CRUD-RELACIONAL-ENGDADOS",
    "env": {
        "DB_URL": "jdbc:postgresql://<endpoint>:5432/<database>?stringtype=unspecified",
        "DB_USER": "postgres",
        "DB_PASSWORD": "<senha>"
    }
}
```

> ⚠️ Nunca suba o `launch.json` com credenciais reais para um repositório público. Adicione-o ao `.gitignore` ou use um arquivo de exemplo (`launch.example.json`) sem os valores sensíveis.

## Como executar 💡

1. Configure as variáveis de ambiente `DB_URL`, `DB_USER` e `DB_PASSWORD` (ou o `launch.json`, se estiver usando VS Code).
2. Garanta que o banco PostgreSQL já tenha o schema `universidade` criado (tabelas, domínios e tipos `ENUM`).
3. Compile e rode o projeto com Maven:
   ```bash
   mvn clean compile
   mvn exec:java -Dexec.mainClass="main.Main"
   ```
   ou execute a classe `main.Main` diretamente pela sua IDE.

## Estrutura do banco de dados 🛠️

O schema `universidade` contém, entre outras, as tabelas `Usuario`, `Estudante`, `Curso` e `Vinculo`, além dos seguintes domínios e tipos customizados relevantes:

- `tipo_grau`: `Bacharelado`, `Licenciatura Plena`
- `tipo_turno`: `Matutino`, `Vespertino`, `Noturno`, `Turno Indefinido`
- `tipo_nivel`: `Graduação`, `Mestrado`, `Doutorado`, `Lato`
- `status_estudante`: `Ativo`, `Cancelada`, `Formando`, `Graduado`

## Autores :busts_in_silhouette:

- Bernardo Abrahão Nóbrega;
- João Pedro Costa Cruz;
- Lucas Antônio Araújo Santos;

