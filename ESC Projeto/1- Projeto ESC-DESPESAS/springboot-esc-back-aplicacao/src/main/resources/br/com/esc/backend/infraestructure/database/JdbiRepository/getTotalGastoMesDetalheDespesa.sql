SELECT
    ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10, 2))), 0) AS VLR_GASTO_MES
FROM
    tbd_DetalheDespesasMensais
WHERE id_Despesa = :idDespesa
  AND id_DetalheDespesa = :idDetalheDespesa
  AND id_Funcionario = :idFuncionario
  AND id_Parcela <= 1
  AND tp_Anotacao = 'N'
  AND tp_LinhaSeparacao = 'N'
  and id_Consolidacao = 0