package br.com.monitoria;

import br.com.monitoria.interfaces.ReportFormatter;
import br.com.monitoria.model.Aluno;
import br.com.monitoria.model.Disciplina;
import br.com.monitoria.model.EditalDeMonitoria;
import br.com.monitoria.model.Inscricao;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

/**
 * Classe Abstrata para Geração de Relatórios (Abstração do Padrão Bridge)
 *
 * Esta classe define a interface de alto nível para a geração de relatórios,
 * delegando os detalhes de formatação para uma implementação de ReportFormatter.
 */
public class GeradorDeRelatorios {

    protected ReportFormatter formatter;

    /**
     * Construtor que recebe a implementação de formatação (Bridge).
     * @param formatter A implementação concreta do formatador de relatórios.
     */
    public GeradorDeRelatorios(ReportFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Gera um relatório com o resultado completo de um edital.
     * A lógica de formatação é delegada ao objeto 'formatter'.
     *
     * @param edital O edital cujo resultado será exportado.
     * @throws Exception Se ocorrer um erro durante a geração do relatório.
     */
    public void gerarResultadoEdital(EditalDeMonitoria edital) throws Exception {
        String nomeArquivo = "Resultado_Edital_" + edital.getNumero() + "." + formatter.getTipoFormmater().toLowerCase();
        try {
            formatter.iniciarDocumento(nomeArquivo, true);
            formatter.adicionarTituloPrincipal("Resultado do Edital de Monitoria nº " + edital.getNumero());

            // Itera sobre cada disciplina do edital, delegando a criação da tabela
            for (Disciplina disciplina : edital.getDisciplinas()) {
                formatter.adicionarTabelaResultado(disciplina, edital.getRanquePorDisciplina().get(disciplina.getNomeDisciplina()));
            }

            formatter.salvarEFecharDocumento();
            System.out.println("Relatório gerado com sucesso: " + nomeArquivo);
        } catch (Exception e) {
            // Garante que o documento seja fechado em caso de erro
            if (formatter != null) {
                formatter.salvarEFecharDocumento();
            }
            // Propaga a exceção para que a camada de UI possa notificar o usuário
            throw new Exception("Erro ao gerar o relatório: " + e.getMessage());
        }
    }

    /**
     * Gera um comprovante contendo as informações de cada inscrição a vaga de monitor de alguma disciplina que o aluno efetuou em um mesmo edital de monitoria.
     * <p>O comprovante é salvo no diretório do projeto.</p>
     * @param matricula A matrícula do aluno
     * @param idEdital O ID do edital de monitoria
     */
    public static void obterComprovanteDeInscricoesAluno(String matricula, long idEdital) {
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
           EditalDeMonitoria edital = GerenciadorDeDados.getInstancia().buscarEditalPorId(idEdital);
           Aluno aluno = GerenciadorDeDados.getInstancia().buscarAlunoPorMatricula(matricula);

           if (aluno == null) {
               documento.add(new Paragraph("Aluno não encontrado com a matrícula: " + matricula));
               documento.close();
               return;
           }

           documento.add(new Paragraph("Nome do Aluno: " + aluno.getNome()));
           documento.add(new Paragraph("Matrícula: " + matricula));
           documento.add(new Paragraph("Edital de nº: " + edital.getNumero()));
           documento.add(new Paragraph("\n"));

           // Busca as inscrições do aluno no edital
           boolean temInscricao = false;
           for (Inscricao inscricao : edital.getInscricoes()) {
               if (inscricao.getAluno().getMatricula().equals(matricula) && !inscricao.isDesistiu()) {
                   if (!temInscricao) {
                       documento.add(new Paragraph("Aluno inscrito em:"));
                       temInscricao = true;
                   }
                   documento.add(new Paragraph("    " + inscricao.getDisciplina().getNomeDisciplina() +
                           " (CRE: " + inscricao.getCre() + ", Nota: " + inscricao.getNota() +
                           ", Vaga: " + inscricao.getTipoVaga() + ")"));
               }
           }

           if (!temInscricao) {
               documento.add(new Paragraph("O aluno não está inscrito em nenhuma disciplina deste edital."));
           }

           documento.close(); // Fecha o documento
           System.out.println("Relatório gerado com sucesso: relatorio.pdf");

       } catch (Exception e) {
           System.err.println("Erro ao gerar relatório: " + e.getMessage());
       }
   }
}
