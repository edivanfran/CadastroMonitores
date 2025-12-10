package br.com.monitoria;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import br.com.monitoria.excecoes.*;

public class Main {
    public static void main(String[] args) {
        // Toma o diretório onde o usuário executa o programa e adiciona todos os arquivos .xml deste em uma lista
        File[] pasta = new File(System.getProperty("user.dir")).listFiles(); /* "user.dir" → diretório onde o programa é executado */
        ArrayList<File> arquivos = new ArrayList<>();
        for (File arquivo : pasta) {
            if (arquivo.getName().endsWith(".xml")) {
                arquivos.add(arquivo);
            }
        }

        Scanner sc = new Scanner(System.in);

        // Saúda o usuário corretamente dependendo do período do dia em que ele executa o programa
        if (LocalTime.now().getHour() >= 18) {
            System.out.println("Boa noite.");
        } else if (LocalTime.now().getHour() >= 12) {
            System.out.println("Boa tarde.");
        } else if (LocalTime.now().getHour() >= 5) {
            System.out.println("Bom dia.");
        } else {
            System.out.println("Boa madrugada.");
        }

        // Tenta recuperar as centrais diretamente dos arquivos, caso encontradas, ou cria uma nova caso não
        Persistencia persistencia = new Persistencia();
        CentralDeInformacoes central;
        String nomeArquivo;
        if (arquivos.isEmpty()) {
            System.out.println("Não foi encontrado nenhum arquivo de Central de Informações de Alunos.\nCriando nova Central...");
            central = new CentralDeInformacoes();
            nomeArquivo = "central".toUpperCase();
        } else {
            System.out.println("Foram encontradas as seguintes Centrais de Informações de Alunos:\n-------------------------------");
            for (File arquivo : arquivos) {
                System.out.println("  \"" + arquivo.getName() + "\"");
            }
            System.out.println("-------------------------------");
            System.out.print("Espere um momento estamos pegar arquivo da nuvem » ");
            nomeArquivo = "central".toUpperCase();
            central = persistencia.recuperarCentral(nomeArquivo);
        }

        InicializadorGUI.iniciar(central, persistencia, "CENTRAL");

    }
}