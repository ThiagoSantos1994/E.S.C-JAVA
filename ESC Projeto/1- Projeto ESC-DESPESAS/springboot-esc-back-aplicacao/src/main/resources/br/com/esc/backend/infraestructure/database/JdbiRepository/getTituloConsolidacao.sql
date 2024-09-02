SELECT
	id_Consolidacao,
	ds_TituloConsolidacao
FROM
	tbd_Consolidacao
WHERE
    (:tpBaixado IS NULL OR tp_Baixado = :tpBaixado)
    AND id_Funcionario = :idFuncionario
ORDER BY
    ds_TituloConsolidacao ASC