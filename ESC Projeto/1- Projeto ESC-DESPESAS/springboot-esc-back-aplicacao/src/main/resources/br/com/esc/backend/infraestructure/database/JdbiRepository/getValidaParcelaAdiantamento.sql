SELECT
    (CASE WHEN COUNT(tp_ParcelaAdiada) > 0 THEN 'S' ELSE 'N' END) as tp_ParcelaAdiada
FROM
    tbd_Parcelas
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Parcelas = :idParcela
    AND id_Funcionario = :idFuncionario
    AND tp_ParcelaAdiada = 'S'