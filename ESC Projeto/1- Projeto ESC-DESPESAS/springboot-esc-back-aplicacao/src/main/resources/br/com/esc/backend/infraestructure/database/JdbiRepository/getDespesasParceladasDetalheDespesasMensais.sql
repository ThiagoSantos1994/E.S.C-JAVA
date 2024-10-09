SELECT
    a.id_Despesa,
    a.id_DetalheDespesa,
    UPPER(LTRIM(RTRIM(CASE a.ds_Descricao
        WHEN '*PRC' THEN b.ds_TituloDespesaParcelada + ' - ' + c.nr_Parcela + '/' +
        (CASE c.tp_Quitado WHEN 'S' THEN '# [BAIXA-TOTAL] #' ELSE
        (SELECT CASE COUNT(tp_ParcelaAdiada) WHEN 0 THEN CAST(b.nr_TotalParcelas AS VarChar(10)) ELSE CAST((b.nr_TotalParcelas + COUNT(tp_ParcelaAdiada)) AS VarChar(10)) + '*' END FROM tbd_Parcelas WHERE tp_ParcelaAdiada = 'S' AND id_DespesaParcelada = a.id_DespesaParcelada)
        END)
        WHEN '*CONS' THEN cons.ds_TituloConsolidacao ELSE a.ds_Descricao
    END))) AS ds_TituloDespesa,
    UPPER(LTRIM(RTRIM(a.ds_Descricao))) AS ds_Descricao,
    a.id_Ordem,
    a.id_Parcela,
    a.id_DespesaParcelada,
    a.id_Consolidacao,
    a.id_DespesaConsolidacao,
    a.id_Funcionario,
    a.id_DespesaLinkRelatorio,
    a.vl_Total,
    a.vl_TotalPago,
    CASE ISNULL(desp.tp_Relatorio, 'N') WHEN 'S' THEN 'REF: ' + (SELECT ds_NomeDespesa FROM tbd_DespesaMensal WHERE id_Despesa = a.id_Despesa AND id_DetalheDespesa = a.id_DetalheDespesa AND id_Funcionario = a.id_Funcionario) ELSE a.ds_Observacao END as ds_Observacao,
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
    LEFT JOIN tbd_DespesasParceladas b on b.id_DespesaParcelada = a.id_DespesaParcelada
    LEFT JOIN tbd_Parcelas c on c.id_DespesaParcelada = b.id_DespesaParcelada and c.id_Parcelas = a.id_Parcela
    LEFT JOIN tbd_DespesaMensal desp on desp.id_Despesa = a.id_Despesa AND desp.id_DetalheDespesa = a.id_DetalheDespesa
    LEFT JOIN tbd_Consolidacao cons on cons.id_Consolidacao = a.id_Consolidacao
WHERE
    a.id_Despesa = :idDespesa
    AND a.id_Funcionario = :idFuncionario
    AND a.id_DespesaParcelada > 0
