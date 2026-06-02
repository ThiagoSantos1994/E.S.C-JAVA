SELECT DISTINCT
	ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(b.vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0)
FROM
    tbd_DetalheDespesasMensais b
WHERE
    b.id_Despesa IN (SELECT DISTINCT(id_Despesa) from tbd_DespesasFixasMensais WHERE id_Funcionario = :idFuncionario AND ds_Ano = :anoReferencia)
    AND b.id_DetalheDespesa = :idDetalheDespesa
    AND b.id_Funcionario = :idFuncionario
    AND b.tp_Anotacao = 'N' AND b.tp_LinhaSeparacao = 'N'
    AND b.id_Consolidacao = 0