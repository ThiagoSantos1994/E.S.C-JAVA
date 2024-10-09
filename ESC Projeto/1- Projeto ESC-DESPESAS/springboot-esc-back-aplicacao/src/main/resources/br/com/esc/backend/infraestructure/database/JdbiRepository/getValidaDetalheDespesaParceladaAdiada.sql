SELECT
    CASE WHEN COUNT(id_Despesa) > 0 THEN 'S' ELSE 'N' END AS adiantamento
FROM
    tbd_DetalheDespesasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario
    AND tp_ParcelaAdiada = 'S'