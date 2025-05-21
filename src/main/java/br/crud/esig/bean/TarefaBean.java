package br.crud.esig.bean;

import br.crud.esig.dao.TarefaDao;
import br.crud.esig.dao.UsuarioDao;
import br.crud.esig.exception.ValidacaoException;
import br.crud.esig.model.Tarefa;
import br.crud.esig.model.Prioridades;
import br.crud.esig.model.Status;
import br.crud.esig.model.Usuario;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import br.crud.esig.util.FacesUtil;
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
        try {
            Object tarefaFlash = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tarefa");
            if (tarefaFlash != null && tarefaFlash instanceof Tarefa) {
                tarefa = (Tarefa) tarefaFlash;
            } else {
                tarefa = novaTarefa();
            }

            usuarios = usuarioDao.listar();

            if (usuarios.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Nenhum usuário cadastrado. Cadastre usuários primeiro.", null));
            }

            tarefas = tarefaDao.listar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro ao inicializar: " + e.getMessage(), null));
        }
    }

    private Tarefa novaTarefa() {
        Tarefa t = new Tarefa();
        t.setDataCadastro(LocalDate.now());
        t.setStatus(Status.PENDENTE);
        return t;
    }

    private void validarTarefa() throws ValidacaoException {
        if (tarefa.getTitulo() == null || tarefa.getTitulo().trim().isEmpty()) {
            throw new ValidacaoException("Título é obrigatório");
        }
        if (tarefa.getUsuarioResponsavel() == null) {
            throw new ValidacaoException("Selecione um responsável");
        }
        if (tarefa.getPrioridade() == null) {
            throw new ValidacaoException("Selecione uma prioridade");
        }
    }

    public String cadastrar() {
        try {
            validarTarefa();
            tarefaDao.salvar(tarefa);
            FacesUtil.addInfoMessage("Tarefa cadastrada com sucesso!");
            return "/views/listarTarefa.xhtml?faces-redirect=true";
        } catch (ValidacaoException e) {
            FacesUtil.addErrorMessage(e.getMessage());
            return null;
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro ao cadastrar tarefa: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String atualizar() {
        try {
            validarTarefa();
            tarefaDao.atualizar(tarefa);
            FacesUtil.addInfoMessage("Tarefa atualizada com sucesso!");
            return "/views/listarTarefa.xhtml?faces-redirect=true";
        } catch (ValidacaoException e) {
            FacesUtil.addErrorMessage(e.getMessage());
            return null;
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro ao atualizar tarefa: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String editar(Tarefa tarefaSelecionada) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("tarefa", tarefaSelecionada);
        return "/views/editarTarefa.xhtml?faces-redirect=true";
    }

    public void filtrar() {
        try {
            if (filtroUsuario != null || filtroPrioridade != null || filtroStatus != null) {
                tarefas = tarefaDao.buscarPorFiltro(filtroStatus, filtroPrioridade, filtroUsuario);
            } else {
                carregarTodasTarefas();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro ao filtrar tarefas: " + e.getMessage(), null));
        }
    }

    public void carregarTodasTarefas() {
        try {
            tarefas = tarefaDao.listar();
            resetarFiltros();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro ao carregar tarefas: " + e.getMessage(), null));
        }
    }

    private void resetarFiltros() {
        filtroUsuario = null;
        filtroPrioridade = null;
        filtroStatus = null;
    }

    public void remover(Tarefa tarefaSelecionada) {
        try {
            tarefaDao.remover(tarefaSelecionada);
            tarefas = tarefaDao.listar();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Tarefa removida com sucesso!", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro ao remover tarefa: " + e.getMessage(), null));
        }
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

    public List<Prioridades> getPrioridadeList() {
        return Arrays.asList(Prioridades.values());
    }

    public List<Status> getStatusList() {
        return Arrays.asList(Status.values());
    }
}
