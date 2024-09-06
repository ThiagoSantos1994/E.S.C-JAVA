SELECT
    id_DespesaParcelada
FROM
    tbd_DespesasParceladas
WHERE
    id_Consolidacao = :idConsolidacao
    AND id_Funcionario = :idFuncionario
    AND tp_Baixado = 'N'
ORDER BY
    id_DespesaParcelada