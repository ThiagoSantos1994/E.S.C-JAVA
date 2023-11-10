SELECT
	DISTINCT (CASE WHEN ds_Mes = '' THEN 'ERRO' ELSE (ds_Mes + '/' + ds_Ano) END)
FROM
    tbd_DespesasFixasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario