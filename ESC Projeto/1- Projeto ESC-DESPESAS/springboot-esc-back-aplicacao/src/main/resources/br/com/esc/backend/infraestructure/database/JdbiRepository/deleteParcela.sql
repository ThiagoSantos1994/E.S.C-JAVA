DELETE FROM
    tbd_Parcelas
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Parcelas = :idParcela
    AND id_Funcionario = :idFuncionario