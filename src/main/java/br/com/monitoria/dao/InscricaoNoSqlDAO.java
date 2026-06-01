package br.com.monitoria.dao;

import br.com.monitoria.PreferenciaInscricao;
import br.com.monitoria.Vaga;
import br.com.monitoria.model.Inscricao;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class InscricaoNoSqlDAO implements DAO<Inscricao, String> {

    private final MongoCollection<Document> inscricaoCollection;

    public InscricaoNoSqlDAO(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("bancomonitores");
        this.inscricaoCollection = database.getCollection("inscricoes");
    }

    @Override
    public void salvar(Inscricao inscricao) {
        Document inscricaoDoc = toDocument(inscricao);
        inscricaoCollection.insertOne(inscricaoDoc);
        // O ID é gerado pelo MongoDB
        inscricao.setId(inscricaoDoc.getObjectId("_id").toString());
    }

    @Override
    public void atualizar(Inscricao inscricao) {
        if (inscricao.getId() == null) {
            throw new IllegalArgumentException("O ID da inscrição não pode ser nulo para atualização.");
        }
        Document inscricaoDoc = toDocument(inscricao);
        inscricaoCollection.replaceOne(Filters.eq("_id", new ObjectId(inscricao.getId())), inscricaoDoc);
    }

    @Override
    public void excluir(Inscricao inscricao) {
        if (inscricao.getId() == null) {
            throw new IllegalArgumentException("O ID da inscrição não pode ser nulo para exclusão.");
        }
        inscricaoCollection.deleteOne(Filters.eq("_id", new ObjectId(inscricao.getId())));
    }

    @Override
    public Inscricao buscarPorId(String id) {
        Document document = inscricaoCollection.find(Filters.eq("_id", new ObjectId(id))).first();
        return (document != null) ? fromDocument(document) : null;
    }

    @Override
    public List<Inscricao> retornarTodos() {
        List<Inscricao> inscricoes = new ArrayList<>();
        for (Document document : inscricaoCollection.find()) {
            inscricoes.add(fromDocument(document));
        }
        return inscricoes;
    }

    public List<Inscricao> buscarPorEditalId(String editalId) {
        List<Inscricao> inscricoes = new ArrayList<>();
        for (Document document : inscricaoCollection.find(Filters.eq("edital_id", editalId))) {
            inscricoes.add(fromDocument(document));
        }
        return inscricoes;
    }

    public List<Inscricao> buscarPorAlunoId(String alunoId) {
        List<Inscricao> inscricoes = new ArrayList<>();
        for (Document document : inscricaoCollection.find(Filters.eq("aluno_id", alunoId))) {
            inscricoes.add(fromDocument(document));
        }
        return inscricoes;
    }

    public void excluirPorEditalId(String editalId) {
        inscricaoCollection.deleteMany(Filters.eq("edital_id", editalId));
    }

    private Document toDocument(Inscricao inscricao) {
        Document document = new Document()
                .append("edital_id", inscricao.getEditalId())
                .append("aluno_id", inscricao.getAlunoId())
                .append("disciplina_nome", inscricao.getDisciplinaNome())
                .append("cre", inscricao.getCre())
                .append("nota", inscricao.getNota())
                .append("tipo_vaga", inscricao.getTipoVaga() != null ? inscricao.getTipoVaga().toString() : null)
                .append("ordem_preferencia", inscricao.getOrdemPreferencia())
                .append("preferencia_vaga", inscricao.getPreferenciaVaga() != null ? inscricao.getPreferenciaVaga().toString() : null)
                .append("desistiu", inscricao.isDesistiu())
                .append("pontuacao_final", inscricao.getPontuacaoFinal());
        if (inscricao.getId() != null) {
            document.put("_id", new ObjectId(inscricao.getId()));
        }
        return document;
    }

    private Inscricao fromDocument(Document document) {
        if (document == null) {
            return null;
        }
        return new Inscricao(
                document.getObjectId("_id").toString(),
                document.getString("edital_id"),
                document.getString("aluno_id"),
                document.getString("disciplina_nome"),
                document.getDouble("cre"),
                document.getDouble("nota"),
                document.getString("tipo_vaga") != null ? Vaga.valueOf(document.getString("tipo_vaga")) : null,
                document.getInteger("ordem_preferencia"),
                document.getString("preferencia_vaga") != null ? PreferenciaInscricao.valueOf(document.getString("preferencia_vaga")) : null,
                document.getBoolean("desistiu", false),
                document.getDouble("pontuacao_final")
        );
    }
}
