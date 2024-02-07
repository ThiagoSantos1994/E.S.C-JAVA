SELECT DISTINCT
	ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(b.vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0)
FROM
    tbd_DetalheDespesasMensais b
WHERE
    b.id_Despesa = :idDespesa
    AND b.id_DetalheDespesa = :idDetalheDespesa
    AND b.id_Funcionario = :idFuncionario
    AND b.tp_Anotacao = 'N' AND b.tp_LinhaSeparacao = 'N'