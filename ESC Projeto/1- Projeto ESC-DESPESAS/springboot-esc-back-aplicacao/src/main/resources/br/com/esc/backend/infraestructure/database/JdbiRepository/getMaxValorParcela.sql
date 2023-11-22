SELECT
    MAX(vl_Parcela)
FROM
    tbd_Parcelas
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario