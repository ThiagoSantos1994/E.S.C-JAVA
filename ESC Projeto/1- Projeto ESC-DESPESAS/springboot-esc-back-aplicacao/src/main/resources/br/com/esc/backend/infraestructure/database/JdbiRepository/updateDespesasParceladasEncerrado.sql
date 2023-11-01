UPDATE
    tbd_DespesasParceladas
SET
    tp_Baixado = 'S'
WHERE
    tp_Baixado = 'N'
    AND id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario