package br.crud.esig.dao;

import br.crud.esig.model.Prioridades;
import br.crud.esig.model.Status;
import br.crud.esig.model.Tarefa;
import br.crud.esig.model.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TarefaDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void salvarOuAtualizar(Tarefa tarefa) {
        if (tarefa.getId() == null) {
            em.persist(tarefa);
        } else {
            em.merge(tarefa);
        }
    }

    @Transactional
    public void remover(Tarefa tarefa) {
        Tarefa tarefaGerenciada = em.find(Tarefa.class, tarefa.getId());
        if (tarefaGerenciada != null) {
            em.remove(tarefaGerenciada);
        }
    }

    public List<Tarefa> listar() {
        return em.createQuery("SELECT t FROM Tarefa t ORDER BY t.dataCadastro DESC", Tarefa.class)
                .getResultList();
    }

    public List<Tarefa> buscarPorFiltro(Status status, Prioridades prioridade) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM Tarefa t WHERE 1=1");

        if (status != null) {
            jpql.append(" AND t.status = :status");
        }

        if (prioridade != null) {
            jpql.append(" AND t.prioridade = :prioridade");
        }

        TypedQuery<Tarefa> query = em.createQuery(jpql.toString(), Tarefa.class);

        if (status != null) {
            query.setParameter("status", status);
        }

        if (prioridade != null) {
            query.setParameter("prioridade", prioridade);
        }

        return query.getResultList();
    }

    // Novo método para listar tarefas filtrando por usuário
    public List<Tarefa> listarPorUsuario(Usuario usuario) {
        String jpql = "SELECT t FROM Tarefa t WHERE t.usuarioResponsavel = :usuario ORDER BY t.dataCadastro DESC";
        TypedQuery<Tarefa> query = em.createQuery(jpql, Tarefa.class);
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }
}
