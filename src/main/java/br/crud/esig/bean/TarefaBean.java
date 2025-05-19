package br.crud.esig.bean;

import br.crud.esig.model.Tarefa;
import br.crud.esig.model.Prioridades;
import br.crud.esig.model.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class TarefaBean implements Serializable {

    private Tarefa tarefa;
    private List<Tarefa> tarefas;
    private Status filtroStatus;
    private Prioridades filtroPrioridade;

    @Inject
    private br.crud.esig.dao.TarefaDao tarefaDao;

    @PostConstruct
    public void init() {
        tarefa = new Tarefa();
        tarefas = tarefaDao.listar();
    }

    public void salvar() {
        tarefaDao.salvarOuAtualizar(tarefa);
        tarefas = tarefaDao.listar();
        tarefa = new Tarefa();
    }


    public void editar(Tarefa tarefaSelecionada) {
        this.tarefa = tarefaSelecionada;
    }

    public void remover(Tarefa tarefaSelecionada)
    {
        tarefaDao.remover(tarefaSelecionada);
        tarefas = tarefaDao.listar();
    }


    public void filtrar() {
        tarefas = tarefaDao.buscarPorFiltro(filtroStatus, filtroPrioridade);
    }
}
