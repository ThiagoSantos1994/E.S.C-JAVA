SELECT
    ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS vl_Total
FROM
    tbd_DetalheDespesasMensais a
    INNER JOIN tbd_DespesaMensal b ON b.id_Despesa = a.id_Despesa AND b.id_DetalheDespesa = a.id_DetalheDespesa
WHERE
    a.id_Despesa = :idDespesa
    AND a.id_Funcionario = :idFuncionario
    AND a.tp_Anotacao = 'N'
    AND a.tp_LinhaSeparacao = 'N'
    AND b.tp_Emprestimo = 'N'
    AND b.tp_EmprestimoAPagar = 'N'
    AND b.tp_Anotacao = 'N'
    AND b.tp_Poupanca = 'N'
    AND b.tp_PoupancaNegativa = 'N'
    AND b.tp_Relatorio = 'N'