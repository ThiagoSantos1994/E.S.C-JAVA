SELECT
    ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(vl_Parcela, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS vl_Total
FROM
	tbd_Parcelas
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario