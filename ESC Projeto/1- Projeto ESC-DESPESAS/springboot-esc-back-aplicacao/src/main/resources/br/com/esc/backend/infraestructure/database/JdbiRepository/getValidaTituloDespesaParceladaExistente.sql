SELECT
    count(*)
FROM
    tbd_DespesasParceladas
WHERE
    ds_TituloDespesaParcelada = :dsTituloDespesaParcelada
    AND id_DespesaParcelada <> :idDespesaParcelada
    AND id_Funcionario = :idFuncionario
