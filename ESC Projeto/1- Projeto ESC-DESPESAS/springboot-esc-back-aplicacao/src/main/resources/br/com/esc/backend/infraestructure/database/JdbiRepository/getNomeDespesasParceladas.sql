SELECT DISTINCT
    ds_TituloDespesaParcelada
FROM
    tbd_DespesasParceladas
WHERE
    id_Funcionario = :idFuncionario
    AND id_DespesaParcelada <> 0