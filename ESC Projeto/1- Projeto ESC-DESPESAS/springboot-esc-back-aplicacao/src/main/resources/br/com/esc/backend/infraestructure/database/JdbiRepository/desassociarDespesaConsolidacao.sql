DELETE FROM
    tbd_ConsolidacaoDespesasParceladas
WHERE
    id_Consolidacao = :despesa.idConsolidacao
    AND id_DespesaParcelada = :despesa.idDespesaParcelada
    AND id_Funcionario = :despesa.idFuncionario