SELECT
    ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS vl_Total
FROM
    tbd_DetalheDespesasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario
    AND id_DespesaLinkRelatorio = :idDespesaLinkRelatorio
    AND tp_Relatorio = 'S'
    AND tp_LinhaSeparacao = 'N'