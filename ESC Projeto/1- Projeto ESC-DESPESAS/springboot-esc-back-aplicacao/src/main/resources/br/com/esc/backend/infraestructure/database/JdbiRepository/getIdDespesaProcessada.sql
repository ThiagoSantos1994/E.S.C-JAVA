SELECT DISTINCT
	id_Despesa
FROM
	tbd_DespesasFixasMensais
WHERE
	id_Despesa >= :idDespesa
	AND id_Funcionario = :idFuncionario