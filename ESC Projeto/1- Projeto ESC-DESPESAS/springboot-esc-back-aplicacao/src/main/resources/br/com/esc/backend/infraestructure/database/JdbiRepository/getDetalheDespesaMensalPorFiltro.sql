SELECT
    a.id_Despesa,
    a.id_DetalheDespesa,
    UPPER(LTRIM(RTRIM(a.ds_Descricao))) AS ds_Descricao,
    null AS ds_TituloDespesa,
    a.id_Ordem,
    a.id_Parcela,
    a.id_DespesaParcelada,
    a.id_Consolidacao,
    a.id_DespesaConsolidacao,
    a.id_Funcionario,
    a.id_DespesaLinkRelatorio,
    a.vl_Total,
    a.vl_TotalPago,
    a.ds_Observacao,
    a.ds_Observacao2,
    a.tp_CategoriaDespesa,
    ISNULL(a.tp_Status, 'N') AS tp_Status,
    ISNULL(a.tp_Reprocessar, 'N') AS tp_Reprocessar,
    ISNULL(a.tp_Anotacao, 'N') AS tp_Anotacao,
    ISNULL(a.tp_Relatorio, 'N') AS tp_Relatorio,
    ISNULL(a.tp_LinhaSeparacao, 'N') AS tp_LinhaSeparacao,
    ISNULL(a.tp_ParcelaAdiada, 'N') AS tp_ParcelaAdiada,
    ISNULL(a.tp_ParcelaAmortizada, 'N') AS tp_ParcelaAmortizada,
    ISNULL(a.tp_Meta, 'N') AS tp_Meta
FROM
    tbd_DetalheDespesasMensais a
WHERE
    (:detalhe.idDespesa IS NULL OR a.id_Despesa = :detalhe.idDespesa)
    AND (:detalhe.idDetalheDespesa IS NULL OR a.id_DetalheDespesa = :detalhe.idDetalheDespesa)
    AND (:detalhe.idDespesaParcelada IS NULL OR a.id_DespesaParcelada = :detalhe.idDespesaParcelada)
    AND (:detalhe.idParcela IS NULL OR a.id_Parcela = :detalhe.idParcela)
    AND (:detalhe.idConsolidacao IS NULL OR a.id_Consolidacao = :detalhe.idConsolidacao)
    AND (:detalhe.idDespesaConsolidacao IS NULL OR a.id_DespesaConsolidacao = :detalhe.idDespesaConsolidacao)
    AND (:detalhe.dsDescricao IS NULL OR a.ds_Descricao = :detalhe.dsDescricao)
    AND (:detalhe.idFuncionario IS NULL OR a.id_Funcionario = :detalhe.idFuncionario)
    AND (:detalhe.tpReprocessar IS NULL OR a.tp_Reprocessar = :detalhe.tpReprocessar)
    AND (:detalhe.tpAnotacao IS NULL OR a.tp_Anotacao = :detalhe.tpAnotacao)
    AND (:detalhe.tpRelatorio IS NULL OR a.tp_Relatorio = :detalhe.tpRelatorio)
    AND (:detalhe.tpLinhaSeparacao IS NULL OR a.tp_LinhaSeparacao = :detalhe.tpLinhaSeparacao)
    AND (:detalhe.tpParcelaAdiada IS NULL OR a.tp_ParcelaAdiada = :detalhe.tpParcelaAdiada)
    AND (:detalhe.idOrdem IS NULL OR a.id_Ordem = :detalhe.idOrdem)
