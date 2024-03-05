SELECT
    CASE WHEN COUNT(*) = 0 THEN 'False' ELSE 'True' END
FROM
    tbd_DetalheDespesasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Funcionario = :idFuncionario
    AND tp_Status = 'Pendente'
    AND tp_Anotacao = 'N'
    AND tp_LinhaSeparacao = 'N'