SELECT
    ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(b.vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS valor
FROM
    tbd_DespesasFixasMensais b
WHERE
    b.id_Despesa IN (SELECT DISTINCT(id_Despesa)
                     FROM tbd_DespesasFixasMensais
                     WHERE ds_Ano = :dsAno AND id_Funcionario = :idFuncionario
    )
  AND b.id_Funcionario = :idFuncionario AND b.tp_Status = 'P(+)'