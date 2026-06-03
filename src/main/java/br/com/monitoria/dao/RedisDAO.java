package br.com.monitoria.dao;

import br.com.monitoria.model.EditalDeMonitoria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDAO {

    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;
    private static final String EDITAL_PREFIX = "edital:";

    public RedisDAO() {
        // Configura o pool de conexões do Jedis
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        this.jedisPool = new JedisPool(poolConfig, "localhost", 6379);

        // Instancia o ObjectMapper e registra o módulo para lidar com java.time.LocalDate
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void salvarEdital(EditalDeMonitoria edital) {
        if (edital == null || edital.getId() == null) {
            return;
        }
        String key = EDITAL_PREFIX + edital.getId();
        try (Jedis jedis = jedisPool.getResource()) {
            String editalJson = objectMapper.writeValueAsString(edital);
            // Define um tempo de expiração de 10 minutos (360 segundos)
            jedis.setex(key, 360, editalJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public EditalDeMonitoria buscarEditalPorId(String id) {
        String key = EDITAL_PREFIX + id;
        try (Jedis jedis = jedisPool.getResource()) {
            String editalJson = jedis.get(key);
            if (editalJson != null) {
                try {
                    return objectMapper.readValue(editalJson, EditalDeMonitoria.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public void removerEdital(String id) {
        String key = EDITAL_PREFIX + id;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        }
    }

    public void fechar() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}
