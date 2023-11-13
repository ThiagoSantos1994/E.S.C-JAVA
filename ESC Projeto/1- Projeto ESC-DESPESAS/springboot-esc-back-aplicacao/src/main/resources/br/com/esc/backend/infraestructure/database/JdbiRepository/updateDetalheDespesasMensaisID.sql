UPDATE
    tbd_DetalheDespesasMensais
SET
    id_DetalheDespesa = :idDetalheDespesaNova
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Funcionario = :idFuncionario
