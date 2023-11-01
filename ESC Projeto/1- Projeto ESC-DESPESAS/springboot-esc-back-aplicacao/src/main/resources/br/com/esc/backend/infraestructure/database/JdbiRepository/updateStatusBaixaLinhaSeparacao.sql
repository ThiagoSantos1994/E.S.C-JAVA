UPDATE
    tbd_DetalheDespesasMensais
SET
    tp_Status = 'Pago',
    ds_Observacao = 'Baixa Automatica - Linha Separacao'
WHERE
   id_Despesa = :idDespesa
   AND id_Funcionario = :idFuncionario
   AND tp_LinhaSeparacao = 'S'