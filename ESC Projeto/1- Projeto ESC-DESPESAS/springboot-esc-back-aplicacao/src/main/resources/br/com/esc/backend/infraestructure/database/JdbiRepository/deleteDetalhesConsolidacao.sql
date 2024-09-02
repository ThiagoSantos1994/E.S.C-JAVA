DELETE FROM
    tbd_ConsolidacaoDespesasParceladas
WHERE
    id_Consolidacao = :consolidacao.idConsolidacao
    AND id_Funcionario = :consolidacao.idFuncionario