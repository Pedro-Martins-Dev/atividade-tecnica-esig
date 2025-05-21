package br.crud.esig.dao;

import br.crud.esig.model.Usuario;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class UsuarioDao {

    private static final Logger LOGGER = Logger.getLogger(UsuarioDao.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Transactional(TxType.SUPPORTS)
    public List<Usuario> listar() {
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u ORDER BY u.nome", Usuario.class);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar usuários", e);
            throw new RuntimeException("Falha ao listar usuários: " + e.getMessage(), e);
        }
    }

    @Transactional(TxType.SUPPORTS)
    public Usuario buscarPorId(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID não pode ser nulo");
            }
            return em.find(Usuario.class, id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar usuário por ID: " + id, e);
            throw new RuntimeException("Falha ao buscar usuário: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void salvar(Usuario usuario) {
        try {
            if (usuario == null) {
                throw new IllegalArgumentException("Usuário não pode ser nulo");
            }

            if (usuario.getId() == null) {
                em.persist(usuario);
                LOGGER.log(Level.INFO, "Usuário persistido com ID: {0}", usuario.getId());
            } else {
                em.merge(usuario);
                LOGGER.log(Level.INFO, "Usuário atualizado com ID: {0}", usuario.getId());
            }
            em.flush();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar usuário", e);
            throw new RuntimeException("Falha ao salvar usuário: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void remover(Usuario usuario) {
        try {
            if (usuario == null || usuario.getId() == null) {
                throw new IllegalArgumentException("Usuário ou ID não pode ser nulo");
            }

            Usuario usuarioGerenciado = em.find(Usuario.class, usuario.getId());
            if (usuarioGerenciado != null) {
                em.remove(usuarioGerenciado);
                LOGGER.log(Level.INFO, "Usuário removido com ID: {0}", usuario.getId());
                em.flush();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao remover usuário", e);
            throw new RuntimeException("Falha ao remover usuário: " + e.getMessage(), e);
        }
    }

    @Transactional(TxType.SUPPORTS)
    public Usuario buscarPorNome(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome não pode ser vazio");
            }

            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.nome = :nome", Usuario.class);
            query.setParameter("nome", nome);

            return query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar usuário por nome: " + nome, e);
            throw new RuntimeException("Falha ao buscar usuário por nome: " + e.getMessage(), e);
        }
    }

    @Transactional(TxType.SUPPORTS)
    public boolean existeUsuarioComNome(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return false;
            }

            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM Usuario u WHERE u.nome = :nome", Long.class)
                    .setParameter("nome", nome)
                    .getSingleResult();

            return count > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao verificar existência de usuário", e);
            throw new RuntimeException("Falha ao verificar usuário: " + e.getMessage(), e);
        }
    }
}