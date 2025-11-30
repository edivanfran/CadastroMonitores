import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Utilitário que proporciona a possibilidade de serialização e desserialização de objetos {@link CentralDeInformacoes}.
 */
public class Persistencia {
    private XStream xstream = new XStream(new DomDriver()); /* responsável pela conversão de objetos `CentralDeInformacoes` para XML */

    public Persistencia() { // Configuração inicial do persistidor
        XStream.setupDefaultSecurity(xstream); //TODO| substituir, pois esse método está depreciado/obsoleto
        xstream.allowTypes(new Class[] { // Permitir os tipos usados no projeto para desserialização segura
            CentralDeInformacoes.class,
            Aluno.class,
            EditalDeMonitoria.class,
            Disciplina.class,
            Sexo.class, //TODO| lembrar de, depois, trocar o nome "Sexo" por "Gênero" visando consistência (há vários atributos usando esse tipo que são nomeados "gênero", e recebem tipo `Sexo` — isso é inconsistente)
            LocalDate.class,
            ArrayList.class
        });
        xstream.alias("central", CentralDeInformacoes.class);
        xstream.alias("aluno", Aluno.class);
        xstream.alias("edital", EditalDeMonitoria.class);
        xstream.alias("disciplina", Disciplina.class); /* campos apropriados no XML, ex.: "aluno" → `<Aluno>...</Aluno>` */
    }

    /**
     * Serializa as informações armazenadas no {@link CentralDeInformacoes} para XML, e então salva-as em um arquivo no diretório atual do programa.
     * @param cDI O objeto que será serializado
     * @param nomeArquivo O nome do arquivo onde o XML será escrito
     */
    public void salvarCentral(CentralDeInformacoes cDI, String nomeArquivo) {
        String caminho = System.getProperty("user.dir") + File.separator + nomeArquivo + ".xml"; /* `user.dir` → diretório atual */
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
            e.printStackTrace(); //TODO| é mais apropriado usar um modal com mensagem de erro
        }
    }

    /**
     * Lê as informações escritas no arquivo e tenta desserializá-las para recuperar o {@link CentralDeInformacoes} que foi serializado anteriormente.
     * <p>Caso não consiga desserializar, ou caso o arquivo não exista, instancia um novo objeto desse tipo.</p>
     * @param nomeArquivo O nome do arquivo a ser lido
     * @return O objeto central que estava no arquivo, ou um recém-instanciado
     */
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
