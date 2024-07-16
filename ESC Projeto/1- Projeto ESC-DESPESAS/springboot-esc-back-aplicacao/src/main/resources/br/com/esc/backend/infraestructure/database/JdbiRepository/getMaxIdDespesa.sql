SELECT
    ISNULL(MAX(id_Despesa),0)
FROM
    tbd_DespesasFixasMensais
WHERE
    id_Funcionario = :idFuncionario