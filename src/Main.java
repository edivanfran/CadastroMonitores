import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {
   public static void main(String[] args) {
       File[] pasta = new File(System.getProperty("user.dir")).listFiles();
       ArrayList<File> arquivos = new ArrayList<>();

       for (File arquivo : pasta) {
           if (arquivo.getName().endsWith(".xml")) {
               arquivos.add(arquivo);
           }
       }
       Scanner sc = new Scanner(System.in);
       if (LocalTime.now().getHour() >= 18) {
           System.out.println("Boa noite.");
       } else if (LocalTime.now().getHour() >= 12) {
           System.out.println("Boa tarde.");
       } else if (LocalTime.now().getHour() >= 5) {
           System.out.println("Bom dia.");
       } else {
           System.out.println("Boa madrugada.");
       }
       Persistencia persistencia = new Persistencia();
       CentralDeInformacoes central;
       String nomeArquivo;
       if (arquivos.isEmpty()) {
           System.out.println("Não foi encontrado nenhum arquivo de Central de Informações de Alunos.\nCriando nova Central...");
           central = new CentralDeInformacoes();
           System.out.print("Forneça um nome para a Central › ");
           nomeArquivo = sc.nextLine().strip();
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

       //Pensando em colocar em um while, mas o usuário tem que ser cadastro antes.
       System.out.println("Coloque as seguintes informações para acessar o sistema >>");
       System.out.print("Digite o seu email: ");
       String entrada_email = sc.nextLine();
       System.out.print("Digite sua senha: ");
       String entrada_senha = sc.nextLine();

       if (central.isLoginPermitido(entrada_email, entrada_senha)) {
           System.out.println("Login Permitido.");
           central.darBoasVindasUsuario(entrada_email, entrada_senha);
       } else {
           System.out.println("Login não reconhecido.");
       }
       // Seria legal a opção de esqueci a senha.

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
       String input;
       System.out.println(menu);
       do {
           input = sc.nextLine();
           switch (input.strip()) {
               case "1" -> {
                   System.out.println("Cadastrando novo aluno...\n-------------------------------");
                   String nome;
                   do {
                       System.out.print("  Nome completo › ");
                       nome = sc.nextLine().strip();
                       if (!nome.matches("^[A-ZÀ-Ÿ][a-zà-ÿ]+(?: (?:[dD]e|[dD]a|[dD]os|[dD]as|[eE])? ?[A-ZÀ-Ÿ]?[a-zà-ÿ]+)+$")) {
                           System.out.println("  [Erro] Nome inválido; tente novamente.");
                       } else break;
                   } while (true);
                   String matricula;
                   do {
                       System.out.print("  Matrícula › ");
                       matricula = sc.nextLine().strip();
                       if (!matricula.matches("^\\d+$")) {
                           System.out.println("  [Erro] Nome inválido; tente novamente.");
                       } else break;
                   } while (true);
                   String email;
                   do {
                       System.out.print("  E-mail › ");
                       email = sc.nextLine().strip();
                       if (!email.matches("(?i)^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")) {
                           System.out.println("  [Erro] E-mail inválido; tente novamente.");
                       } else break;
                   } while (true);
                   String senha;
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


                   Aluno novo = new Aluno(nome, matricula, senha, email, genero);
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
               case "2" -> {
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
               case "3" -> {
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
               case "4" -> {
                   System.out.println("Cadastrando novo edital...\n-------------------------------");
                   String numeroEdital;
                   do {
                       System.out.print("  Número do edital › ");
                       numeroEdital = sc.nextLine().strip();
                       if (!numeroEdital.matches("^\\d+$")) {
                           System.out.println("  [Erro] Nome inválido; tente novamente.");
                       } else break;
                   } while (true);
                   LocalDate dataInicio;
                   do {
                       System.out.print("  Data de início (DD/MM/AAAA) › ");
                       String temp = sc.nextLine().strip();
                       if (!temp.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$")) {
                           System.out.println("  [Erro] Data inválida; tente novamente.");
                       } else {
                           String[] dataDesconstruida = temp.split("/");
                           dataInicio = LocalDate.parse(dataDesconstruida[2] + "-" + dataDesconstruida[1] + "-" + dataDesconstruida[0]);
                           break;
                       }
                   } while (true);
                   LocalDate dataLimite;
                   do {
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
                   do {
                       System.out.println("  Adicionando Disciplina ao edital...\n  -----------------------------");
                       System.out.println("    Nome da Disciplina › ");
                       String nomeDisciplina = sc.nextLine();
                       String qtdeVagas;
                       do {
                           System.out.println("    Quantidade de vagas › ");
                           qtdeVagas = sc.nextLine().strip();
                           if (!qtdeVagas.matches("^\\d+$")) {
                               System.out.println("  [Erro] Valor inválido; tente novamente.");
                           } else break;
                       } while (true);
                       novoEdital.adicionarDisciplina(new Disciplina(nomeDisciplina, Integer.parseInt(qtdeVagas)));
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
               case "5" -> {
                   ArrayList<EditalDeMonitoria> lista = central.getTodosOsEditais();
                   if (lista.isEmpty()) {
                       System.out.println("Nenhum edital cadastrado ainda.");
                   } else {
                       System.out.println("Quantidade de editais cadastrados: " + lista.size());
                       System.out.println(menu);
                   }
                   central.mostrarIdEditais();
               }
               case "6" -> {
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
               case "7" -> {
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
                   if (inscrito) {
                       persistencia.salvarCentral(central, nomeArquivo);
                       Mensageiro.enviarEmail(aluno.getEmail(), String.format("""
                               Sua inscrição no Edital de Monitoria n.º %s foi efetuada.
                               """, edital.getNumero()));
                   } else {
                       System.out.println("[Erro] Não foi possível inscrever o aluno.");
                   }
                   System.out.println(menu);
               }
               case "8" -> {
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
               case "S", "s" -> System.out.println("Programa encerrado.");
               default -> System.out.println("[Erro] Opção inválida.");
           }
       } while (!input.strip().equalsIgnoreCase("S"));
   }
}
