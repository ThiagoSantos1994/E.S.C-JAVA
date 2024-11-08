SELECT
    ds_LogDespesa
FROM
    tbd_DetalheDespesasMensaisLogs
WHERE
    id_DetalheDespesaLog = :idDetalheDespesaLog
    AND id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Funcionario = :idFuncionario