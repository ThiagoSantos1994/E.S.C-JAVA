SELECT
	DISTINCT(ds_Mes + '/' + ds_Ano)
FROM
    tbd_DespesasFixasMensaisTemp
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario