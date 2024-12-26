SELECT DISTINCT
    id_Consolidacao,
    ds_TituloConsolidacao
FROM
    tbd_Consolidacao
WHERE
    id_Funcionario = :idFuncionario
    AND id_Consolidacao IS NOT NULL
    AND tp_Baixado = 'N'
ORDER BY
	ds_TituloConsolidacao