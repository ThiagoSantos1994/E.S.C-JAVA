UPDATE
    tbd_DetalheDespesasMensais
SET
    id_DespesaConsolidacao = :idConsolidacao
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario
