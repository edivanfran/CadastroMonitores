package br.com.monitoria;

import br.com.monitoria.interfaces.ReportFormatter;
import br.com.monitoria.model.Disciplina;
import br.com.monitoria.model.Inscricao;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Implementação do ReportFormatter para o formato PDF (Implementação Concreta do Padrão Bridge).
 */
public class PdfFormatter implements ReportFormatter {

    private Document documento;
    public String tipoFormatter;

    public PdfFormatter() {
        this.tipoFormatter = "PDF";
    }

    @Override
    public String getTipoFormatter() {
        return tipoFormatter;
    }

    @Override
    public void iniciarDocumento(String nomeArquivo, boolean landscape) throws Exception {
        this.documento = landscape ? new Document(PageSize.A4.rotate()) : new Document();
        PdfWriter.getInstance(this.documento, new FileOutputStream(nomeArquivo));
        this.documento.open();
    }

    @Override
    public void adicionarTituloPrincipal(String texto) throws Exception {
        Font fonteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph titulo = new Paragraph(texto, fonteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        this.documento.add(titulo);
    }

    private Paragraph criarParagrafoSubtitulo(String texto) {
        Font fonteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph subtitulo = new Paragraph(texto, fonteSubtitulo);
        subtitulo.setSpacingAfter(10);
        return subtitulo;
    }

    @Override
    public void adicionarSubtitulo(String texto) throws Exception {
        // Cria o subtítulo usando o lógica interior
        this.documento.add(criarParagrafoSubtitulo(texto));
    }

    @Override
    public void adicionarParagrafo(String texto) throws Exception {
        Paragraph paragrafo = new Paragraph(texto);
        paragrafo.setSpacingAfter(10);
        this.documento.add(paragrafo);
    }

    @Override
    public void adicionarTabelaResultado(Disciplina disciplina, ArrayList<Inscricao> ranque) throws Exception {
        // Cria o parágrafo do subtítulo usando o lógica interior
        Paragraph subtitulo = criarParagrafoSubtitulo(disciplina.getNomeDisciplina());

        // Cria a tabela de resultados
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
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5);
            tabela.addCell(cell);
        }

        // Corpo da Tabela
        if (ranque != null && !ranque.isEmpty()) {
            int pos = 1;
            for (Inscricao inscricao : ranque) {
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
            // Mensagem para disciplina sem candidatos
            PdfPCell cell = new PdfPCell(new Phrase("Não há candidatos classificados para esta disciplina."));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(10);
            tabela.addCell(cell);
        }

        // Cria um container para manter o subtítulo e a tabela juntos
        PdfPTable container = new PdfPTable(1);
        container.setWidthPercentage(100);
        container.setSpacingAfter(15f);
        PdfPCell cellContainer = new PdfPCell();
        cellContainer.setBorder(Rectangle.NO_BORDER);
        cellContainer.addElement(subtitulo);
        cellContainer.addElement(tabela);
        container.addCell(cellContainer);

        // Adiciona o container (com subtítulo e tabela) ao documento
        this.documento.add(container);
    }

    @Override
    public void salvarEFecharDocumento() {
        if (this.documento != null && this.documento.isOpen()) {
            this.documento.close();
        }
    }
}
