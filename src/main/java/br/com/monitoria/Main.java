package br.com.monitoria;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import br.com.monitoria.dao.JPAUtil;
import br.com.monitoria.excecoes.*;
import com.mongodb.client.MongoDatabase;

public class Main {
    public static void main(String[] args) {
        JPAUtil.getEntityManager().close();

        MongoDatabase db = PersistenciaNoSql.getDatabase();
        System.out.println("Conectado: " + db.getName());

        File[] pasta = new File(System.getProperty("user.dir")).listFiles();
        ArrayList<File> arquivos = new ArrayList<>();
        for (File arquivo : pasta) {
            if (arquivo.getName().endsWith(".xml")) {
                arquivos.add(arquivo);
            }
        }

        Scanner sc = new Scanner(System.in);

        if (LocalTime.now().getHour() >= 18) {
            System.out.println("Boa noite.");
        } else if (LocalTime.now().getHour() >= 12) {
            System.out.println("Boa tarde.");
        } else if (LocalTime.now().getHour() >= 5) {
            System.out.println("Bom dia.");
        } else {
            System.out.println("Boa madrugada.");
        }

        if (arquivos.isEmpty()) {
            System.out.println("Não foi encontrado nenhum arquivo de Central de Informações de Alunos.\nCriando nova Central...");
        } else {
            System.out.println("Foram encontradas as seguintes Centrais de Informações de Alunos:\n-------------------------------");
            for (File arquivo : arquivos) {
                System.out.println("  \"" + arquivo.getName() + "\"");
            }
            System.out.println("-------------------------------");
            System.out.print("Espere um momento estamos pegar arquivo da nuvem » ");
        }
        PersistenciaNoSql.fechar();
        InicializadorGUI.iniciar();
    }
}