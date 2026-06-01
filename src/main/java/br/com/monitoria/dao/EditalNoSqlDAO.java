package br.com.monitoria.dao;

import br.com.monitoria.model.Disciplina;
import br.com.monitoria.model.EditalDeMonitoria;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EditalNoSqlDAO implements DAO<EditalDeMonitoria, String> {

    private final MongoCollection<Document> editalCollection;

    public EditalNoSqlDAO(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("bancomonitores");
        this.editalCollection = database.getCollection("editais");
    }

    @Override
    public void salvar(EditalDeMonitoria edital) {
        Document editalDoc = toDocument(edital);
        editalCollection.insertOne(editalDoc);
        edital.setId(editalDoc.getObjectId("_id").toString());
    }

    @Override
    public void atualizar(EditalDeMonitoria edital) {
        if (edital.getId() == null) {
            throw new IllegalArgumentException("O ID do edital não pode ser nulo para atualização.");
        }
        Document editalDoc = toDocument(edital);
        ObjectId objectId = new ObjectId(edital.getId());
        editalCollection.replaceOne(Filters.eq("_id", objectId), editalDoc);
    }

    @Override
    public void excluir(EditalDeMonitoria edital) {
        if (edital.getId() == null) {
            throw new IllegalArgumentException("O ID do edital não pode ser nulo para exclusão.");
        }
        editalCollection.deleteOne(Filters.eq("_id", new ObjectId(edital.getId())));
    }

    @Override
    public EditalDeMonitoria buscarPorId(String id) {
        Document doc = editalCollection.find(Filters.eq("_id", new ObjectId(id))).first();
        return (doc != null) ? fromDocument(doc) : null;
    }

    @Override
    public List<EditalDeMonitoria> retornarTodos() {
        List<EditalDeMonitoria> editais = new ArrayList<>();
        for (Document doc : editalCollection.find()) {
            editais.add(fromDocument(doc));
        }
        return editais;
    }

    private Document toDocument(EditalDeMonitoria edital) {
        List<Document> disciplinasDocs = new ArrayList<>();
        for (Disciplina d : edital.getDisciplinas()) {
            Document disciplinaDoc = new Document("nome", d.getNomeDisciplina())
                        .append("vagas_remuneradas", d.getVagasRemuneradas())
                        .append("vagas_voluntarias", d.getVagasVoluntarias());
            disciplinasDocs.add(disciplinaDoc);
        }

        Document doc = new Document()
                .append("numero", edital.getNumero())
                .append("data_inicio", edital.getDataInicio().toString())
                .append("data_limite", edital.getDataLimite().toString())
                .append("disciplinas", disciplinasDocs)
                .append("aberto", edital.isAberto())
                .append("peso_cre", edital.getPesoCre())
                .append("peso_nota", edital.getPesoNota())
                .append("resultado_calculado", edital.isResultadoCalculado())
                .append("periodo_desistencia_encerrado", edital.isPeriodoDesistenciaEncerrado());

        if (edital.getId() != null) {
            doc.put("_id", new ObjectId(edital.getId()));
        }

        return doc;
    }

    private EditalDeMonitoria fromDocument(Document doc) {
        if (doc == null) {
            return null;
        }

        List<Document> disciplinasDocs = doc.getList("disciplinas", Document.class, new ArrayList<>());
        List<Disciplina> disciplinas = new ArrayList<>();
        for (Document d : disciplinasDocs) {
            disciplinas.add(new Disciplina(
                        d.getString("nome"),
                        d.getInteger("vagas_voluntarias", 0),
                        d.getInteger("vagas_remuneradas", 0)
            ));
        }

        return new EditalDeMonitoria(
                doc.getObjectId("_id").toString(),
                doc.getString("numero"),
                LocalDate.parse(doc.getString("data_inicio")),
                LocalDate.parse(doc.getString("data_limite")),
                disciplinas,
                doc.getBoolean("aberto", false),
                doc.getDouble("peso_cre"),
                doc.getDouble("peso_nota"),
                doc.getBoolean("resultado_calculado", false),
                doc.getBoolean("periodo_desistencia_encerrado", false)
        );
    }
}
