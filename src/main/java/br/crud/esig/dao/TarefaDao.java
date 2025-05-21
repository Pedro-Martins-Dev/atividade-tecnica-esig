package br.crud.esig.dao;

import br.crud.esig.model.Prioridades;
import br.crud.esig.model.Status;
import br.crud.esig.model.Tarefa;
import br.crud.esig.model.Usuario;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class TarefaDao {

    private static final Logger LOGGER = Logger.getLogger(TarefaDao.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void salvar(Tarefa tarefa) {
        try {
            if (tarefa == null) {
                throw new IllegalArgumentException("Tarefa não pode ser nula");
            }
            if (tarefa.getId() != null) {
                throw new IllegalArgumentException("Tarefa já possui ID. Use atualizar para modificar.");
            }
            // Valida campos obrigatórios para cadastro
            if (tarefa.getTitulo() == null || tarefa.getTitulo().trim().isEmpty()) {
                throw new IllegalArgumentException("Título é obrigatório.");
            }
            if (tarefa.getDescricao() == null || tarefa.getDescricao().trim().isEmpty()) {
                throw new IllegalArgumentException("Descrição é obrigatória.");
            }
            if (tarefa.getUsuarioResponsavel() == null) {
                throw new IllegalArgumentException("Usuário responsável é obrigatório.");
            }
            if (tarefa.getPrioridade() == null) {
                throw new IllegalArgumentException("Prioridade é obrigatória.");
            }
            // Define dataCadastro como hoje
            tarefa.setDataCadastro(LocalDate.now());

            em.persist(tarefa);
            em.flush();
            LOGGER.log(Level.INFO, "Tarefa persistida com ID: {0}", tarefa.getId());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar tarefa", e);
            throw new RuntimeException("Falha ao salvar tarefa: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void atualizar(Tarefa tarefa) {
        try {
            if (tarefa == null) {
                throw new IllegalArgumentException("Tarefa não pode ser nula");
            }
            if (tarefa.getId() == null) {
                throw new IllegalArgumentException("Tarefa não possui ID. Use salvar para criar nova tarefa.");
            }

            // Aqui você pode validar campos opcionais ou obrigatórios para atualização, se quiser

            em.merge(tarefa);
            em.flush();
            LOGGER.log(Level.INFO, "Tarefa atualizada com ID: {0}", tarefa.getId());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar tarefa", e);
            throw new RuntimeException("Falha ao atualizar tarefa: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void remover(Tarefa tarefa) {
        try {
            if (tarefa == null || tarefa.getId() == null) {
                throw new IllegalArgumentException("Tarefa ou ID não pode ser nulo");
            }

            Tarefa t = em.find(Tarefa.class, tarefa.getId());
            if (t != null) {
                em.remove(t);
                LOGGER.log(Level.INFO, "Tarefa removida com ID: {0}", tarefa.getId());
                em.flush();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao remover tarefa", e);
            throw new RuntimeException("Falha ao remover tarefa: " + e.getMessage(), e);
        }
    }

    @Transactional(TxType.SUPPORTS)
    public Tarefa buscarPorId(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID não pode ser nulo");
            }
            return em.find(Tarefa.class, id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar tarefa por ID", e);
            throw new RuntimeException("Falha ao buscar tarefa: " + e.getMessage(), e);
        }
    }

    @Transactional(TxType.SUPPORTS)
    public List<Tarefa> listar() {
        try {
            return em.createQuery("SELECT t FROM Tarefa t ORDER BY t.dataCadastro DESC", Tarefa.class)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar tarefas", e);
            throw new RuntimeException("Falha ao listar tarefas: " + e.getMessage(), e);
        }
    }

    @Transactional(TxType.SUPPORTS)
    public List<Tarefa> buscarPorFiltro(Status status, Prioridades prioridade, Usuario usuario) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tarefa> cq = cb.createQuery(Tarefa.class);
            Root<Tarefa> root = cq.from(Tarefa.class);

            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (prioridade != null) {
                predicates.add(cb.equal(root.get("prioridade"), prioridade));
            }
            if (usuario != null) {
                predicates.add(cb.equal(root.get("usuarioResponsavel"), usuario));
            }

            cq.where(predicates.toArray(new Predicate[0]));
            cq.orderBy(cb.desc(root.get("dataCadastro")));

            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao filtrar tarefas", e);
            throw new RuntimeException("Falha ao filtrar tarefas: " + e.getMessage(), e);
        }
    }

    @Transactional(TxType.SUPPORTS)
    public List<Tarefa> listarPorUsuario(Usuario usuario) {
        try {
            if (usuario == null) {
                return listar();
            }

            return em.createQuery(
                            "SELECT t FROM Tarefa t WHERE t.usuarioResponsavel = :usuario ORDER BY t.dataCadastro DESC",
                            Tarefa.class)
                    .setParameter("usuario", usuario)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar tarefas por usuário", e);
            throw new RuntimeException("Falha ao listar tarefas por usuário: " + e.getMessage(), e);
        }
    }

    @Transactional(TxType.SUPPORTS)
    public long contarTarefasPorStatus(Status status) {
        try {
            return em.createQuery(
                            "SELECT COUNT(t) FROM Tarefa t WHERE t.status = :status", Long.class)
                    .setParameter("status", status)
                    .getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao contar tarefas por status", e);
            throw new RuntimeException("Falha ao contar tarefas por status: " + e.getMessage(), e);
        }
    }

    public List<Tarefa> buscarPorUsuario(Usuario usuario) {
        return em.createQuery("SELECT t FROM Tarefa t WHERE t.usuarioResponsavel = :usuario", Tarefa.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }
}