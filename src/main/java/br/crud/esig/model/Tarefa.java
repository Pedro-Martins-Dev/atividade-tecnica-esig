package br.crud.esig.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridades prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuarioResponsavel;

    @Column(nullable = false, updatable = false)
    private LocalDate dataCadastro;

    private LocalDate dataConclusaoPrevista;
    private LocalDate dataConclusao;

    // Construtores
    public Tarefa() {}

    public Tarefa(String titulo, String descricao, Prioridades prioridade, Usuario usuarioResponsavel, LocalDate dataConclusaoPrevista) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = Status.PENDENTE;
        this.usuarioResponsavel = usuarioResponsavel;
        this.dataCadastro = LocalDate.now();
        this.dataConclusaoPrevista = dataConclusaoPrevista;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Prioridades getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridades prioridade) { this.prioridade = prioridade; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Usuario getUsuarioResponsavel() { return usuarioResponsavel; }
    public void setUsuarioResponsavel(Usuario usuarioResponsavel) { this.usuarioResponsavel = usuarioResponsavel; }

    public LocalDate getDataCadastro() { return dataCadastro; }

    public LocalDate getDataConclusaoPrevista() { return dataConclusaoPrevista; }
    public void setDataConclusaoPrevista(LocalDate dataConclusaoPrevista) { this.dataConclusaoPrevista = dataConclusaoPrevista; }

    public LocalDate getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao = dataConclusao; }

    // Métodos utilitários
    @Override
    public String toString() {
        return String.format("Tarefa {id=%d, titulo='%s', status=%s, prioridade=%s}", id, titulo, status, prioridade);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tarefa)) return false;
        Tarefa tarefa = (Tarefa) obj;
        return Objects.equals(id, tarefa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
