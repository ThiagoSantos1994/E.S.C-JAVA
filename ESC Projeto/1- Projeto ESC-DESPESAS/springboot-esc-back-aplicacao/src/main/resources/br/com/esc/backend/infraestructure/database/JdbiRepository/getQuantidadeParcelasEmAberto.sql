SELECT
    COUNT(id_Parcelas)
FROM
    tbd_Parcelas
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario
    AND tp_Baixado = 'N'
