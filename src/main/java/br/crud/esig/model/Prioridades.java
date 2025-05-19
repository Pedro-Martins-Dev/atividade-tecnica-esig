package br.crud.esig.model;

public enum Prioridades
{
    ALTA("Alta prioridade"),
    MEDIA("Prioridade média"),
    BAIXA("Baixa prioridade");

    private final String descricao;

    Prioridades(String descricao)
    {
        this.descricao = descricao;
    }

    public String getDescricao()
    {
        return descricao;
    }
}
