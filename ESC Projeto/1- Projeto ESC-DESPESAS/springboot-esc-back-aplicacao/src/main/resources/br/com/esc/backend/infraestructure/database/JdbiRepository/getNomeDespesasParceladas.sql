SELECT DISTINCT
    id_DespesaParcelada,
    ds_TituloDespesaParcelada
FROM
    tbd_DespesasParceladas
WHERE
    id_Funcionario = :idFuncionario
    AND id_DespesaParcelada <> 0
ORDER BY
    ds_TituloDespesaParcelada