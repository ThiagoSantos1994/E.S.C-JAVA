DELETE FROM
    tbd_ConsolidacaoDespesasParceladas
WHERE
    (:despesa.idConsolidacao IS NULL OR id_Consolidacao = :despesa.idConsolidacao)
    AND id_DespesaParcelada = :despesa.idDespesaParcelada
    AND id_Funcionario = :despesa.idFuncionario