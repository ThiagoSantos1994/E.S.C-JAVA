SELECT DISTINCT
	id_DetalheDespesa,
	ds_NomeDespesa
FROM
	tbd_DespesaMensal
WHERE
	id_Despesa IN (SELECT DISTINCT id_Despesa FROM tbd_DespesasFixasMensais WHERE ds_Ano = :anoReferencia AND id_Funcionario = :idFuncionario)
	AND tp_LinhaSeparacao = 'N' AND tp_Relatorio = 'N' AND tp_Anotacao = 'N'
	AND id_DetalheDespesa NOT IN (SELECT DISTINCT id_DetalheDespesa FROM tbd_DespesaMensal WHERE id_Despesa = :idDespesa AND id_Funcionario = :idFuncionario)
	AND id_Funcionario = :idFuncionario
ORDER BY id_DetalheDespesa