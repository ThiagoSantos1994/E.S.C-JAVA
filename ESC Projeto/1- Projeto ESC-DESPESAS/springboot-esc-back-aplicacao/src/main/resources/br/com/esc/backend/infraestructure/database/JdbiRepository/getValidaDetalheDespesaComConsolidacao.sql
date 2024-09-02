SELECT
    CASE WHEN COUNT(id_Consolidacao) > 0 THEN 'S' ELSE 'N' END AS consolidacao
FROM
    tbd_DetalheDespesasMensais
WHERE
    id_Consolidacao = :idConsolidacao
    AND id_Funcionario = :idFuncionario