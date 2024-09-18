UPDATE
    tbd_DetalheDespesasMensais
SET
    tp_Status = 'Pago',
    tp_ParcelaAdiada = 'S',
    tp_Anotacao = 'S',
    ds_Observacao = 'A(s) Parcela(s) da consolidação foram adiantada(s).',
    ds_Observacao2 = '',
    vl_TotalParcelaAdiantada = :vlTotalParcelaAdiantada
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Consolidacao = :idConsolidacao
    AND id_Funcionario = :idFuncionario
