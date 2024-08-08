SELECT
    ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(a.vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS vl_Total
FROM
    tbd_DetalheDespesasMensais a
    INNER JOIN tbd_DespesaMensal b on b.id_Despesa = a.id_Despesa and b.id_DetalheDespesa = a.id_DetalheDespesa
WHERE
    a.id_Despesa = :idDespesa
    AND a.id_Funcionario = :idFuncionario
    AND b.tp_Poupanca = 'S'
    AND a.tp_Status = 'Pendente'
    AND a.tp_Relatorio = 'N'
    AND a.tp_LinhaSeparacao = 'N'

