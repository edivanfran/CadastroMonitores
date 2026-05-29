package br.com.monitoria.dao;

import br.com.monitoria.PreferenciaInscricao;
import br.com.monitoria.Vaga;
import br.com.monitoria.model.Inscricao;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
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
        inscricao.setId(inscricaoDoc.getObjectId("_id").toString());
    }

    @Override
    public void atualizar(Inscricao inscricao) {
        if (inscricao.getId() == null) {
            throw new IllegalArgumentException("O ID da inscrição não pode ser nulo para atualização.");
        }
        Document inscricaoDoc = toDocument(inscricao);
        inscricaoCollection.replaceOne(Filters.eq("_id", new ObjectId(inscricao.getId())), inscricaoDoc, new ReplaceOptions().upsert(true));
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
        Document doc = inscricaoCollection.find(Filters.eq("_id", new ObjectId(id))).first();
        return (doc != null) ? fromDocument(doc) : null;
    }

    @Override
    public List<Inscricao> retornarTodos() {
        List<Inscricao> inscricoes = new ArrayList<>();
        try (MongoCursor<Document> cursor = inscricaoCollection.find().iterator()) {
            while (cursor.hasNext()) {
                inscricoes.add(fromDocument(cursor.next()));
            }
        }
        return inscricoes;
    }

    public List<Inscricao> buscarPorEditalId(String editalId) {
        List<Inscricao> inscricoes = new ArrayList<>();
        try (MongoCursor<Document> cursor = inscricaoCollection.find(Filters.eq("edital_id", editalId)).iterator()) {
            while (cursor.hasNext()) {
                inscricoes.add(fromDocument(cursor.next()));
            }
        }
        return inscricoes;
    }

    public void excluirPorEditalId(String editalId) {
        inscricaoCollection.deleteMany(Filters.eq("edital_id", editalId));
    }

    private Document toDocument(Inscricao inscricao) {
        return new Document()
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
    }

    private Inscricao fromDocument(Document doc) {
        if (doc == null) {
            return null;
        }
        return new Inscricao(
                doc.getObjectId("_id").toString(),
                doc.getString("edital_id"),
                doc.getString("aluno_id"),
                doc.getString("disciplina_nome"),
                doc.getDouble("cre"),
                doc.getDouble("nota"),
                doc.getString("tipo_vaga") != null ? Vaga.valueOf(doc.getString("tipo_vaga")) : null,
                doc.getInteger("ordem_preferencia"),
                doc.getString("preferencia_vaga") != null ? PreferenciaInscricao.valueOf(doc.getString("preferencia_vaga")) : null,
                doc.getBoolean("desistiu", false),
                doc.getDouble("pontuacao_final")
        );
    }
}
