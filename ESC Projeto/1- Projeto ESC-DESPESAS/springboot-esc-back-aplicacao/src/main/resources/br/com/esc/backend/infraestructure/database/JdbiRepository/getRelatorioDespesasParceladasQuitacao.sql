SELECT
	UPPER(b.ds_TituloDespesaParcelada) AS ds_TituloDespesaParcelada,
	ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(c.vl_Parcela, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS VLR_DESPESA
FROM
	tbd_DetalheDespesasMensais a
	INNER JOIN tbd_DespesasParceladas b on b.id_DespesaParcelada = a.id_DespesaParcelada
	INNER JOIN tbd_Parcelas c on c.id_DespesaParcelada = b.id_DespesaParcelada and c.id_Parcelas = a.id_Parcela
WHERE
	a.id_Despesa = :idDespesa
	AND a.id_Funcionario = :idFuncionario
	AND CAST(c.nr_Parcela AS INTEGER) = CAST((b.nr_TotalParcelas + b.nr_ParcelasAdiantadas) AS INTEGER)
GROUP BY
	b.ds_TituloDespesaParcelada
