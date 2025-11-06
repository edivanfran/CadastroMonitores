import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

public class Persistencia {
    private XStream xstream = new XStream(new DomDriver()); // obj. responsável pela conversão de objetos p/ XML

    public Persistencia() {
        XStream.setupDefaultSecurity(xstream);
        // permitir os tipos usados no projeto para desserialização segura
        xstream.allowTypes(new Class[] {
            CentralDeInformacoes.class,
            Aluno.class,
            EditalDeMonitoria.class,
            Disciplina.class,
            Sexo.class,
            LocalDate.class,
            ArrayList.class
        });
        xstream.alias("central", CentralDeInformacoes.class);
        xstream.alias("aluno", Aluno.class);
        xstream.alias("edital", EditalDeMonitoria.class);
        xstream.alias("disciplina", Disciplina.class);
    }

    public void salvarCentral(CentralDeInformacoes cDI, String nomeArquivo) {
        String caminho = System.getProperty("user.dir") + File.separator + nomeArquivo + ".xml";
        File arquivo = new File(caminho);
        String xml = xstream.toXML(cDI);

        try {
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
            PrintWriter gravar = new PrintWriter(arquivo);
            gravar.println(xml);
            gravar.close();
            System.out.println("Central salva com sucesso em: " + caminho);
        } catch (IOException e) {
            System.out.println("[Erro] Não foi possível salvar a central: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public CentralDeInformacoes recuperarCentral(String nomeArquivo) {
        String caminho = System.getProperty("user.dir") + File.separator + nomeArquivo + ".xml";
        File arquivo = new File(caminho);

        if (!arquivo.exists()) {
            System.out.println("[Erro] Arquivo não encontrado: " + caminho + "\nCriando nova Central...");
            return new CentralDeInformacoes();
        }

        try (FileInputStream file = new FileInputStream(arquivo)) {
            Object obj = xstream.fromXML(file);

            if (obj instanceof CentralDeInformacoes) {
                System.out.println("Central carregada com sucesso.");
                return (CentralDeInformacoes) obj;
            } else {
                System.out.println("[Erro] O XML não contém uma Central de Informações válida.");
            }
        } catch (Exception e) {
            System.out.println("[Erro] Não foi possível recuperar central: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Criando nova Central...");
        return new CentralDeInformacoes();
    }
}
