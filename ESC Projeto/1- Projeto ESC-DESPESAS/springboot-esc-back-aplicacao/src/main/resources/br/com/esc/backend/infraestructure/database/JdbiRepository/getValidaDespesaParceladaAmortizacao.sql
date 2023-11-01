SELECT
    (CASE WHEN COUNT(tp_ParcelaAmortizada) >= 1 THEN 'S' ELSE 'N' END) as parcelaAmortizada
FROM
    tbd_Parcelas
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario
    AND tp_ParcelaAmortizada = 'S'

