package conexao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

// Fábrica única e compartilhada de conexões com o banco (EntityManagerFactory).
public class JPAUtil {

    private static volatile EntityManagerFactory emf;

    private JPAUtil() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        // Dupla checagem: evita recriar a fábrica sem necessidade e evita problema caso
        // duas threads cheguem a essa linha ao mesmo tempo (proteção extra, ainda que o JavaFX seja majoritariamente single-thread)
        if (emf == null || !emf.isOpen()) {
            synchronized (JPAUtil.class) {
                if (emf == null || !emf.isOpen()) {
                    //Lê as variáveis de ambiente configuradas no launch.json (env), assim como já era feito nos controllers
                    Map<String, String> propriedadesModificadas = new HashMap<>();
                    propriedadesModificadas.put("jakarta.persistence.jdbc.url", System.getenv("DB_URL"));
                    propriedadesModificadas.put("jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
                    propriedadesModificadas.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));

                    emf = Persistence.createEntityManagerFactory("universidade-pu", propriedadesModificadas);
                }
            }
        }
        return emf;
    }

    // Fecha a fábrica de conexões. Deve ser chamado apenas UMA vez, ao encerrar a aplicação por completo
    // (veja SistemaAcademico.stop()), nunca dentro de um controller individual.
    public static synchronized void fechar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
