SELECT
	DISTINCT (CASE WHEN ds_Mes = '' THEN 'ERRO' ELSE (CAST(ds_Mes AS VARCHAR(2)) + '/' + CAST(ds_Ano AS VARCHAR(4))) END)
FROM
    tbd_DespesasFixasMensaisTemp
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario