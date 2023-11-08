UPDATE
    tbd_DetalheDespesasMensais
SET
    id_Ordem = :idNovaOrdem
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND (:idDespesaParcelada IS NULL OR id_DespesaParcelada = :idDespesaParcelada)
    AND id_Ordem = :idOrdem
    AND id_Funcionario = :idFuncionario
