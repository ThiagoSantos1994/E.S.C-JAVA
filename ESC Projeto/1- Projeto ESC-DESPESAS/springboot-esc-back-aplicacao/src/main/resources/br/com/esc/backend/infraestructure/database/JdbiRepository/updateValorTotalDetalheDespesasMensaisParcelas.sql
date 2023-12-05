UPDATE
    tbd_DetalheDespesasMensais
SET
    vl_Total = :vlTotal
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Parcela = :idParcela
    AND id_Funcionario = :idFuncionario
    AND tp_ParcelaAdiada = 'N'
    AND (:status IS NULL OR tp_Status = :status)