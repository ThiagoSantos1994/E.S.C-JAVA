INSERT INTO tbd_DespesasFixasMensais
    (
    id_Despesa,
    ds_Descricao,
    vl_Total,
    tp_Status,
    ds_Mes,
    ds_Ano,
    id_Funcionario,
    id_Ordem,
    tpFixasObrigatorias,
    tp_DespesaDebitoCartao,
    id_DetalheDespesaDebitoCartao
    )
VALUES (
    :fixas.idDespesa,
    UPPER(:fixas.dsDescricao),
    :fixas.vlTotal,
    :fixas.tpStatus,
    :fixas.dsMes,
    :fixas.dsAno,
    :fixas.idFuncionario,
    :fixas.idOrdem,
    :fixas.tpFixasObrigatorias,
    :fixas.tpDespesaDebitoCartao,
    :fixas.idDetalheDespesaDebitoCartao
)
