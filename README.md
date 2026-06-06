# CadastroMonitores

Sistema desktop em Java para gerenciamento de monitoria acadêmica, com interface gráfica em Swing, autenticação de usuários, cadastro de alunos e coordenadores, criação e gestão de editais, inscrições em disciplinas, cálculo de resultado e geração de relatórios. O projeto usa Maven, Java 17, JPA/Hibernate, além de dependências para MongoDB, MySQL, H2, PDF, e-mail.

## Visão geral

O sistema organiza o fluxo de monitoria em torno de três perfis principais: coordenador, aluno e usuário autenticado. A sessão do usuário é centralizada por `SessaoUsuario`, que identifica o perfil logado e permite alternar a navegação conforme o tipo de acesso.

A base de persistência principal é gerenciada por `GerenciadorDeDados`, que cria um `EntityManagerFactory` com a unidade `monitoriaPU` e executa operações de salvar, atualizar, remover e consultar entidades por meio de DAOs. O repositório também inclui uma classe `PersistenciaNoSql` com conexão explícita ao MongoDB.

## Funcionalidades

### Autenticação e acesso
- Login com validação de e-mail e senha.
- Redirecionamento automático para a tela principal após autenticação válida.
- Criação do primeiro coordenador quando ainda não existe administrador cadastrado.
- Saída do sistema com limpeza da sessão.
- Recuperação de senha por código de verificação.

### Cadastro de usuários
- Cadastro de aluno com nome, matrícula, e-mail, senha e gênero.
- Validação de e-mail, confirmação de senha e verificação de duplicidade de e-mail e matrícula.
- Cadastro do coordenador com nome, e-mail, senha e confirmação de senha.

### Gestão de editais
- Cadastro de edital de monitoria.
- Listagem de editais com exibição de número, período, status e situação do resultado.
- Abertura, fechamento e reabertura de edital com regras de permissão e prazo.
- Encerramento do período de desistência, transformando o resultado em final.

### Inscrições e participação
- Inscrição do aluno em edital por disciplina, com vínculo entre aluno, disciplina, edital, CRE, nota, tipo de vaga, ordem de preferência e preferência de vaga.
- Registro de desistência e recálculo do resultado quando necessário.
- Visualização da lista de inscritos por edital.
- Envio de e-mail para alunos inscritos diretamente pela tela de inscritos.

### Perfil do aluno
- Tela de perfil com edição de dados cadastrais. 
- Exibição de histórico de monitorias. 

### Resultado e relatórios
- Cálculo de resultado do edital através de serviço dedicado. 
- Exportação do resultado completo para PDF. 
- Resultado final travado após o fechamento do período de desistência. 

## Tecnologias utilizadas

- Java 17. 
- Maven. 
- Swing para interface gráfica. 
- JPA/Hibernate para persistência relacional. 
- MongoDB para a camada NoSQL presente no projeto. 
- Geração de PDF com iText 5. 
- Envio de e-mails com Jakarta Mail. 
- Integração com Google Drive. 

## Estrutura do projeto

O código está organizado principalmente em:
- `src/main/java/br/com/monitoria/model`: entidades de domínio como `Usuario`, `Aluno`, `Coordenador`, `Disciplina`, `EditalDeMonitoria` e `Inscricao`. 
- `src/main/java/br/com/monitoria`: telas, serviços, sessão, relatórios, mensagens e gerenciamento de dados. 
- `src/interfaces/swing`: componentes e documentação da interface Swing. 

## Como executar

1. Compile o projeto com Maven. O `pom.xml` define o `mainClass` como `br.com.monitoria.Main`. 
2. Se for usar a parte NoSQL, suba o MongoDB com o `docker-compose.yml`, que cria um container `mongo:7.0` com porta `27017`. 
3. Configure a unidade de persistência `monitoriaPU` conforme a camada JPA do projeto. 
4. Execute a classe principal do sistema. 

## Observações técnicas

O projeto mostra uma evolução arquitetural interessante: a interface gráfica foi refatorada para concentrar dependências compartilhadas em `TelaBase`, com uso de herança e injeção por construtor nas telas filhas. Isso melhora o encapsulamento e reduz repetição. 

As entidades de domínio também seguem uma modelagem rica: `Usuario` é a classe base, `Aluno` adiciona matrícula e gênero, `Coordenador` representa o perfil administrativo, `Disciplina` modela vagas remuneradas/voluntárias, `Inscricao` guarda CRE, nota, preferência, desistência e pontuação final, e `EditalDeMonitoria` concentra a lógica de abertura, fechamento, inscrição e cálculo de resultado.