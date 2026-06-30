SELECT
    DISTINCT (CAST(ds_Ano AS VARCHAR(4)))
FROM
    tbd_DespesasFixasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario