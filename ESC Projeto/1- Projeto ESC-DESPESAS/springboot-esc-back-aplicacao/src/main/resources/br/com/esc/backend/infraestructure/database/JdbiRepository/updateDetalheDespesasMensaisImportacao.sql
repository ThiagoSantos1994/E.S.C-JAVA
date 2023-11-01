UPDATE
    tbd_DetalheDespesasMensais
SET
    ds_Descricao = :detalhe.dsDescricao,
    id_Ordem = :detalhe.idOrdem,
    id_DespesaParcelada = :detalhe.idDespesaParcelada,
    id_Parcela = :detalhe.idParcela,
    id_DespesaLinkRelatorio =:detalhe.idDespesaLinkRelatorio,
    ds_Observacao = :detalhe.dsObservacao,
    ds_Observacao2 = :detalhe.dsObservacao2,
    tp_CategoriaDespesa = :detalhe.tpCategoriaDespesa,
    tp_Status = :detalhe.tpStatus,
    tp_Reprocessar = :detalhe.tpReprocessar,
    tp_Anotacao = :detalhe.tpAnotacao,
    tp_Relatorio = :detalhe.tpRelatorio,
    tp_LinhaSeparacao = :detalhe.tpLinhaSeparacao,
    tp_ParcelaAdiada = :detalhe.tpParcelaAdiada,
    tp_ParcelaAmortizada = :detalhe.tpParcelaAmortizada,
    tp_Meta = :detalhe.tpMeta
WHERE
    id_Despesa = :detalhe.idDespesa
    AND id_DetalheDespesa = :detalhe.idDetalheDespesa
    AND id_DespesaParcelada = :detalhe.idDespesaParcelada
    AND id_Parcela = :detalhe.idParcela
    AND (:detalhe.dsDescricao IS NULL OR ds_Descricao = :detalhe.dsDescricao)
    AND id_Funcionario = :detalhe.idFuncionario
    AND tp_Reprocessar = :detalhe.tpReprocessar
    AND tp_Anotacao = :detalhe.tpAnotacao
    AND tp_Relatorio = :detalhe.tpRelatorio
    AND tp_LinhaSeparacao = :detalhe.tpLinhaSeparacao
    AND (:detalhe.idOrdem IS NULL OR id_Ordem = :detalhe.idOrdem)
    AND tp_Meta = :detalhe.tpMeta
