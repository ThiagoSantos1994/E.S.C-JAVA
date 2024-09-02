SELECT
    ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS vl_Total
FROM
    tbd_DetalheDespesasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario
    AND tp_LinhaSeparacao = 'N'
    AND (tp_Anotacao = 'N' OR tp_Anotacao = 'S' AND tp_ParcelaAdiada = 'S')
    AND ((id_Parcela > 1 AND id_DespesaParcelada IS NOT NULL) OR tp_Reprocessar = 'S')
    AND id_Consolidacao = 0