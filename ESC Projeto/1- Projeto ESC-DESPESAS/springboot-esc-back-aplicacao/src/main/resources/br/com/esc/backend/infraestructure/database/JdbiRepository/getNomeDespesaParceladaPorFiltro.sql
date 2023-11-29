SELECT
    ds_TituloDespesaParcelada
FROM
    tbd_DespesasParceladas
WHERE
    (:idDespesaParcelada IS NULL OR id_DespesaParcelada = :idDespesaParcelada)
    AND id_Funcionario = :idFuncionario
ORDER BY
    ds_TituloDespesaParcelada