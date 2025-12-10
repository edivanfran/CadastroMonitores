package br.com.monitoria;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class DriveService {

    private static final String APPLICATION_NAME = "Cadastro Monitores";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    // O caminho para a chave da conta de serviço na pasta resources
    private static final String SERVICE_ACCOUNT_KEY_PATH = "/service-account-key.json";
    // Escopo para permitir leitura e escrita de arquivos
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static Drive service;

    /**
     * Cria as credenciais para a API do Drive usando a Conta de Serviço.
     * @return Objeto GoogleCredential autorizado.
     * @throws IOException Se o arquivo da chave não for encontrado.
     */
    private static GoogleCredential getCredentials() throws IOException {
        InputStream in = DriveService.class.getResourceAsStream(SERVICE_ACCOUNT_KEY_PATH);
        if (in == null) {
            throw new IOException("Arquivo da conta de serviço não encontrado: " + SERVICE_ACCOUNT_KEY_PATH);
        }
        return GoogleCredential.fromStream(in).createScoped(SCOPES);
    }

    /**
     * Retorna uma instância do serviço Google Drive autorizado via Conta de Serviço.
     * @return Uma instância do serviço Drive.
     * @throws IOException Se houver erro de I/O.
     * @throws GeneralSecurityException Se houver erro de segurança.
     */
    public static Drive getDriveService() throws IOException, GeneralSecurityException {
        if (service == null) {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            GoogleCredential credential = getCredentials();
            service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        return service;
    }

    /**
     * Procura o ID de um arquivo no Google Drive pelo seu nome.
     * @param nomeArquivo O nome do arquivo a ser procurado.
     * @return O ID do arquivo, ou null se não for encontrado.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static String procurarArquivoPorNome(String nomeArquivo) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();
        String query = "name = '" + nomeArquivo + "' and trashed = false";
        FileList result = driveService.files().list()
                .setQ(query)
                .setSpaces("drive")
                .setFields("files(id, name)")
                .execute();
        if (result.getFiles().isEmpty()) {
            return null; // Arquivo não encontrado
        }
        return result.getFiles().get(0).getId(); // Retorna o ID do primeiro arquivo encontrado
    }

    /**
     * Envia um arquivo para o Google Drive. Se o arquivo já existir, ele será atualizado.
     * @param arquivoJava O arquivo local a ser enviado.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static void enviarArquivo(java.io.File arquivoJava) throws IOException, GeneralSecurityException {
        String fileId = procurarArquivoPorNome(arquivoJava.getName());
        FileContent mediaContent = new FileContent("application/xml", arquivoJava);

        if (fileId == null) {
            // Upload de novo arquivo
            File fileMetadata = new File();
            fileMetadata.setName(arquivoJava.getName());
            Drive driveService = getDriveService();
            driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("Arquivo enviado para o Google Drive com sucesso.");
        } else {
            // Atualização de arquivo existente
            Drive driveService = getDriveService();
            driveService.files().update(fileId, null, mediaContent).execute();
            System.out.println("Arquivo no Google Drive atualizado com sucesso.");
        }
    }

    /**
     * Baixa um arquivo do Google Drive para um caminho local de forma segura, usando um arquivo temporário.
     * @param nomeArquivo O nome do arquivo no Drive.
     * @param caminhoDestino O caminho completo onde o arquivo será salvo localmente.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static void baixarArquivo(String nomeArquivo, String caminhoDestino) throws IOException, GeneralSecurityException {
        String fileId = procurarArquivoPorNome(nomeArquivo);
        if (fileId == null) {
            System.out.println("Arquivo '" + nomeArquivo + "' não encontrado no Google Drive. Usando versão local se existir.");
            return;
        }

        Drive driveService = getDriveService();
        java.io.File arquivoTemporario = new java.io.File(caminhoDestino + ".temp");

        try (OutputStream outputStream = new FileOutputStream(arquivoTemporario)) {
            driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            
            // Se o download for bem-sucedido, substitui o arquivo original pelo temporário
            Files.move(arquivoTemporario.toPath(), Paths.get(caminhoDestino), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo baixado do Google Drive com sucesso para: " + caminhoDestino);

        } catch (IOException e) {
            // Se o download falhar, apaga o arquivo temporário para não deixar lixo
            if (arquivoTemporario.exists()) {
                arquivoTemporario.delete();
            }
            // Lança a exceção para que a classe Persistencia saiba que o download falhou
            throw new IOException("Falha ao baixar o arquivo do Google Drive: " + e.getMessage(), e);
        }
    }
}
