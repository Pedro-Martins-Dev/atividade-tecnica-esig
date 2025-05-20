package br.crud.esig.bean;

import br.crud.esig.dao.UsuarioDao;
import br.crud.esig.model.Usuario;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UsuarioBean implements Serializable
{

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    private List<Usuario> usuarios;

    @Inject
    private UsuarioDao usuarioDao;

    @PostConstruct
    public void init()
    {
        usuario = new Usuario();
        usuarios = usuarioDao.listar();
    }

    public void salvar()
    {
        usuarioDao.salvar(usuario);
        usuarios = usuarioDao.listar();
        usuario = new Usuario();
    }

    public void editar(Usuario usuarioSelecionado)
    {
        this.usuario = usuarioSelecionado;
    }

    public void remover(Usuario usuarioSelecionado)
    {
        usuarioDao.remover(usuarioSelecionado);
        usuarios = usuarioDao.listar();
    }

    public Usuario getUsuario()
    {
        return usuario;
    }

    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios()
    {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios)
    {
        this.usuarios = usuarios;
    }
}
