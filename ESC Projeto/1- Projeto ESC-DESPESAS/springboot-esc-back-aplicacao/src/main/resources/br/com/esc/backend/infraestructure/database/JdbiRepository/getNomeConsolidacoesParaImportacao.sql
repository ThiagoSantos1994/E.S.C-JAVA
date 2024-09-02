SELECT DISTINCT
    id_Consolidacao,
    ds_TituloConsolidacao
FROM
    tbd_Consolidacao
WHERE
    id_Funcionario = :idFuncionario
    AND id_Consolidacao NOT IN (SELECT DISTINCT id_Consolidacao
                                    FROM tbd_DetalheDespesasMensais
                                    WHERE id_Consolidacao > 0
                                    AND id_Consolidacao IS NOT NULL
                                    AND id_Funcionario = :idFuncionario)
