UPDATE
    tbd_DetalheDespesasMensais
SET
    tp_Status = 'Pendente',
    tp_ParcelaAdiada = 'N',
    tp_Anotacao = 'N',
    ds_Observacao = '',
    ds_Observacao2 = '',
    vl_TotalParcelaAdiantada = null
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Consolidacao = :idConsolidacao
    AND tp_ParcelaAdiada = 'S'
    AND id_Funcionario = :idFuncionario
