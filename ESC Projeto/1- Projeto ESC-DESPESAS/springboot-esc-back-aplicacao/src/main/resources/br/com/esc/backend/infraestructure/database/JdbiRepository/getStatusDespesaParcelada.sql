SELECT
    tp_Baixado
FROM
    tbd_DespesasParceladas
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario