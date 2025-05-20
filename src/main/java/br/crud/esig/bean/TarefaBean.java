package br.crud.esig.bean;

import br.crud.esig.dao.UsuarioDao;
import br.crud.esig.model.Tarefa;
import br.crud.esig.model.Prioridades;
import br.crud.esig.model.Status;
import br.crud.esig.model.Usuario;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class TarefaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Tarefa tarefa;
    private List<Tarefa> tarefas;
    private Status filtroStatus;
    private Prioridades filtroPrioridade;

    @Inject
    private UsuarioDao usuarioDao;

    private List<Usuario> usuarios;
    private Usuario filtroUsuario;

    @Inject
    private br.crud.esig.dao.TarefaDao tarefaDao;


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


    public void editar(Tarefa tarefaSelecionada) {
        this.tarefa = tarefaSelecionada;
    }

    public void remover(Tarefa tarefaSelecionada) {
        tarefaDao.remover(tarefaSelecionada);
        tarefas = tarefaDao.listar();
    }

    public void filtrar() {
        // Filtra tarefas com base nos filtros de status e prioridade
        tarefas = tarefaDao.buscarPorFiltro(filtroStatus, filtroPrioridade);
    }

    // Método para filtrar por usuário
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

    // Getters e Setters

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

    // Método auxiliar para o select de Prioridades
    public List<Prioridades> getPrioridadeList() {
        return Arrays.asList(Prioridades.values());
    }

    // Método auxiliar para o select de Status
    public List<Status> getStatusList() {
        return Arrays.asList(Status.values());
    }

    // Método auxiliar para o select de Usuários
    public List<Usuario> getUsuarioList() {
        return new ArrayList<>();
    }
}
