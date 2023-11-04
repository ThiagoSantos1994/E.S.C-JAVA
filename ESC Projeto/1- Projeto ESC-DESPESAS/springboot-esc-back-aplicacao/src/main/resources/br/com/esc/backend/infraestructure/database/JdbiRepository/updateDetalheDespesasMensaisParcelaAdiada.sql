UPDATE
    tbd_DetalheDespesasMensais
SET
    tp_Status = 'Pago',
    tp_ParcelaAdiada = 'S',
    tp_Anotacao = 'S',
    vl_Total = '0,00',
    ds_Observacao = :dsObservacoes,
    ds_Observacao2 = :dsObservacoes2,
    vl_TotalParcelaAdiantada = :vlTotalParcelaAdiantada
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario
