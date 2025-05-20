UPDATE
    tbd_DetalheDespesasMensais
SET
    ds_Descricao = :detalhe.dsDescricao,
    id_Ordem = :detalhe.idOrdem,
    id_DespesaParcelada = :detalhe.idDespesaParcelada,
    id_DespesaLinkRelatorio = :detalhe.idDespesaLinkRelatorio,
    id_Consolidacao = :detalhe.idConsolidacao,
    id_DespesaConsolidacao = :detalhe.idDespesaConsolidacao,
    id_Parcela = :detalhe.idParcela,
    vl_Total = :detalhe.vlTotal,
    vl_TotalPago = :detalhe.vlTotalPago,
    ds_Observacao = :detalhe.dsObservacao,
    ds_Observacao2 = :detalhe.dsObservacao2,
    tp_CategoriaDespesa = :detalhe.tpCategoriaDespesa,
    tp_Status = :detalhe.tpStatus,
    tp_Reprocessar = :detalhe.tpReprocessar,
    tp_Anotacao = :detalhe.tpAnotacao,
    tp_Relatorio = :detalhe.tpRelatorio,
    tp_LinhaSeparacao = :detalhe.tpLinhaSeparacao,
    tp_Meta = :detalhe.tpMeta,
    tp_ParcelaAdiada = :detalhe.tpParcelaAdiada,
    tp_ParcelaAmortizada = :detalhe.tpParcelaAmortizada
WHERE
    id_Despesa = :detalhe.idDespesa
    AND id_DetalheDespesa = :detalhe.idDetalheDespesa
    AND id_Ordem = :detalhe.idOrdem
    AND id_Funcionario = :detalhe.idFuncionario
