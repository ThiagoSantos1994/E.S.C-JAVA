UPDATE
    tbd_DespesasParceladas
SET
    id_Consolidacao = :idConsolidacao
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario