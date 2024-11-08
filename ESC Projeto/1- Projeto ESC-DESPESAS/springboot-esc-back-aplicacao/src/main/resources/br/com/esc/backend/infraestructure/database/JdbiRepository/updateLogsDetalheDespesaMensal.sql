UPDATE
   tbd_DetalheDespesasMensaisLogs
SET
   ds_LogDespesa = :detalhe.dsLogDespesa
WHERE
   id_Despesa = :detalhe.idDespesa
   AND id_DetalheDespesa = :detalhe.idDetalheDespesa
   AND id_DetalheDespesaLog = :detalhe.idDetalheDespesaLog
   AND id_Funcionario = :detalhe.idFuncionario
