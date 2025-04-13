SELECT (
    (SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(b.vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS valor
        FROM tbd_DespesasFixasMensais b
        WHERE b.id_Despesa = :idDespesa AND b.id_Funcionario = :idFuncionario AND b.tp_Status = '+' AND b.tpFixasObrigatorias = 'S')
    -
    (SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(a.vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS valor
        FROM tbd_DespesasFixasMensais a
        WHERE a.id_Despesa = :idDespesa AND a.id_Funcionario = :idFuncionario AND (a.tp_Status = '-' OR a.tp_Status = '->') AND a.tpFixasObrigatorias = 'S')
) AS 'valor'