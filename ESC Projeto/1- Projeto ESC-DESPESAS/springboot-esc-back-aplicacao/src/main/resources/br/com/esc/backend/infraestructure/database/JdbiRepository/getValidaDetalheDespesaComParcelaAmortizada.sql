SELECT
    (CASE WHEN COUNT(tp_ParcelaAmortizada) >= 1 THEN 'S' ELSE 'N' END) as parcelaAmortizada
FROM
    tbd_DetalheDespesasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Funcionario = :idFuncionario
    AND tp_ParcelaAmortizada = 'S'

