package br.com.monitoria;

import br.com.monitoria.dao.*;
import br.com.monitoria.model.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.List;

public class GerenciadorDeDados {

    private static GerenciadorDeDados instancia;
    // Mundo JPA
    private final EntityManagerFactory emf;
    // Mundo NoSQL
    private final MongoClient mongoClient;
    private final EditalNoSqlDAO editalNoSqlDAO;
    private final InscricaoNoSqlDAO inscricaoNoSqlDAO;

    // Cache com Redis
    private final RedisDAO redisDAO;

    private GerenciadorDeDados() {
        // Inicializa a conexão JPA
        this.emf = Persistence.createEntityManagerFactory("monitoriaPU");

        // Inicializa a conexão com o MongoDB
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        this.editalNoSqlDAO = new EditalNoSqlDAO(mongoClient);
        this.inscricaoNoSqlDAO = new InscricaoNoSqlDAO(mongoClient);

        // Inicializa a conexão com o Redis
        this.redisDAO = new RedisDAO();
    }

    public static synchronized GerenciadorDeDados getInstancia() {
        if (instancia == null){
            instancia = new GerenciadorDeDados();
        }
        return instancia;
    }

    // Métodos para aluno
    public void salvarAluno(Aluno aluno) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new AlunoDAO(em).salvar(aluno);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void atualizarAluno(Aluno aluno) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new AlunoDAO(em).atualizar(aluno);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void removerAluno(Aluno aluno) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new AlunoDAO(em).excluir(aluno);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Aluno buscarAlunoPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return new AlunoDAO(em).buscarPorId(id);
        } finally {
            em.close();
        }
    }

    // Sobrecarga para buscar aluno por Id no formato String
    public Aluno buscarAlunoPorId(String id) {
        try {
            Long longId = Long.parseLong(id);
            return buscarAlunoPorId(longId);
        } catch (NumberFormatException e) {
            // Logar o erro em um sistema real seria uma boa prática
            System.err.println("Erro ao converter ID de Aluno para Long: " + id);
            return null;
        }
    }

    public Aluno buscarAlunoPorMatricula(String matricula) {
        EntityManager em = emf.createEntityManager();
        try {
            return new AlunoDAO(em).buscarPorMatricula(matricula);
        } finally {
            em.close();
        }
    }

    public List<Aluno> getTodosOsAlunos() {
        EntityManager em = emf.createEntityManager();
        try {
            return new AlunoDAO(em).retornarTodos();
        } finally {
            em.close();
        }
    }

    // Métodos para coordenador

    public void salvarCoordenador(Coordenador coordenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new CoordenadorDAO(em).salvar(coordenador);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void atualizarCoordenador(Coordenador coordenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new CoordenadorDAO(em).atualizar(coordenador);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void removerCoordenador(Coordenador coordenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            new CoordenadorDAO(em).excluir(coordenador);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Coordenador buscarCoordenadorPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return new CoordenadorDAO(em).buscarPorId(id);
        } finally {
            em.close();
        }
    }

    public Coordenador getCoordenador() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Coordenador> coordenadores = new CoordenadorDAO(em).retornarTodos();
            return coordenadores.isEmpty() ? null : coordenadores.get(0);
        } finally {
            em.close();
        }
    }

    public List<Coordenador> getTodosOsCoordenadores() {
        EntityManager em = emf.createEntityManager();
        try {
            return new CoordenadorDAO(em).retornarTodos();
        } finally {
            em.close();
        }
    }

    public void atualizarUsuario(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
            GerenciadorDeEventos.getInstancia().notificarAtualizacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Usuario getUsuarioPorEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class
            );
            query.setParameter("email", email.toLowerCase());
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Usuario autenticarUsuario(String email, String senha) {
        Usuario usuario = getUsuarioPorEmail(email);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario;
        }
        return null;
    }

    public boolean isLoginPermitido(String email, String senha) {
        Usuario usuario = getUsuarioPorEmail(email);
        if (usuario == null) {
            return false;
        }
        return usuario.autenticarUsuario(email, senha);
    }

    // --- MÉTODOS MIGRADOS PARA NoSQL ---

    public void salvarEdital(EditalDeMonitoria edital) {
        editalNoSqlDAO.salvar(edital);
        GerenciadorDeEventos.getInstancia().notificarAtualizacao();
    }

    public void atualizarEdital(EditalDeMonitoria edital) {
        editalNoSqlDAO.atualizar(edital);
        // Invalida o cache para garantir consistência
        if (edital != null && edital.getId() != null) {
            redisDAO.removerEdital(edital.getId());
        }
        GerenciadorDeEventos.getInstancia().notificarAtualizacao();
    }

    public void excluirEdital(EditalDeMonitoria edital) {
        inscricaoNoSqlDAO.excluirPorEditalId(edital.getId());
        editalNoSqlDAO.excluir(edital);
        // Remove do cache
        if (edital != null && edital.getId() != null) {
            redisDAO.removerEdital(edital.getId());
        }
        GerenciadorDeEventos.getInstancia().notificarAtualizacao();
    }

    public EditalDeMonitoria buscarEditalPorId(String id) {
        // Tenta buscar do cache (Redis)
        EditalDeMonitoria edital = redisDAO.buscarEditalPorId(id);

        // Se não estiver no cache (cache miss), busca no banco (MongoDB)
        if (edital == null) {
            edital = editalNoSqlDAO.buscarPorId(id);
            // Se encontrou no banco, armazena no cache para futuras consultas
            if (edital != null) {
                redisDAO.salvarEdital(edital);
            }
        }
        // Se estiver no cache (cache hit), retorna diretamente
        return edital;
    }

    public List<EditalDeMonitoria> getTodosOsEditais() {
        return editalNoSqlDAO.retornarTodos();
    }

    // --- MÉTODOS HÍBRIDOS E EM TRANSIÇÃO ---

    public void inscreverAlunoEmEdital(EditalDeMonitoria edital, Aluno aluno, Disciplina disciplina, double cre, double nota, Vaga tipoVaga, int ordem, PreferenciaInscricao pref) throws Exception {
        // AVISO: Esta operação não é transacionalmente atômica entre os bancos de dados.
        Inscricao novaInscricao = new Inscricao(aluno, disciplina, edital, cre, nota, tipoVaga, ordem, pref);
        inscricaoNoSqlDAO.salvar(novaInscricao);
        GerenciadorDeEventos.getInstancia().notificarAtualizacao();
    }

    // Métodos de Inscricao
    public void atualizarInscricoes(List<Inscricao> inscricoes) {
        for (Inscricao inscricao : inscricoes) {
            inscricaoNoSqlDAO.atualizar(inscricao);
        }
        // Poderíamos notificar um evento específico de inscrição se necessário
        // GerenciadorDeEventos.getInstancia().notificarAtualizacao();
    }

    public List<Inscricao> getInscricoesPorAluno(Aluno aluno) {
        // MIGRAÇÃO: Busca as inscrições do MongoDB usando o ID do aluno.
        if (aluno == null || aluno.getId() == null) {
            return Collections.emptyList();
        }
        return inscricaoNoSqlDAO.buscarPorAlunoId(aluno.getId().toString());
    }

    public List<Inscricao> getInscricoesPorEdital(EditalDeMonitoria edital) {
        // MIGRAÇÃO: Busca as inscrições do MongoDB usando o ID do aluno.
        if (edital == null || edital.getId() == null) {
            return Collections.emptyList();
        }
        return inscricaoNoSqlDAO.buscarPorEditalId(edital.getId());
    }

    public void fechar() {
        if (emf != null && emf.isOpen()){
            emf.close();
        }
        if (mongoClient != null) {
            mongoClient.close();
        }
        if (redisDAO != null) {
            redisDAO.fechar();
        }
    }
}
