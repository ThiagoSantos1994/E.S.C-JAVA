SELECT
	id_Consolidacao,
	ds_TituloConsolidacao
FROM
	tbd_Consolidacao
WHERE
    <status>
    AND id_Funcionario = :idFuncionario
ORDER BY
    ds_TituloConsolidacao ASC