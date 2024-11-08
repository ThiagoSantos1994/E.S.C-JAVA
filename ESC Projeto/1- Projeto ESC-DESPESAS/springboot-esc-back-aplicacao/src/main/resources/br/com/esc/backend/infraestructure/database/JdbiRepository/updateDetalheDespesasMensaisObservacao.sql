UPDATE
    tbd_DetalheDespesasMensais
SET
    id_Observacao = :idObservacao
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Ordem = :idOrdem
    AND id_Funcionario = :idFuncionario
