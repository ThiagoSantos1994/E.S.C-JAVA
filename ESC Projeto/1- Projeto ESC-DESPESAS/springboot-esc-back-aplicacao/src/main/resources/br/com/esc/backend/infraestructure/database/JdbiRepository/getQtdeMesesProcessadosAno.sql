SELECT
    COUNT(DISTINCT(id_Despesa))
FROM
    tbd_DespesasFixasMensais
WHERE
    ds_Ano = :anoReferencia
    AND id_Funcionario = :idFuncionario