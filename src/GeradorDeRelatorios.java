import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;

/**
 * Classe responsável pela geração de relatórios.
 * <p>Seus métodos são todos estáticos.</p>
 */
public class GeradorDeRelatorios {
    /**
     * Gera um comprovante contendo as informações de cada inscrição a vaga de monitor de alguma disciplina que o aluno efetuou em um mesmo edital de monitoria.
     * <p>O comprovante é salvo no diretório do projeto.</p>
     * @param matricula A matrícula do aluno
     * @param idEdital O ID do edital de monitoria
     * @param central O objeto {@code CentralDeInformacoes} que contém os dados das inscrições
     */
    public static void obterComprovanteDeInscricoesAluno(String matricula, long idEdital, CentralDeInformacoes central) {
       try {
           Document documento = new Document(); // Instancia o documento o qual será trabalhado
           PdfWriter.getInstance(documento, new FileOutputStream("relatorio.pdf")); // Define o nome do documento
           documento.open(); // Abre o documento para poder ser trabalhado

           // Título do documento
           Font fonteTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD); /* Fonte: Helvetica Bold 18 */
           Paragraph titulo = new Paragraph("Comprovante de Inscrições do Aluno\n\n", fonteTitulo);
           titulo.setAlignment(Element.ALIGN_CENTER);
           documento.add(titulo);

           // Corpo do documento
           documento.add(new Paragraph("Nome do Aluno: " + central.recuperarAluno(matricula).getNome()));
           documento.add(new Paragraph("Matrícula: " + matricula));
           documento.add(new Paragraph("Edital de nº: " + central.recuperarEdital(idEdital).getNumero()));
           documento.add(new Paragraph("\n"));
           for (Disciplina disciplina : central.recuperarEdital(idEdital).getDisciplinas()) {
               boolean flag = false;
               for (Aluno aluno : disciplina.getAlunosInscritos()) { //TODO| resolver método inválido em Disciplina
                   if (aluno.getMatricula().equals(matricula)) {
                       if (!flag) { /* Se o aluno está inscrito em alguma disciplina, acrescenta esse preâmbulo na primeira vez */
                           documento.add(new Paragraph("Aluno inscrito em:"));
                           flag = true;
                       }
                       documento.add(new Paragraph("    " + disciplina.getNomeDisciplina()));
                   }
               }
               if (!flag) { /* Do contrário, acrescentar apenas este */
                   documento.add(new Paragraph("O aluno não está inscrito em nenhuma disciplina."));
               }
           }

           documento.close(); // Fecha o documento
           System.out.println("Relatório gerado com sucesso: relatorio.pdf");

       } catch (Exception e) {
           System.err.println("Erro ao gerar relatório: " + e.getMessage()); //TODO| Ao invés de usar `System.err.println()`, pode ser mais funcional propagar a exceção para ser tratada de forma mais específica (por exemplo, lançar uma tela com uma mensagem de erro — não usaremos saída de erro tal como `System.err` muito provavelmente)
       }
   }
}
