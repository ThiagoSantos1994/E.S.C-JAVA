UPDATE
    tbd_Parcelas
SET
    vl_Parcela = :vlParcela
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario
    AND tp_ParcelaAdiada = 'N'