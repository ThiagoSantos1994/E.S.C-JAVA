SELECT
	a.id_DespesaParcelada
FROM
	tbd_DetalheDespesasMensais a
	INNER JOIN tbd_ConsolidacaoDespesasParceladas b ON b.id_DespesaParcelada = a.id_DespesaParcelada
	INNER JOIN tbd_Consolidacao c ON c.id_Consolidacao = b.id_Consolidacao
	INNER JOIN tbd_Parcelas d ON d.id_DespesaParcelada = b.id_DespesaParcelada AND d.id_Parcelas = a.id_Parcela
WHERE
	a.id_Despesa = :idDespesa
	AND a.id_DetalheDespesa = :idDetalheDespesa
	AND a.id_Funcionario = :idFuncionario
	AND a.id_DespesaConsolidacao = :idConsolidacao
ORDER BY
    a.id_DespesaParcelada