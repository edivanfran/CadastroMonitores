import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {
   public static void main(String[] args) {
       // Toma o diretório onde o usuário executa o programa e adiciona todos os arquivos .xml deste em uma lista
       File[] pasta = new File(System.getProperty("user.dir")).listFiles(); /* "user.dir" → diretório onde o programa é executado */
       ArrayList<File> arquivos = new ArrayList<>();
       for (File arquivo : pasta) {
           if (arquivo.getName().endsWith(".xml")) {
               arquivos.add(arquivo);
           }
       }

       Scanner sc = new Scanner(System.in); // Inicializa leitor de entrada do usuário

       // Saúda o usuário corretamente dependendo do período do dia em que ele executa o programa
       if (LocalTime.now().getHour() >= 18) {
           System.out.println("Boa noite.");
       } else if (LocalTime.now().getHour() >= 12) {
           System.out.println("Boa tarde.");
       } else if (LocalTime.now().getHour() >= 5) {
           System.out.println("Bom dia.");
       } else {
           System.out.println("Boa madrugada.");
       }

       // Tenta recuperar as centrais diretamente dos arquivos, caso encontradas, ou cria uma nova caso não
       Persistencia persistencia = new Persistencia();
       CentralDeInformacoes central;
       String nomeArquivo;
       if (arquivos.isEmpty()) {
           System.out.println("Não foi encontrado nenhum arquivo de Central de Informações de Alunos.\nCriando nova Central...");
           central = new CentralDeInformacoes();
           System.out.print("Forneça um nome para a Central › ");
           nomeArquivo = sc.nextLine().strip().toUpperCase();
       } else {
           System.out.println("Foram encontradas as seguintes Centrais de Informações de Alunos:\n-------------------------------");
           for (File arquivo : arquivos) {
               System.out.println("  \"" + arquivo.getName() + "\"");
           }
           System.out.println("-------------------------------");
           System.out.print("Digite o nome do arquivo da Central que deseja recuperar, ou forneça um nome para criar uma nova Central\n» ");
           nomeArquivo = sc.nextLine().strip();
           central = persistencia.recuperarCentral(nomeArquivo);
       }

       // Cria coordenador só se ainda não existir
       Usuario secaoAtual;
           /*TODO|
              futuramente, servirá para o programa saber se a seção
              atual é de um aluno — e portanto, só deve mostrar a tela do aluno —,
              ou se é de um coordenador — e portanto, deve mostrar a tela do coordenador;
              só terá utilidade quando houver a divisão das atribuições
            */
       if (!central.temCoordenador()) {
           System.out.println("\nNenhum coordenador cadastrado ainda!");
           System.out.println("Vamos realizar primeiro o cadastro de Coordenador:");
           System.out.print("  E-mail › ");
           String emailCoord = sc.nextLine(); //TODO| adicionar tratamento para o caso de e-mail inválido
           System.out.print("  Senha › "); //TODO| adicionar mecanismo simples de confirmação de senha
           String senhaCoord = sc.nextLine(); //TODO| adicionar tratamento para o caso de senha vazia
           central.cadastrarCoordenador(emailCoord, senhaCoord);
           persistencia.salvarCentral(central, nomeArquivo);
           System.out.println("Coordenador cadastrado com sucesso!\n");
           secaoAtual = Usuario.COORDENADOR;
       } else { /* se o coordenador acabou de se cadastrar, não há necessidade de pedir login */
           do {
               System.out.println("Logando...");
               System.out.print("  Digite seu e-mail\n  » ");
               String entrada_email = sc.nextLine();
               System.out.print("  Digite sua senha | Esqueceu a senha? Deixe vazio e tecle ENTER\n  » ");
               String entrada_senha = sc.nextLine();
               // Recuperação de senha
               if (entrada_senha.isEmpty()) {
                   System.out.println("Só um momento estamos enviando o email...");
                   String codigo_recuperacao = ((Integer)(int)(Math.random() * 100000)).toString(); /* número aleatório de 00000 a 99999 */

                   Mensageiro.enviarEmail(entrada_email, "Recuperação da conta no Cadastro", "O código de recuperação da sua conta é: " + codigo_recuperacao);
                   System.out.println("  Um e-mail será enviado em alguns instantes contendo um código de recuperação.");
                   System.out.println("  Informe o código recebido › ");
                   String entrada_codigo = sc.nextLine();
                   if (entrada_codigo.equals(codigo_recuperacao)) {
                       System.out.println("  Código validado.");
                       do {
                           System.out.println("  Informe uma nova senha › ");
                           entrada_senha = sc.nextLine();
                           System.out.print("  Confirme a senha › ");
                           String confirmacao = sc.nextLine();
                           if (entrada_senha.equals(confirmacao)) {
                               Aluno aluno = central.retornarAlunoPeloEmail(entrada_email);
                               if (aluno == null) {
                                   System.out.println("Você não está cadastrado no sistema.");
                                   break;
                               }
                               aluno.setSenha(entrada_senha);
                               System.out.println("Sua senha foi atualizada.");
                               break;
                           } else {
                               System.out.println("  [Erro] As senhas não coincidem; tente novamente.");
                           }
                       } while (true);
                   } else {
                       System.out.println("  Código inválido; acesso negado — você deverá repetir o processo de login");
                       continue;
                   }
               }
               // Verifica se é o coordenador
               if (central.getCoordenador() != null &&
                       central.getCoordenador().autenticar(entrada_email, entrada_senha)) {
                   System.out.println("Logou como Coordenador.");
                   System.out.println("Boas-vindas!");
                   secaoAtual = Usuario.COORDENADOR;
                   break;
               } // Verifica se é aluno
               if (central.isLoginPermitido(entrada_email, entrada_senha)) {
                   System.out.println("Logou como Aluno.");
                   central.darBoasVindasUsuario(entrada_email, entrada_senha);
                   secaoAtual = Usuario.ALUNO;
                   break;
               } else {
                   System.out.println("Acesso negado — você deverá repetir o processo de login");
               }
           } while (true);
       }

       String menu = """
           \nA seguir, escolha uma opção:
           -------------------------------
             1 - Novo aluno
             2 - Listar todos os alunos
             3 - Exibir info. de um aluno específico
             4 - Novo edital
             5 - Informar quantidade de editais cadastrados e ID
             6 - Detalhar um edital específico
             7 - Inscrever aluno em vaga de algum edital
             8 - Gerar comprov. das inscrições de um aluno em um edital
             S - Sair
           -------------------------------
           »\s""";

       // Rotina principal do programa
       String input;
       System.out.print(menu);
       do {
           input = sc.nextLine();
           switch (input.strip()) {
               case "1" -> { /* 1 - Novo aluno */
                   System.out.println("Cadastrando novo aluno...\n-------------------------------");
                   String nome;
                   do {
                       System.out.print("  Nome completo › ");
                       nome = sc.nextLine().strip();
                       if (!nome.matches("^[A-ZÀ-Ÿ][a-zà-ÿ]+(?: (?:[dD]e|[dD]a|[dD]os|[dD]as|[eE])? ?[A-ZÀ-Ÿ]?[a-zà-ÿ]+)+$")) {
                           System.out.println("  [Erro] Nome inválido; tente novamente.");
                           /* nome precisa ser real; depois, nome é passado para caixa baixa */
                       } else {
                           nome = nome.toLowerCase();
                           break;
                       }
                   } while (true);
                   String matricula; // Recebe matrícula e verifica se é válida
                   do {
                       System.out.print("  Matrícula › ");
                       matricula = sc.nextLine().strip();
                       if (!matricula.matches("^\\d+$")) /* ← matrícula deve ser numérica */ {
                           System.out.println("  [Erro] Matrícula inválida; tente novamente.");
                       } else break;
                   } while (true);
                   String email; // Recebe e-mail e verifica se é válido
                   do {
                       System.out.print("  E-mail › ");
                       email = sc.nextLine().strip();
                       if (!email.matches("(?i)^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")) {
                           System.out.println("  [Erro] E-mail inválido; tente novamente.");
                           /* e-mail precisa ser real, e deve possuir apenas minúsculas; depois, e-mail é passado para caixa baixa */
                       } else {
                           email = email.toLowerCase();
                           break;
                       }
                   } while (true);
                   String senha; // Recebe a senha e obriga o usuário a repetí-la para validá-la
                   while (true) {
                       System.out.print("  Senha › ");
                       senha = sc.nextLine();
                       System.out.print("  Confirme a senha › ");
                       String confirmacao = sc.nextLine();
                       if (senha.equals(confirmacao)) break;
                       else {
                           System.out.println("  [Erro] As senhas não coincidem; tente novamente.");}
                   }
                   Sexo genero = null;
                   while (genero == null) {
                       System.out.print("  Gênero (M/F/NB) › ");
                       String entrada = sc.nextLine().strip().toUpperCase();
                       switch (entrada) {
                           case "M" -> genero = Sexo.MASCULINO;
                           case "F" -> genero = Sexo.FEMININO;
                           case "NB" -> genero = Sexo.NAO_BINARIO;
                           default -> System.out.println("  [Erro] Opção inválida. Digite 'M', 'F' ou 'NB'.");
                       }
                   }
                   Aluno novo = new Aluno(nome, matricula, senha, email, genero); // Cria o aluno, e verifica se já existe algum conflitante
                   boolean flag = central.adicionarAluno(novo);
                   if (!flag) {
                       System.out.println("  [Erro] Já existe um aluno com essa matrícula.\n-------------------------------");
                   } else {
                       persistencia.salvarCentral(central, nomeArquivo);
                       System.out.println("-------------------------------");
                       System.out.println("Aluno cadastrado com sucesso.");
                   }
                   System.out.println(menu);
               }

               case "2" -> { /* 2 - Listar todos os alunos */
                   ArrayList<Aluno> lista = central.getTodosOsAlunos();
                   if (lista.isEmpty()) {
                       System.out.println("Nenhum aluno cadastrado ainda.");
                   } else {
                       System.out.println("Lista de alunos cadastrados:");
                       System.out.println("-------------------------------");
                       for (Aluno a : lista) {
                           System.out.println("Nome: " + a.getNome());
                           System.out.println("Matrícula: " + a.getMatricula());
                           System.out.println("Gênero: " + (a.getGenero() == Sexo.NAO_BINARIO? "NÃO BINÁRIO": String.valueOf(a.getGenero())));
                       }
                       System.out.println("-------------------------------");
                       System.out.println("Total de alunos: " + lista.size());
                   }
                   System.out.println(menu);
               }

               case "3" -> { /* 3 - Exibir info. de um aluno específico */
                   System.out.print("Digite a matrícula do aluno › ");
                   String matricula = sc.nextLine().strip();
                   Aluno alunoEncontrado = central.recuperarAluno(matricula);
                   if (alunoEncontrado == null) {
                       System.out.println("[Erro] Nenhum aluno encontrado com essa matrícula.");
                   } else {
                       System.out.println("-------------------------------");
                       System.out.println("  Nome: " + alunoEncontrado.getNome());
                       System.out.println("  Matrícula: " + alunoEncontrado.getMatricula());
                       System.out.println("  Email: " + alunoEncontrado.getEmail());
                       System.out.println("  Gênero: " + (alunoEncontrado.getGenero() == Sexo.NAO_BINARIO? "NÃO BINÁRIO": String.valueOf(alunoEncontrado.getGenero())));
                       System.out.println("-------------------------------");
                   }
                   System.out.println(menu);
               }

               case "4" -> { /* 4 - Novo edital */
                   System.out.println("Cadastrando novo edital...\n-------------------------------");
                   String numeroEdital;
                   do {
                       System.out.print("  Número do edital › ");
                       numeroEdital = sc.nextLine().strip();
                       if (!numeroEdital.matches("^\\d+$")) /* ← garante que seja numérico */ {
                           System.out.println("  [Erro] Nome inválido; tente novamente.");
                       } else break;
                   } while (true);
                   LocalDate dataInicio;
                   do { // Recebe a data em formato DD/MM/AAAA e, após validada, converte-a para AAAA-MM-DD (→ LocalDate)
                       System.out.print("  Data de início (DD/MM/AAAA) › ");
                       String temp = sc.nextLine().strip();
                       if (!temp.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$")) /* ← valida a data */{
                           System.out.println("  [Erro] Data inválida; tente novamente.");
                       } else {
                           String[] dataDesconstruida = temp.split("/");
                           dataInicio = LocalDate.parse(dataDesconstruida[2] + "-" + dataDesconstruida[1] + "-" + dataDesconstruida[0]);
                           break;
                       }
                   } while (true);
                   LocalDate dataLimite;
                   do { // Mesmo procedimento para data limite.
                       System.out.print("  Data de limite (DD/MM/AAAA) › ");
                       String temp = sc.nextLine().strip();
                       if (!temp.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$")) {
                           System.out.println("  [Erro] Data inválida; tente novamente.");
                       } else {
                           String[] dataDesconstruida = temp.split("/");
                           dataLimite = LocalDate.parse(dataDesconstruida[2] + "-" + dataDesconstruida[1] + "-" + dataDesconstruida[0]);
                           break;
                       }
                   } while (true);
                   EditalDeMonitoria novoEdital = new EditalDeMonitoria(System.currentTimeMillis(), numeroEdital, dataInicio, dataLimite);
                   boolean flag = false;
                   do { // Adiciona as disciplinas no edital
                       System.out.println("  Adicionando Disciplina ao edital...\n  -----------------------------");
                       System.out.println("    Nome da Disciplina › ");
                       String nomeDisciplina = sc.nextLine();
                       String qtdeVagasVoluntarias = "";
                       String qtdeVagasRemuneradas = "";
                       // TODO| Remover a verificação isdigit de vaga e colocá-lo como integer.
                       do {
                           System.out.println("    Quantidade de vagas remuneradas › ");
                           qtdeVagasRemuneradas = sc.nextLine().strip();
                           if (!qtdeVagasRemuneradas.matches("^\\d+$")) {
                               System.out.println("  [Erro] Valor inválido; tente novamente.");
                           } else break;
                           System.out.println("    Quantidade de vagas voluntarias › ");
                           qtdeVagasVoluntarias = sc.nextLine().strip();
                           if (!qtdeVagasVoluntarias.matches("^\\d+$")) {
                               System.out.println("  [Erro] Valor inválido; tente novamente.");
                           } else break;
                       } while (true);
                       novoEdital.adicionarDisciplina(new Disciplina(nomeDisciplina, Integer.parseInt(qtdeVagasRemuneradas), Integer.parseInt(qtdeVagasVoluntarias)));
                       System.out.print("  -----------------------------\n  Disciplina adicionada. Deseja adicionar outra? (S/N)\n» ");
                       do {
                           String resposta = sc.nextLine();
                           if (resposta.equalsIgnoreCase("S")) {
                               flag = true;
                               break;
                           } else if (resposta.equalsIgnoreCase("N")) {
                               break;
                           }
                       } while (true);
                   } while (flag);
                   boolean ok = central.adicionarEdital(novoEdital);
                   System.out.println("-------------------------------");
                   if (ok) {
                       persistencia.salvarCentral(central, nomeArquivo);
                       System.out.println("Edital cadastrado com sucesso; ID do edital: " + novoEdital.getId());
                   } else {
                       System.out.println("[Erro] Já existe um edital com esse ID.");
                   }
                   System.out.println(menu);
               }

               case "5" -> { /* 5 - Informar quantidade de editais cadastrados e ID */
                   ArrayList<EditalDeMonitoria> lista = central.getTodosOsEditais();
                   if (lista.isEmpty()) {
                       System.out.println("Nenhum edital cadastrado ainda.");
                   } else {
                       System.out.println("Quantidade de editais cadastrados: " + lista.size());
                       System.out.println(menu);
                   }
                   central.mostrarIdEditais();
               }

               case "6" -> { /* 6 - Detalhar um edital específico */
                   long id;
                   do {
                       System.out.print("Digite o ID do edital › ");
                       String temp = sc.nextLine().strip();
                       if (!temp.matches("^\\d+$")) {
                           System.out.println("[Erro] ID inválido; tente novamente.");
                       } else {
                           id = Long.parseLong(temp);
                           break;
                       }
                   } while (true);
                   EditalDeMonitoria edital = central.recuperarEdital(id);
                   if (edital == null) {
                       System.out.println("[Erro] Nenhum edital encontrado com esse ID.");
                   } else {
                       System.out.println("-------------------------------");
                       System.out.println(edital);
                       System.out.println("-------------------------------");
                   }
                   System.out.println(menu);
               }

               case "7" -> { /* 7 - Inscrever aluno em vaga de algum edital */
                   System.out.print("Digite a matrícula do aluno › ");
                   String matricula = sc.nextLine().strip();
                   Aluno aluno = central.recuperarAluno(matricula);
                   if (aluno == null) {
                       System.out.println("[Erro] Nenhum aluno encontrado com essa matrícula.");
                       System.out.println(menu);
                       break;
                   }
                   long id;
                   do {
                       System.out.print("Digite o ID do edital › ");
                       String temp = sc.nextLine().strip();
                       if (!temp.matches("^\\d+$")) {
                           System.out.println("[Erro] ID inválido; tente novamente.");
                       } else {
                           id = Long.parseLong(temp);
                           break;
                       }
                   } while (true);
                   EditalDeMonitoria edital = central.recuperarEdital(id);
                   if (edital == null) {
                       System.out.println("[Erro] Nenhum edital encontrado com esse ID.");
                   } else if (edital.jaAcabou()) {
                       System.out.println("[Erro] Este edital já foi encerrado, não é possível inscrever alunos.");
                       System.out.println(menu);
                       break;
                   }
                   System.out.print("Digite o nome da disciplina › ");
                   String nomeDisciplina = sc.nextLine().strip();
                   boolean inscrito = edital.inscreverAluno(aluno, nomeDisciplina);
                   // TODO colocar um anexo do comprovante de inscrição no email
                   if (inscrito) {
                       persistencia.salvarCentral(central, nomeArquivo);
                       Mensageiro.enviarEmail(aluno.getEmail(), "Confirmação de inscrição",
                               String.format("""
                               Sua inscrição no Edital de Monitoria n.º %s foi efetuada.
                               """, edital.getNumero()));
                   } else {
                       System.out.println("[Erro] Não foi possível inscrever o aluno.");
                   }
                   System.out.println(menu);
               }

               case "8" -> { /* 8 - Gerar comprov. das inscrições de um aluno em um edital */
                   System.out.print("Digite a matrícula do aluno › ");
                   String matricula = sc.nextLine().strip();
                   Aluno alunoEncontrado = central.recuperarAluno(matricula);
                   if (alunoEncontrado == null) {
                       System.out.println("[Erro] Nenhum aluno encontrado com essa matrícula.");
                       continue;
                   }
                   long idEdital;
                   try {
                       System.out.println("Digite o ID do edital › ");
                       idEdital = sc.nextLong();
                   } catch (InputMismatchException e) {
                       System.out.println("[Erro] Este não é um ID válido.");
                       continue;
                   }
                   for (EditalDeMonitoria edital : central.getTodosOsEditais())
                       if (edital.getId() == idEdital) {
                           GeradorDeRelatorios.obterComprovanteDeInscricoesAluno(matricula, idEdital, central);
                       }
                   System.out.println(menu);
               }

               case "S", "s" /* S - Sair */ -> System.out.println("Programa encerrado.");
               default -> System.out.println("[Erro] Opção inválida.");
           }
       } while (!input.strip().equalsIgnoreCase("S"));
   }
}
