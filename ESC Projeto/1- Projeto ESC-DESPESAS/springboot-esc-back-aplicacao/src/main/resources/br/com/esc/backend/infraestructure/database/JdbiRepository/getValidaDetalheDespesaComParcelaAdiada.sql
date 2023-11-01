SELECT
    (CASE WHEN COUNT(tp_ParcelaAdiada) >= 1 THEN 'S' ELSE 'N' END) as parcelaAdiada
FROM
    tbd_DetalheDespesasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Funcionario = :idFuncionario
    AND tp_ParcelaAdiada = 'S'

