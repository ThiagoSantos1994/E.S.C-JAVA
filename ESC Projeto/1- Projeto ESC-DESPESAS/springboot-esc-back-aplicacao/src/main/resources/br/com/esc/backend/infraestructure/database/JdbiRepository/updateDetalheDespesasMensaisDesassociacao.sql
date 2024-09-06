UPDATE
    tbd_DetalheDespesasMensais
SET
    id_DespesaConsolidacao = 0
WHERE
    id_DespesaConsolidacao = :idConsolidacao
    AND (:idDespesaParcelada IS NULL OR id_DespesaParcelada = :idDespesaParcelada)
    AND id_Funcionario = :idFuncionario
