UPDATE
    tbd_DetalheDespesasMensais
SET
    vl_Total = :vlTotal,
    vl_TotalPago = :vlTotalPago,
    tp_Status = :tpStatus,
    ds_Observacao = :dsObservacoes,
    ds_Observacao2 = :dsObservacoesComplementares
WHERE
    id_Despesa = :idDespesa
    AND id_Ordem = :idOrdem
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Funcionario = :idFuncionario