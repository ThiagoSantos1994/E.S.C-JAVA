SELECT
	c.id_Consolidacao,
	c.id_DespesaParcelada,
	c.dt_Associacao,
	c.id_Funcionario,
	(SELECT DISTINCT a.ds_TituloDespesaParcelada
    	+ ' - ' +
    	(	SELECT COALESCE(MIN(nr_Parcela), '000') FROM tbd_Parcelas WHERE id_DespesaParcelada = c.id_DespesaParcelada AND tp_Baixado = 'N')
    	+ '/' +
    		(SELECT CASE COUNT(tp_ParcelaAdiada) WHEN 0 THEN CAST(a.nr_TotalParcelas AS VarChar(10)) ELSE CAST((a.nr_TotalParcelas + COUNT(tp_ParcelaAdiada)) AS VarChar(10)) + '*' END FROM tbd_Parcelas WHERE tp_ParcelaAdiada = 'S' AND id_DespesaParcelada = a.id_DespesaParcelada)
    FROM
    	tbd_DespesasParceladas a
    	INNER JOIN tbd_Parcelas b on b.id_DespesaParcelada = a.id_DespesaParcelada
    WHERE
    	a.id_DespesaParcelada = c.id_DespesaParcelada
    	AND a.id_Funcionario = c.id_Funcionario
    ) AS ds_DescricaoDespesa,
    (CASE WHEN (d.tp_Baixado = 'N') THEN 'Em Aberto' ELSE 'Quitado' END) as statusDespesa,
    (CASE WHEN (d.tp_Baixado = 'S') THEN
		(
            SELECT e.vl_Parcela FROM tbd_Parcelas e
            WHERE e.id_DespesaParcelada = c.id_DespesaParcelada
            AND e.id_Parcelas = (SELECT MAX(nr_Parcela) FROM tbd_Parcelas WHERE id_DespesaParcelada = c.id_DespesaParcelada AND tp_Baixado = 'S')
        )
	ELSE
		(
            SELECT e.vl_Parcela FROM tbd_Parcelas e
            WHERE e.id_DespesaParcelada = c.id_DespesaParcelada
            AND e.id_Parcelas = (SELECT MIN(nr_Parcela) FROM tbd_Parcelas WHERE id_DespesaParcelada = c.id_DespesaParcelada AND tp_Baixado = 'N')
        )
    END) as vl_Fatura,
    d.nr_ParcelasAdiantadas
FROM
	tbd_ConsolidacaoDespesasParceladas c
	INNER JOIN tbd_DespesasParceladas d ON d.id_Consolidacao = c.id_Consolidacao and d.id_DespesaParcelada = c.id_DespesaParcelada
WHERE
    c.id_Consolidacao = :idConsolidacao
    AND c.id_Funcionario = :idFuncionario
ORDER BY
    c.id_Consolidacao ASC