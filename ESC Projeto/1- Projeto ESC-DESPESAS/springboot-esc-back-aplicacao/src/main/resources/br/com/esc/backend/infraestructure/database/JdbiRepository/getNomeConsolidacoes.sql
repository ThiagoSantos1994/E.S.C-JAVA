SELECT DISTINCT
    id_Consolidacao,
    ds_TituloConsolidacao
FROM
    tbd_Consolidacao
WHERE
    id_Funcionario = :idFuncionario
    AND id_Consolidacao <> 0
ORDER BY
    ds_TituloConsolidacao