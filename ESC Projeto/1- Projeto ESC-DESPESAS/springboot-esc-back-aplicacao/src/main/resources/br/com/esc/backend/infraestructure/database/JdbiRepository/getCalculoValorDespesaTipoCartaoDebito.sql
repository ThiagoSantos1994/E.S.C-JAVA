SELECT
    ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(a.vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0)
FROM
    tbd_DetalheDespesasMensais a
    INNER JOIN tbd_DespesaMensal b ON b.id_Despesa = a.id_Despesa AND b.id_DetalheDespesa = a.id_DetalheDespesa
WHERE
    a.id_Despesa = :idDespesa
    AND a.id_DetalheDespesa = :idDetalheDespesa
    AND a.id_Funcionario = :idFuncionario
    AND a.tp_Anotacao = 'N'
    AND a.tp_LinhaSeparacao = 'N'
    AND b.tp_DebitoCartao = 'S'
