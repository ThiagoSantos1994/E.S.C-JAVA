SELECT
    MAX(id_Despesa)
FROM
    tbd_DespesasFixasMensais
WHERE
    id_Funcionario = :idFuncionario