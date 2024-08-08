SELECT
	tp_CategoriaDespesa,
	ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS VLR_DESPESAS
FROM
	tbd_DetalheDespesasMensais
WHERE
	id_Despesa = :idDespesa
	AND (tp_Anotacao = 'N' OR (tp_Anotacao = 'S' AND tp_ParcelaAdiada = 'S'))
	AND tp_LinhaSeparacao = 'N'
	AND id_Funcionario = :idFuncionario
	AND tp_CategoriaDespesa <> 'NULL'
	AND tp_CategoriaDespesa <> ''
GROUP BY tp_CategoriaDespesa