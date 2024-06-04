INSERT INTO tbd_ObservacaoDetalheDespesaMensal
    (
    id_Despesa,
    id_DetalheDespesa,
    id_Ordem,
    id_Funcionario,
    ds_AnotacaoDespesa,
    ds_DataHoraInclusao
    )
VALUES (
    :detalhe.idDespesa,
    :detalhe.idDetalheDespesa,
    :detalhe.idOrdem,
    :detalhe.idFuncionario,
    :detalhe.dsObservacoes,
    GETDATE()
)
