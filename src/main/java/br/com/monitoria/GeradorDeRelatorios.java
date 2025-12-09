package br.com.monitoria;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Classe responsável pela geração de relatórios em PDF.
 */
public class GeradorDeRelatorios {

    /**
     * Gera um PDF com o resultado completo de um edital, incluindo o ranqueamento de todas as disciplinas.
     * @param edital O edital cujo resultado será exportado.
     * @throws Exception Se ocorrer um erro durante a geração do PDF.
     */
    public static void gerarResultadoEdital(EditalDeMonitoria edital) throws Exception {
        Document documento = new Document(PageSize.A4.rotate()); // Paisagem para caber a tabela
        String nomeArquivo = "Resultado_Edital_" + edital.getNumero() + ".pdf";

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(nomeArquivo));
            documento.open();

            // Título Principal
            Font fonteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph titulo = new Paragraph("Resultado do Edital de Monitoria nº " + edital.getNumero(), fonteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            documento.add(titulo);

            // Itera sobre cada disciplina do edital
            for (Disciplina disciplina : edital.getDisciplinas()) {
                documento.add(criarTabelaDisciplina(disciplina, edital));
            }

            documento.close();
            System.out.println("Relatório gerado com sucesso: " + nomeArquivo);

        } catch (Exception e) {
            // Propaga a exceção para que a tela possa notificar o usuário
            throw new Exception("Erro ao gerar o relatório em PDF: " + e.getMessage());
        }
    }

    /**
     * Cria uma tabela PDF para uma disciplina específica.
     * @param disciplina A disciplina para a qual a tabela será criada.
     * @param edital O edital que contém os dados.
     * @return Um objeto PdfPTable pronto para ser adicionado ao documento.
     * @throws DocumentException Se houver erro na criação da tabela.
     */
    private static PdfPTable criarTabelaDisciplina(Disciplina disciplina, EditalDeMonitoria edital) throws DocumentException {
        // Subtítulo da Disciplina
        Font fonteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph subtitulo = new Paragraph(disciplina.getNomeDisciplina(), fonteSubtitulo);
        subtitulo.setSpacingAfter(10);
        
        // Tabela
        PdfPTable tabela = new PdfPTable(4); // 4 colunas: Posição, Aluno, Pontuação, Status
        tabela.setWidthPercentage(100);
        tabela.setSpacingBefore(15f);
        tabela.setSpacingAfter(15f);

        // Cabeçalho da Tabela
        Font fonteCabecalho = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        String[] cabecalhos = {"Pos.", "Aluno", "Pontuação", "Status"};
        for (String cabecalho : cabecalhos) {
            PdfPCell cell = new PdfPCell(new Phrase(cabecalho, fonteCabecalho));
            cell.setBackgroundColor(BaseColor.GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            tabela.addCell(cell);
        }

        // Corpo da Tabela
        ArrayList<Inscricao> ranqueAtivo = edital.getRanquePorDisciplina().get(disciplina.getNomeDisciplina());
        if (ranqueAtivo != null && !ranqueAtivo.isEmpty()) {
            int pos = 1;
            for (Inscricao inscricao : ranqueAtivo) {
                String status;
                if (pos <= disciplina.getVagasRemuneradas()) {
                    status = "Contemplado (Bolsa)";
                } else if (pos <= disciplina.getVagasRemuneradas() + disciplina.getVagasVoluntarias()) {
                    status = "Contemplado (Voluntário)";
                } else {
                    status = "Não Contemplado";
                }

                tabela.addCell(String.valueOf(pos++));
                tabela.addCell(inscricao.getNomeAluno());
                tabela.addCell(String.format("%.2f", inscricao.getPontuacaoFinal()));
                tabela.addCell(status);
            }
        } else {
            // Caso não haja inscritos ou ranque
            PdfPCell cell = new PdfPCell(new Phrase("Não há candidatos classificados para esta disciplina."));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(10);
            tabela.addCell(cell);
        }
        
        // Adiciona o subtítulo e a tabela a uma célula para mantê-los juntos
        PdfPTable container = new PdfPTable(1);
        container.setWidthPercentage(100);
        PdfPCell cellContainer = new PdfPCell();
        cellContainer.setBorder(Rectangle.NO_BORDER);
        cellContainer.addElement(subtitulo);
        cellContainer.addElement(tabela);
        container.addCell(cellContainer);

        return container;
    }

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
           EditalDeMonitoria edital = central.recuperarEdital(idEdital);
           Aluno aluno = central.recuperarAluno(matricula);
           
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
