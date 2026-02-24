package br.com.monitoria;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Utilitário que proporciona a possibilidade de serialização e desserialização de objetos {@link CentralDeInformacoes}.
 * Agora com integração ao Google Drive.
 */
public class Persistencia {
    private XStream xstream = new XStream(new DomDriver());

    public Persistencia() {
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(new Class[] {
            CentralDeInformacoes.class, Aluno.class, Coordenador.class, Usuario.class,
            EditalDeMonitoria.class, Disciplina.class, Inscricao.class,
            Sexo.class, Vaga.class, LocalDate.class, ArrayList.class
        });
        xstream.alias("central", CentralDeInformacoes.class);
        xstream.alias("aluno", Aluno.class);
        xstream.alias("coordenador", Coordenador.class);
        xstream.alias("edital", EditalDeMonitoria.class);
        xstream.alias("disciplina", Disciplina.class);
        xstream.alias("inscricao", Inscricao.class);
    }

    /**
     * Serializa a CentralDeInformacoes para XML, salva localmente e depois envia para o Google Drive.
     * @param cDI O objeto a ser serializado
     * @param nomeArquivo O nome do arquivo (sem extensão)
     */
    public void salvarCentral(CentralDeInformacoes cDI, String nomeArquivo) {
        String nomeCompleto = nomeArquivo + ".xml";
        String caminho = System.getProperty("user.dir") + File.separator + nomeCompleto;
        File arquivo = new File(caminho);
        String xml = xstream.toXML(cDI);

        try (PrintWriter gravar = new PrintWriter(arquivo)) {
            gravar.println(xml);
            System.out.println("Central salva com sucesso em: " + caminho);

            // Depois de salvar localmente, envia para o Google Drive
            DriveService.enviarArquivo(arquivo);

        } catch (IOException e) {
            System.out.println("[Erro] Não foi possível salvar a central localmente: " + e.getMessage());
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            System.out.println("[Erro] Falha de segurança ao tentar enviar para o Google Drive: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Baixa a CentralDeInformacoes do Google Drive, e depois a desserializa.
     * Se não encontrar no Drive, tenta carregar uma versão local. Se falhar, cria uma nova.
     * @param nomeArquivo O nome do arquivo (sem extensão)
     * @return O objeto CentralDeInformacoes
     */
    public CentralDeInformacoes recuperarCentral(String nomeArquivo) {
        String nomeCompleto = nomeArquivo + ".xml";
        String caminho = System.getProperty("user.dir") + File.separator + nomeCompleto;
        File arquivo = new File(caminho);
        boolean downloadSucesso = false;

        try {
            // Tenta baixar a versão mais recente do Google Drive primeiro
            DriveService.baixarArquivo(nomeCompleto, caminho);
            downloadSucesso = true;
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("[Aviso] Não foi possível baixar o arquivo do Google Drive: " + e.getMessage());
            System.out.println("Tentando carregar a versão local, se existir...");
        }

        // Só tenta ler o arquivo se ele existir e tiver algum conteúdo
        if (arquivo.exists() && arquivo.length() > 0) {
            try (FileInputStream file = new FileInputStream(arquivo)) {
                Object obj = xstream.fromXML(file);

                if (obj instanceof CentralDeInformacoes) {
                    System.out.println("Central carregada com sucesso.");
                    return (CentralDeInformacoes) obj;
                } else {
                    System.out.println("[Erro] O XML não contém uma Central de Informações válida.");
                }
            } catch (Exception e) {
                System.out.println("[Erro] Não foi possível recuperar a central do arquivo local: " + e.getMessage());
                e.printStackTrace();
                // Se o download falhou e o arquivo local está corrompido, o melhor é criar uma nova central
                if (!downloadSucesso) {
                    System.out.println("Arquivo local corrompido. Criando nova Central...");
                    return new CentralDeInformacoes();
                }
            }
        } else {
            if (downloadSucesso) {
                System.out.println("[Aviso] O arquivo baixado do Google Drive está vazio. Criando nova Central...");
            } else {
                System.out.println("[Aviso] Arquivo local não encontrado ou vazio. Criando nova Central...");
            }
        }

        // Se tudo falhar, cria uma nova central
        return new CentralDeInformacoes();
    }
}
