package br.crud.esig.bean;

import br.crud.esig.dao.TarefaDao;
import br.crud.esig.dao.UsuarioDao;
import br.crud.esig.model.Tarefa;
import br.crud.esig.model.Prioridades;
import br.crud.esig.model.Status;
import br.crud.esig.model.Usuario;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import org.omnifaces.cdi.ViewScoped;
@Named
@ViewScoped
public class TarefaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Tarefa tarefa;
    private List<Tarefa> tarefas;

    private Status filtroStatus;
    private Prioridades filtroPrioridade;
    private Usuario filtroUsuario;

    private List<Usuario> usuarios;

    @Inject
    private UsuarioDao usuarioDao;

    @Inject
    private TarefaDao tarefaDao;

    @PostConstruct
    public void init() {
        tarefa = new Tarefa();
        tarefas = tarefaDao.listar();
        usuarios = usuarioDao.listar();
    }

    public void salvar() {
        tarefaDao.salvarOuAtualizar(tarefa);
        tarefas = tarefaDao.listar();
        tarefa = new Tarefa();
    }

    public String editar(Tarefa tarefaSelecionada) {
        this.tarefa = tarefaSelecionada;
        return "/views/editarTarefa.xhtml?faces-redirect=true";
    }

    public void filtrarPorUsuario() {
        if (filtroUsuario != null) {
            tarefas = tarefaDao.listarPorUsuario(filtroUsuario);
        } else {
            carregarTodasTarefas();
        }
    }

    public void carregarTodasTarefas() {
        tarefas = tarefaDao.listar();
        filtroUsuario = null;
    }

    public String remover(Tarefa tarefaSelecionada) {
        tarefaDao.remover(tarefaSelecionada);
        tarefas = tarefaDao.listar();
        return null;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public Status getFiltroStatus() {
        return filtroStatus;
    }

    public void setFiltroStatus(Status filtroStatus) {
        this.filtroStatus = filtroStatus;
    }

    public Prioridades getFiltroPrioridade() {
        return filtroPrioridade;
    }

    public void setFiltroPrioridade(Prioridades filtroPrioridade) {
        this.filtroPrioridade = filtroPrioridade;
    }

    public Usuario getFiltroUsuario() {
        return filtroUsuario;
    }

    public void setFiltroUsuario(Usuario filtroUsuario) {
        this.filtroUsuario = filtroUsuario;
    }

    public List<Usuario> getUsuarioList() {
        return usuarios;
    }
}
