UPDATE
    tbd_Parcelas
SET
    tp_ParcelaAmortizada = 'S'
WHERE
   id_DespesaParcelada = :idDespesaParcelada
   AND id_Parcelas = :idParcela
   AND id_Funcionario = :idFuncionario