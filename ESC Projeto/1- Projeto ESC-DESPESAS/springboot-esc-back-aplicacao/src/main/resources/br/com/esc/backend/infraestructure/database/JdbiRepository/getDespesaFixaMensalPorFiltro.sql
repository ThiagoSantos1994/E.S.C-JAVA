SELECT
    id_Despesa,
    ds_Descricao,
    vl_Total,
    tp_Status,
    ds_Mes,
    ds_Ano,
    id_Funcionario,
    id_Ordem,
    tpFixasObrigatorias,
    tp_DespesaDebitoCartao
FROM
    tbd_DespesasFixasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_Ordem = :idOrdem
    AND id_Funcionario = :idFuncionario
