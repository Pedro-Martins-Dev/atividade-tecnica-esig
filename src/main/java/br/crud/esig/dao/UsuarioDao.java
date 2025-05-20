package br.crud.esig.dao;

import br.crud.esig.model.Usuario;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class UsuarioDao {

    @PersistenceContext
    private EntityManager em;

    public List<Usuario> listar() {
        return em.createQuery("SELECT u FROM Usuario u ORDER BY u.nome", Usuario.class)
                .getResultList();
    }

    public Usuario buscarPorId(Long id) {
        return em.find(Usuario.class, id);
    }

    public void salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            em.persist(usuario);
        } else {
            em.merge(usuario);
        }
    }

    public void remover(Usuario usuario) {
        Usuario usuarioGerenciado = em.find(Usuario.class, usuario.getId());
        if (usuarioGerenciado != null) {
            em.remove(usuarioGerenciado);
        }
    }
}
