import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;


public class GeradorDeRelatorios {
   public static void obterComprovanteDeInscricoesAluno(String matricula, long idEdital, CentralDeInformacoes central) {
       try {
           Document documento = new Document();
           PdfWriter.getInstance(documento, new FileOutputStream("relatorio.pdf"));
           documento.open();


           Font fonteTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
           Paragraph titulo = new Paragraph("Comprovante de Inscrições do Aluno\n\n", fonteTitulo);
           titulo.setAlignment(Element.ALIGN_CENTER);
           documento.add(titulo);


           documento.add(new Paragraph("Nome do Aluno: " + central.recuperarAluno(matricula).getNome()));
           documento.add(new Paragraph("Matrícula: " + matricula));
           documento.add(new Paragraph("Edital de nº: " + central.recuperarEdital(idEdital).getNumero()));
           documento.add(new Paragraph("\n"));


           for (Disciplina disciplina : central.recuperarEdital(idEdital).getDisciplinas()) {
               boolean flag = false;
               for (Aluno aluno : disciplina.getAlunosInscritos()) {
                   if (aluno.getMatricula().equals(matricula)) {
                       if (!flag) {
                           documento.add(new Paragraph("Aluno inscrito em:"));
                           flag = true;
                       }
                       documento.add(new Paragraph("    " + disciplina.getNomeDisciplina()));
                   }
               }
               if (!flag) {
                   documento.add(new Paragraph("O aluno não está inscrito em nenhuma disciplina."));
               }
           }


           documento.close();
           System.out.println("Relatório gerado com sucesso: relatorio.pdf");


       } catch (Exception e) {
           System.err.println("Erro ao gerar relatório: " + e.getMessage());
       }
   }
}
